package commons;

import java.util.Objects;

public class Debt {

    private Participant creditor;

    private Participant debtor;

    private double amount;

    private boolean hasPaid;

    private String summary;

    private String description;

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

    /**
     * Getter method for creditor
     * @return Creditor
     */
    public Participant getCreditor() {
        return creditor;
    }

    /**
     * Setter method for creditor
     * @param creditor - Participant who is owed money
     */
    public void setCreditor(Participant creditor) {
        this.creditor = creditor;
    }

    /**
     * Getter method for debtor
     * @return Debtor
     */
    public Participant getDebtor() {
        return debtor;
    }

    /**
     * Setter method for debtor
     * @param debtor - Participant who owes money
     */
    public void setDebtor(Participant debtor) {
        this.debtor = debtor;
    }

    /**
     * Getter method for amount owed
     * @return Amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Setter method for amount owed
     * @param amount - owed
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Standard equals method
     * @param o - other
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Debt debt = (Debt) o;

        if (Double.compare(amount, debt.amount) != 0) return false;
        if (!Objects.equals(creditor, debt.creditor)) return false;
        return Objects.equals(debtor, debt.debtor);
    }

    /**
     * Standard hashcode method
     * @return hashcode
     */
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

    /**
     * toString method for debugging
     * @return String representation of object
     */
    @Override
    public String toString() {
        return "Debt{" +
                "creditor=" + creditor +
                ", debtor=" + debtor +
                ", amount=" + amount +
                '}';
    }
}
