import javax.swing.SwingUtilities;

/**
 * Main entry point for the Go Home game.
 * Launches the game UI.
 */
public class Main {
    public static void main(String[] args) {
        // Launch the UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new GameUI();
        });
    }
}
