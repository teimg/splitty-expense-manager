package commons;

public class BankAccount {
    private int id;
    private int userId;
    private String iBAN;
    private String bIC;
    private double balance;

    /**
     *
     * @param id
     * @param userId
     * @param iBAN
     * @param bIC
     * @param balance
     */
    public BankAccount(int id, int userId, String iBAN, String bIC, double balance) {
        this.id = id;
        this.userId = userId;
        this.IBAN = IBAN;
        this.bIC = bIC;
        this.balance = balance;
    }

}