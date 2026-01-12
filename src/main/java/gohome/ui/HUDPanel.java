package gohome.ui;

import gohome.core.Game;
import gohome.core.Figure;

import javax.swing.*;
import java.awt.*;

public class HUDPanel extends JPanel {
    private final Game game;
    private final JLabel infoLabel;

    private final JPanel tossPanel;
    private final JLabel coinIcon1;
    private final JLabel coinIcon2;
    private final JLabel tossTextLabel;

    private final JPanel controlsPanel;
    private final JButton upBtn, downBtn, leftBtn, rightBtn;

    private ImageIcon coinRedIcon, coinBlueIcon;

    public HUDPanel(Game game, DirectionListener listener) {
        this.game = game;
        setLayout(new BorderLayout());
        infoLabel = new JLabel();

        // toss panel with two icons and a text label
        tossPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        coinIcon1 = new JLabel();
        coinIcon2 = new JLabel();
        tossTextLabel = new JLabel();
        tossPanel.add(coinIcon1);
        tossPanel.add(coinIcon2);
        tossPanel.add(tossTextLabel);

        // controls panel (direction buttons)
        controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        upBtn = new JButton("\u2191");
        downBtn = new JButton("\u2193");
        leftBtn = new JButton("\u2190");
        rightBtn = new JButton("\u2192");
        upBtn.setFocusable(false); leftBtn.setFocusable(false); rightBtn.setFocusable(false); downBtn.setFocusable(false);
        controlsPanel.add(leftBtn);
        controlsPanel.add(upBtn);
        controlsPanel.add(downBtn);
        controlsPanel.add(rightBtn);

        // hook up actions
        upBtn.addActionListener(e -> listener.onDirectionChosen(Figure.Dir.UP));
        downBtn.addActionListener(e -> listener.onDirectionChosen(Figure.Dir.DOWN));
        leftBtn.addActionListener(e -> listener.onDirectionChosen(Figure.Dir.LEFT));
        rightBtn.addActionListener(e -> listener.onDirectionChosen(Figure.Dir.RIGHT));

        loadCoinIcons();
        update();
        add(infoLabel, BorderLayout.WEST);
        add(tossPanel, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.EAST);

        JButton settings = new JButton("Settings");
        settings.addActionListener(e -> new SettingsDialog(null).setVisible(true));
        add(settings, BorderLayout.SOUTH);

        setAwaiting(false);
    }

    private void loadCoinIcons() {
        try {
            java.io.File cr = new java.io.File("assets/coin_red.png");
            if (cr.exists()) coinRedIcon = new ImageIcon(cr.getAbsolutePath());
            java.io.File cb = new java.io.File("assets/coin_blue.png");
            if (cb.exists()) coinBlueIcon = new ImageIcon(cb.getAbsolutePath());
        } catch (Exception e) {
            // ignore
        }
    }

    public void update() {
        // use ASCII hyphen to avoid encoding issues in packaged exe
        infoLabel.setText("GoHome - current player: " + game.getCurrentPlayer());
        Figure.Color[] toss = game.getLastToss();
        if (toss == null) {
            tossTextLabel.setText("Toss: (none)");
            coinIcon1.setIcon(null);
            coinIcon2.setIcon(null);
        } else {
            tossTextLabel.setText("" + toss[0].name() + ", " + toss[1].name());
            coinIcon1.setIcon(toss[0] == Figure.Color.RED ? coinRedIcon : coinBlueIcon);
            coinIcon2.setIcon(toss[1] == Figure.Color.RED ? coinRedIcon : coinBlueIcon);
        }
    }

    public void setAwaiting(boolean awaiting) {
        upBtn.setEnabled(awaiting);
        downBtn.setEnabled(awaiting);
        leftBtn.setEnabled(awaiting);
        rightBtn.setEnabled(awaiting);
        if (awaiting) {
            infoLabel.setText(infoLabel.getText() + "  (choose direction: arrows or buttons)");
        } else {
            // remove hint if present
            String s = infoLabel.getText();
            int idx = s.indexOf("  (choose direction:");
            if (idx >= 0) s = s.substring(0, idx);
            infoLabel.setText(s);
        }
    }

    /**
     * Play a short coin-flip animation (icons alternate) and call onComplete afterwards.
     */
    public void playCoinFlip(Figure.Color[] result, Runnable onComplete) {
        // play sound
        gohome.audio.SoundPlayer.playCoinFlip();

        final int duration = 600; // ms
        final int interval = 80; // ms
        final long start = System.currentTimeMillis();
        javax.swing.Timer t = new javax.swing.Timer(interval, null);
        t.addActionListener(e -> {
            long now = System.currentTimeMillis();
            int elapsed = (int)(now - start);
            boolean showRed = (elapsed / interval) % 2 == 0;
            coinIcon1.setIcon(showRed ? coinRedIcon : coinBlueIcon);
            coinIcon2.setIcon(showRed ? coinBlueIcon : coinRedIcon);
            tossTextLabel.setText("");
            if (elapsed >= duration) {
                t.stop();
                // display actual result
                if (result != null) {
                    tossTextLabel.setText("" + result[0].name() + ", " + result[1].name());
                    coinIcon1.setIcon(result[0] == Figure.Color.RED ? coinRedIcon : coinBlueIcon);
                    coinIcon2.setIcon(result[1] == Figure.Color.RED ? coinRedIcon : coinBlueIcon);
                }
                if (onComplete != null) onComplete.run();
            }
        });
        t.start();
    }
}
