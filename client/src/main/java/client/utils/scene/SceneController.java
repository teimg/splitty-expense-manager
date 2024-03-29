package client.utils.scene;

import client.dialog.Popup;
import client.language.Translator;

public interface SceneController {
    default void handleException(Exception e, Translator translator){
        Popup.TYPE type = Popup.TYPE.ERROR;

        String msg = translator.getTranslation(
            "Popup." + e.getMessage()
        );
        (new Popup(msg, type)).show();
    }
}
