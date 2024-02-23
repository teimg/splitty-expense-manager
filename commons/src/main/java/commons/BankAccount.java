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

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }
}