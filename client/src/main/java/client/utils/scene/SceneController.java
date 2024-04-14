package client.utils.scene;

import client.dialog.Popup;
import client.scenes.MainCtrl;
import client.scenes.StartScreenCtrl;
import jakarta.ws.rs.ProcessingException;

public abstract class SceneController {
    private final MainCtrl mainCtrl;

    private Popup.TYPE type = Popup.TYPE.ERROR;

    public SceneController(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    private void handleNetworkException(Exception e){
        String msg = mainCtrl.getTranslator().getTranslation(
            "Popup.ServerOffline"
        );

        (new Popup(msg, type)).show();

        if (!(this instanceof StartScreenCtrl)) mainCtrl.showStartScreen();
    }
    protected void handleException(Exception e){
        if (e instanceof ProcessingException){
            handleNetworkException(e);
            return;
        }

        String msg = mainCtrl.getTranslator().getTranslation(
            "Popup." + e.getMessage()
        );
        (new Popup(msg, type)).show();
    }
}
