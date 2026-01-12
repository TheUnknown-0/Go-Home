package gohome.ui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception e) {
                System.err.println("Failed to set Look and Feel: " + e.getMessage());
            }

            MainWindow mw = new MainWindow();
            mw.setVisible(true);
        });
    }
}
