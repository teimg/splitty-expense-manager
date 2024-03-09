package client.scenes;

import client.language.LanguageSwitch;
import client.utils.ServerUserCommunicator;
import client.utils.IServerUserCommunicator;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jakarta.ws.rs.NotFoundException;

public class StartScreenCtrl implements LanguageSwitch {
    private final IServerUserCommunicator server;

    @FXML
    private Label createNewEventLabel;

    @FXML
    private Label joinEventLabel;

    @FXML
    private Label recentlyViewedEventsLabel;

    @FXML
    private MenuBar menuBar;

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
    public StartScreenCtrl(ServerUserCommunicator server, MainCtrl mainCtrl) {
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
        ));
        mainCtrl.setTitle(mainCtrl.getTranslator().getTranslation(
                "Titles.StartScreen"));
    }

    public void createEvent() {
        String name = newEventField.getText();
        if (name.isEmpty()) {
            // We need to show this message in the GUI later
            System.out.println("The name should not be empty");
        } else {
            Event event = new Event(newEventField.getText());
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
