package model;

import java.util.Random;

/**
 * Represents a dice that can be rolled to generate random numbers 1-6.
 */
public class Dice {
    private Random random;
    
    public Dice() {
        this.random = new Random();
    }
    
    /**
     * Rolls the dice and returns a value between 1 and 6.
     * @return Random value between 1 and 6
     */
    public int roll() {
        return random.nextInt(6) + 1;
    }
}
