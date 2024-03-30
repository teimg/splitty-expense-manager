package client.utils.communicators.implementations;

import client.utils.ClientConfiguration;
import client.utils.communicators.interfaces.IAdminCommunicator;
import com.google.inject.Inject;
import commons.PasswordRequest;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

public class AdminCommunicator implements IAdminCommunicator {

    private final String origin;

    @Inject
    public AdminCommunicator(ClientConfiguration config) {
        origin = config.getServer();
    }

    @Override
    public boolean validatePassword(String password) {
        PasswordRequest passwordRequest = new PasswordRequest(password);

        int status = ClientBuilder.newClient()
            .target(origin)
            .path("/api/admin/auth")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(passwordRequest, MediaType.APPLICATION_JSON))
            .getStatus();

        return status >= 200 && status < 300;
    }
}
