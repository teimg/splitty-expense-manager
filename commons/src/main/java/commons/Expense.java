package commons;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * The Expense class represents an expense record associated with an event.
 * It contains details about a specific purchase, the amount, the payer, and a list of debtors.
 */
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
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
     * @param id       the unique identifier for the expense
     * @param event    the event associated with the expense
     * @param purchase the description of the purchase
     * @param amount   the amount of the expense
     * @param payer    the participant who paid for the expense
     * @param debtors  the list of participants who owe a share of the expense
     * @param date     the date that the expense was paid
     */
    public Expense(int id, Event event, String purchase, double amount,
                   Participant payer, List<Participant> debtors, LocalDate date) {
        this.id = id;
        this.event = event;
        this.purchase = purchase;
        this.amount = amount;
        this.payer = payer;
        this.debtors = debtors;
        this.date = date;
    }

    public Expense() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Participant getPayer() {
        return payer;
    }

    public void setPayer(Participant payer) {
        this.payer = payer;
    }

    public List<Participant> getDebtors() {
        return debtors;
    }

    public void setDebtors(List<Participant> debtors) {
        this.debtors = debtors;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expense expense)) return false;
        return id == expense.id &&
                Double.compare(expense.amount, amount) == 0 &&
                Objects.equals(event, expense.event) &&
                Objects.equals(purchase, expense.purchase) &&
                Objects.equals(payer, expense.payer) &&
                Objects.equals(debtors, expense.debtors) &&
                Objects.equals(date, expense.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, purchase, amount, payer, debtors, date);
    }

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
}
