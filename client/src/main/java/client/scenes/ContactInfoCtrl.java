package client.scenes;

import client.language.LanguageSwitch;
import client.utils.*;
import com.google.inject.Inject;
import commons.BankAccount;
import commons.Event;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private final IParticipantCommunicator participantServer;

    private final IEventCommunicator eventServer;

    private Event event;

    private Participant participant;

    @Inject
    public ContactInfoCtrl (MainCtrl mainCtrl, ParticipantCommunicator participantCommunicator,
                            EventCommunicator eventCommunicator) {
        this.mainCtrl = mainCtrl;
        this.participantServer = participantCommunicator;
        this.eventServer = eventCommunicator;
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
        clearFields();
        this.participant = participant;
        this.event = event;
        if (participant != null) {
            fillInFields();
        }
    }

    private void clearFields(){
        emailField.setText("");
        nameField.setText("");
        bicField.setText("");
        ibanField.setText("");
    }

    private void fillInFields() {
        emailField.setText(participant.getEmail());
        nameField.setText(participant.getName());
        if (participant.getBankAccount() != null) {
            ibanField.setText(participant.getBankAccount().getIban());
            bicField.setText(participant.getBankAccount().getBic());
        }
    }

    private boolean isValidEmail(String email) {
        // Regular expression pattern for email validation
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(email);

        return matcher.matches();
    }

    private boolean validInput() {
        return (isValidEmail(emailField.getText())
            && !nameField.getText().isEmpty()
            && (bicField.getText().isEmpty() && ibanField.getText().isEmpty()
                || !bicField.getText().isEmpty() && !ibanField.getText().isEmpty()));
    }

    public void abortButtonPressed(ActionEvent event) {
        mainCtrl.showEventOverview(eventServer.getEventByInviteCode(this.event.getInviteCode()));
    }

    public void addButtonPressed(ActionEvent event) {
        if (!validInput()) {
            System.out.println("Error");
            return;
        }
        if (participant == null) {
            BankAccount bankAccount = (!ibanField.getText().isEmpty()
                    && !bicField.getText().isEmpty())
                    ? new BankAccount(ibanField.getText(), bicField.getText()) : null;
            participantServer.createParticipant(
                    this.event,
                    nameField.getText(),
                    emailField.getText(),
                    bankAccount);
        }
        else {
            participant.setName(nameField.getText());
            participant.setEmail(emailField.getText());
            BankAccount bankAccount = (!ibanField.getText().isEmpty()
                    && !bicField.getText().isEmpty())
                    ? new BankAccount(ibanField.getText(), bicField.getText()) : null;
            participant.setBankAccount(bankAccount);
            // TODO: decipher why an exception is thrown here
            try {
                participantServer.updateParticipant(participant);
            }
            catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
        mainCtrl.showEventOverview(eventServer.getEventByInviteCode(this.event.getInviteCode()));
    }

}
