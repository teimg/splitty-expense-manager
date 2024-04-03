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
        return (1/currencyCommunicator.getConversion(amount, currency, date));
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
            case "USD" -> "$";
            case "EUR" -> "€";
            case "CHF" -> "fr.";
            case "JPY" -> "¥";
            default -> "ER";
        };
    }

}
