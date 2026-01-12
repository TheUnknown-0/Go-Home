package gohome.ai;

import gohome.core.Game;
import gohome.core.Figure;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AITest {
    @Test
    public void testSimpleAIDoesMoveTowardsHome() {
        Game g = new Game(5,5, new java.util.Random(1));
        // put red at (0,2) so moving RIGHT helps
        g.getFigure(Figure.Color.RED).setPosition(0,2);
        Figure.Color[] toss = new Figure.Color[]{Figure.Color.RED, Figure.Color.RED}; // double-step
        SimpleAI ai = new SimpleAI(Figure.Color.RED);
        Figure.Dir d = ai.chooseDirection(g, toss);
        assertEquals(Figure.Dir.RIGHT, d);
    }
}