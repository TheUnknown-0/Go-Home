package gohome.ui;

import gohome.core.Figure;

public class MoveTarget {
    public final Figure.Color color;
    public final int fromX, fromY, toX, toY; // board coordinates
    public final int steps;

    public MoveTarget(Figure.Color color, int fromX, int fromY, int toX, int toY, int steps) {
        this.color = color;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.steps = steps;
    }
}