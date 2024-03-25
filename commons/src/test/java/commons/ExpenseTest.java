package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class ExpenseTest {

    private Expense expenseOne;
    private Expense expenseTwo;
    private Expense expenseThree;
    private Event event;


    @BeforeEach
    public void setUp() {
        Participant participant1 = new Participant("First One");
        Participant participant2 = new Participant("Second One");
        Participant participant3 = new Participant("Third One");
        List<Participant> debtors = new ArrayList<>();
        debtors.add(participant2);
        debtors.add(participant3);
        Event event = new Event("Test", "InviteCode", debtors,
                new Date(2024, 1, 10), new Date(2024, 2, 10));
        this.expenseOne = new Expense(event, "Food", 30.5,
                participant1, debtors, LocalDate.of(2024, Month.APRIL, 1),
                new Tag("Test", 0, 0, 0));
        this.expenseTwo = new Expense(event, "Food", 31.5,
                participant2, debtors, LocalDate.of(2024, Month.APRIL, 2),
                new Tag("Test", 0, 0, 0));
        this.expenseThree = new Expense(event, "Food", 30.5,
                participant1, debtors, LocalDate.of(2024, Month.APRIL, 1),
                new Tag("Test", 0, 0, 0));
        expenseOne.setId(1);
        expenseThree.setId(1);
        this.event = event;
    }

    @Test
    public void testConstructors() {
        assertNotNull(expenseOne);
        assertNotNull(expenseTwo);
        assertNotNull(expenseThree);
        Expense jpaConstructor = new Expense();
        assertNotNull(jpaConstructor);
    }

    @Test
    public void getId() {
        assertEquals(1, expenseOne.getId());
    }

    @Test
    public void getEvent() {
        assertEquals(event, expenseOne.getEvent());
    }

    @Test
    public void getPurchase() {
        assertEquals("Food", expenseOne.getPurchase());
    }

    @Test
    public void getAmount() {
        assertEquals(30.5, expenseOne.getAmount());
    }

    @Test
    public void getPayer() {
        assertEquals(new Participant("First One"),
                expenseOne.getPayer());
    }

    @Test
    public void getDebtors() {
        Participant participant2 = new Participant("Second One");
        Participant participant3 = new Participant("Third One");
        List<Participant> debtors = new ArrayList<>();
        debtors.add(participant2);
        debtors.add(participant3);
        assertEquals(debtors, expenseOne.getDebtors());
    }

    @Test
    public void getDate() {
        assertEquals(LocalDate.of(2024, Month.APRIL, 1),
                expenseOne.getDate());
    }

    @Test
    public void setId() {
        expenseOne.setId(100);
        assertEquals(100, expenseOne.getId());
    }

    @Test
    public void setEvent() {
        List<Participant> debtors = new ArrayList<>();
        Event newEvent = new Event("Change", "InviteCode", debtors,
                new Date(2024, 1, 10), new Date(2024, 2, 10));
        expenseOne.setEvent(newEvent);
        assertEquals(newEvent, expenseOne.getEvent());
    }

    @Test
    public void setPurchase() {
        expenseOne.setPurchase("Change");
        assertEquals("Change",expenseOne.getPurchase());
    }

    @Test
    public void setAmount() {
        expenseOne.setAmount(100);
        assertEquals(100, expenseOne.getAmount());
    }

    @Test
    public void setPayer() {
        Participant payer = new Participant("Changed");
        expenseOne.setPayer(payer);
        assertEquals(payer, expenseOne.getPayer());
    }

    @Test
    public void setDebtors() {
        Participant participant1 = new Participant("A");
        Participant participant2 = new Participant("B");
        Participant participant3 = new Participant("C");
        List<Participant> debtors = new ArrayList<>();
        debtors.add(participant1);
        debtors.add(participant2);
        debtors.add(participant3);
        expenseOne.setDebtors(debtors);
        assertEquals(debtors, expenseOne.getDebtors());
    }

    @Test
    public void setDate() {
        expenseOne.setDate(LocalDate.of(2000, Month.APRIL, 1));
        assertEquals(LocalDate.of(2000, Month.APRIL, 1), expenseOne.getDate());
    }

    @Test
    public void testEqualsNull(){
        assertNotEquals(expenseOne, null);
    }

    @Test
    public void testEqualsSame(){
        assertEquals(expenseOne, expenseOne);
    }

    @Test
    public void testEqualsEqual(){
        assertEquals(expenseOne, expenseThree);
    }

    @Test
    public void testEqualsDifferent(){
        assertNotEquals(expenseOne, expenseTwo);
    }

    @Test
    public void testHashCodeSame(){
        assertEquals(expenseOne.hashCode(), expenseOne.hashCode());
    }

    @Test
    public void testHashCodeEqual(){
        assertEquals(expenseOne.hashCode(), expenseThree.hashCode());
    }

    @Test
    public void testHashCodeDifferent(){
        assertNotEquals(expenseOne.hashCode(), expenseTwo.hashCode());
    }

    @Test
    public void testHashCodeDifferentClass(){
        assertNotEquals(expenseOne.hashCode(), "Hello".hashCode());
    }

    @Test
    public void testToString() {
//        assertEquals("Expense{id=1, event=Event{" +
//                "name='Test', inviteCode='InviteCode', participants=[Participant{id=0, " +
//                "name='Second One', bankAccount=null}, Participant{id=0, name='Third One', " +
//                "bankAccount=null}], creationDate=Sun Feb 10 00:00:00 CET 3924, " +
//                "lastActivity=Mon Mar 10 00:00:00 CET 3924}, purchase='Food', " +
//                "amount=30.5, payer=Participant{id=0, name='First One', bankAccount=null}, " +
//                "debtors=[Participant{id=0, name='Second One', bankAccount=null}, " +
//                "Participant{id=0, name='Third One', bankAccount=null}], date=2024-04-01}",
//                expenseOne.toString());
    }
}
