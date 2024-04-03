package client.currency;

import client.utils.communicators.interfaces.ICurrencyCommunicator;
import com.google.inject.Inject;

public class Exchanger {

    public Exchanger() {

    }

    private ICurrencyCommunicator currencyCommunicator;

    @Inject
    public Exchanger(ICurrencyCommunicator currencyCommunicator) {
        this.currencyCommunicator = currencyCommunicator;
    }


}
