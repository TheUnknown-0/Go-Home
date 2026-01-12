package gohome.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import gohome.core.Figure;

public class SettingsDialog extends JDialog {
    private boolean singlePlayer = false;
    private boolean pixelArt = true; // default on
    private int aiLevel = 1; // 1..3
    private Figure.Color aiSide = Figure.Color.BLUE;

    public SettingsDialog(Frame owner) {
        super(owner, "Settings", true);
        setLayout(new BorderLayout());
        JPanel p = new JPanel(new GridLayout(0,1));
        JCheckBox sp = new JCheckBox("Single-player (AI)", singlePlayer);
        JCheckBox pa = new JCheckBox("Use pixel art (48×48)", pixelArt);
        JCheckBox sound = new JCheckBox("Enable sound", gohome.settings.Settings.get().isSoundEnabled());
        JSlider volume = new JSlider(0, 100, Math.round(gohome.settings.Settings.get().getVolume() * 100));
        volume.setMajorTickSpacing(25);
        volume.setPaintTicks(true);
        volume.setPaintLabels(true);
        p.add(sp); p.add(pa); p.add(sound); p.add(new JLabel("Volume")); p.add(volume);

        JPanel aiPanel = new JPanel(new GridLayout(0,2));
        aiPanel.add(new JLabel("AI side:"));
        JComboBox<String> sideBox = new JComboBox<>(new String[]{"BLUE","RED"});
        sideBox.setSelectedItem(aiSide.name());
        aiPanel.add(sideBox);
        aiPanel.add(new JLabel("AI level:"));
        JComboBox<String> levelBox = new JComboBox<>(new String[]{"1 - Straight Home","2 - Rule-based","3 - Minimax"});
        levelBox.setSelectedIndex(aiLevel-1);
        aiPanel.add(levelBox);

        JButton ok = new JButton("OK");
        ok.addActionListener(e -> {
            singlePlayer = sp.isSelected();
            pixelArt = pa.isSelected();
            aiSide = Figure.Color.valueOf((String) sideBox.getSelectedItem());
            aiLevel = levelBox.getSelectedIndex() + 1;
            // sound settings
            gohome.settings.Settings.get().setSoundEnabled(sound.isSelected());
            gohome.settings.Settings.get().setVolume(volume.getValue() / 100f);
            setVisible(false);
            dispose();
        });

        add(p, BorderLayout.NORTH);
        add(aiPanel, BorderLayout.CENTER);
        add(ok, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isSinglePlayer() { return singlePlayer; }
    public boolean isPixelArt() { return pixelArt; }
    public int getAiLevel() { return aiLevel; }
    public Figure.Color getAiSide() { return aiSide; }
}
