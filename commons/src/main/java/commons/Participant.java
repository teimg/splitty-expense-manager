package commons;


public class Participant {
    private int id;
    private String firstName;
    private String lastName;
    private BankAccount bankAccount;

    /**
     *
     * @param id
     * @param firstName
     * @param lastName
     */
    public Participant(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}