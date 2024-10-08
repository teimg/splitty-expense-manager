package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @OneToOne(cascade = CascadeType.ALL)
    private BankAccount bankAccount;

    @JsonIgnore
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Event event;

    @Column(name = "event_id")
    private Long eventId;
    private String email;

    /**
     * Constructs a participant with a given name
     * @param name represents how the participant is called
     */
    public Participant(String name) {
        this.name = name;
    }

    /**
     * Constructor for participant with a bankAccount
     * @param name name
     * @param bankAccount bankAccount
     */
    public Participant(String name, BankAccount bankAccount) {
        this.name = name;
        this.bankAccount = bankAccount;
    }

    /**
     * Constructor for participant with email
     * @param name name
     * @param email email
     */
    public Participant(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * The no-arg constructor is needed for the persistence framework
     */
    public Participant() {

    }

    /**
     * Id Getter
     * @return Id
     */
    public long getId() {
        return id;
    }
    /**
     * Id Setter
     * @param id set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Name Getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Name Setter
     * @param name set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * BankAccount getter
     * @return bankAccount
     */
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    /**
     * BankAccount Setter
     * @param bankAccount set
     */
    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    /**
     * Email getter
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Email setter
     * @param email set
     */
    public void setEmail(String email) {
        this.email = email;
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

    /**
     * toString
     * @return String representation
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Participant{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", bankAccount=").append(bankAccount);
        sb.append('}');
        return sb.toString();
    }
}