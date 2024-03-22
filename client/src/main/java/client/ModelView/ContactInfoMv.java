package client.ModelView;

import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IParticipantCommunicator;
import com.google.inject.Inject;
import commons.BankAccount;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactInfoMv {

    private final IEventCommunicator eventServer;

    private final IParticipantCommunicator participantServer;

    private final StringProperty email;
    private final StringProperty bic;
    private final StringProperty iban;
    private final StringProperty name;

    private Event event;
    private Participant participant;


    @Inject
    public ContactInfoMv(IEventCommunicator eventCommunicator,
                         IParticipantCommunicator participantCommunicator) {
        this.eventServer = eventCommunicator;
        this.participantServer = participantCommunicator;

        email =  new SimpleStringProperty("");
        bic = new SimpleStringProperty("");
        name = new SimpleStringProperty("");
        iban = new SimpleStringProperty("");

    }

    public String getEventInviteCode() {
        return this.event.getInviteCode();
    }

    public Event getEventByInviteCode(String inviteCode) {
        try {
            return eventServer.getEventByInviteCode(inviteCode);
        } catch (ProcessingException e) {
            throw new ProcessingException("ServerOffLine");
        }
    }

    public void clearScene(){
        this.participant = null;
        this.event = null;
        clearFields();
    }

    private void clearFields(){
        email.setValue("");
        name.setValue("");
        bic.setValue("");
        iban.setValue("");
    }

    public void loadInfo(Event event, Participant participant) {
        clearFields();
        this.participant = participant;
        this.event = event;
        if (participant != null) {
            fillInFields();
        }
    }


    private void fillInFields() {
        email.setValue(participant.getEmail());
        name.setValue(participant.getName());
        if (participant.getBankAccount() != null) {
            iban.setValue(participant.getBankAccount().getIban());
            bic.setValue(participant.getBankAccount().getBic());
        }
    }


    public void createParticipant(Event event, String name, String email, BankAccount bankAccount) {
        try {
            participantServer.createParticipant(event, name, email, bankAccount);
        }catch (jakarta.ws.rs.NotFoundException e) {
            throw new NotFoundException("CodeNotFound");
        }catch (ProcessingException e) {
            throw new ProcessingException("ServerOffline");
        }
    }

    public void updateParticipant(Participant participant) {
        try {
            participantServer.updateParticipant(participant);
        }catch (jakarta.ws.rs.NotFoundException e) {
            throw new NotFoundException("CodeNotFound");
        }catch (ProcessingException e) {
            throw new ProcessingException("ServerOffline");
        }
    }

    public boolean isValidEmail(String email) {
        // Regular expression pattern for email validation
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(email);

        return matcher.matches();
    }

    public boolean validInput() {
        return (isValidEmail(email.getValue())
                && !name.getValue().isEmpty()
                && (bic.getValue().isEmpty() && iban.getValue().isEmpty()
                    ||!bic.getValue().isEmpty() && !iban.getValue().isEmpty()));
    }

    public void addButtonPressed(ActionEvent event) {
        if (!validInput()) {
            System.out.println("Error");
            return;
        }

        Event currentEvent = getCurrentEvent();

        if (participant == null) {
            BankAccount bankAccount = (!ibanProperty().isEmpty().get()
                    && !bicProperty().isEmpty().get())
                    ? new BankAccount(ibanProperty().get(),
                    bicProperty().get()) : null;
            createParticipant(currentEvent,
                    nameProperty().get(),
                    emailProperty().get(), bankAccount);
        } else {
            participant.setName(nameProperty().get());
            participant.setEmail(emailProperty().get());
            BankAccount bankAccount = (!ibanProperty().isEmpty().get()
                    && !bicProperty().isEmpty().get())
                    ? new BankAccount(ibanProperty().get(),
                    bicProperty().get()) : null;
            participant.setBankAccount(bankAccount);
            updateParticipant(participant);
        }
    }

    public Event getCurrentEvent() {
        return this.event;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty bicProperty() {
        return bic;
    }

    public StringProperty ibanProperty() {
        return iban;
    }
}
