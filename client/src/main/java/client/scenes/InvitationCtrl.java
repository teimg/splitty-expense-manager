package client.scenes;

import client.language.LanguageSwitch;
import client.utils.SceneController;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class InvitationCtrl implements Initializable, LanguageSwitch, SceneController {

    @FXML
    private Text inviteLabel;

    @FXML
    private Button sendInviteButton;

    @FXML
    private Text eventTitle;

    @FXML
    private Text inviteMessage;

    @FXML
    private TextArea addressesArea;

    private Event event;

    private final MainCtrl mainCtrl;

    @Inject
    public InvitationCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Sets the Event to invite Participants to.
     * @param event Event to use
     */
    public void loadEvent(Event event) {
        this.event = event;
        eventTitle.setText(mainCtrl.getTranslator().getTranslation(
                "Invitation.Title-label") + " " + event.getName());
        inviteMessage.setText(mainCtrl.getTranslator().getTranslation(
                "Invitation.Invite-Message-label") + " " + event.getInviteCode());
    }

    /**
     * Called on press of the Send Invites button.
     */
    public void handleSendInvites() {}

    @Override
    public void setLanguage() {
        inviteLabel.setText(mainCtrl.getTranslator().getTranslation(
                "Invitation.Invite-label"));
        sendInviteButton.setText(mainCtrl.getTranslator().getTranslation(
                "Invitation.Send-Invite-Button"));
        eventTitle.setText(mainCtrl.getTranslator().getTranslation(
                "Invitation.Title-label") + " " + event.getName());
        inviteMessage.setText(mainCtrl.getTranslator().getTranslation(
                "Invitation.Invite-Message-label") + " " + event.getInviteCode());
    }
}
