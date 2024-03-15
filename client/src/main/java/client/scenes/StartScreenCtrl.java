package client.scenes;

import client.language.LanguageSwitch;
import client.utils.SceneController;
import client.utils.EventCommunicator;
import client.utils.IEventCommunicator;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.ws.rs.NotFoundException;

public class StartScreenCtrl implements LanguageSwitch, SceneController {
    private final IEventCommunicator server;

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

    @Inject
    public StartScreenCtrl(EventCommunicator server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
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
        String name = newEventField.getText();
        if (name.isEmpty()) {
            // We need to show this message in the GUI later
            System.out.println("The name should not be empty");
        } else {
            Event event = new Event(name);
            event = server.createEvent(event);
            mainCtrl.showEventOverview(event);
        }
    }

    public void joinEvent() {
        String inviteCode = joinEventField.getText();
        try {
            Event event = server.getEventByInviteCode(inviteCode);
            mainCtrl.showEventOverview(event);
        } catch (NotFoundException e) {
            // We need to show this message in the GUI later
            System.out.println("The invite code is invalid");
        }
    }
}
