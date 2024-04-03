package client.utils.communicators.implementations;

import client.utils.ClientConfiguration;
import client.utils.communicators.interfaces.ICurrencyCommunicator;
import com.google.inject.Inject;
import jakarta.ws.rs.client.ClientBuilder;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class CurrencyCommunicator implements ICurrencyCommunicator {

    private final String origin;

    @Inject
    public CurrencyCommunicator(ClientConfiguration config) {
        origin = config.getServer();
    }

    @Override
    public String getConversion(int amount, String base, String conversion) {
        return ClientBuilder.newClient()
                .target(origin)
                .path("api/currency/{amount}/{baseCurrency}/{conversionCurrency}")
                .resolveTemplate("amount", amount)
                .resolveTemplate("baseCurrency", base)
                .resolveTemplate("conversionCurrency", conversion)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(String.class);
    }

    @Override
    public String test(int amount) {
        return ClientBuilder.newClient()
                .target(origin)
                .path("api/currency/{amount}/{baseCurrency}/{conversionCurrency}")
                .resolveTemplate("baseCurrency", "USD")
                .resolveTemplate("conversionCurrency", "CHF")
                .resolveTemplate("amount", amount)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(String.class);
    }


}
