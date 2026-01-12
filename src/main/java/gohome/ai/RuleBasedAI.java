package gohome.ai;

import gohome.core.Game;
import gohome.core.Figure;

/**
 * Level 2: rule-based heuristic
 * - Prefer directions that decrease own distance to home and increase opponent distance
 */
public class RuleBasedAI implements AIPlayer {
    private final Figure.Color myColor;
    private final Figure.Color oppColor;

    public RuleBasedAI(Figure.Color myColor) {
        this.myColor = myColor;
        this.oppColor = (myColor == Figure.Color.RED) ? Figure.Color.BLUE : Figure.Color.RED;
    }

    @Override
    public Figure.Dir chooseDirection(Game game, Figure.Color[] toss) {
        Figure.Dir best = Figure.Dir.UP;
        int bestScore = Integer.MIN_VALUE;
        int beforeOwn = game.distanceToHome(myColor);
        int beforeOpp = game.distanceToHome(oppColor);

        for (Figure.Dir d : Figure.Dir.values()) {
            Game sim = game.copy();
            sim.applyMove(d, toss);
            int afterOwn = sim.distanceToHome(myColor);
            int afterOpp = sim.distanceToHome(oppColor);
            int score = (beforeOwn - afterOwn) - (beforeOpp - afterOpp); // higher is better
            if (score > bestScore) {
                bestScore = score;
                best = d;
            }
        }
        return best;
    }
}