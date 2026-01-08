package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game controller that manages the game state and logic.
 * Aggregates players and board.
 */
public class Game {
    private List<Player> players;
    private Board board;
    private int currentPlayerIndex;
    private Dice dice;
    private int lastRoll;
    private Piece selectedPiece;
    private String statusMessage;
    
    public Game() {
        this.board = new Board();
        this.dice = new Dice();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.lastRoll = 0;
        this.statusMessage = "";
        initializePlayers();
    }
    
    private void initializePlayers() {
        // Create 4 players with different colors at different start positions
        players.add(new Player("Rot", Color.RED, 0));
        players.add(new Player("Blau", Color.BLUE, 10));
        players.add(new Player("Grün", Color.GREEN, 20));
        players.add(new Player("Gelb", Color.YELLOW, 30));
    }
    
    public void startGame() {
        currentPlayerIndex = 0;
        lastRoll = 0;
        selectedPiece = null;
        statusMessage = getCurrentPlayer().getName() + " ist am Zug!";
    }
    
    public int rollDice() {
        lastRoll = dice.roll();
        statusMessage = getCurrentPlayer().getName() + " hat " + lastRoll + " gewürfelt!";
        return lastRoll;
    }
    
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    public void nextTurn() {
        selectedPiece = null;
        
        // Check if current player won
        if (getCurrentPlayer().allPiecesHome()) {
            statusMessage = getCurrentPlayer().getName() + " hat gewonnen!";
            return;
        }
        
        // Move to next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        
        // Skip players who have already won
        int skipCount = 0;
        while (getCurrentPlayer().allPiecesHome() && skipCount < players.size()) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            skipCount++;
        }
        
        lastRoll = 0;
        statusMessage = getCurrentPlayer().getName() + " ist am Zug!";
    }
    
    public boolean canMovePiece(Piece piece, int steps) {
        if (piece == null || piece.getOwner() != getCurrentPlayer()) {
            return false;
        }
        
        // If piece is at home, can only enter with a 6
        if (piece.isAtHome()) {
            return steps == 6;
        }
        
        // If piece is already in goal, cannot move
        if (piece.isInGoal()) {
            return false;
        }
        
        int currentPos = piece.getPosition();
        int newPos = calculateNewPosition(piece, steps);
        
        // Check if the move is valid
        if (newPos < 0) {
            return false;
        }
        
        // Check if target field is occupied by own piece
        Field targetField = board.getField(newPos);
        if (targetField != null && targetField.isOccupied()) {
            Piece occupant = targetField.getOccupant();
            if (occupant.getOwner() == getCurrentPlayer()) {
                return false; // Cannot capture own piece
            }
        }
        
        return true;
    }
    
    public boolean movePiece(Piece piece, int steps) {
        if (!canMovePiece(piece, steps)) {
            return false;
        }
        
        Player currentPlayer = getCurrentPlayer();
        
        // Handle entering the board from home
        if (piece.isAtHome()) {
            int startField = currentPlayer.getStartFieldIndex();
            Field field = board.getField(startField);
            
            // Check if start field is occupied
            if (field.isOccupied()) {
                Piece occupant = field.getOccupant();
                if (occupant.getOwner() == currentPlayer) {
                    statusMessage = "Startfeld ist durch eigene Figur blockiert!";
                    return false;
                } else {
                    // Capture opponent's piece
                    occupant.setPosition(-1);
                    board.removeOccupant(startField);
                    statusMessage = currentPlayer.getName() + " hat eine gegnerische Figur geschlagen!";
                }
            }
            
            piece.setPosition(startField);
            board.setOccupant(startField, piece);
            return true;
        }
        
        // Normal move
        int oldPos = piece.getPosition();
        int newPos = calculateNewPosition(piece, steps);
        
        if (newPos < 0) {
            return false;
        }
        
        // Remove from old position
        board.removeOccupant(oldPos);
        
        // Check target field
        Field targetField = board.getField(newPos);
        if (targetField.isOccupied()) {
            Piece captured = targetField.getOccupant();
            captured.setPosition(-1);
            statusMessage = currentPlayer.getName() + " hat eine Figur geschlagen!";
        }
        
        // Place on new position
        piece.setPosition(newPos);
        board.setOccupant(newPos, piece);
        
        return true;
    }
    
    private int calculateNewPosition(Piece piece, int steps) {
        int currentPos = piece.getPosition();
        Player owner = piece.getOwner();
        int playerIndex = players.indexOf(owner);
        int mainPathLength = board.getMainPathLength();
        
        // Calculate how far from home entrance
        int homeEntranceField = (owner.getStartFieldIndex() + mainPathLength - 1) % mainPathLength;
        
        int newPos = currentPos + steps;
        
        // Check if entering home path
        int distanceToHome = 0;
        for (int i = 1; i <= steps; i++) {
            int checkPos = (currentPos + i) % mainPathLength;
            if (checkPos == homeEntranceField) {
                // Enter home path
                distanceToHome = steps - i;
                int homeStartIndex = mainPathLength + (playerIndex * 4);
                newPos = homeStartIndex + distanceToHome;
                
                // Check if move goes beyond finish
                if (newPos >= homeStartIndex + 4) {
                    return -1; // Invalid move
                }
                return newPos;
            }
        }
        
        // Stay on main path
        if (newPos < mainPathLength) {
            return newPos;
        } else {
            newPos = newPos % mainPathLength;
            return newPos;
        }
    }
    
    public void selectPiece(Piece piece) {
        this.selectedPiece = piece;
    }
    
    public Piece getSelectedPiece() {
        return selectedPiece;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public List<Player> getPlayers() {
        return players;
    }
    
    public int getLastRoll() {
        return lastRoll;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
    
    public void setStatusMessage(String message) {
        this.statusMessage = message;
    }
    
    public boolean checkWin(Player player) {
        return player.allPiecesHome();
    }
}
