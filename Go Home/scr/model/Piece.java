package model;

/**
 * Represents a game piece that belongs to a player.
 * The piece has a position on the board (-1 means not yet on the board).
 */
public class Piece {
    private Player owner;
    private int positionIndex;
    
    public Piece(Player owner) {
        this.owner = owner;
        this.positionIndex = -1; // Not on board yet
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public int getPosition() {
        return positionIndex;
    }
    
    public void setPosition(int index) {
        this.positionIndex = index;
    }
    
    public boolean isAtHome() {
        return positionIndex == -1;
    }
    
    public boolean isInGoal() {
        return positionIndex >= 40; // Fields 40+ are goal/home fields
    }
}
