package client.utils;

import commons.Event;
import commons.Expense;
import commons.Participant;

import java.util.List;
import java.util.Objects;

public class ExpenseBuilder {

    private int id;
    private Event event;
    private String purchase;
    private double amount;
    private Participant payer;
    private List<Participant> debtors;

    public ExpenseBuilder() {
    }

    public Expense build(){
        return  new Expense(id, event, purchase, amount, payer, debtors);
    }

    public int getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public String getPurchase() {
        return purchase;
    }

    public double getAmount() {
        return amount;
    }

    public Participant getPayer() {
        return payer;
    }

    public List<Participant> getDebtors() {
        return debtors;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPayer(Participant payer) {
        this.payer = payer;
    }

    public void setDebtors(List<Participant> debtors) {
        this.debtors = debtors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseBuilder that = (ExpenseBuilder) o;
        return getId() == that.getId() && Double.compare(getAmount(), that.getAmount()) == 0 && Objects.equals(getEvent(), that.getEvent()) && Objects.equals(getPurchase(), that.getPurchase()) && Objects.equals(getPayer(), that.getPayer()) && Objects.equals(getDebtors(), that.getDebtors());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEvent(), getPurchase(), getAmount(), getPayer(), getDebtors());
    }

    @Override
    public String
    toString() {
        final StringBuilder sb = new StringBuilder("ExpenseBuilder{");
        sb.append("id=").append(id);
        sb.append(", event=").append(event);
        sb.append(", purchase='").append(purchase).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", payer=").append(payer);
        sb.append(", debtors=").append(debtors);
        sb.append('}');
        return sb.toString();
    }
}
