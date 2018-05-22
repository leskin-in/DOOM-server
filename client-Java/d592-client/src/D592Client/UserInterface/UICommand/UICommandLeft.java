package D592Client.UserInterface.UICommand;

/**
 * 'CMD_A'
 */
public class UICommandLeft extends UICommandExecutable {
    public UICommandLeft() {
        super((byte)'a');
    }

    public String toString() {
        return "CMD_A";
    }
}
