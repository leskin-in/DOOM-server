package D592Client.UserInterface;

import D592Client.GameObjects.GameState;

public interface IndicatorField {
    /**
     * Update state object and repaint the indicator
     */
    void updateField(GameState state);
}
