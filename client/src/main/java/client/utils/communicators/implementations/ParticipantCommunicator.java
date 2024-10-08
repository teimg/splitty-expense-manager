package client.utils.communicators.implementations;

import client.utils.ClientConfiguration;
import client.utils.communicators.ClientSupplier;
import client.utils.communicators.interfaces.IParticipantCommunicator;
import com.google.inject.Inject;
import commons.BankAccount;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.client.Entity;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ParticipantCommunicator implements IParticipantCommunicator {

    private final String origin;

    private final ClientSupplier client;

    @Inject
    public ParticipantCommunicator(ClientConfiguration config, ClientSupplier client) {
        this.origin = config.getServer();
        this.client = client;
    }

    @Override
    public Participant createParticipant(Event event, String name,
                                         String email, BankAccount bankAccount) {
        Participant toServer = new Participant(name, email);
        toServer.setEventId(event.getId());
        toServer.setBankAccount(bankAccount);
        return client.getClient()
                .target(origin).path("api/participants")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(toServer, APPLICATION_JSON), Participant.class);
    }

    @Override
    public Participant getParticipant(long id) {
        return client.getClient()
                .target(origin).path("api/participants/{id}")
                .resolveTemplate("id", id)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(Participant.class);
    }

    @Override
    public Participant updateParticipant(Participant participant) {
        return client.getClient()
                .target(origin).path("api/participants/{id}")
                .resolveTemplate("id", participant.getId())
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .put(Entity.entity(participant, APPLICATION_JSON), Participant.class);
    }

    @Override
    public Participant deleteParticipant(long id) {
        return client.getClient()
                .target(origin).path("api/participants/{id}")
                .resolveTemplate("id", id)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .delete(Participant.class);
    }

}
