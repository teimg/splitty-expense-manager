package client.utils.communicators.interfaces;

public interface ICurrencyCommunicator {

    String getConversion(int amount, String base, String conversion);

    String test(int amount);
}
