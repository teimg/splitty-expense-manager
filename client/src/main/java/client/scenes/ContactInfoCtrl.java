package client.scenes;

import client.language.LanguageSwitch;
import client.utils.SceneController;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ContactInfoCtrl implements LanguageSwitch, SceneController {

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

    private final MainCtrl mainCtrl;

    private Event event;

    private Participant participant;

    public void abortButtonPressed(ActionEvent event) {

    }

    public void addButtonPressed(ActionEvent event) {
        
    }

    @Inject
    public ContactInfoCtrl (MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
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
    }

    public void loadInfo(Event event, Participant participant) {
        this.participant = participant;
        this.event = event;
        if (participant != null) {
           fillInFields();
        }
    }

    private void fillInFields() {
        emailField.setText(participant.getEmail());
        nameField.setText(participant.getName());
        if (participant.getBankAccount() != null) {
            ibanField.setText(participant.getBankAccount().getIban());
            bicField.setText(participant.getBankAccount().getBic());
        }
    }
}
