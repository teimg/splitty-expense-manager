package client.utils;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ExpenseBuilder {

    private int id;
    private Long eventId;
    private String purchase;
    private long amount;
    private Participant payer;
    private List<Participant> debtors;
    private LocalDate date;
    private Tag tag;

    public ExpenseBuilder() {
    }

    public Expense build(){
        double expenseAmount = amount / 100.0;

        return  new Expense(eventId, purchase, expenseAmount, payer, debtors, date, tag);
    }

    public int getId() {
        return id;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getPurchase() {
        return purchase;
    }

    public long getAmount() {
        return amount;
    }

    public Participant getPayer() {
        return payer;
    }

    public List<Participant> getDebtors() {
        return debtors;
    }

    public Tag getTag() {
        return tag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setPayer(Participant payer) {
        this.payer = payer;
    }

    public void setDebtors(List<Participant> debtors) {
        this.debtors = debtors;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseBuilder that = (ExpenseBuilder) o;
        return getId() == that.getId() && getAmount() == that.getAmount()
            && Objects.equals(getEventId(), that.getEventId())
            && Objects.equals(getPurchase(), that.getPurchase())
            && Objects.equals(getPayer(), that.getPayer())
            && Objects.equals(getDebtors(), that.getDebtors())
            && Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEventId(),
            getPurchase(), getAmount(),
            getPayer(), getDebtors(), getDate());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExpenseBuilder{");
        sb.append("id=").append(id);
        sb.append(", eventId=").append(eventId);
        sb.append(", payer=").append(payer);
        sb.append(", purchase='").append(purchase).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", date=").append(date);
        sb.append("\ndebtors: \n");

        for (Participant x : debtors){
            sb.append(x.getName()).append("\n");
        }

        sb.append('}');
        return sb.toString();
    }
}
