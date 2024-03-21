package commons;

import java.util.Objects;

public class Debt {

    private Participant creditor;

    private Participant debtor;

    private double amount;

    private boolean hasPaid;

    /**
     * Constructor method for the Debt class
     *
     * @param creditor Participant who is owed money
     * @param debtor   Participant who owes money
     * @param amount   Amount of money owed
     */
    public Debt(Participant creditor, Participant debtor, double amount) {
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
        this.hasPaid = false;
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
     * Getter method for paid status
     * @return hasPaid
     */
    public boolean isHasPaid() {
        return hasPaid;
    }

    /**
     * Setter for hasPaid
     * @param hasPaid - paid status
     */
    public void setHasPaid(boolean hasPaid) {
        this.hasPaid = hasPaid;
    }

    /**
     * getter method for summary
     * @return summary of debt
     */
    public String getSummary() {
        return "";
    }

    /**
     * getter method for description
     * @return debt description
     */
    public String getDescription() {
        return "";
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
        if (hasPaid != debt.hasPaid) return false;
        if (!Objects.equals(creditor, debt.creditor)) return false;
        return Objects.equals(debtor, debt.debtor);
    }

    /**
     * Standard hashcode method
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(creditor, debtor, amount, hasPaid);
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
                ", hasPaid=" + hasPaid +
                '}';
    }
}
