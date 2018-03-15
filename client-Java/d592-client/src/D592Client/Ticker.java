package D592Client;

/**
 * A special time measurement class
 */
public class Ticker {
    public static synchronized Ticker getInstance() {
        if (instance == null) {
            instance = new Ticker();
        }
        return instance;
    }

    /**
     * Make a tick (that is, increment the current time value by 1)
     */
    public synchronized void tick() {
        currentTick += 1;
    }

    /**
     * @return current time maintained by the ticker
     */
    public synchronized long getNow() {
        // TODO: Advanced lock (synchronized is not effective)
        return currentTick;
    }


    private static Ticker instance = null;

    private long currentTick = 1;
    private Ticker() {}
}
