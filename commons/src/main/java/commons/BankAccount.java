package commons;

import jakarta.persistence.Embeddable;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankAccount that = (BankAccount) o;

        if (!Objects.equals(iban, that.iban)) return false;
        return Objects.equals(bic, that.bic);
    }

    @Override
    public int hashCode() {
        int result = iban != null ? iban.hashCode() : 0;
        result = 31 * result + (bic != null ? bic.hashCode() : 0);
        return result;
    }
}