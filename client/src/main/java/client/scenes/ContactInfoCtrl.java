package client.scenes;

import client.ModelView.ContactInfoMv;
import client.language.LanguageSwitch;
import client.utils.*;
import com.google.inject.Inject;
import commons.BankAccount;
import commons.Event;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactInfoCtrl implements LanguageSwitch, SceneController, Initializable {

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

//    private final IParticipantCommunicator participantServer;

//    private final IEventCommunicator eventServer;

    private Event event;

    private Participant participant;

    private final ContactInfoMv contactInfoMv;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBindings();
    }
    @Inject
    public ContactInfoCtrl(MainCtrl mainCtrl, ContactInfoMv contactInfoMv) {
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

    private void clearScene(){
        this.participant = null;
        this.event = null;
        clearFields();
    }

    public void quitScene(){
        String inviteCode = this.event.getInviteCode();
        Event newEvent = contactInfoMv.getEventByInviteCode(inviteCode);
        clearScene();
        mainCtrl.showEventOverview(newEvent);

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
        quitScene();
    }

    public void addButtonPressed(ActionEvent event) {
        if (!validInput()) {
            System.out.println("Error");
            return;
        }

        Event currentEvent = getCurrentEvent();

        if (participant == null) {
            BankAccount bankAccount = (!contactInfoMv.ibanProperty().isEmpty().get()
                    && !contactInfoMv.bicProperty().isEmpty().get())
                    ? new BankAccount(contactInfoMv.ibanProperty().get(),
                    contactInfoMv.bicProperty().get()) : null;
            contactInfoMv.createParticipant(currentEvent,
                    contactInfoMv.nameProperty().get(),
                    contactInfoMv.emailProperty().get(), bankAccount);
        } else {
            participant.setName(contactInfoMv.nameProperty().get());
            participant.setEmail(contactInfoMv.emailProperty().get());
            BankAccount bankAccount = (!contactInfoMv.ibanProperty().isEmpty().get()
                    && !contactInfoMv.bicProperty().isEmpty().get())
                    ? new BankAccount(contactInfoMv.ibanProperty().get(),
                    contactInfoMv.bicProperty().get()) : null;
            participant.setBankAccount(bankAccount);
            contactInfoMv.updateParticipant(participant);
        }
        quitScene();    }

    private Event getCurrentEvent() {
        return this.event;
    }

    private void initBindings() {
        nameField.textProperty().bindBidirectional(contactInfoMv.nameProperty());
        emailField.textProperty().bindBidirectional(contactInfoMv.emailProperty());
        ibanField.textProperty().bindBidirectional(contactInfoMv.ibanProperty());
        bicField.textProperty().bindBidirectional(contactInfoMv.bicProperty());
    }
}
