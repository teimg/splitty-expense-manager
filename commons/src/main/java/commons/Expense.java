package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnore
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Event event;
    @Column(name = "event_id")
    private Long eventId;

    private String purchase;
    private double amount;
    @ManyToOne
    private Participant payer;
    @ManyToMany
    private List<Participant> debtors;
    private LocalDate date;
    @JsonIgnoreProperties({"expenses"})
    @ManyToOne(fetch = FetchType.EAGER)
    private Tag tag;

    public Expense(Long eventId, String purchase, double amount,
                   Participant payer, List<Participant> debtors, LocalDate date, Tag tag) {
        this.eventId = eventId;
        this.purchase = purchase;
        this.amount = amount;
        this.payer = payer;
        this.debtors = debtors;
        this.date = date;
        this.tag = tag;
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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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
     * Tag getter
     * @return tag
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * Tag setter
     * @param tag set
     */
    public void setTag(Tag tag) {
        this.tag = tag;
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
                Objects.equals(tag, expense.tag) &&
                Objects.equals(date, expense.date);
    }

    /**
     * Hashcode
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, event, purchase, amount, payer, debtors, date, tag);
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
                ", tag=" + tag +
                '}';
    }

}
