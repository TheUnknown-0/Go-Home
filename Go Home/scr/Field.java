/**
 * Represents a field on the game board.
 * A field can hold a figure or be empty.
 */
public class Field {
    private Figure figure;
    private int position;
    
    /**
     * Creates a new field at the given position.
     * @param position The position of this field on the board
     */
    public Field(int position) {
        this.position = position;
        this.figure = null;
    }
    
    /**
     * Places a figure on this field.
     * @param figure The figure to place
     */
    public void setFigure(Figure figure) {
        this.figure = figure;
    }
    
    /**
     * Gets the figure on this field.
     * @return The figure on this field, or null if empty
     */
    public Figure getFigure() {
        return this.figure;
    }
    
    /**
     * Checks if this field is empty.
     * @return true if no figure is on this field
     */
    public boolean isEmpty() {
        return this.figure == null;
    }
    
    /**
     * Removes and returns the figure from this field.
     * @return The figure that was on this field
     */
    public Figure removeFigure() {
        Figure temp = this.figure;
        this.figure = null;
        return temp;
    }
    
    /**
     * Gets the position of this field.
     * @return The position number
     */
    public int getPosition() {
        return this.position;
    }
}
