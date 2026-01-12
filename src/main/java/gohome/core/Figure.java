package gohome.core;

public class Figure {
    public enum Color { RED, BLUE }
    public enum Dir { UP, RIGHT, DOWN, LEFT }

    private final Color color;
    private int x, y;

    public Figure(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public Color getColor() { return color; }
    public int getX() { return x; }
    public int getY() { return y; }

    public void move(Dir dir, int steps, Board board) {
        switch(dir) {
            case UP: y = board.wrapY(y - steps); break;
            case RIGHT: x = board.wrapX(x + steps); break;
            case DOWN: y = board.wrapY(y + steps); break;
            case LEFT: x = board.wrapX(x - steps); break;
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isAtHome(Board board) {
        return board.isHome(x, y);
    }

    @Override
    public String toString() {
        return String.format("Figure[%s,@%d,%d]", color, x, y);
    }
}
