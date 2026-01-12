package gohome.ai;

import gohome.core.Game;
import gohome.core.Figure;

/**
 * Level 1: straight home - picks direction that reduces distance to home for own piece
 */
public class SimpleAI implements AIPlayer {
    private final Figure.Color myColor;

    public SimpleAI(Figure.Color myColor) {
        this.myColor = myColor;
    }

    @Override
    public Figure.Dir chooseDirection(Game game, Figure.Color[] toss) {
        Figure.Dir best = Figure.Dir.UP;
        int bestDist = Integer.MAX_VALUE;
        for (Figure.Dir d : Figure.Dir.values()) {
            Game sim = game.copy();
            sim.applyMove(d, toss);
            int dist = sim.distanceToHome(myColor);
            if (dist < bestDist) {
                bestDist = dist;
                best = d;
            }
        }
        return best;
    }
}