package commons;

import java.util.Objects;

public class Debt {

    private Participant creditor;

    private Participant debtor;

    private double amount;

    /**
     * Constructor method for the debt class
     * @param creditor - Participant who is owed money
     * @param debtor - Participant who owes money
     * @param amount - Amount of money owed
     */
    public Debt(Participant creditor, Participant debtor, double amount) {
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
    }
    
    public Participant getCreditor() {
        return creditor;
    }

    public void setCreditor(Participant creditor) {
        this.creditor = creditor;
    }

    public Participant getDebtor() {
        return debtor;
    }

    public void setDebtor(Participant debtor) {
        this.debtor = debtor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Debt debt = (Debt) o;

        if (Double.compare(amount, debt.amount) != 0) return false;
        if (!Objects.equals(creditor, debt.creditor)) return false;
        return Objects.equals(debtor, debt.debtor);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = creditor != null ? creditor.hashCode() : 0;
        result = 31 * result + (debtor != null ? debtor.hashCode() : 0);
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    @Override
    public String toString() {
        return "Debt{" +
                "creditor=" + creditor +
                ", debtor=" + debtor +
                ", amount=" + amount +
                '}';
    }
}
