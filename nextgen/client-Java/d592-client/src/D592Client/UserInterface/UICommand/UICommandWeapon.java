package D592Client.UserInterface.UICommand;

/**
 * 'CMD_WEAPON'
 */
public class UICommandWeapon extends UICommandExecutable {
    public UICommandWeapon() {
        super((byte)'b');
    }

    public String toString() {
        return "CMD_WEAPON";
    }
}
