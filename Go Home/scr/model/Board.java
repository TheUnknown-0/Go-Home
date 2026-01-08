package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board with all fields.
 * The board is composed of fields (composition relationship).
 */
public class Board {
    private List<Field> fields;
    private static final int MAIN_PATH_LENGTH = 40;
    private static final int HOME_PATH_LENGTH = 4; // Per player
    
    public Board() {
        this.fields = new ArrayList<>();
        initializeFields();
    }
    
    private void initializeFields() {
        // Create main circular path (40 fields)
        for (int i = 0; i < MAIN_PATH_LENGTH; i++) {
            FieldType type = FieldType.NORMAL;
            
            // Mark start positions for each player
            if (i == 0 || i == 10 || i == 20 || i == 30) {
                type = FieldType.START;
            }
            
            fields.add(new Field(i, type));
        }
        
        // Create home paths for 4 players (4 fields each)
        for (int player = 0; player < 4; player++) {
            for (int i = 0; i < HOME_PATH_LENGTH; i++) {
                int index = MAIN_PATH_LENGTH + (player * HOME_PATH_LENGTH) + i;
                FieldType type = (i == HOME_PATH_LENGTH - 1) ? FieldType.FINISH : FieldType.HOME;
                fields.add(new Field(index, type));
            }
        }
    }
    
    public Field getField(int index) {
        if (index < 0 || index >= fields.size()) {
            return null;
        }
        return fields.get(index);
    }
    
    public boolean isOccupied(int index) {
        Field field = getField(index);
        return field != null && field.isOccupied();
    }
    
    public void setOccupant(int index, Piece piece) {
        Field field = getField(index);
        if (field != null) {
            field.setOccupant(piece);
        }
    }
    
    public void removeOccupant(int index) {
        Field field = getField(index);
        if (field != null) {
            field.removeOccupant();
        }
    }
    
    public List<Field> getFields() {
        return fields;
    }
    
    public int getMainPathLength() {
        return MAIN_PATH_LENGTH;
    }
}
