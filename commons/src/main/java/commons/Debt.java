package commons;

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

}
