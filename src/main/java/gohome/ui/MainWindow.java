package gohome.ui;

import gohome.core.Game;
import gohome.core.Figure;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame implements DirectionListener {
    private final Game game;
    private final GamePanel gamePanel;
    private final HUDPanel hud;
    private final SettingsDialog settings;
    private gohome.ai.AIPlayer ai = null;
    private Figure.Color aiSide = null;

    private boolean awaitingDirection = false;
    private Figure.Color[] pendingToss = null;

    public MainWindow() {
        super("GoHome - Swing Remake");
        // show settings first
        settings = new SettingsDialog(this);
        settings.setVisible(true);

        this.game = new Game();

        if (settings.isSinglePlayer()) {
            aiSide = settings.getAiSide();
            int level = settings.getAiLevel();
            switch(level) {
                case 1: ai = new gohome.ai.SimpleAI(aiSide); break;
                case 2: ai = new gohome.ai.RuleBasedAI(aiSide); break;
                default: ai = new gohome.ai.MinimaxAI(aiSide); break;
            }
        }

        this.gamePanel = new GamePanel(game, this);
        this.hud = new HUDPanel(game, this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 680);
        setLayout(new BorderLayout());
        add(gamePanel, BorderLayout.CENTER);
        add(hud, BorderLayout.SOUTH);
        setLocationRelativeTo(null);

        // start first turn
        SwingUtilities.invokeLater(this::startTurn);
    }

    private void startTurn() {
        pendingToss = game.tossCoins();
        // play coin-flip animation and then continue the turn
        hud.playCoinFlip(pendingToss, () -> {
            hud.update();
            if (ai != null && aiSide == game.getCurrentPlayer()) {
                // let AI decide after flip animation
                hud.setAwaiting(false);
                Figure.Dir dir = ai.chooseDirection(game, pendingToss);
                processMove(dir);
            } else {
                awaitingDirection = true;
                hud.setAwaiting(true);
                gamePanel.requestFocusInWindow();
            }
        });
    }

    private void processMove(Figure.Dir dir) {
        awaitingDirection = false;
        // simulate on a copy to calculate targets and result without mutating actual game state yet
        Game sim = game.copy();
        Game.GameResult simResult = sim.applyMove(dir, pendingToss);

        java.util.List<MoveTarget> moves = new java.util.ArrayList<>();
        for (Figure.Color c : new Figure.Color[]{Figure.Color.RED, Figure.Color.BLUE}) {
            Figure actual = game.getFigure(c);
            Figure target = sim.getFigure(c);
            if (actual.getX() != target.getX() || actual.getY() != target.getY()) {
                int steps = Math.abs(sim.toroidalManhattanDistance(actual.getX(), actual.getY(), target.getX(), target.getY()));
                moves.add(new MoveTarget(c, actual.getX(), actual.getY(), target.getX(), target.getY(), steps));
            }
        }

        // animate moves; once animation finishes, apply the move to the real model and handle result
        if (!moves.isEmpty()) {
            // play move sound then animate
            gohome.audio.SoundPlayer.playMove();
            hud.setAwaiting(false);
            gamePanel.animateMoves(moves, () -> {
                Game.GameResult r = game.applyMove(dir, pendingToss); // apply real
                // play win sound if terminal
                if (r == Game.GameResult.RED_WINS || r == Game.GameResult.BLUE_WINS || r == Game.GameResult.TIE) {
                    gohome.audio.SoundPlayer.playWin();
                }
                hud.update();
                gamePanel.repaint();
                if (r != Game.GameResult.ONGOING) {
                    String msg;
                    if (r == Game.GameResult.TIE) msg = "Tie! Both reached home.";
                    else if (r == Game.GameResult.RED_WINS) msg = "Red wins!";
                    else msg = "Blue wins!";
                    int ans = JOptionPane.showConfirmDialog(this, msg + " Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
                    if (ans == JOptionPane.YES_OPTION) {
                        resetGame();
                        startTurn();
                    } else {
                        System.exit(0);
                    }
                    return;
                }
                game.nextPlayer();
                startTurn();
            });
        } else {
            hud.setAwaiting(false);
            // no visible moves (shouldn't happen often) - apply immediately
            Game.GameResult r = game.applyMove(dir, pendingToss);
            hud.update();
            gamePanel.repaint();
            if (r != Game.GameResult.ONGOING) {
                String msg;
                if (r == Game.GameResult.TIE) msg = "Tie! Both reached home.";
                else if (r == Game.GameResult.RED_WINS) msg = "Red wins!";
                else msg = "Blue wins!";
                int ans = JOptionPane.showConfirmDialog(this, msg + " Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.YES_OPTION) {
                    resetGame();
                    startTurn();
                } else {
                    System.exit(0);
                }
                return;
            }
            game.nextPlayer();
            startTurn();
        }
    }

    private void resetGame() {
        // simple reset: recreate game and UI
        Figure red = game.getFigure(Figure.Color.RED);
        Figure blue = game.getFigure(Figure.Color.BLUE);
        // default corners
        red.setPosition(0,0);
        blue.setPosition(game.getBoard().getWidth()-1, game.getBoard().getHeight()-1);
        hud.setAwaiting(false);
        hud.update();
        gamePanel.repaint();
    }

    @Override
    public void onDirectionChosen(Figure.Dir dir) {
        if (!awaitingDirection) return;
        processMove(dir);
    }
}
