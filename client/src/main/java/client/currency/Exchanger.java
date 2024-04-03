package client.currency;

import client.utils.communicators.interfaces.ICurrencyCommunicator;
import com.google.inject.Inject;

import java.time.LocalDate;
import java.util.Optional;

public class Exchanger {

    public Exchanger() {

    }

    private ICurrencyCommunicator currencyCommunicator;

    @Inject
    public Exchanger(ICurrencyCommunicator currencyCommunicator) {
        this.currencyCommunicator = currencyCommunicator;
    }

    public double getExchangeAgainstBase(double amount, String currency, LocalDate date) {
        return currencyCommunicator.getConversion(amount, currency, date);
    }

    public double getExchangeAgainstNew(double amount, String currency, LocalDate date) {
        return (1/currencyCommunicator.getConversion(amount, currency, date));
    }

}
