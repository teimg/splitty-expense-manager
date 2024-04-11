package client.ModelView;


import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IEventUpdateProvider;
import client.utils.communicators.interfaces.IExpenseCommunicator;
import client.utils.communicators.interfaces.IParticipantCommunicator;
import com.google.inject.Inject;
import commons.Event;
import commons.EventChange;
import commons.Participant;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;


import java.util.Optional;


public class EventOverviewMv {


    private final IEventCommunicator eventCommunicator;

    private final IParticipantCommunicator participantCommunicator;

    private final IExpenseCommunicator expenseCommunicator;

    private final IEventUpdateProvider eventUpdateProvider;


    private Participant selectedPayer;

    @Inject
    public EventOverviewMv(IEventCommunicator eventCommunicator,
                           IParticipantCommunicator participantCommunicator,
                           IExpenseCommunicator expenseCommunicator,
                           IEventUpdateProvider eventUpdateProvider) {
        this.eventCommunicator = eventCommunicator;
        this.participantCommunicator = participantCommunicator;
        this.expenseCommunicator = expenseCommunicator;
        this.eventUpdateProvider = eventUpdateProvider;
    }


    public Event getEvent() {
        return eventUpdateProvider.event();
    }

    public IEventUpdateProvider getEventUpdateProvider() {
        return eventUpdateProvider;
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
        content.putString(getEvent().getInviteCode());
        clipboard.setContent(content);
    }


    public void deleteParticipant(Optional<Participant> p) {
        participantCommunicator.deleteParticipant(p.get().getId());
    }


    public EventChange eventCommunicatorCheckForUpdate(long eventId) {
        return eventCommunicator.checkForEventUpdates(eventId);
    }

    public Event eventCommRenameEvent(String newName) {
        return eventCommunicator.renameEvent(getEvent().getId(), newName);
    }

    public void deleteEvent(long id) {
        expenseCommunicator.deleteExpense(id);
        eventCommunicator.updateEvent(getEvent());

    }


    public Event eventCommunicatorGetEvent() {
        return eventCommunicator.getEvent(getEvent().getId());
    }


}

