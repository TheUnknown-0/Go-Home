package ui;

import model.Game;
import javax.swing.*;
import java.awt.*;

/**
 * Main application window for the Go Home board game.
 */
public class GoHomeMain extends JFrame {
    private Game game;
    private BoardPanel boardPanel;
    private ControlPanel controlPanel;
    
    public GoHomeMain() {
        game = new Game();
        game.startGame();
        
        setTitle("Go Home - Brettspiel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create panels
        boardPanel = new BoardPanel(game);
        controlPanel = new ControlPanel(game, boardPanel);
        
        // Add panels to frame
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        // Create menu bar
        createMenuBar();
        
        // Finalize window
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu gameMenu = new JMenu("Spiel");
        
        JMenuItem newGameItem = new JMenuItem("Neues Spiel");
        newGameItem.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                "Möchten Sie ein neues Spiel starten?",
                "Neues Spiel",
                JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                game = new Game();
                game.startGame();
                boardPanel = new BoardPanel(game);
                controlPanel = new ControlPanel(game, boardPanel);
                
                getContentPane().removeAll();
                add(boardPanel, BorderLayout.CENTER);
                add(controlPanel, BorderLayout.SOUTH);
                revalidate();
                repaint();
            }
        });
        gameMenu.add(newGameItem);
        
        gameMenu.addSeparator();
        
        JMenuItem rulesItem = new JMenuItem("Spielregeln");
        rulesItem.addActionListener(e -> showRules());
        gameMenu.add(rulesItem);
        
        gameMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("Beenden");
        exitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(exitItem);
        
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }
    
    private void showRules() {
        String rules = """
            Go Home - Spielregeln
            
            Ziel des Spiels:
            Bringe alle 4 eigenen Figuren vom Startbereich ins Ziel!
            
            Spielablauf:
            1. Würfeln Sie, um die Anzahl der Schritte zu erhalten
            2. Klicken Sie auf eine Ihrer Figuren, um sie zu bewegen
            3. Mit einer 6 können Sie eine neue Figur ins Spiel bringen
            4. Wenn Sie auf ein Feld mit einer gegnerischen Figur ziehen,
               wird diese geschlagen und muss neu starten
            5. Sie können Ihre eigenen Figuren nicht schlagen
            6. Ziehen Sie alle Figuren ins Zielfeld, um zu gewinnen!
            
            Steuerung:
            - "Würfeln": Würfelt den Würfel
            - Mausklick auf Figur: Bewegt die Figur
            - "Zug beenden": Beendet den aktuellen Zug
            """;
        
        JTextArea textArea = new JTextArea(rules);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this,
            scrollPane,
            "Spielregeln",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GoHomeMain());
    }
}
