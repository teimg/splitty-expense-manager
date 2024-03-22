package client.ModelView;

import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IParticipantCommunicator;
import com.google.inject.Inject;
import commons.BankAccount;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;

public class ContactInfoMv {

    private final IEventCommunicator eventServer;

    private final IParticipantCommunicator participantServer;

    @Inject
    public ContactInfoMv(IEventCommunicator eventCommunicator,
                         IParticipantCommunicator participantCommunicator) {
        this.eventServer = eventCommunicator;
        this.participantServer = participantCommunicator;
    }

    public Event getEventByInviteCode(String inviteCode) {
        try {
            return eventServer.getEventByInviteCode(inviteCode);
        } catch (ProcessingException e) {
            throw new ProcessingException("ServerOffLine");
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
}
