package gohome.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class GameTest {
    @Test
    public void testWrapAround() {
        Board b = new Board(5,5);
        assertEquals(0, b.wrapX(5));
        assertEquals(4, b.wrapX(-1));
        assertTrue(b.isHome(2,2));
    }

    @Test
    public void testMovementAndWin() {
        Game g = new Game(5,5, new Random(123));
        Figure red = g.getFigure(Figure.Color.RED);
        red.move(Figure.Dir.RIGHT, 2, g.getBoard());
        // not at home yet
        assertFalse(red.isAtHome(g.getBoard()));
        // move red to center
        red.move(Figure.Dir.DOWN, 2, g.getBoard());
        assertTrue(red.isAtHome(g.getBoard()));
    }

    @Test
    public void testApplyMoveSingleAndDoubleStep() {
        Game g = new Game(5,5, new Random(42));
        // place red left of center and blue far right
        g.getFigure(Figure.Color.RED).setPosition(1,2);
        g.getFigure(Figure.Color.BLUE).setPosition(4,2);

        // different-color toss: both move 1 step to the RIGHT => red reaches home, blue wraps
        Figure.Color[] toss = new Figure.Color[]{Figure.Color.RED, Figure.Color.BLUE};
        Game.GameResult res = g.applyMove(Figure.Dir.RIGHT, toss);
        assertEquals(Game.GameResult.RED_WINS, res);

        // reset positions for double-step
        g.getFigure(Figure.Color.RED).setPosition(0,2);
        Figure.Color[] toss2 = new Figure.Color[]{Figure.Color.RED, Figure.Color.RED};
        Game.GameResult res2 = g.applyMove(Figure.Dir.RIGHT, toss2);
        assertEquals(Game.GameResult.RED_WINS, res2);
    }
}
