package client.scenes;

import client.ModelView.InvitationMv;
import client.dialog.ConfPopup;
import client.language.LanguageSwitch;
import client.nodes.UIIcon;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class InvitationCtrl implements Initializable, LanguageSwitch, SceneController {

    @FXML
    private ListView<String> emailsField;

    @FXML
    private Button abortBtn;

    @FXML
    private Button emailInputBtn;

    @FXML
    private TextField emailInputField;

    @FXML
    private Button clearBtn;

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

    private final MainCtrl mainCtrl;

    private final InvitationMv invitationMv;

    private class  EmailCell extends ListCell<String>{
        private HBox container;
        private Label email;
        private Pane filler;
        private Button deleteButton;

        public EmailCell() {
            super();
            container = new HBox();
            email = new Label();
            filler = new Pane();
            deleteButton = new Button("", UIIcon.icon(UIIcon.NAME.DELETE));
            HBox.setHgrow(filler, Priority.ALWAYS);
            container.getChildren().addAll(email, filler, deleteButton);
        }

        @Override
        protected void updateItem(String email, boolean empty) {
            super.updateItem(email, empty);

            if(empty){
                setGraphic(null);
                return;
            }

            this.email.setText(email);

            this.deleteButton.setOnAction(e ->{
                emailsField.getItems().remove(getIndex());
//                emailsField.getItems().removeLast();
            });

            setGraphic(container);

        }
    }

    @Inject
    public InvitationCtrl(MainCtrl mainCtrl, InvitationMv invitationMv) {
        this.mainCtrl = mainCtrl;
        this.invitationMv = invitationMv;

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
        emailInputField.setOnKeyReleased(e ->{
            if (e.getCode() == KeyCode.ENTER){
                emailAddPressed();
            }
        });
        new EmailCell();

        emailsField.setCellFactory(listView -> new EmailCell());
        initBindings();

    }

    private void initBindings() {
        emailsField.itemsProperty().bindBidirectional(invitationMv.emailsProperty());
        emailInputField.textProperty().bindBidirectional(invitationMv.emailEnteredProperty());
    }


    /**
     * Sets the Event to invite Participants to.
     * @param event Event to use
     */
    public void loadEvent(Event event) {
        invitationMv.loadEvent(event);
        eventTitle.setText(mainCtrl.getTranslator().getTranslation(
            "Invitation.Title-label") + " " + event.getName());
        inviteMessage.setText(mainCtrl.getTranslator().getTranslation(
            "Invitation.Invite-Message-label") + " " + event.getInviteCode());
    }

    /**
     * Called on press of the Send Invites button.
     */
    public void handleSendInvites() {
        invitationMv.handleSendInvites();
        mainCtrl.showEventOverview(this.invitationMv.getEvent());
    }


    @Override
    public void setLanguage() {
        inviteLabel.setText(mainCtrl.getTranslator().getTranslation(
            "Invitation.Invite-label"));
        sendInviteButton.setText(mainCtrl.getTranslator().getTranslation(
            "Invitation.Send-Invite-Button"));
        eventTitle.setText(mainCtrl.getTranslator().getTranslation(
            "Invitation.Title-label") + " " + invitationMv.getEvent().getName());
        inviteMessage.setText(mainCtrl.getTranslator().getTranslation(
            "Invitation.Invite-Message-label") + " " + invitationMv.getEvent().getInviteCode());
        emailInputBtn.setText(mainCtrl.getTranslator().getTranslation(
            "Invitation.AddEmail"));
        abortBtn.setText(mainCtrl.getTranslator().getTranslation(
            "AddEditExpense.Abort-Button"));
        clearBtn.setText(mainCtrl.getTranslator().getTranslation(
            "Invitation.Clear"));
    }

    public void clearButtonPressed() {
        boolean res=ConfPopup
            .create(mainCtrl.getTranslator().getTranslation("Conf.ClearEverything"))
            .isConfirmed();
        if(res){
            invitationMv.clear();
        }
    }

    public void abortButtonPressed() {
        mainCtrl.showEventOverview(this.invitationMv.getEvent());
    }

    public void emailAddPressed() {
        try {
            invitationMv.emailAdd();

        }catch (Exception e){
            handleException(e, mainCtrl.getTranslator());
        }

    }

}
