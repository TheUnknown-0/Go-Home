package gohome.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final Board board;
    private final List<Figure> figures = new ArrayList<>();
    private final Random rng;
    private int currentPlayerIndex = 0; // 0 == RED, 1 == BLUE

    public Game() {
        this(5, 5, new Random());
    }

    public Game(int w, int h, Random rng) {
        this.board = new Board(w, h);
        this.rng = rng;
        // default start positions (corners)
        figures.add(new Figure(Figure.Color.RED, 0, 0));
        figures.add(new Figure(Figure.Color.BLUE, w-1, h-1));
    }

    public Board getBoard() { return board; }
    public List<Figure> getFigures() { return figures; }

    public Figure getFigure(Figure.Color color) {
        for (Figure f : figures) if (f.getColor() == color) return f;
        return null;
    }

    /**
     * Simulate two coin tosses (true = RED, false = BLUE)
     * return colors of selected figures (could be same twice)
     */
    public Figure.Color[] tossCoins() {
        Figure.Color[] out = new Figure.Color[2];
        for (int i=0;i<2;i++) out[i] = rng.nextBoolean() ? Figure.Color.RED : Figure.Color.BLUE;
        this.lastToss = out;
        return out;
    }

    private Figure.Color[] lastToss = null;

    public Figure.Color[] getLastToss() { return lastToss; }

    public enum GameResult { ONGOING, RED_WINS, BLUE_WINS, TIE }

    /**
     * Apply a direction for the previously tossed coins (or given toss) and perform moves.
     * Returns the game result after the move.
     */
    public GameResult applyMove(Figure.Dir dir, Figure.Color[] toss) {
        if (toss == null || toss.length != 2) throw new IllegalArgumentException("Invalid toss");
        this.lastToss = toss;
        int redSteps = 0, blueSteps = 0;
        if (toss[0] == toss[1]) {
            if (toss[0] == Figure.Color.RED) redSteps = 2; else blueSteps = 2;
        } else {
            // each color moves one
            redSteps = (toss[0] == Figure.Color.RED || toss[1] == Figure.Color.RED) ? 1 : 0;
            blueSteps = (toss[0] == Figure.Color.BLUE || toss[1] == Figure.Color.BLUE) ? 1 : 0;
        }

        boolean redWin = false, blueWin = false;
        if (redSteps > 0) redWin = move(Figure.Color.RED, dir, redSteps);
        if (blueSteps > 0) blueWin = move(Figure.Color.BLUE, dir, blueSteps);

        if (redWin && blueWin) return GameResult.TIE;
        if (redWin) return GameResult.RED_WINS;
        if (blueWin) return GameResult.BLUE_WINS;
        return GameResult.ONGOING;
    }

    public int getHomeX() { return board.getWidth() / 2; }
    public int getHomeY() { return board.getHeight() / 2; }

    public int toroidalManhattanDistance(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x1 - x2);
        dx = Math.min(dx, board.getWidth() - dx);
        int dy = Math.abs(y1 - y2);
        dy = Math.min(dy, board.getHeight() - dy);
        return dx + dy;
    }

    public int distanceToHome(Figure.Color color) {
        Figure f = getFigure(color);
        if (f == null) return Integer.MAX_VALUE;
        return toroidalManhattanDistance(f.getX(), f.getY(), getHomeX(), getHomeY());
    }

    /**
     * Create a simple copy of the game for simulation. Figures positions are cloned.
     */
    public Game copy() {
        Game g = new Game(board.getWidth(), board.getHeight(), new Random());
        // set positions to match current
        for (Figure f : g.getFigures()) {
            Figure original = getFigure(f.getColor());
            f.setPosition(original.getX(), original.getY());
        }
        g.currentPlayerIndex = this.currentPlayerIndex;
        return g;
    }

    public boolean move(Figure.Color color, Figure.Dir dir, int steps) {
        Figure f = getFigure(color);
        if (f == null) return false;
        f.move(dir, steps, board);
        return f.isAtHome(board);
    }

    public Figure.Color getCurrentPlayer() {
        return currentPlayerIndex == 0 ? Figure.Color.RED : Figure.Color.BLUE;
    }

    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
    }
}
