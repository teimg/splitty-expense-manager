package client.utils.communicators.implementations;

import client.utils.ClientConfiguration;
import client.utils.communicators.interfaces.ICurrencyCommunicator;
import com.google.inject.Inject;
import jakarta.ws.rs.BadRequestException;
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
    public double getConversion(double amount, String currency, LocalDate date) throws IllegalArgumentException {
        try {
            return ClientBuilder.newClient()
                    .target(origin)
                    .path("api/currency/{amount}/{currency}/{date}")
                    .resolveTemplate("amount", amount)
                    .resolveTemplate("currency", currency)
                    .resolveTemplate("date", date)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(Double.class);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Currency Error - Cannot Convert");
        }
    }

}
