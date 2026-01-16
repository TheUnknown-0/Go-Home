package gohome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.border.EmptyBorder;
import java.util.Random;

public class GoHomeUI extends JFrame {
    private Spiel spiel;
    private BoardPanel boardPanel;
    private CoinPanel coinPanel;
    private PlayerPanel playerPanel;
    private JLabel statusLabel;
    private JButton btnUp, btnRight, btnDown, btnLeft, btnNewGame, btnThrow;
    private JToggleButton btnAuto;
    private Timer autoTimer;

    public GoHomeUI() {
        spiel = new Spiel();
        spiel.start();
        initUI();
        updateUIFromGame();
    }

    private void initUI() {
        setTitle("Go Home — Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 1000)); // Minimale größe
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Automatisch maximale Größe
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBackground(new Color(245, 246, 250));
        content.setBorder(new EmptyBorder(12, 12, 12, 12));

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Go Home");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(new Color(34, 40, 49));
        JLabel subtitle = new JLabel("");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(new Color(99, 102, 106));
        header.add(title, BorderLayout.WEST);
        header.add(subtitle, BorderLayout.SOUTH);
        content.add(header, BorderLayout.NORTH);

        // center board
        boardPanel = new BoardPanel();
        content.add(boardPanel, BorderLayout.CENTER);

        // right control panel
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);
        right.setBorder(new EmptyBorder(6, 6, 6, 6));

        coinPanel = new CoinPanel();
        right.add(coinPanel);
        right.add(Box.createVerticalStrut(12));

        playerPanel = new PlayerPanel();
        right.add(playerPanel);
        right.add(Box.createVerticalStrut(8));
/*
        statusLabel = new JLabel("Status");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        statusLabel.setBorder(new EmptyBorder(6, 6, 6, 6));
        right.add(statusLabel);
        right.add(Box.createVerticalStrut(12));

 */
/*
        JPanel dpad = createDPad();
        right.add(dpad);
        right.add(Box.createVerticalStrut(12));

 */

        btnNewGame = makeButton("Neues Spiel", new Color(34, 197, 94));
        btnNewGame.setFocusable(false);
        btnNewGame.setForeground(new Color(0, 0, 0));
        btnNewGame.addActionListener(e -> {
            if (!spiel.spielIstFertig()) { // Sofern das Spiel noch läuft, Bestätigung erfordern
                int ans = JOptionPane.showConfirmDialog(this, "Neues Spiel starten? Der aktuelle Spielstand geht verloren.", "Bestätigen", JOptionPane.YES_NO_OPTION);
                if (ans != JOptionPane.YES_OPTION) return;
            }

            spiel = new Spiel();
            spiel.start();
            boardPanel.stopAnimationAndReset();
            updateUIFromGame();
        });
        right.add(btnNewGame);
        right.add(Box.createVerticalStrut(8));

        btnThrow = makeButton("Münzen werfen", new Color(59, 130, 246));
        btnThrow.setForeground(new Color(0, 0, 0));
        btnThrow.addActionListener(e -> {
            spiel.muenzenWerfen();
            updateUIFromGame();
        });
        right.add(btnThrow);
        right.add(Box.createVerticalStrut(8));

/*
        btnAuto = new JToggleButton("Zufällige Züge"); // Zufällige Züge Modus
        btnAuto.setFocusable(false);
        btnAuto.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAuto.addActionListener(e -> {
            if (btnAuto.isSelected()) startAuto(); else stopAuto();
        });
        right.add(btnAuto);
        right.add(Box.createVerticalGlue());
*/

        content.add(right, BorderLayout.EAST);

        add(content);

        setupKeyBindings();

        autoTimer = new Timer(700, ev -> {
            if (spiel.spielIstFertig()) {
                stopAuto();
                return;
            }
            int dir = new Random().nextInt(4);
            performMove(dir);
        });
    }

    private JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        b.setOpaque(true);
        b.putClientProperty("baseColor", bg);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(((Color)b.getClientProperty("baseColor")).brighter()); }
            public void mouseExited(MouseEvent e) { b.setBackground((Color)b.getClientProperty("baseColor")); }
        });
        return b;
    }

    /*
    private JPanel createDPad() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        btnUp = createArrowButton("▲", "Up");
        btnUp.setFocusable(false);
        btnRight = createArrowButton("▶", "Right");
        btnRight.setFocusable(false);
        btnDown = createArrowButton("▼", "Down");
        btnDown.setFocusable(false);
        btnLeft = createArrowButton("◀", "Left");
        btnLeft.setFocusable(false);

        btnUp.addActionListener(e -> performMove(0));
        btnRight.addActionListener(e -> performMove(1));
        btnDown.addActionListener(e -> performMove(2));
        btnLeft.addActionListener(e -> performMove(3));

        gbc.gridx = 1; gbc.gridy = 0; panel.add(btnUp, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(btnLeft, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(new JLabel(""), gbc);
        gbc.gridx = 2; gbc.gridy = 1; panel.add(btnRight, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(btnDown, gbc);
        return panel;
    }
*/
    private JButton createArrowButton(String label, String tip) {
        JButton b = new JButton(label);
        b.setToolTipText(tip);
        b.setPreferredSize(new Dimension(56, 56));
        b.setFont(new Font("SansSerif", Font.BOLD, 20));
        b.setBackground(new Color(75, 85, 99));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(64, 75, 89), 1, true));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(b.getBackground().brighter()); }
            public void mouseExited(MouseEvent e) { b.setBackground(new Color(75, 85, 99)); }
        });
        return b;
    } // Nicht verwendet, da Pfeiltasten implementiert sind

    private void setupKeyBindings() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();
        im.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        im.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        am.put("moveUp", new AbstractAction() { public void actionPerformed(ActionEvent e) { performMove(0); } });
        am.put("moveRight", new AbstractAction() { public void actionPerformed(ActionEvent e) { performMove(1); } });
        am.put("moveDown", new AbstractAction() { public void actionPerformed(ActionEvent e) { performMove(2); } });
        am.put("moveLeft", new AbstractAction() { public void actionPerformed(ActionEvent e) { performMove(3); } });
    }

    private void startAuto() { btnAuto.setSelected(true); autoTimer.start(); toggleControls(false); }
    private void stopAuto() { btnAuto.setSelected(false); autoTimer.stop(); toggleControls(true); }
    private void toggleControls(boolean enabled) {

        btnUp.setEnabled(enabled); btnDown.setEnabled(enabled); btnLeft.setEnabled(enabled); btnRight.setEnabled(enabled);
        btnNewGame.setEnabled(enabled); btnThrow.setEnabled(enabled);


    }

    private void performMove(int richtung) {
        // ignorieren eines inputs während des aktuellen Zuges
        if (boardPanel != null && boardPanel.isAnimating()) return;

        if (spiel.spielIstFertig()) {
            showWinner();
            return;
        }

        // snapshot before move
        int f1xBefore = spiel.f1.x, f1yBefore = spiel.f1.y;
        int f2xBefore = spiel.f2.x, f2yBefore = spiel.f2.y;

        // bewegt die aktuelle Figur in die angegebene Richtung
        switch (richtung) {
            case 0: spiel.nachObenBewegen(); break;
            case 1: spiel.nachRechtsBewegen(); break;
            case 2: spiel.nachUntenBewegen(); break;
            case 3: spiel.nachLinksBewegen(); break;
        }

        // snapshot after move
        int f1xAfter = spiel.f1.x, f1yAfter = spiel.f1.y;
        int f2xAfter = spiel.f2.x, f2yAfter = spiel.f2.y;

        // Falls keine Bewegung stattfand, UI sofort aktualisieren, fallback
        if (f1xBefore == f1xAfter && f1yBefore == f1yAfter &&
            f2xBefore == f2xAfter && f2yBefore == f2yAfter) {
            updateUIFromGame();
            if (spiel.spielIstFertig()) { showWinner(); stopAuto(); }
            return;
        }

        // Animiere die Bewegung, danke raptor mini
        boardPanel.animateMoves(f1xBefore, f1yBefore, f1xAfter, f1yAfter,
                               f2xBefore, f2yBefore, f2xAfter, f2yAfter,
                               () -> {
                                   updateUIFromGame();
                                   if (spiel.spielIstFertig()) { showWinner(); stopAuto(); }
                               });
    }

    private void showWinner() { // Ausgabe des Spielsiegers
        String winner = spiel.druckeGewinner();
        JOptionPane.showMessageDialog(this, winner, "Gewinner", JOptionPane.INFORMATION_MESSAGE);
        boardPanel.repaint();
    }

    private void updateUIFromGame() { // aktualisiert die UI
        boardPanel.repaint();
        coinPanel.repaint();
        playerPanel.repaint();
        // statusLabel.setText(spiel.getStatusString());
        boolean finished = spiel.spielIstFertig();
        /*
        btnUp.setEnabled(!finished);
        btnRight.setEnabled(!finished);
        btnDown.setEnabled(!finished);
        btnLeft.setEnabled(!finished);
        */

    }


    // innere Klasse für das Spielbrett mit Animation

    private class BoardPanel extends JPanel {
        private final int GRID = 5;
        private final Color TILE = new Color(237, 242, 247);
        private final Color HOME = new Color(250, 213, 102);
        private final Color GRID_LINE = new Color(200, 206, 214);
        private final Color BLUE = new Color(59, 130, 246);
        private final Color RED = new Color(239, 68, 68);
        // animation state
        private Timer animTimer;
        private long animStart;
        private int animDuration = 400; // ms, dauer der Animation
        private boolean animateF1 = false, animateF2 = false;
        private double s1x, s1y, e1x, e1y, c1x, c1y;
        private double s2x, s2y, e2x, e2y, c2x, c2y;
        private Runnable animCallback;

        public boolean isAnimating() { return animTimer != null && animTimer.isRunning(); }

        public void stopAnimationAndReset() {
            if (animTimer != null) {
                animTimer.stop();
                animTimer = null;
            }
            animateF1 = false; animateF2 = false;
            if (spiel != null && spiel.f1 != null && spiel.f2 != null) {
                c1x = spiel.f1.x; c1y = spiel.f1.y;
                c2x = spiel.f2.x; c2y = spiel.f2.y;
            } else {
                c1x = c1y = c2x = c2y = 0;
            }
            animCallback = null;
            repaint();
        }

        public void animateMoves(int f1sx, int f1sy, int f1tx, int f1ty,
                                 int f2sx, int f2sy, int f2tx, int f2ty,
                                 Runnable onComplete) {
            animateF1 = (f1sx != f1tx || f1sy != f1ty);
            animateF2 = (f2sx != f2tx || f2sy != f2ty);
            if (!animateF1 && !animateF2) {
                if (onComplete != null) SwingUtilities.invokeLater(onComplete);
                return;
            }
            s1x = f1sx; s1y = f1sy; e1x = f1tx; e1y = f1ty;
            s2x = f2sx; s2y = f2sy; e2x = f2tx; e2y = f2ty;
            c1x = s1x; c1y = s1y; c2x = s2x; c2y = s2y;
            animCallback = onComplete;
            animStart = System.currentTimeMillis();
            if (animTimer != null && animTimer.isRunning()) animTimer.stop();
            animTimer = new Timer(16, e -> {
                long now = System.currentTimeMillis();
                double t = Math.min(1.0, (now - animStart) / (double) animDuration);
                double et = ease(t);
                if (animateF1) { c1x = lerp(s1x, e1x, et); c1y = lerp(s1y, e1y, et); }
                if (animateF2) { c2x = lerp(s2x, e2x, et); c2y = lerp(s2y, e2y, et); }
                repaint();
                if (t >= 1.0) {
                    animTimer.stop();
                    if (animateF1) { c1x = e1x; c1y = e1y; }
                    if (animateF2) { c2x = e2x; c2y = e2y; }
                    repaint();
                    if (animCallback != null) SwingUtilities.invokeLater(animCallback);
                }
            });
            animTimer.setCoalesce(true);
            animTimer.start();
        }

        private double lerp(double a, double b, double t) { return a + (b - a) * t; }
        private double ease(double t) { 
            // Smoothstep easing für sanftere Beschleunigung und Abbremsung
            return t * t * t * (t * (t * 6 - 15) + 10);
        }

        BoardPanel() {
            setPreferredSize(new Dimension(520, 520));
            setBackground(new Color(245, 246, 250));
            setOpaque(true);
            setBorder(new EmptyBorder(8, 8, 8, 8));
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // background: use same solid background as the main content
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());

            int pad = 20;
            int w = getWidth() - pad * 2;
            int h = getHeight() - pad * 2;
            int cell = Math.min(w / GRID, h / GRID);
            int gridW = cell * GRID;
            int offsetX = (getWidth() - gridW) / 2;
            int offsetY = (getHeight() - gridW) / 2;

            // draw tiles
            for (int y = 0; y < GRID; y++) {
                for (int x = 0; x < GRID; x++) {
                    int rx = offsetX + x * cell;
                    int ry = offsetY + y * cell;
                    RoundRectangle2D rr = new RoundRectangle2D.Double(rx + 6, ry + 6, cell - 12, cell - 12, 12, 12);
                    if (x == 2 && y == 2 && !spiel.spielIstFertig()) {
                        g2.setColor(HOME);
                        g2.fill(rr);
                    } else {
                        g2.setColor(TILE);
                        g2.fill(rr);
                    }
                    g2.setColor(GRID_LINE);
                    g2.setStroke(new BasicStroke(2));
                    g2.draw(rr);
                }
            }

            // highlight current figures
            if (!spiel.spielIstFertig()) {
                g2.setStroke(new BasicStroke(3));
                g2.setColor(new Color(59, 130, 246, 0));
                if (spiel.aktuelleFigur1 != null) {
                    int x = spiel.aktuelleFigur1.x, y = spiel.aktuelleFigur1.y;
                    int rx = offsetX + x * cell, ry = offsetY + y * cell;
                    g2.drawOval(rx + 10, ry + 10, cell - 20, cell - 20);
                }
                g2.setColor(new Color(239, 68, 68, 0));
                if (spiel.aktuelleFigur2 != null) {
                    int x = spiel.aktuelleFigur2.x, y = spiel.aktuelleFigur2.y;
                    int rx = offsetX + x * cell, ry = offsetY + y * cell;
                    g2.drawOval(rx + 10, ry + 10, cell - 20, cell - 20);
                }
            }

            // draw pieces (support fractional positions during animation)
            int radius = (int) (cell * 0.36);
            double f1gx = animateF1 ? c1x : spiel.f1.x;
            double f1gy = animateF1 ? c1y : spiel.f1.y;
            double f2gx = animateF2 ? c2x : spiel.f2.x;
            double f2gy = animateF2 ? c2y : spiel.f2.y;
            int px1 = offsetX + (int) (f1gx * cell) + cell / 2;
            int py1 = offsetY + (int) (f1gy * cell) + cell / 2;
            int px2 = offsetX + (int) (f2gx * cell) + cell / 2;
            int py2 = offsetY + (int) (f2gy * cell) + cell / 2;
            // if both virtually on same cell draw halves
            if (Math.abs(f1gx - f2gx) < 0.01 && Math.abs(f1gy - f2gy) < 0.01) {
                int r2 = (int) (cell * 0.34);
                g2.setColor(BLUE);
                g2.fill(new Arc2D.Double(px1 - r2, py1 - r2, r2 * 2, r2 * 2, 90, 180, Arc2D.PIE));
                g2.setColor(RED);
                g2.fill(new Arc2D.Double(px1 - r2, py1 - r2, r2 * 2, r2 * 2, 270, 180, Arc2D.PIE));
                g2.setColor(new Color(40, 40, 40, 50));
                g2.setStroke(new BasicStroke(1));
                g2.drawOval(px1 - r2, py1 - r2, r2 * 2, r2 * 2);
            } else {
                g2.setColor(BLUE);
                g2.fillOval(px1 - radius, py1 - radius, radius * 2, radius * 2);
                g2.setColor(new Color(255, 255, 255, 0));
                g2.fillOval(px1 - radius / 2, py1 - radius / 2 - 6, radius, radius / 3);
                g2.setColor(RED);
                g2.fillOval(px2 - radius, py2 - radius, radius * 2, radius * 2);
                g2.setColor(new Color(255, 255, 255, 0));
                g2.fillOval(px2 - radius / 2, py2 - radius / 2 - 6, radius, radius / 3);
            }

            // draw center marker
            if (!spiel.spielIstFertig()) {
                int cx = offsetX + 2 * cell + cell / 2;
                int cy = offsetY + 2 * cell + cell / 2;
                g2.setColor(new Color(36, 36, 36, 140));
                g2.setFont(new Font("SansSerif", Font.BOLD, cell / 3));
                FontMetrics fm = g2.getFontMetrics();
                String s = "🏠";
                int sw = fm.stringWidth(s);
                g2.drawString(s, cx - sw / 2, cy + fm.getAscent() / 2 - 4);
            }

            // finished overlay
            if (spiel.spielIstFertig()) {
                String winnerText = spiel.druckeGewinner();
                Color winnerColor = spiel.f1.gewonnen() && !spiel.f2.gewonnen() ? BLUE : (spiel.f2.gewonnen() && !spiel.f1.gewonnen() ? RED : new Color(127, 0, 255));
                // dark overlay
                g2.setColor(new Color(0, 0, 0, 160));
                g2.fillRect(offsetX, offsetY, gridW, gridW);
                // colored banner
                int bannerH = cell;
                g2.setColor(winnerColor);
                g2.fillRoundRect(offsetX + gridW/8, offsetY + gridW/2 - bannerH/2 - 10, gridW*3/4, bannerH, 16, 16);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 36));
                FontMetrics fm = g2.getFontMetrics();
                int sw = fm.stringWidth(winnerText);
                int sx = offsetX + (gridW - sw) / 2;
                int sy = offsetY + gridW / 2 + fm.getAscent() / 2 - 4;
                g2.drawString(winnerText, sx, sy);
                // small celebratory dots
                g2.setColor(new Color(255,255,255,0));
                g2.fillOval(sx - 40, sy - 30, 10, 10);
                g2.fillOval(sx + sw + 10, sy - 25, 10, 6);
                g2.fillOval(sx + sw/2, sy - 60, 10, 10);
            }

            g2.dispose();
        }
    }

    private class CoinPanel extends JPanel {
        CoinPanel() { setPreferredSize(new Dimension(400, 90)); setOpaque(false); }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(); int h = getHeight();
            RoundRectangle2D card = new RoundRectangle2D.Double(4, 4, w - 8, h - 8, 12, 12);
            g2.setColor(new Color(255,255,255,0)); g2.fill(card);
            g2.setColor(new Color(220,220,220)); g2.draw(card);


            int cx1 = (int) (w * 0.25);   //
            int cx2 = (int) (w * 0.75);   //
            int cy  = (int) (h * 0.45);   // Poisiton der Münzen

            int r = (int) (h * 0.2);   // skaliert sauber

            Color blue = new Color(59,130,246), red = new Color(239,68,68), gray = new Color(200,200,200);
            Color c1 = gray, c2 = gray;
            if (spiel.muenze1 != null) c1 = spiel.muenze1.equals("blau") ? blue : red;
            if (spiel.muenze2 != null) c2 = spiel.muenze2.equals("blau") ? blue : red;
            g2.setColor(c1); g2.fillOval(cx1 - r, cy - r, r*2, r*2);
            g2.setColor(c2); g2.fillOval(cx2 - r, cy - r, r*2, r*2);
            g2.setColor(new Color(255,255,255,0)); g2.fillOval(cx1 - r/2, cy - r/2 - 6, r, r/3);
            g2.setColor(new Color(255,255,255,0)); g2.fillOval(cx2 - r/2, cy - r/2 - 6, r, r/3);
            g2.setColor(new Color(0,0,0,40)); g2.setStroke(new BasicStroke(1));
            g2.drawOval(cx1 - r, cy - r, r*2, r*2); g2.drawOval(cx2 - r, cy - r, r*2, r*2);
            g2.setColor(new Color(80,80,80)); g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString(spiel.muenze1 == null ? "-" : spiel.muenze1, cx1 - 10, cy + r + 18);
            g2.drawString(spiel.muenze2 == null ? "-" : spiel.muenze2, cx2 - 10, cy + r + 18);
            g2.dispose();
        }
    }

    private class PlayerPanel extends JPanel {
        PlayerPanel() { setPreferredSize(new Dimension(200, 44)); setOpaque(false); }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int h = getHeight();
            int r = 12;
            String text = "Am Zug: " + (spiel.aktuellerSpieler == null ? "(nicht gesetzt)" : spiel.aktuellerSpieler.farbe);
            Color col = new Color(140,140,140);
            if (spiel.aktuellerSpieler == spiel.f1) col = new Color(59,130,246);
            else if (spiel.aktuellerSpieler == spiel.f2) col = new Color(239,68,68);
            g2.setColor(col);
            g2.fillOval(8, (h - r*2)/2, r*2, r*2);
            g2.setColor(new Color(0,0,0,140));
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString(text, 36, (h + g2.getFontMetrics().getAscent())/2 - 3);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new GoHomeUI().setVisible(true);
        });
    }
}
