package client.ModelView;

import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IParticipantCommunicator;
import commons.Event;
import commons.Participant;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventOverviewMvTest {

    @Mock
    private IEventCommunicator eventCommunicator;

    @Mock
    private IParticipantCommunicator participantCommunicator;

    private Event event;
    private Participant participant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.event = new Event("name");
        this.participant = new Participant();
    }

    @Test
    void testGetSetEvent() {
        EventOverviewMv mv = new EventOverviewMv(eventCommunicator, participantCommunicator);
        mv.setEvent(event);
        assertEquals(event, mv.getEvent());
    }

    @Test
    void getEventCommunicator() {
        EventOverviewMv mv = new EventOverviewMv(eventCommunicator, participantCommunicator);
        assertNotNull(mv.getEventCommunicator());
    }

    @Test
    void testGetSetSelectedPayer() {
        EventOverviewMv mv = new EventOverviewMv(eventCommunicator, participantCommunicator);
        mv.setSelectedPayer(participant);
        assertEquals(participant, mv.getSelectedPayer());
    }

//    @Test
//    void testCopyInviteCode() {
//        EventOverviewMv mv = new EventOverviewMv(eventCommunicator, participantCommunicator);
//        Clipboard clipboardMock = mock(Clipboard.class);
//        when(Clipboard.getSystemClipboard()).thenReturn(clipboardMock);
//
//        ClipboardContent contentMock = mock(ClipboardContent.class);
//        when(clipboardMock.getContent(DataFormat.PLAIN_TEXT)).thenReturn(contentMock);
//
//        mv.setEvent(event);
//        when(event.getInviteCode()).thenReturn("invite code");
//
//        mv.copyInviteCode();
//
//        verify(contentMock).putString("invite code");
//        verify(clipboardMock).setContent(contentMock);
//    }

    @Test
    void deleteParticipantTest() {
        EventOverviewMv mv = new EventOverviewMv(eventCommunicator, participantCommunicator);
        Optional<Participant> optionalParticipant = Optional.of(participant);
        participant.setId(592L);
        mv.deleteParticipant(optionalParticipant);

        verify(participantCommunicator).deleteParticipant(592L);
    }

    @Test
    void eventCommunicatorCheckForUpdateTest() {
        EventOverviewMv mv = new EventOverviewMv(eventCommunicator, participantCommunicator);
        long eventId = 811L;
        when(eventCommunicator.checkForEventUpdates(eventId)).thenReturn(event);

        assertEquals(event, mv.eventCommunicatorCheckForUpdate(eventId));
    }

    @Test
    void eventCommunicatorGetEventTest() {
        EventOverviewMv mv = new EventOverviewMv(eventCommunicator, participantCommunicator);
        mv.setEvent(event);
        event.setId(333L);
        when(eventCommunicator.getEvent(333L)).thenReturn(event);

        assertEquals(event, mv.eventCommunicatorGetEvent());
    }


}
