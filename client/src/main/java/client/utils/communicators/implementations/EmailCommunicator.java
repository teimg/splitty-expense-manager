package client.utils.communicators.implementations;

import client.utils.ClientConfiguration;
import client.utils.communicators.interfaces.IEmailCommunicator;
import com.google.inject.Inject;
import commons.EmailRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class EmailCommunicator implements IEmailCommunicator {

    private final String origin;

    private final Client client;

    @Inject
    public EmailCommunicator(ClientConfiguration config, Client client) {
        this.origin = config.getServer();
        this.client = client;
    }

    public String getOrigin() {
        return origin;
    }

    @Override
    public EmailRequest sendEmail(EmailRequest emailRequest) {
        return client
                .target(origin).path("api/email")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(emailRequest, APPLICATION_JSON), EmailRequest.class);
    }

    @Override
    public EmailRequest getAll() {
        return client
                .target(origin).path("api/email")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(EmailRequest.class);
    }


}
