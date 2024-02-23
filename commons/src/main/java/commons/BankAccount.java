package commons;

import jakarta.persistence.Embeddable;

@Embeddable
public class BankAccount {
    private String iban;
    private String bic;

    public BankAccount() {

    }

    public BankAccount(String iban, String bic) {
        this.iban = iban;
        this.bic = bic;
    }
}