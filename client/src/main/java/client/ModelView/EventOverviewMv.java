package client.ModelView;

import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IParticipantCommunicator;
import commons.Event;
import commons.Participant;
import javafx.concurrent.Task;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.Optional;

public class EventOverviewMv {

    private final IEventCommunicator eventCommunicator;

    private final IParticipantCommunicator participantCommunicator;

    private Event event;

    private Participant selectedPayer;

    private Task<Void> longPollingTask = null;
    private Thread pollingThread = null;

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

    public Participant getSelectedPayer() {
        return selectedPayer;
    }

    public void setSelectedPayer(Participant selectedPayer) {
        this.selectedPayer = selectedPayer;
    }

    public Task<Void> getLongPollingTask() {
        return longPollingTask;
    }

    public void setLongPollingTask(Task<Void> longPollingTask) {
        this.longPollingTask = longPollingTask;
    }

    public Thread getPollingThread() {
        return pollingThread;
    }

    public void setPollingThread(Thread pollingThread) {
        this.pollingThread = pollingThread;
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

    public Event eventCommunicatorGetEvent() {
        return eventCommunicator.getEvent(event.getId());
    }

}
