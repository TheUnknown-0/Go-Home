/**
 * Represents a game figure (pawn) that can move on the board.
 */
public class Figure {
    private String color;
    private int currentPosition;
    private boolean isHome;
    private Player owner;
    
    /**
     * Creates a new figure with the given color.
     * @param color The color of the figure
     * @param owner The player who owns this figure
     */
    public Figure(String color, Player owner) {
        this.color = color;
        this.owner = owner;
        this.currentPosition = -1; // -1 means not on board yet
        this.isHome = false;
    }
    
    /**
     * Gets the color of this figure.
     * @return The color string
     */
    public String getColor() {
        return this.color;
    }
    
    /**
     * Gets the current position of this figure.
     * @return The position number, or -1 if not on board
     */
    public int getCurrentPosition() {
        return this.currentPosition;
    }
    
    /**
     * Sets the current position of this figure.
     * @param position The new position
     */
    public void setCurrentPosition(int position) {
        this.currentPosition = position;
    }
    
    /**
     * Checks if this figure has reached home.
     * @return true if the figure is home
     */
    public boolean isHome() {
        return this.isHome;
    }
    
    /**
     * Marks this figure as having reached home.
     */
    public void setHome() {
        this.isHome = true;
    }
    
    /**
     * Gets the player who owns this figure.
     * @return The owner player
     */
    public Player getOwner() {
        return this.owner;
    }
    
    /**
     * Moves this figure by the given number of steps.
     * @param steps Number of steps to move
     * @return The new position
     */
    public int move(int steps) {
        this.currentPosition += steps;
        return this.currentPosition;
    }
}
