import java.util.Random;

/**
 * Represents a six-sided dice.
 */
public class Dice {
    private Random random;
    private int lastRoll;
    
    /**
     * Creates a new dice.
     */
    public Dice() {
        this.random = new Random();
        this.lastRoll = 0;
    }
    
    /**
     * Rolls the dice and returns a random number between 1 and 6.
     * @return The dice roll result (1-6)
     */
    public int roll() {
        this.lastRoll = random.nextInt(6) + 1;
        return this.lastRoll;
    }
    
    /**
     * Gets the result of the last roll.
     * @return The last roll result
     */
    public int getLastRoll() {
        return this.lastRoll;
    }
}
