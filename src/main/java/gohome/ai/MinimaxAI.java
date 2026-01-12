package gohome.ai;

import gohome.core.Game;
import gohome.core.Figure;

/**
 * Level 3: simple lookahead (expected opponent response)
 * Decision-making is time-limited to avoid long UI freezes. Default maxMillis = 5000ms.
 */
public class MinimaxAI implements AIPlayer {
    private final Figure.Color myColor;
    private final Figure.Color oppColor;
    private final AIPlayer opponentModel; // use same level or simpler
    private final long maxMillis;

    public MinimaxAI(Figure.Color myColor) {
        this(myColor, 5000L);
    }

    /**
     * @param myColor AI's color
     * @param maxMillis maximum milliseconds allowed for chooseDirection
     */
    public MinimaxAI(Figure.Color myColor, long maxMillis) {
        this.myColor = myColor;
        this.oppColor = (myColor == Figure.Color.RED) ? Figure.Color.BLUE : Figure.Color.RED;
        // for modeling opponent, use rule-based AI
        this.opponentModel = new RuleBasedAI(oppColor);
        this.maxMillis = Math.max(1, maxMillis);
    }

    @Override
    public Figure.Dir chooseDirection(Game game, Figure.Color[] toss) {
        Figure.Dir best = Figure.Dir.UP;
        double bestValue = Double.NEGATIVE_INFINITY;

        final long start = System.nanoTime();
        final long deadline = start + (maxMillis * 1_000_000L);

        for (Figure.Dir d : Figure.Dir.values()) {
            // time check
            if (System.nanoTime() > deadline) break;

            Game sim = game.copy();
            Game.GameResult r = sim.applyMove(d, toss);
            if (r != Game.GameResult.ONGOING) {
                // immediate win or loss
                double val = (r == (myColor == Figure.Color.RED ? Game.GameResult.RED_WINS : Game.GameResult.BLUE_WINS)) ? 1000 : -1000;
                if (val > bestValue) { bestValue = val; best = d; }
                continue;
            }

            // opponent's turn: compute expected value over toss outcomes but abort if time runs out
            double expected = 0.0;
            Figure.Color[] all = new Figure.Color[]{Figure.Color.RED, Figure.Color.BLUE};
            int combinations = 0;
            boolean outOfTime = false;
            for (Figure.Color t0 : all) {
                for (Figure.Color t1 : all) {
                    if (System.nanoTime() > deadline) { outOfTime = true; break; }
                    Figure.Color[] oppToss = new Figure.Color[]{t0, t1};
                    // opponent chooses best response
                    Figure.Dir oppDir = opponentModel.chooseDirection(sim, oppToss);
                    Game sim2 = sim.copy();
                    Game.GameResult r2 = sim2.applyMove(oppDir, oppToss);
                    double val;
                    if (r2 == Game.GameResult.ONGOING) {
                        // heuristic evaluation: difference in distances
                        int myDist = sim2.distanceToHome(myColor);
                        int oppDist = sim2.distanceToHome(oppColor);
                        val = (oppDist - myDist);
                    } else {
                        // terminal
                        val = (r2 == (myColor == Figure.Color.RED ? Game.GameResult.RED_WINS : Game.GameResult.BLUE_WINS)) ? 1000 : -1000;
                    }
                    expected += val;
                    combinations++;
                }
                if (outOfTime) break;
            }
            if (combinations > 0) expected /= combinations;

            if (expected > bestValue) {
                bestValue = expected;
                best = d;
            }
        }
        return best;
    }
}