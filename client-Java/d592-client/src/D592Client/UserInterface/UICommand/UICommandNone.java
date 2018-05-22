package D592Client.UserInterface.UICommand;

/**
 * 'CMD_NONE'
 */
public class UICommandNone extends UICommandExecutable {
    public UICommandNone() {
        super((byte)' ');
    }

    public String toString() {
        return "CMD_NONE";
    }
}
