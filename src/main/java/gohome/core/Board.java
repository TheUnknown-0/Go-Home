package gohome.core;

public class Board {
    private final int width;
    private final int height;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int wrapX(int x) {
        int r = x % width;
        if (r < 0) r += width;
        return r;
    }

    public int wrapY(int y) {
        int r = y % height;
        if (r < 0) r += height;
        return r;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isHome(int x, int y) {
        return x == width/2 && y == height/2;
    }
}
