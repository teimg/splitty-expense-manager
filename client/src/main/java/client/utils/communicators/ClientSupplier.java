package client.utils.communicators;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

public class ClientSupplier {

    public Client getClient() {
        return ClientBuilder.newClient();
    }

}
