import java.util.ArrayList;
import java.util.List;

/**
 * Main game controller that manages the game logic and state.
 */
public class Game {
    private Board board;
    private List<Player> players;
    private Dice dice;
    private int currentPlayerIndex;
    private boolean gameOver;
    private Player winner;
    
    /**
     * Creates a new game with the specified number of players.
     * @param numPlayers Number of players (2-4)
     */
    public Game(int numPlayers) {
        if (numPlayers < 2 || numPlayers > 4) {
            throw new IllegalArgumentException("Number of players must be between 2 and 4");
        }
        
        this.board = new Board(40); // Standard board size
        this.dice = new Dice();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameOver = false;
        this.winner = null;
        
        // Initialize players with different colors and positions
        String[] colors = {"Rot", "Blau", "Grün", "Gelb"};
        int[] startPositions = {0, 10, 20, 30};
        int[] homePositions = {39, 9, 19, 29};
        
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Spieler " + (i + 1), colors[i], 
                                  startPositions[i], homePositions[i]));
        }
    }
    
    /**
     * Gets the current player.
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    /**
     * Rolls the dice.
     * @return The dice roll result
     */
    public int rollDice() {
        return dice.roll();
    }
    
    /**
     * Gets the last dice roll.
     * @return The last roll result
     */
    public int getLastDiceRoll() {
        return dice.getLastRoll();
    }
    
    /**
     * Moves a figure by the current dice roll.
     * @param figure The figure to move
     * @return true if move was successful
     */
    public boolean moveFigure(Figure figure) {
        if (figure == null || figure.isHome()) {
            return false;
        }
        
        int diceRoll = dice.getLastRoll();
        int currentPos = figure.getCurrentPosition();
        
        // If figure is not on board yet, place it at start position
        if (currentPos == -1) {
            if (diceRoll == 6) {
                int startPos = figure.getOwner().getStartPosition();
                board.placeFigure(figure, startPos);
                return true;
            }
            return false;
        }
        
        // Calculate new position
        int newPos = currentPos + diceRoll;
        int homePos = figure.getOwner().getHomePosition();
        
        // Check if figure reaches home
        if (newPos >= homePos) {
            board.removeFigure(currentPos);
            figure.setHome();
            return true;
        }
        
        // Move figure on board
        if (newPos < board.getSize()) {
            Figure kicked = board.moveFigure(currentPos, newPos);
            return true;
        }
        
        return false;
    }
    
    /**
     * Advances to the next player's turn.
     */
    public void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        checkWinCondition();
    }
    
    /**
     * Checks if any player has won.
     */
    private void checkWinCondition() {
        for (Player player : players) {
            if (player.hasWon()) {
                gameOver = true;
                winner = player;
                break;
            }
        }
    }
    
    /**
     * Checks if the game is over.
     * @return true if game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Gets the winner of the game.
     * @return The winning player, or null if game is not over
     */
    public Player getWinner() {
        return winner;
    }
    
    /**
     * Gets all players.
     * @return List of players
     */
    public List<Player> getPlayers() {
        return players;
    }
    
    /**
     * Gets the game board.
     * @return The board
     */
    public Board getBoard() {
        return board;
    }
    
    /**
     * Resets the game to initial state.
     */
    public void reset() {
        int numPlayers = players.size();
        this.board = new Board(40);
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameOver = false;
        this.winner = null;
        
        String[] colors = {"Rot", "Blau", "Grün", "Gelb"};
        int[] startPositions = {0, 10, 20, 30};
        int[] homePositions = {39, 9, 19, 29};
        
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Spieler " + (i + 1), colors[i], 
                                  startPositions[i], homePositions[i]));
        }
    }
}
