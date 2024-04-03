package client.utils.communicators.implementations;

import client.utils.ClientConfiguration;
import client.utils.communicators.interfaces.ICurrencyCommunicator;
import com.google.inject.Inject;
import jakarta.ws.rs.client.ClientBuilder;

import java.time.LocalDate;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CurrencyCommunicator implements ICurrencyCommunicator {

    private final String origin;

    @Inject
    public CurrencyCommunicator(ClientConfiguration config) {
        origin = config.getServer();
    }

    @Override
    public String getConversion(double amount, String currency, LocalDate date) {
        return ClientBuilder.newClient()
                .target(origin)
                .path("api/currency/{amount}/{currency}/{date}")
                .resolveTemplate("amount", amount)
                .resolveTemplate("currency", currency)
                .resolveTemplate("date", date)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(String.class);
    }

}
