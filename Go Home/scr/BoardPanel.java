import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel that visualizes the game board and figures.
 */
public class BoardPanel extends JPanel {
    private Game game;
    private Map<String, Color> colorMap;
    private static final int FIELD_SIZE = 40;
    private static final int FIGURE_SIZE = 30;
    
    /**
     * Creates a new board panel for the given game.
     * @param game The game to visualize
     */
    public BoardPanel(Game game) {
        this.game = game;
        this.colorMap = new HashMap<>();
        initializeColorMap();
        
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(245, 245, 220)); // Beige background
    }
    
    /**
     * Initializes the color mapping for players.
     */
    private void initializeColorMap() {
        colorMap.put("Rot", Color.RED);
        colorMap.put("Blau", Color.BLUE);
        colorMap.put("Grün", Color.GREEN);
        colorMap.put("Gelb", Color.YELLOW);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawBoard(g2d);
        drawFigures(g2d);
        drawPlayerInfo(g2d);
    }
    
    /**
     * Draws the game board fields.
     */
    private void drawBoard(Graphics2D g2d) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = 200;
        
        Board board = game.getBoard();
        int numFields = board.getSize();
        
        // Draw circular board
        for (int i = 0; i < numFields; i++) {
            double angle = 2 * Math.PI * i / numFields - Math.PI / 2;
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));
            
            // Draw field
            g2d.setColor(Color.WHITE);
            g2d.fillRect(x - FIELD_SIZE / 2, y - FIELD_SIZE / 2, FIELD_SIZE, FIELD_SIZE);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x - FIELD_SIZE / 2, y - FIELD_SIZE / 2, FIELD_SIZE, FIELD_SIZE);
            
            // Draw field number
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            String fieldNum = String.valueOf(i);
            int textWidth = g2d.getFontMetrics().stringWidth(fieldNum);
            g2d.drawString(fieldNum, x - textWidth / 2, y + 5);
        }
        
        // Highlight start positions
        for (Player player : game.getPlayers()) {
            int startPos = player.getStartPosition();
            double angle = 2 * Math.PI * startPos / numFields - Math.PI / 2;
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));
            
            Color playerColor = colorMap.get(player.getColor());
            g2d.setColor(new Color(playerColor.getRed(), playerColor.getGreen(), 
                                  playerColor.getBlue(), 100));
            g2d.fillRect(x - FIELD_SIZE / 2, y - FIELD_SIZE / 2, FIELD_SIZE, FIELD_SIZE);
        }
    }
    
    /**
     * Draws all figures on the board.
     */
    private void drawFigures(Graphics2D g2d) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = 200;
        int numFields = game.getBoard().getSize();
        
        for (Player player : game.getPlayers()) {
            Color playerColor = colorMap.get(player.getColor());
            g2d.setColor(playerColor);
            
            for (Figure figure : player.getFigures()) {
                if (figure.isHome()) {
                    // Draw figures that reached home in a corner
                    int homeIndex = game.getPlayers().indexOf(player);
                    int homeX = 50 + (homeIndex % 2) * 700;
                    int homeY = 50 + (homeIndex / 2) * 500;
                    int figIndex = player.getFigures().indexOf(figure);
                    
                    g2d.fillOval(homeX + figIndex * 35, homeY, FIGURE_SIZE, FIGURE_SIZE);
                    g2d.setColor(Color.BLACK);
                    g2d.drawOval(homeX + figIndex * 35, homeY, FIGURE_SIZE, FIGURE_SIZE);
                    g2d.setColor(playerColor);
                } else if (figure.getCurrentPosition() >= 0) {
                    // Draw figure on board
                    int pos = figure.getCurrentPosition();
                    double angle = 2 * Math.PI * pos / numFields - Math.PI / 2;
                    int x = centerX + (int) (radius * Math.cos(angle));
                    int y = centerY + (int) (radius * Math.sin(angle));
                    
                    g2d.fillOval(x - FIGURE_SIZE / 2, y - FIGURE_SIZE / 2, 
                               FIGURE_SIZE, FIGURE_SIZE);
                    g2d.setColor(Color.BLACK);
                    g2d.drawOval(x - FIGURE_SIZE / 2, y - FIGURE_SIZE / 2, 
                               FIGURE_SIZE, FIGURE_SIZE);
                    g2d.setColor(playerColor);
                }
            }
        }
    }
    
    /**
     * Draws player information on the board.
     */
    private void drawPlayerInfo(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);
            Color playerColor = colorMap.get(player.getColor());
            
            int x = 50;
            int y = 150 + i * 100;
            
            // Draw player name and color
            g2d.setColor(playerColor);
            g2d.fillRect(x, y, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, 20, 20);
            g2d.drawString(player.getName(), x + 30, y + 15);
            
            // Show if current player
            if (game.getCurrentPlayer() == player) {
                g2d.drawString("← Aktuell", x + 150, y + 15);
            }
            
            // Show figures at home count
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString("Im Ziel: " + player.getFiguresAtHome() + "/4", x + 30, y + 35);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
        }
    }
    
    /**
     * Refreshes the board display.
     */
    public void refresh() {
        repaint();
    }
}
