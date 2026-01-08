package model;

/**
 * Represents a field on the game board.
 * A field can be occupied by a piece or be empty.
 */
public class Field {
    private int index;
    private Piece occupant;
    private FieldType type;
    
    public Field(int index, FieldType type) {
        this.index = index;
        this.type = type;
        this.occupant = null;
    }
    
    public int getIndex() {
        return index;
    }
    
    public FieldType getType() {
        return type;
    }
    
    public Piece getOccupant() {
        return occupant;
    }
    
    public void setOccupant(Piece piece) {
        this.occupant = piece;
    }
    
    public void removeOccupant() {
        this.occupant = null;
    }
    
    public boolean isOccupied() {
        return occupant != null;
    }
}
