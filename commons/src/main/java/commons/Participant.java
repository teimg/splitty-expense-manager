package commons;


public class Participant {
    private int id;
    private String name;
    private BankAccount bankAccount;

    /**
     *
     * @param id
     * @param name
     */
    public Participant(int id, String name) {
        this.id = id;
        this.name = name;
    }

}