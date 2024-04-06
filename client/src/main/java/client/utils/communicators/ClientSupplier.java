package client.utils.communicators;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientSupplier {

    @Bean
    public Client getClient() {
        return ClientBuilder.newClient();
    }

}
