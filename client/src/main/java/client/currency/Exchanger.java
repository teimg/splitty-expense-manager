package client.currency;

import client.utils.communicators.interfaces.ICurrencyCommunicator;
import com.google.inject.Inject;

import java.time.LocalDate;

public class Exchanger {

    public Exchanger() {
    }

    private ICurrencyCommunicator currencyCommunicator;

    private String currency;

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

    public void setCurrentCurrency(String amount) {
        currency = amount;
    }

    public double getStandardConversion(double amount, LocalDate date) {
        return currencyCommunicator.getConversion(amount, currency, date);
    }

    public String getCurrentCurreny() {
        return currency;
    }
}
