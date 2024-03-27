package client.ModelView;

import client.utils.communicators.implementations.EmailCommunicator;
import commons.Event;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvitationMvTest {

    private InvitationMv invitationMv;

    @Mock
    EmailCommunicator mockEmailCommunicator;

    @BeforeEach
    void setup (){
        invitationMv = new InvitationMv(mockEmailCommunicator);
    }

    @Test
    void loadEvent() {
        Event res = new Event("Hello");

        invitationMv.loadEvent(res);

        assertEquals(res, invitationMv.getEvent());
    }

    @Test
    void emailAdd() {
        invitationMv.emailEnteredProperty().setValue("janedoe@example.com");

        invitationMv.emailAdd();
        assertEquals("janedoe@example.com", invitationMv.getAllEmails().getFirst());

        invitationMv.emailEnteredProperty().setValue("");

        assertThrows(IllegalArgumentException.class, () ->{
            invitationMv.emailAdd();
        });

        invitationMv.emailEnteredProperty().setValue("janedoe@example.com");

        assertThrows(IllegalArgumentException.class, () ->{
            invitationMv.emailAdd();
        });

    }

    @Test
    void clear() {
        invitationMv.emailEnteredProperty().setValue("janedoe@example.com");
        invitationMv.emailAdd();
        invitationMv.emailEnteredProperty().setValue("joe@example.com");
        invitationMv.emailAdd();
        invitationMv.emailEnteredProperty().setValue("hank@example.com");
        invitationMv.emailAdd();
        invitationMv.emailEnteredProperty().setValue("trien@example.com");
        invitationMv.emailAdd();

        invitationMv.clear();

        assertEquals(0, invitationMv.emailsProperty().get().size());

    }

    @Test
    void getAllEmails() {
        List<String> res = List.of("janedoe@example.com", "joe@example.com");

        invitationMv.emailEnteredProperty().setValue("janedoe@example.com");
        invitationMv.emailAdd();
        invitationMv.emailEnteredProperty().setValue("joe@example.com");
        invitationMv.emailAdd();

        assertEquals(res, invitationMv.getAllEmails());

    }

    @Test
    void handleSendInvites() {
        assertDoesNotThrow(() ->{
            invitationMv.handleSendInvites();
        });
    }

    @Test
    void emailsProperty() {
        assertEquals(FXCollections.observableArrayList(), invitationMv.emailsProperty().get());
    }

    @Test
    void emailEnteredProperty() {
        assertEquals("", invitationMv.emailEnteredProperty().get());
    }

}