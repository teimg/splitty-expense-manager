package client.ModelView;

import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IParticipantCommunicator;
import commons.Event;
import commons.Participant;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.Optional;

public class EventOverviewMv {

    private final IEventCommunicator eventCommunicator;

    private final IParticipantCommunicator participantCommunicator;

    private Event event;

    private Participant selectedPayer;

    public EventOverviewMv(IEventCommunicator eventCommunicator,
                           IParticipantCommunicator participantCommunicator) {
        this.eventCommunicator = eventCommunicator;
        this.participantCommunicator = participantCommunicator;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public IEventCommunicator getEventCommunicator() {
        return eventCommunicator;
    }

    public Participant getSelectedPayer() {
        return selectedPayer;
    }

    public void setSelectedPayer(Participant selectedPayer) {
        this.selectedPayer = selectedPayer;
    }


    public void copyInviteCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(event.getInviteCode());
        clipboard.setContent(content);
    }

    public void deleteParticipant(Optional<Participant> p) {
        participantCommunicator.deleteParticipant(p.get().getId());
    }

    public Event eventCommunicatorCheckForUpdate(long eventId) {
        return eventCommunicator.checkForEventUpdates(eventId);
    }

    public Event eventCommRenameEvent(String newName) {
        return eventCommunicator.renameEvent(event.getId(), newName);
    }

    public Event eventCommunicatorGetEvent() {
        return eventCommunicator.getEvent(event.getId());
    }

}
