package client.utils.scene;

import client.dialog.Popup;
import client.language.Translator;
import client.scenes.MainCtrl;
import jakarta.ws.rs.ProcessingException;

public abstract class SceneController {
    private final MainCtrl mainCtrl;

    public SceneController(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    private void handleNetworkException(Exception e){
        Popup.TYPE type = Popup.TYPE.ERROR;

        String msg = mainCtrl.getTranslator().getTranslation(
            "Popup.ServerOffline"
        );
        mainCtrl.showStartScreen();
        (new Popup(msg, type)).show();
    }
    protected void handleException(Exception e){
        Popup.TYPE type = Popup.TYPE.ERROR;

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
