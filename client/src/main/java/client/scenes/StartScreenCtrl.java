package client.scenes;

import client.ModelView.StartScreenMv;
import client.dialog.Popup;
import client.language.LanguageSwitch;
import client.utils.*;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class StartScreenCtrl implements Initializable, LanguageSwitch, SceneController {

    @FXML
    private Label createNewEventLabel;

    @FXML
    private Label joinEventLabel;

    @FXML
    private Label recentlyViewedEventsLabel;

    @FXML
    private TextField newEventField;

    @FXML
    private TextField joinEventField;

    @FXML
    private Button createEventButton;

    @FXML
    private Button joinEventButton;

    private final MainCtrl mainCtrl;

    private final StartScreenMv startScreenMv;

    @Inject
    public StartScreenCtrl(MainCtrl mainCtrl, StartScreenMv startScreenMv) {;
        this.mainCtrl = mainCtrl;
        this.startScreenMv = startScreenMv;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBinding();

    }
    public void initBinding(){
        newEventField.textProperty().bindBidirectional(startScreenMv.newEventProperty());
        joinEventField.textProperty().bindBidirectional(startScreenMv.joinEventProperty());
    }

    @Override
    public void setLanguage() {
        createNewEventLabel.setText(mainCtrl.getTranslator().getTranslation(
            "StartScreen.Create-New-Event-label"));
        joinEventLabel.setText(mainCtrl.getTranslator().getTranslation(
            "StartScreen.Join-Event-label"
        ));
        recentlyViewedEventsLabel.setText(mainCtrl.getTranslator().getTranslation(
            "StartScreen.Recently-Viewed-label"
        ));
        createEventButton.setText(mainCtrl.getTranslator().getTranslation(
            "StartScreen.Create-Event-Button"
        ));
        joinEventButton.setText(mainCtrl.getTranslator().getTranslation(
            "StartScreen.Join-Event-Button"
        ));;
    }

    public void createEvent() {
        try{
            mainCtrl.showEventOverview(
                startScreenMv.createEvent()
            );
        }catch (Exception e){
            handleException(e);
        }
    }

    public void joinEvent() {
        try {
            mainCtrl.showEventOverview(
                startScreenMv.joinEvent()
            );
        } catch (Exception e){
            handleException(e);
        }
    }

    void handleException(Exception e){
        Popup.TYPE type = Popup.TYPE.ERROR;


        String msg = mainCtrl.getTranslator().getTranslation(
            "Popup." + e.getMessage()
        );
        (new Popup(msg, type)).show();
    }

}
