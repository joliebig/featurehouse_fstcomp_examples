

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

public class KeyMonitor implements CommandListener {

    private GameManager gameManager;

    public KeyMonitor(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void KeyPressed(int keyCode) {

        switch (keyCode) {
            case -1:
            case 50:
                gameManager.keyPressBehandeln(38);
                break;
            case -2:
            case 56:
                gameManager.keyPressBehandeln(40);
                break;
            case -3:
            case 52:
                gameManager.keyPressBehandeln(37);
                break;
            case -4:
            case 54:
                gameManager.keyPressBehandeln(39);
                break;
            case -5:
            case 53:
                gameManager.keyPressBehandeln(17);
                break;
            case -30:
            case 42:
                gameManager.keyPressBehandeln(10);
                break;
            case -31:
            case 35:
                gameManager.keyPressBehandeln(27);
                break;

        }
    }

    public void keyReleased(int keyCode) {

        switch (keyCode) {
            case -1:
            case 50:
                gameManager.keyReleaseBehandeln(38);
                break;
            case -2:
            case 56:
                gameManager.keyReleaseBehandeln(40);
                break;
            case -3:
            case 52:
                gameManager.keyReleaseBehandeln(37);
                break;
            case -4:
            case 54:
                gameManager.keyReleaseBehandeln(39);
                break;
            case -5:
            case 53:
                gameManager.keyReleaseBehandeln(17);
                break;
        }

    }

    public void commandAction(Command arg0, Displayable arg1) {
    }
}