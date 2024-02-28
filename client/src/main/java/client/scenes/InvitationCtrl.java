package client.scenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class InvitationCtrl implements Initializable {

    @FXML
    private MenuBar menuBar;

    @FXML
    private Text eventTitle;

    @FXML
    private Text inviteMessage;

    @FXML
    private TextArea addressesArea;

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
     * Sets the invite message for a certain invite code.
     * @param code invite code
     */
    public void setInviteCode(String code) {
        inviteMessage.setText("Give people the following invite code: " + code);
    }

    /**
     * Called on press of the Send Invites button.
     */
    public void handleSendInvites() {}
}
