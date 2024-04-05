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
        if (currency.equals("USD")) return amount;
        return currencyCommunicator.getConversion(amount, currency, date);
    }

    public double getExchangeAgainstNew(double amount, String currency, LocalDate date) {
        if (currency.equals("USD")) return amount;
        double rate = amount/currencyCommunicator.getConversion(amount, currency, date);
        return (rate*amount);
    }

    public void setCurrentCurrency(String amount) {
        currency = amount;
    }

    public double getStandardConversion(double amount, LocalDate date) {
        if (currency.equals("USD")) return amount;
        return currencyCommunicator.getConversion(amount, currency, date);
    }

    public String getCurrentCurrency() {
        return currency;
    }

    public String getCurrentSymbol() {
        return switch (currency) {
            case "USD" -> "\u0024";
            case "EUR" -> "\u20AC";
            case "CHF" -> "fr.";
            case "JPY" -> "\u00A5";
            default -> "ERROR";
        };
    }

}
