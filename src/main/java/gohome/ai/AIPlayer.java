package gohome.ai;

import gohome.core.Game;
import gohome.core.Figure;

public interface AIPlayer {
    /**
     * Choose a direction for the current toss result (selected figures and steps are encoded in toss array)
     */
    Figure.Dir chooseDirection(Game game, Figure.Color[] toss);
}