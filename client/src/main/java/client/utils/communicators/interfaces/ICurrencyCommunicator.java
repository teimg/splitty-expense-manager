package client.utils.communicators.interfaces;

import java.time.LocalDate;

public interface ICurrencyCommunicator {

    double getConversion(double amount, String currency, LocalDate date);

}
