package gohome.ui;

import gohome.core.Game;
import gohome.core.Figure;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel {
    private final Game game;
    private final DirectionListener listener;
    private BufferedImage pieceRed, pieceBlue, tileHome, coinRed, coinBlue;
    private final int tileSize = 48; // user requested 48px

    // animation state per color
    private static class AnimState {
        final long startMillis;
        final int durationMillis;
        final float fromX, fromY, toX, toY;
        AnimState(long startMillis, int durationMillis, float fromX, float fromY, float toX, float toY) {
            this.startMillis = startMillis; this.durationMillis = durationMillis;
            this.fromX = fromX; this.fromY = fromY; this.toX = toX; this.toY = toY;
        }
        float progress(long now) {
            float p = (now - startMillis) / (float) durationMillis;
            if (p < 0) p = 0; if (p > 1) p = 1;
            return p;
        }
        float curX(long now) { return fromX + (toX - fromX) * progress(now); }
        float curY(long now) { return fromY + (toY - fromY) * progress(now); }
        boolean finished(long now) { return progress(now) >= 1.0f; }
    }

    private final java.util.Map<Figure.Color, AnimState> anims = new java.util.HashMap<>();
    private javax.swing.Timer animTimer = null;

    public GamePanel(Game game, DirectionListener listener) {
        this.game = game;
        this.listener = listener;
        setPreferredSize(new Dimension(game.getBoard().getWidth() * tileSize,
                game.getBoard().getHeight() * tileSize));
        setFocusable(true);
        loadAssets();
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(WHEN_FOCUSED);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke("UP"), "up");
        im.put(KeyStroke.getKeyStroke("DOWN"), "down");
        im.put(KeyStroke.getKeyStroke("LEFT"), "left");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        am.put("up", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { listener.onDirectionChosen(Figure.Dir.UP); } });
        am.put("down", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { listener.onDirectionChosen(Figure.Dir.DOWN); } });
        am.put("left", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { listener.onDirectionChosen(Figure.Dir.LEFT); } });
        am.put("right", new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) { listener.onDirectionChosen(Figure.Dir.RIGHT); } });
    }

    private void loadAssets() {
        try {
            File a = new File("assets/piece_red.png");
            if (a.exists()) pieceRed = ImageIO.read(a);
            File b = new File("assets/piece_blue.png");
            if (b.exists()) pieceBlue = ImageIO.read(b);
            File c = new File("assets/tile_home.png");
            if (c.exists()) tileHome = ImageIO.read(c);
            File cr = new File("assets/coin_red.png"); if (cr.exists()) coinRed = ImageIO.read(cr);
            File cb = new File("assets/coin_blue.png"); if (cb.exists()) coinBlue = ImageIO.read(cb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Animate a set of moves. Each target carries from/to coordinates (board cells) and steps.
     * Duration scales with steps (150ms per step).
     */
    public void animateMoves(java.util.List<MoveTarget> targets, Runnable onComplete) {
        anims.clear();
        long now = System.currentTimeMillis();
        int totalDuration = 0;
        for (MoveTarget t : targets) {
            float fromX = t.fromX;
            float fromY = t.fromY;
            float toX = t.toX;
            float toY = t.toY;
            int dur = Math.max(120, 150 * Math.max(1, t.steps));
            totalDuration = Math.max(totalDuration, dur);
            anims.put(t.color, new AnimState(now, dur, fromX, fromY, toX, toY));
        }

        if (animTimer != null && animTimer.isRunning()) animTimer.stop();
        animTimer = new javax.swing.Timer(16, e -> {
            long cur = System.currentTimeMillis();
            boolean allDone = true;
            for (java.util.Iterator<java.util.Map.Entry<Figure.Color, AnimState>> it = anims.entrySet().iterator(); it.hasNext(); ) {
                java.util.Map.Entry<Figure.Color, AnimState> en = it.next();
                if (en.getValue().finished(cur)) {
                    it.remove();
                } else allDone = false;
            }
            repaint();
            if (allDone) {
                ((javax.swing.Timer)e.getSource()).stop();
                if (onComplete != null) onComplete.run();
            }
        });
        animTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        int w = game.getBoard().getWidth();
        int h = game.getBoard().getHeight();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int px = x * tileSize;
                int py = y * tileSize;
                if (game.getBoard().isHome(x, y)) {
                    if (tileHome != null) g2.drawImage(tileHome, px, py, tileSize, tileSize, null);
                    else {
                        g2.setColor(new Color(0xFBC02D));
                        g2.fillRect(px, py, tileSize, tileSize);
                    }
                } else {
                    g2.setColor(new Color(0xEDEDED));
                    g2.fillRect(px, py, tileSize, tileSize);
                }
                g2.setColor(Color.GRAY);
                g2.drawRect(px, py, tileSize, tileSize);
            }
        }

        long now = System.currentTimeMillis();
        for (Figure f : game.getFigures()) {
            float drawX, drawY;
            AnimState s = anims.get(f.getColor());
            if (s != null) {
                drawX = s.curX(now);
                drawY = s.curY(now);
            } else {
                drawX = f.getX();
                drawY = f.getY();
            }
            int px = Math.round(drawX * tileSize);
            int py = Math.round(drawY * tileSize);

            if (f.getColor() == Figure.Color.RED) {
                if (pieceRed != null) g2.drawImage(pieceRed, px, py, tileSize, tileSize, null);
                else {
                    g2.setColor(Color.RED);
                    g2.fillOval(px+4, py+4, tileSize-8, tileSize-8);
                }
            } else {
                if (pieceBlue != null) g2.drawImage(pieceBlue, px, py, tileSize, tileSize, null);
                else {
                    g2.setColor(Color.BLUE);
                    g2.fillOval(px+4, py+4, tileSize-8, tileSize-8);
                }
            }
        }

        g2.dispose();
    }
}
