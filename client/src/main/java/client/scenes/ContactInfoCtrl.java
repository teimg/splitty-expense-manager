package client.scenes;

import client.ModelView.ContactInfoMv;
import client.keyBoardCtrl.ShortCuts;
import client.language.LanguageSwitch;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;


public class ContactInfoCtrl extends SceneController
    implements LanguageSwitch, Initializable, ShortCuts {

    @FXML
    private Label titleLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label ibanLabel;

    @FXML
    private Label bicLabel;

    @FXML
    private Button abortButton;

    @FXML
    private Button addButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField bicField;

    @FXML
    private TextField ibanField;

    @FXML
    private Label explanationLabel;

    private final MainCtrl mainCtrl;


    private final ContactInfoMv contactInfoMv;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBindings();
    }
    @Inject
    public ContactInfoCtrl(MainCtrl mainCtrl, ContactInfoMv contactInfoMv) {
        super(mainCtrl);
        this.mainCtrl = mainCtrl;
        this.contactInfoMv = contactInfoMv;
    }

    @Override
    public void setLanguage() {
        titleLabel.setText(mainCtrl.getTranslator().getTranslation(
                "ContactInfo.Title-label"));
        nameLabel.setText(mainCtrl.getTranslator().getTranslation(
                "ContactInfo.Name-label"));
        emailLabel.setText(mainCtrl.getTranslator().getTranslation(
                "ContactInfo.Email-label"));
        ibanLabel.setText(mainCtrl.getTranslator().getTranslation(
                "ContactInfo.IBAN-label"));
        bicLabel.setText(mainCtrl.getTranslator().getTranslation(
                "ContactInfo.BIC-label"));
        abortButton.setText(mainCtrl.getTranslator().getTranslation(
                "ContactInfo.Abort-Button"));
        addButton.setText(mainCtrl.getTranslator().getTranslation(
                "ContactInfo.Add-Button"));
        explanationLabel.setText(mainCtrl.getTranslator().getTranslation(
                "ContactInfo.Explanation-label"));
    }

    public void loadInfo(Event event, Participant participant) {
        contactInfoMv.loadInfo(event, participant);
    }

    private void clearScene(){
        contactInfoMv.clearScene();
    }

    public void quitScene(){
        String inviteCode = contactInfoMv.getEventInviteCode();
        Event newEvent = contactInfoMv.getEventByInviteCode(inviteCode);
        clearScene();
        mainCtrl.showEventOverview(newEvent);
    }

    private boolean validInput() {
        return contactInfoMv.validInput();
    }

    public void abortButtonPressed(ActionEvent event) {
        quitScene();
    }

    public void addButtonPressed(ActionEvent event) {
        try {
            contactInfoMv.addButtonPressed(event);
            quitScene();
        }catch (Exception e){
            e.printStackTrace();
            handleException(e);
        }
    }

    private void initBindings() {
        nameField.textProperty().bindBidirectional(contactInfoMv.nameProperty());
        emailField.textProperty().bindBidirectional(contactInfoMv.emailProperty());
        ibanField.textProperty().bindBidirectional(contactInfoMv.ibanProperty());
        bicField.textProperty().bindBidirectional(contactInfoMv.bicProperty());
    }

    @Override
    public void listeners() {
        Scene s = nameField.getScene();
        mainCtrl.getKeyBoardListeners().addListener(
                s, KeyCode.B, () -> abortButtonPressed(new ActionEvent()));
        mainCtrl.getKeyBoardListeners().addListener(
                s, KeyCode.ENTER, () -> addButtonPressed(new ActionEvent()));
    }

}
