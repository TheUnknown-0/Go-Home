package ui;

import model.Game;
import model.Player;
import javax.swing.*;
import java.awt.*;

/**
 * Panel that displays game controls and status information.
 */
public class ControlPanel extends JPanel {
    private Game game;
    private JButton rollButton;
    private JButton nextTurnButton;
    private JLabel statusLabel;
    private JLabel diceLabel;
    private BoardPanel boardPanel;
    
    public ControlPanel(Game game, BoardPanel boardPanel) {
        this.game = game;
        this.boardPanel = boardPanel;
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 100));
        setBackground(new Color(220, 220, 220));
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        // Top panel for status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(new Color(220, 220, 220));
        statusLabel = new JLabel(game.getStatusMessage());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.NORTH);
        
        // Center panel for controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlsPanel.setBackground(new Color(220, 220, 220));
        
        rollButton = new JButton("Würfeln");
        rollButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollButton.setPreferredSize(new Dimension(120, 40));
        rollButton.addActionListener(e -> handleRollDice());
        controlsPanel.add(rollButton);
        
        diceLabel = new JLabel("🎲 -");
        diceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        controlsPanel.add(diceLabel);
        
        nextTurnButton = new JButton("Zug beenden");
        nextTurnButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextTurnButton.setPreferredSize(new Dimension(150, 40));
        nextTurnButton.setEnabled(false);
        nextTurnButton.addActionListener(e -> handleNextTurn());
        controlsPanel.add(nextTurnButton);
        
        add(controlsPanel, BorderLayout.CENTER);
        
        // Bottom panel for player info
        JPanel playerInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playerInfoPanel.setBackground(new Color(220, 220, 220));
        
        for (Player player : game.getPlayers()) {
            JLabel playerLabel = new JLabel("■ " + player.getName());
            playerLabel.setForeground(player.getColor());
            playerLabel.setFont(new Font("Arial", Font.BOLD, 12));
            playerInfoPanel.add(playerLabel);
            playerInfoPanel.add(Box.createHorizontalStrut(15));
        }
        
        add(playerInfoPanel, BorderLayout.SOUTH);
    }
    
    private void handleRollDice() {
        int roll = game.rollDice();
        diceLabel.setText("🎲 " + roll);
        statusLabel.setText(game.getStatusMessage() + " Wähle eine Figur!");
        rollButton.setEnabled(false);
        nextTurnButton.setEnabled(true);
        boardPanel.repaint();
    }
    
    private void handleNextTurn() {
        game.nextTurn();
        statusLabel.setText(game.getStatusMessage());
        diceLabel.setText("🎲 -");
        rollButton.setEnabled(true);
        nextTurnButton.setEnabled(false);
        boardPanel.repaint();
        
        // Check for winner
        for (Player player : game.getPlayers()) {
            if (game.checkWin(player)) {
                JOptionPane.showMessageDialog(this,
                    player.getName() + " hat gewonnen!",
                    "Spiel beendet",
                    JOptionPane.INFORMATION_MESSAGE);
                rollButton.setEnabled(false);
                nextTurnButton.setEnabled(false);
                return;
            }
        }
    }
    
    public void updateStatus() {
        statusLabel.setText(game.getStatusMessage());
        if (game.getLastRoll() > 0) {
            diceLabel.setText("🎲 " + game.getLastRoll());
        } else {
            diceLabel.setText("🎲 -");
        }
    }
}
