package D592Client.UserInterface.UICommand;

/**
 * 'CMD_QUIT'
 */
public class UICommandQuit extends UICommandExecutable {
    public UICommandQuit() {
        super((byte)'q');
    }

    public String toString() {
        return "CMD_QUIT";
    }
}
