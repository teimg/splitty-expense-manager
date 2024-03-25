package client.ModelView;

import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IParticipantCommunicator;
import commons.BankAccount;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactInfoMvTest {

    ContactInfoMv contactInfoMv;

    @Mock
    IEventCommunicator eventCommunicator;

    @Mock
    IParticipantCommunicator participantCommunicator;

    @BeforeEach
    void setup() {
        contactInfoMv = new ContactInfoMv(eventCommunicator, participantCommunicator);
    }

    @Test
    void getEventInviteCodeTest() {
        Event event = new Event();
        event.setInviteCode("12345");
        contactInfoMv.loadInfo(event, null);

        assertEquals("12345", contactInfoMv.getEventInviteCode());
    }

    @Test
    void getEventByInviteCodeFail() {
        when(eventCommunicator.getEventByInviteCode(any())).thenThrow(new ProcessingException("ServerOffLine"));
        assertThrows(ProcessingException.class, () -> contactInfoMv.getEventByInviteCode("inviteCode"));
    }

    @Test
    void getEventByInviteCodeSuccess() {
        Event res = new Event();
        when(eventCommunicator.getEventByInviteCode("inviteCode")).thenReturn(res);
        assertEquals(contactInfoMv.getEventByInviteCode("inviteCode"), res);
    }

    @Test
    void cleaSceneTest() {
        Event event = new Event();
        Participant participant = new Participant();
        contactInfoMv.loadInfo(event, participant);

        contactInfoMv.clearScene();

        assertNull(contactInfoMv.getCurrentEvent());
        assertNull(contactInfoMv.getCurrentParticipant());
        assertTrue(contactInfoMv.emailProperty().isEmpty().get());
        assertTrue(contactInfoMv.nameProperty().isEmpty().get());
        assertTrue(contactInfoMv.bicProperty().isEmpty().get());
        assertTrue(contactInfoMv.ibanProperty().isEmpty().get());
    }

    @Test
    void crearFieldsTest() {
        contactInfoMv.emailProperty().setValue("blah blah");
        contactInfoMv.nameProperty().setValue("stuff");
        contactInfoMv.bicProperty().setValue("blah stuff");
        contactInfoMv.ibanProperty().setValue("not empty");
        contactInfoMv.clearFields();

        assertEquals("", contactInfoMv.emailProperty().getValue());
        assertEquals("", contactInfoMv.nameProperty().getValue());
        assertEquals("", contactInfoMv.bicProperty().getValue());
        assertEquals("", contactInfoMv.ibanProperty().getValue());
    }

    @Test
    void loadInfoTest() {
        Event event = new Event();
        Participant participant = new Participant();
        participant.setName("name");
        participant.setEmail("email");
        BankAccount bankAccount = new BankAccount("iban", "bic");
        participant.setBankAccount(bankAccount);
        contactInfoMv.loadInfo(event, participant);

        assertEquals(event, contactInfoMv.getCurrentEvent());
        assertEquals(participant, contactInfoMv.getCurrentParticipant());
    }

    @Test
    void fillInFieldsTest() {
        Event event = new Event();
        Participant participant = new Participant();
        participant.setName("name");
        participant.setEmail("email");
        BankAccount bankAccount = new BankAccount("iban", "bic");
        participant.setBankAccount(bankAccount);
        contactInfoMv.loadInfo(event, participant);

        contactInfoMv.fillInFields();

        assertEquals("email", contactInfoMv.emailProperty().get());
        assertEquals("name", contactInfoMv.nameProperty().get());
        assertEquals("bic", contactInfoMv.bicProperty().get());
        assertEquals("iban", contactInfoMv.ibanProperty().get());
    }

    @Test
    void createParticipantNotFoundException() {
        when(participantCommunicator.createParticipant(any(), any(), any(), any()))
                .thenThrow(new NotFoundException("CodeNotFound"));
        assertThrows(NotFoundException.class, () ->
                contactInfoMv.createParticipant(new Event(), "name", "email", null));
    }

    @Test
    void createParticipantProcessingException(){
        when(participantCommunicator.createParticipant(any(), any(), any(), any()))
                .thenThrow(new ProcessingException("ServerOffline"));
        assertThrows(ProcessingException.class, () ->
                contactInfoMv.createParticipant(new Event(), "name", "email", null));
    }

    @Test
    void createParticipantSuccess() {
        Event event = new Event();
//        Participant participant = new Participant();
        contactInfoMv.createParticipant(event, "name", "email", null);

        verify(participantCommunicator).createParticipant(event, "name", "email", null);
        //idk if i can add stuff to these
    }

    @Test
    void updateParticipantNotFound() {
        when(participantCommunicator.updateParticipant(any()))
                .thenThrow(new NotFoundException("CodeNotFound"));
        assertThrows(NotFoundException.class, () ->
                contactInfoMv.updateParticipant(new Participant()));
    }

    @Test
    void updateParticipantProcessingExc() {
        when(participantCommunicator.updateParticipant(any()))
                .thenThrow(new ProcessingException("ServerOffLine"));
        assertThrows(ProcessingException.class, () ->
                contactInfoMv.updateParticipant(new Participant()));
    }

    @Test
    void updateParticipantSuccess() {
        Participant p = new Participant();
        contactInfoMv.updateParticipant(p);

        verify(participantCommunicator).updateParticipant(p);
        //idk if i can add stuff to these
    }

    @Test
    void isValidFalse() {
        assertFalse(contactInfoMv.isValidEmail("invalid_email.com"));
    }

    @Test
    void isValidTrue() {
        assertTrue(contactInfoMv.isValidEmail("valid_email@example.com"));
    }

    @Test
    void validInputEmptyName() {
        contactInfoMv.emailProperty().setValue("valid_email@mail.com");
        contactInfoMv.nameProperty().setValue("");
        contactInfoMv.bicProperty().setValue("BIC123");
        contactInfoMv.ibanProperty().setValue("IBAN123");
        assertFalse(contactInfoMv.validInput());
    }

    @Test
    void validInputEmptyBic() {
        contactInfoMv.nameProperty().setValue("Actual Name");
        contactInfoMv.bicProperty().setValue("");
        contactInfoMv.emailProperty().setValue("valid_email@mail.com");
        contactInfoMv.ibanProperty().setValue("IBAN123");

        assertFalse(contactInfoMv.validInput());
    }

    @Test
    void validInputInvalidEmail() {
        contactInfoMv.nameProperty().setValue("Actual Name");
        contactInfoMv.ibanProperty().setValue("IBAN123");
        contactInfoMv.bicProperty().setValue("BIC123");
        contactInfoMv.emailProperty().setValue("invalid_email.com");
        assertFalse(contactInfoMv.validInput());
    }

    @Test
    void validInputTrue() {
        contactInfoMv.nameProperty().setValue("Actual Name");
        contactInfoMv.ibanProperty().setValue("IBAN123");
        contactInfoMv.bicProperty().setValue("BIC123");
        contactInfoMv.emailProperty().setValue("valid_email@lala.com");
        assertTrue(contactInfoMv.validInput());
    }

    @Test
    void addButtonPressedIllegalArg() {
        Event event = new Event();
        Participant participant = new Participant();
        contactInfoMv.loadInfo(event, participant);

        contactInfoMv.emailProperty().setValue("");
        assertThrows(IllegalArgumentException.class, () -> contactInfoMv.addButtonPressed(null));
    }

    @Test
    void addButtonPressedNullParticipant() {

    }

    @Test
    void addButtonPressedSuccess() {

    }
}