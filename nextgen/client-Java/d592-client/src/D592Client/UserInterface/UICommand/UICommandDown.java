package D592Client.UserInterface.UICommand;

/**
 * 'CMD_S'
 */
public class UICommandDown extends UICommandExecutable {
    public UICommandDown() {
        super((byte)'s');
    }

    public String toString() {
        return "CMD_S";
    }
}
