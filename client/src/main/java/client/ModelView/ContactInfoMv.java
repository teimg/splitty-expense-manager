package client.ModelView;

import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IParticipantCommunicator;
import com.google.inject.Inject;
import commons.BankAccount;
import commons.Event;
import commons.Participant;

public class ContactInfoMv {

    IEventCommunicator eventServer;

    IParticipantCommunicator participantServer;

    @Inject
    public ContactInfoMv(IEventCommunicator eventCommunicator, IParticipantCommunicator participantCommunicator) {
        this.eventServer = eventCommunicator;
        this.participantServer = participantCommunicator;
    }

    public Event getEventByInviteCode(String inviteCode) {
        return eventServer.getEventByInviteCode(inviteCode);
    }

    public void createParticipant(Event event, String name, String email, BankAccount bankAccount) {
        participantServer.createParticipant(event, name, email, bankAccount);
    }

    public void updateParticipant(Participant participant) {
        participantServer.updateParticipant(participant);
    }
}
