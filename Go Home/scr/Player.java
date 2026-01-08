import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 */
public class Player {
    private String name;
    private String color;
    private List<Figure> figures;
    private int startPosition;
    private int homePosition;
    
    /**
     * Creates a new player with the given name and color.
     * @param name The player's name
     * @param color The player's color
     * @param startPosition The starting position for this player's figures
     * @param homePosition The home position for this player
     */
    public Player(String name, String color, int startPosition, int homePosition) {
        this.name = name;
        this.color = color;
        this.startPosition = startPosition;
        this.homePosition = homePosition;
        this.figures = new ArrayList<>();
        
        // Create 4 figures for this player
        for (int i = 0; i < 4; i++) {
            this.figures.add(new Figure(color, this));
        }
    }
    
    /**
     * Gets the player's name.
     * @return The name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Gets the player's color.
     * @return The color
     */
    public String getColor() {
        return this.color;
    }
    
    /**
     * Gets all figures belonging to this player.
     * @return List of figures
     */
    public List<Figure> getFigures() {
        return this.figures;
    }
    
    /**
     * Gets the starting position for this player.
     * @return The start position
     */
    public int getStartPosition() {
        return this.startPosition;
    }
    
    /**
     * Gets the home position for this player.
     * @return The home position
     */
    public int getHomePosition() {
        return this.homePosition;
    }
    
    /**
     * Checks if all figures have reached home.
     * @return true if all figures are home
     */
    public boolean hasWon() {
        for (Figure figure : figures) {
            if (!figure.isHome()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Gets the number of figures that are home.
     * @return Count of figures at home
     */
    public int getFiguresAtHome() {
        int count = 0;
        for (Figure figure : figures) {
            if (figure.isHome()) {
                count++;
            }
        }
        return count;
    }
}
