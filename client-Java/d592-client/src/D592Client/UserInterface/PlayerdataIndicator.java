package D592Client.UserInterface;

public interface PlayerdataIndicator {
    /**
     * Update health indicators and repaint the indicator
     */
    void updateHealth(int health, int healthPercent);

    /**
     * Update weapon indicators and repaint the indicator
     */
    void updateWeapon(String weapon, int charge);
}
