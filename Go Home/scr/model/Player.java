package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 * Each player has a name, color, and a collection of pieces.
 */
public class Player {
    private String name;
    private Color color;
    private List<Piece> pieces;
    private int startFieldIndex;
    
    public Player(String name, Color color, int startFieldIndex) {
        this.name = name;
        this.color = color;
        this.startFieldIndex = startFieldIndex;
        this.pieces = new ArrayList<>();
        
        // Each player starts with 4 pieces
        for (int i = 0; i < 4; i++) {
            pieces.add(new Piece(this));
        }
    }
    
    public String getName() {
        return name;
    }
    
    public Color getColor() {
        return color;
    }
    
    public List<Piece> getPieces() {
        return pieces;
    }
    
    public int getStartFieldIndex() {
        return startFieldIndex;
    }
    
    /**
     * Checks if all pieces of this player have reached home (goal).
     * @return true if all pieces are in goal
     */
    public boolean allPiecesHome() {
        for (Piece piece : pieces) {
            if (!piece.isInGoal()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Gets pieces that are currently on the board (not at starting position).
     * @return List of pieces on board
     */
    public List<Piece> getPiecesOnBoard() {
        List<Piece> onBoard = new ArrayList<>();
        for (Piece piece : pieces) {
            if (!piece.isAtHome() && !piece.isInGoal()) {
                onBoard.add(piece);
            }
        }
        return onBoard;
    }
    
    /**
     * Gets pieces that are still at home (not yet on board).
     * @return List of pieces at home
     */
    public List<Piece> getPiecesAtHome() {
        List<Piece> atHome = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece.isAtHome()) {
                atHome.add(piece);
            }
        }
        return atHome;
    }
}
