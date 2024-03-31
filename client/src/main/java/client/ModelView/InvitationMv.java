package client.ModelView;

import client.dialog.Popup;
import client.utils.communicators.implementations.EmailCommunicator;
import client.utils.communicators.implementations.ParticipantCommunicator;
import client.utils.communicators.interfaces.IEmailCommunicator;
import client.utils.communicators.interfaces.IParticipantCommunicator;
import com.google.inject.Inject;
import commons.EmailRequest;
import commons.Event;
import commons.Participant;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvitationMv {

    private Event event;

    private ObjectProperty<ObservableList<String>> emails;

    private StringProperty emailEntered;

    private final IEmailCommunicator emailCommunicator;

    private final IParticipantCommunicator participantCommunicator;

    @Inject
    public InvitationMv(EmailCommunicator emailCommunicator,
                        ParticipantCommunicator participantCommunicator) {
        this.emails = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        this.emailEntered = new SimpleStringProperty("");
        this.emailCommunicator = emailCommunicator;
        this.participantCommunicator = participantCommunicator;
    }

    public void loadEvent(Event event) {
        this.event = event;
    }

    private  boolean isAlreadyAdded(String email){
        return emails.get().contains(email);
    }

    private  boolean isEmpty(String email){
        return email == null || email.isEmpty();
    }

    public void emailAdd(){
        String res = emailEntered.getValue();

        if(isEmpty(res)){
            new Popup("Empty email field" , Popup.TYPE.ERROR).showAndWait();
        }
        if (isAlreadyAdded(res)){
            new Popup("Already entered!" , Popup.TYPE.ERROR).showAndWait();
        }

        emails.get().addAll(res);
        emailEntered.setValue("");
    }

    public void clear() {
        int size = emails.get().size();
        for (int i = 0; i < size; i++) {
            emails.get().removeLast();
        }
    }

    public List<String> getAllEmails(){
        return new ArrayList<>(emails.get());
    }

    public void handleSendInvites() {
        sendOutEmails();
        addParticipants();
    }

    public boolean isValidEmail(String email) {
        String pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(email);
        return matcher.matches();
    }

    private void addParticipants() {
        List<String> allEmails = getAllEmails();
        for (String email : allEmails) {
            if (isValidEmail(email)) {
                participantCommunicator.createParticipant(
                        event, email.split("@")[0], email, null);
                event.addParticipant(new Participant(email.split("@")[0], email));
            } else {
                new Popup("Invalid email!", Popup.TYPE.ERROR).showAndWait();
            }
        }
    }

    public void sendOutEmails() {
        List<String> allEmails = getAllEmails();
        for (String email : allEmails) {
            EmailRequest request = new EmailRequest(
                    email,
                    "Invitation to Event!",
                    "You have been invited to the event: " + event.getName()
                            + "\n\nThe invite code is: " + event.getInviteCode()
                            + "\n\nThe server URL is: " + emailCommunicator.getOrigin()
                            + "\n\nHave a nice day!"
            );
            emailCommunicator.sendEmail(request);
        }
        new Popup("Invitation sent!" , Popup.TYPE.INFO).showAndWait();
    }

    public ObjectProperty<ObservableList<String>> emailsProperty() {
        return emails;
    }

    public StringProperty emailEnteredProperty() {
        return emailEntered;
    }

    public Event getEvent() {
        return event;
    }


}
