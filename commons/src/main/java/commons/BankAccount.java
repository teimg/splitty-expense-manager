package commons;

public class BankAccount {
    private int id;
    private int userId;
    private String IBAN;
    private String BIC;
    private double balance;

    /**
     *
     * @param id
     * @param userId
     * @param IBAN
     * @param BIC
     * @param balance
     */
    public BankAccount(int id, int userId, String IBAN, String BIC, double balance) {
        this.id = id;
        this.userId = userId;
        this.IBAN = IBAN;
        this.BIC = BIC;
        this.balance = balance;
    }

}