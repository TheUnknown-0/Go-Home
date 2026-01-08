import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board with fields and manages the board state.
 */
public class Board {
    private List<Field> fields;
    private int boardSize;
    
    /**
     * Creates a new board with the specified number of fields.
     * @param size The number of fields on the board
     */
    public Board(int size) {
        this.boardSize = size;
        this.fields = new ArrayList<>();
        
        // Initialize all fields
        for (int i = 0; i < size; i++) {
            fields.add(new Field(i));
        }
    }
    
    /**
     * Gets a field at the specified position.
     * @param position The position of the field
     * @return The field at that position
     */
    public Field getField(int position) {
        if (position >= 0 && position < boardSize) {
            return fields.get(position);
        }
        return null;
    }
    
    /**
     * Places a figure on the board at the specified position.
     * @param figure The figure to place
     * @param position The position to place it at
     * @return true if successful, false if position is occupied
     */
    public boolean placeFigure(Figure figure, int position) {
        Field field = getField(position);
        if (field != null && field.isEmpty()) {
            field.setFigure(figure);
            figure.setCurrentPosition(position);
            return true;
        }
        return false;
    }
    
    /**
     * Moves a figure from one position to another.
     * @param fromPosition The current position
     * @param toPosition The target position
     * @return The figure that was kicked off, or null
     */
    public Figure moveFigure(int fromPosition, int toPosition) {
        Field fromField = getField(fromPosition);
        Field toField = getField(toPosition);
        
        if (fromField == null || toField == null || fromField.isEmpty()) {
            return null;
        }
        
        Figure movingFigure = fromField.removeFigure();
        Figure kickedFigure = null;
        
        // If target field is occupied, kick that figure off
        if (!toField.isEmpty()) {
            kickedFigure = toField.removeFigure();
            kickedFigure.setCurrentPosition(-1);
        }
        
        toField.setFigure(movingFigure);
        movingFigure.setCurrentPosition(toPosition);
        
        return kickedFigure;
    }
    
    /**
     * Removes a figure from the board.
     * @param position The position to remove from
     * @return The removed figure
     */
    public Figure removeFigure(int position) {
        Field field = getField(position);
        if (field != null && !field.isEmpty()) {
            Figure figure = field.removeFigure();
            figure.setCurrentPosition(-1);
            return figure;
        }
        return null;
    }
    
    /**
     * Gets the size of the board.
     * @return The number of fields
     */
    public int getSize() {
        return this.boardSize;
    }
    
    /**
     * Gets all fields on the board.
     * @return List of all fields
     */
    public List<Field> getAllFields() {
        return this.fields;
    }
}
