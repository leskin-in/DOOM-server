package D592Client.UserInterface.UICommand;

/**
 * 'CMD_W'
 */
public class UICommandUp extends UICommandExecutable {
    public UICommandUp() {
        super((byte)'w');
    }

    public String toString() {
        return "CMD_W";
    }
}
