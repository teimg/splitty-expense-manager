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
     * Constructor method for the Debt class
     * @param creditor Participant who is owed money
     * @param debtor Participant who owes money
     * @param amount Amount of money owed
     * @param hasPaid Status of the debt being paid
     * @param summary Brief summary of the debt
     * @param description Detailed description of the debt
     */
    public Debt(Participant creditor, Participant debtor, double amount, boolean hasPaid, String summary, String description) {
        this.creditor = creditor;
        this.debtor = debtor;
        this.amount = amount;
        this.hasPaid = hasPaid;
        this.summary = summary;
        this.description = description;
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
        return summary;
    }

    /**
     * setter method for summary
     * @param summary debt summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * getter method for description
     * @return debt description
     */
    public String getDescription() {
        return description;
    }

    /**
     * setter method for description
     * @param description description of debt
     */
    public void setDescription(String description) {
        this.description = description;
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
