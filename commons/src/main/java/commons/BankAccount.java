package commons;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * The BankAccount class is used to store
 * the bank details of a participant
 */
@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String iban;
    private String bic;

    /**
     * The no-arg constructor is needed for the persistence framework
     */
    public BankAccount() {

    }

    /**
     * Constructs a new bank account with the specified details
     *
     * @param iban international bank account number
     * @param bic bank identification code
     */
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