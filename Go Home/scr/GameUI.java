import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Main UI window for the Go Home game.
 */
public class GameUI extends JFrame {
    private Game game;
    private BoardPanel boardPanel;
    private JLabel diceLabel;
    private JButton rollDiceButton;
    private JPanel figureButtonsPanel;
    private JTextArea statusArea;
    private int lastDiceRoll;
    
    /**
     * Creates a new game UI.
     */
    public GameUI() {
        initializeGame();
        setupUI();
    }
    
    /**
     * Initializes the game.
     */
    private void initializeGame() {
        // Ask for number of players
        String[] options = {"2 Spieler", "3 Spieler", "4 Spieler"};
        int choice = JOptionPane.showOptionDialog(
            null,
            "Wähle die Anzahl der Spieler:",
            "Go Home - Spieleranzahl",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        int numPlayers = choice + 2;
        if (choice == -1) {
            numPlayers = 2; // Default to 2 players if dialog is closed
        }
        
        game = new Game(numPlayers);
        lastDiceRoll = 0;
    }
    
    /**
     * Sets up the UI components.
     */
    private void setupUI() {
        setTitle("Go Home - Brettspiel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Create board panel
        boardPanel = new BoardPanel(game);
        add(boardPanel, BorderLayout.CENTER);
        
        // Create control panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.EAST);
        
        // Create bottom panel with status
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Menu bar
        createMenuBar();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        updateUI();
    }
    
    /**
     * Creates the control panel with dice and figure buttons.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(250, 600));
        
        // Title
        JLabel titleLabel = new JLabel("Steuerung");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Current player info
        JLabel playerLabel = new JLabel("Aktueller Spieler:");
        playerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(playerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Dice display
        diceLabel = new JLabel("Würfel: -");
        diceLabel.setFont(new Font("Arial", Font.BOLD, 36));
        diceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        diceLabel.setForeground(Color.RED);
        panel.add(diceLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Roll dice button
        rollDiceButton = new JButton("Würfeln");
        rollDiceButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollDiceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollDiceButton.addActionListener(e -> rollDice());
        panel.add(rollDiceButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Figure selection label
        JLabel figureLabel = new JLabel("Figur auswählen:");
        figureLabel.setFont(new Font("Arial", Font.BOLD, 14));
        figureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(figureLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Figure buttons panel
        figureButtonsPanel = new JPanel();
        figureButtonsPanel.setLayout(new GridLayout(4, 1, 5, 5));
        figureButtonsPanel.setMaximumSize(new Dimension(230, 200));
        panel.add(figureButtonsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Next player button
        JButton nextPlayerButton = new JButton("Nächster Spieler");
        nextPlayerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        nextPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextPlayerButton.addActionListener(e -> nextPlayer());
        panel.add(nextPlayerButton);
        
        return panel;
    }
    
    /**
     * Creates the bottom panel with status information.
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        statusArea = new JTextArea(3, 50);
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statusArea.setText("Willkommen beim Go Home Spiel!\nWürfle um zu beginnen.");
        
        JScrollPane scrollPane = new JScrollPane(statusArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the menu bar.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu gameMenu = new JMenu("Spiel");
        
        JMenuItem newGameItem = new JMenuItem("Neues Spiel");
        newGameItem.addActionListener(e -> newGame());
        gameMenu.add(newGameItem);
        
        gameMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("Beenden");
        exitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(exitItem);
        
        menuBar.add(gameMenu);
        
        JMenu helpMenu = new JMenu("Hilfe");
        JMenuItem rulesItem = new JMenuItem("Spielregeln");
        rulesItem.addActionListener(e -> showRules());
        helpMenu.add(rulesItem);
        
        JMenuItem aboutItem = new JMenuItem("Über");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);
        
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Rolls the dice.
     */
    private void rollDice() {
        if (game.isGameOver()) {
            JOptionPane.showMessageDialog(this, "Das Spiel ist vorbei!");
            return;
        }
        
        lastDiceRoll = game.rollDice();
        diceLabel.setText("Würfel: " + lastDiceRoll);
        
        Player currentPlayer = game.getCurrentPlayer();
        statusArea.append("\n" + currentPlayer.getName() + " würfelt: " + lastDiceRoll);
        
        rollDiceButton.setEnabled(false);
        updateFigureButtons();
        boardPanel.refresh();
    }
    
    /**
     * Moves a figure.
     */
    private void moveFigure(Figure figure) {
        Player currentPlayer = game.getCurrentPlayer();
        
        if (figure.getOwner() != currentPlayer) {
            JOptionPane.showMessageDialog(this, "Das ist nicht deine Figur!");
            return;
        }
        
        boolean success = game.moveFigure(figure);
        
        if (success) {
            if (figure.isHome()) {
                statusArea.append("\nFigur hat das Ziel erreicht!");
            } else {
                statusArea.append("\nFigur bewegt zu Position " + figure.getCurrentPosition());
            }
        } else {
            if (figure.getCurrentPosition() == -1 && lastDiceRoll != 6) {
                statusArea.append("\nDu brauchst eine 6 um zu starten!");
            } else {
                statusArea.append("\nZug nicht möglich!");
            }
        }
        
        boardPanel.refresh();
        
        // Check for winner
        if (game.isGameOver()) {
            Player winner = game.getWinner();
            statusArea.append("\n*** " + winner.getName() + " hat gewonnen! ***");
            JOptionPane.showMessageDialog(this, 
                winner.getName() + " hat gewonnen!", 
                "Spiel vorbei", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Advances to the next player.
     */
    private void nextPlayer() {
        game.nextPlayer();
        rollDiceButton.setEnabled(true);
        diceLabel.setText("Würfel: -");
        lastDiceRoll = 0;
        
        Player currentPlayer = game.getCurrentPlayer();
        statusArea.append("\n--- " + currentPlayer.getName() + " ist am Zug ---");
        
        updateUI();
    }
    
    /**
     * Updates the UI components.
     */
    private void updateUI() {
        updateFigureButtons();
        boardPanel.refresh();
    }
    
    /**
     * Updates the figure selection buttons.
     */
    private void updateFigureButtons() {
        figureButtonsPanel.removeAll();
        
        if (lastDiceRoll == 0) {
            JLabel label = new JLabel("Erst würfeln!");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            figureButtonsPanel.add(label);
        } else {
            Player currentPlayer = game.getCurrentPlayer();
            List<Figure> figures = currentPlayer.getFigures();
            
            for (int i = 0; i < figures.size(); i++) {
                Figure figure = figures.get(i);
                String buttonText = "Figur " + (i + 1);
                
                if (figure.isHome()) {
                    buttonText += " (Ziel)";
                } else if (figure.getCurrentPosition() == -1) {
                    buttonText += " (Start)";
                } else {
                    buttonText += " (Pos " + figure.getCurrentPosition() + ")";
                }
                
                JButton button = new JButton(buttonText);
                button.setEnabled(!figure.isHome());
                button.addActionListener(e -> moveFigure(figure));
                figureButtonsPanel.add(button);
            }
        }
        
        figureButtonsPanel.revalidate();
        figureButtonsPanel.repaint();
    }
    
    /**
     * Starts a new game.
     */
    private void newGame() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Möchtest du ein neues Spiel starten?",
            "Neues Spiel",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new GameUI();
        }
    }
    
    /**
     * Shows the game rules.
     */
    private void showRules() {
        String rules = "Go Home - Spielregeln\n\n" +
                      "Ziel: Bringe alle 4 Figuren ins Ziel!\n\n" +
                      "Regeln:\n" +
                      "1. Würfle um eine Zahl zu erhalten\n" +
                      "2. Du brauchst eine 6 um eine Figur zu starten\n" +
                      "3. Bewege eine Figur um die gewürfelte Anzahl\n" +
                      "4. Wenn du auf ein Feld mit einer gegnerischen Figur kommst,\n" +
                      "   wird diese zurück zum Start geschickt\n" +
                      "5. Erreiche das Ziel mit allen Figuren um zu gewinnen\n" +
                      "6. Klicke auf 'Nächster Spieler' nach deinem Zug";
        
        JOptionPane.showMessageDialog(this, rules, "Spielregeln", 
                                     JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows about information.
     */
    private void showAbout() {
        String about = "Go Home - Brettspiel\n\n" +
                      "Version 1.0\n" +
                      "Ein klassisches Brettspiel implementiert in Java mit Swing\n\n" +
                      "Basierend auf: https://inf-schule.de/oop/java/beziehungen/gohome/";
        
        JOptionPane.showMessageDialog(this, about, "Über Go Home", 
                                     JOptionPane.INFORMATION_MESSAGE);
    }
}
