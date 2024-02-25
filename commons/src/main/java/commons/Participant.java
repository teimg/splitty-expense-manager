package commons;


import jakarta.persistence.*;

import java.util.Objects;

/**
 * The Participant class represents a person
 * that is involved in an event
 */
@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @Embedded
    private BankAccount bankAccount;
    @ManyToOne(fetch = FetchType.EAGER)
    private Event event;

    /**
     * Constructs a participant with a given name
     * @param name represents how the participant is called
     */
    public Participant(String name) {
        this.name = name;
    }

    /**
     * The no-arg constructor is needed for the persistence framework
     */
    public Participant() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        if (id != that.id) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(bankAccount, that.bankAccount);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (bankAccount != null ? bankAccount.hashCode() : 0);
        return result;
    }
}