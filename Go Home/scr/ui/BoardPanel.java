package ui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Panel that displays the game board and handles mouse interactions.
 */
public class BoardPanel extends JPanel {
    private Game game;
    private static final int FIELD_SIZE = 30;
    private static final int PIECE_SIZE = 20;
    private static final int BOARD_SIZE = 600;
    
    public BoardPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        setBackground(new Color(240, 240, 240));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawBoard(g2d);
        drawPieces(g2d);
        drawPlayerInfo(g2d);
    }
    
    private void drawBoard(Graphics2D g) {
        int centerX = BOARD_SIZE / 2;
        int centerY = BOARD_SIZE / 2;
        int radius = 200;
        
        // Draw main circular path (40 fields)
        for (int i = 0; i < 40; i++) {
            double angle = Math.toRadians(i * 9); // 360/40 = 9 degrees per field
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            
            Field field = game.getBoard().getField(i);
            
            // Color start fields
            Color fieldColor = Color.WHITE;
            if (field.getType() == FieldType.START) {
                // Determine which player's start field
                if (i == 0) fieldColor = new Color(255, 200, 200); // Red
                else if (i == 10) fieldColor = new Color(200, 200, 255); // Blue
                else if (i == 20) fieldColor = new Color(200, 255, 200); // Green
                else if (i == 30) fieldColor = new Color(255, 255, 200); // Yellow
            }
            
            g.setColor(fieldColor);
            g.fillOval(x - FIELD_SIZE/2, y - FIELD_SIZE/2, FIELD_SIZE, FIELD_SIZE);
            g.setColor(Color.BLACK);
            g.drawOval(x - FIELD_SIZE/2, y - FIELD_SIZE/2, FIELD_SIZE, FIELD_SIZE);
            
            // Draw field number
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            String label = String.valueOf(i);
            g.drawString(label, x - 5, y + 5);
        }
        
        // Draw home paths for each player
        drawHomePath(g, 0, Color.RED, centerX - 150, centerY - 80);
        drawHomePath(g, 1, Color.BLUE, centerX + 80, centerY - 80);
        drawHomePath(g, 2, Color.GREEN, centerX + 80, centerY + 50);
        drawHomePath(g, 3, Color.YELLOW, centerX - 150, centerY + 50);
    }
    
    private void drawHomePath(Graphics2D g, int playerIndex, Color color, int startX, int startY) {
        for (int i = 0; i < 4; i++) {
            int fieldIndex = 40 + (playerIndex * 4) + i;
            int x = startX + (i * 35);
            int y = startY;
            
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
            g.fillRect(x - FIELD_SIZE/2, y - FIELD_SIZE/2, FIELD_SIZE, FIELD_SIZE);
            g.setColor(color);
            g.drawRect(x - FIELD_SIZE/2, y - FIELD_SIZE/2, FIELD_SIZE, FIELD_SIZE);
            
            // Mark finish field
            if (i == 3) {
                g.setFont(new Font("Arial", Font.BOLD, 12));
                g.drawString("★", x - 5, y + 5);
            }
        }
    }
    
    private void drawPieces(Graphics2D g) {
        List<Player> players = game.getPlayers();
        
        for (Player player : players) {
            for (Piece piece : player.getPieces()) {
                Point pos = getPiecePosition(piece, player);
                
                // Draw piece
                g.setColor(player.getColor());
                g.fillOval(pos.x - PIECE_SIZE/2, pos.y - PIECE_SIZE/2, PIECE_SIZE, PIECE_SIZE);
                g.setColor(Color.BLACK);
                g.drawOval(pos.x - PIECE_SIZE/2, pos.y - PIECE_SIZE/2, PIECE_SIZE, PIECE_SIZE);
                
                // Highlight selected piece
                if (piece == game.getSelectedPiece()) {
                    g.setColor(Color.YELLOW);
                    g.setStroke(new BasicStroke(3));
                    g.drawOval(pos.x - PIECE_SIZE/2 - 2, pos.y - PIECE_SIZE/2 - 2, PIECE_SIZE + 4, PIECE_SIZE + 4);
                    g.setStroke(new BasicStroke(1));
                }
            }
        }
    }
    
    private Point getPiecePosition(Piece piece, Player player) {
        int centerX = BOARD_SIZE / 2;
        int centerY = BOARD_SIZE / 2;
        int radius = 200;
        
        if (piece.isAtHome()) {
            // Position in home area (off board)
            int playerIndex = game.getPlayers().indexOf(player);
            List<Piece> piecesAtHome = player.getPiecesAtHome();
            int indexInHome = piecesAtHome.indexOf(piece);
            
            int homeX = 0, homeY = 0;
            switch (playerIndex) {
                case 0: homeX = 50; homeY = 50 + indexInHome * 25; break; // Red
                case 1: homeX = BOARD_SIZE - 50; homeY = 50 + indexInHome * 25; break; // Blue
                case 2: homeX = BOARD_SIZE - 50; homeY = BOARD_SIZE - 50 - indexInHome * 25; break; // Green
                case 3: homeX = 50; homeY = BOARD_SIZE - 50 - indexInHome * 25; break; // Yellow
            }
            return new Point(homeX, homeY);
        } else if (piece.isInGoal()) {
            // Position in goal/home path
            int playerIndex = game.getPlayers().indexOf(player);
            int homePathIndex = piece.getPosition() - 40 - (playerIndex * 4);
            
            int homeX = 0, homeY = 0;
            switch (playerIndex) {
                case 0: homeX = centerX - 150 + homePathIndex * 35; homeY = centerY - 80; break; // Red
                case 1: homeX = centerX + 80 + homePathIndex * 35; homeY = centerY - 80; break; // Blue
                case 2: homeX = centerX + 80 + homePathIndex * 35; homeY = centerY + 50; break; // Green
                case 3: homeX = centerX - 150 + homePathIndex * 35; homeY = centerY + 50; break; // Yellow
            }
            return new Point(homeX, homeY);
        } else {
            // Position on main path
            int pos = piece.getPosition();
            double angle = Math.toRadians(pos * 9);
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            return new Point(x, y);
        }
    }
    
    private void handleMouseClick(int mouseX, int mouseY) {
        if (game.getLastRoll() == 0) {
            return; // Must roll dice first
        }
        
        Player currentPlayer = game.getCurrentPlayer();
        
        // Check if clicked on a piece
        for (Piece piece : currentPlayer.getPieces()) {
            Point pos = getPiecePosition(piece, currentPlayer);
            int dx = mouseX - pos.x;
            int dy = mouseY - pos.y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance <= PIECE_SIZE) {
                // Try to move the piece
                if (game.canMovePiece(piece, game.getLastRoll())) {
                    game.movePiece(piece, game.getLastRoll());
                    game.nextTurn();
                    repaint();
                    getParent().repaint(); // Update control panel
                    return;
                } else {
                    game.setStatusMessage("Ungültiger Zug!");
                    repaint();
                    getParent().repaint();
                    return;
                }
            }
        }
    }
    
    private void drawPlayerInfo(Graphics2D g) {
        // Draw current player indicator
        Player currentPlayer = game.getCurrentPlayer();
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(currentPlayer.getColor());
        g.drawString("Aktueller Spieler: " + currentPlayer.getName(), 10, 20);
    }
}
