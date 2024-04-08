package client.utils.communicators.implementations;

import client.utils.ClientConfiguration;
import client.utils.communicators.ClientSupplier;
import client.utils.communicators.interfaces.ICurrencyCommunicator;
import com.google.inject.Inject;

import java.time.LocalDate;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CurrencyCommunicator implements ICurrencyCommunicator {

    private final String origin;

    private final ClientSupplier client;

    @Inject
    public CurrencyCommunicator(ClientConfiguration config, ClientSupplier client) {
        this.origin = config.getServer();
        this.client = client;
    }

    @Override
    public double getConversion(double amount, String currency, LocalDate date)
            throws IllegalArgumentException {
        try {
            return client.getClient()
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

    @Override
    public String clearCache() {
        return client.getClient()
                .target(origin)
                .path("api/currency/cache")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete(String.class);
    }

}
