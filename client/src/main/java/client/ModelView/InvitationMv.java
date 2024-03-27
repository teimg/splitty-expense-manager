package client.ModelView;

import commons.Event;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class InvitationMv {

    private Event event;

    private ObjectProperty<ObservableList<String>> emails;

    private StringProperty emailEntered;

    public InvitationMv() {
        this.emails = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        this.emailEntered = new SimpleStringProperty("");
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
            throw new IllegalArgumentException("Empty");
        }
        if (isAlreadyAdded(res)){
            throw new IllegalArgumentException("AlreadyEntered");
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
        List<String> allEmails = getAllEmails();
        // TODO: implement email functionality

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
