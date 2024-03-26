package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * The Expense class represents an expense record associated with an event.
 * It contains details about a specific purchase, the amount, the payer, and a list of debtors.
 */
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Expense.class
)
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Event event;
    private String purchase;
    private double amount;
    @ManyToOne
    private Participant payer;
    @ManyToMany
    private List<Participant> debtors;
    private LocalDate date;

    /**
     * Constructs a new Expense with the specified details.
     *
     * @param event    the event associated with the expense
     * @param purchase the description of the purchase
     * @param amount   the amount of the expense
     * @param payer    the participant who paid for the expense
     * @param debtors  the list of participants who owe a share of the expense
     * @param date     the date that the expense was paid
     */
    public Expense(Event event, String purchase, double amount,
                   Participant payer, List<Participant> debtors, LocalDate date) {
        this.id = id;
        this.event = event;
        this.purchase = purchase;
        this.amount = amount;
        this.payer = payer;
        this.debtors = debtors;
        this.date = date;
    }

    /**
     * No arg constructor
     */
    public Expense() {

    }

    /**
     * Id getter
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Id setter
     * @param id set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Event getter
     * @return event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Event setter
     * @param event set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Purchase getter
     * @return purchase
     */
    public String getPurchase() {
        return purchase;
    }

    /**
     * Purchase setter
     * @param purchase set
     */
    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    /**
     * Amount getter
     * @return amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Amount setter
     * @param amount set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Payer getter
     * @return payer
     */
    public Participant getPayer() {
        return payer;
    }

    /**
     * Payer setter
     * @param payer set
     */
    public void setPayer(Participant payer) {
        this.payer = payer;
    }

    /**
     * Debtors getter
     * @return debtors
     */
    public List<Participant> getDebtors() {
        return debtors;
    }

    /**
     * Debtors setter
     * @param debtors set
     */
    public void setDebtors(List<Participant> debtors) {
        this.debtors = debtors;
    }

    /**
     * Date getter
     * @return date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Date setter
     * @param date set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Equals method
     * @param o other
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expense expense)) return false;
        return id == expense.id &&
                Double.compare(expense.amount, amount) == 0 &&
                Objects.equals(purchase, expense.purchase) &&
                Objects.equals(payer, expense.payer) &&
                Objects.equals(debtors, expense.debtors) &&
                Objects.equals(date, expense.date);
    }

    /**
     * Hashcode
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, event, purchase, amount, payer, debtors, date);
    }

    /**
     * toString
     * @return string representation
     */
    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", event=" + event +
                ", purchase='" + purchase + '\'' +
                ", amount=" + amount +
                ", payer=" + payer +
                ", debtors=" + debtors +
                ", date=" + date +
                '}';
    }

    /**
     * Valid String representation
     * @return string representation
     */
    public String description() {
        StringBuilder desc = new StringBuilder(getDate().toString() + "    "
                + getPayer().getName() + " paid "
                + getAmount() + "$ for " + getPurchase() + "\n");
        desc.append(" ".repeat(32));
        desc.append("Debtors: ");
        for (int i = 0; i < debtors.size() - 1; i++) {
            desc.append(debtors.get(i).getName()).append(", ");
        }
        desc.append(debtors.get(debtors.size()-1).getName());
        return desc.toString();
    }
}
