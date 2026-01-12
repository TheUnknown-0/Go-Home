package gohome.ai;

import gohome.core.Game;
import gohome.core.Figure;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MinimaxAITest {
    @Test
    public void testMinimaxAIDecisionTimeBound() {
        Game g = new Game(5,5, new java.util.Random(2));
        // set positions to make some thinking useful
        g.getFigure(Figure.Color.RED).setPosition(0,2);
        g.getFigure(Figure.Color.BLUE).setPosition(4,3);

        Figure.Color[] toss = new Figure.Color[]{Figure.Color.RED, Figure.Color.BLUE};
        MinimaxAI ai = new MinimaxAI(Figure.Color.RED, 5000L);

        long start = System.currentTimeMillis();
        Figure.Dir d = ai.chooseDirection(g, toss);
        long elapsed = System.currentTimeMillis() - start;

        assertNotNull(d, "AI must return a direction");
        assertTrue(elapsed <= 5000, "AI decision took too long: " + elapsed + "ms");
    }
}