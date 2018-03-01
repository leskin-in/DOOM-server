package D592Client;


/**
 * The user interface
 * This is a singleton
 */
public class UI {
    private static UI ourInstance = new UI();

    public static UI getInstance() {
        return ourInstance;
    }

    private UI() {
    }
}
