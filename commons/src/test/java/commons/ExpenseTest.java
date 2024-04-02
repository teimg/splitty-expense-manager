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
        this.expenseOne = new Expense(event.getId(), "Food", 30.5,
                participant1, debtors, LocalDate.of(2024, Month.APRIL, 1),
                new Tag("Test", 0, 0, 0));
        this.expenseTwo = new Expense(event.getId(), "Food", 31.5,
                participant2, debtors, LocalDate.of(2024, Month.APRIL, 2),
                new Tag("Test", 0, 0, 0));
        this.expenseThree = new Expense(event.getId(), "Food", 30.5,
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
    public void getEventID() {
        assertEquals(event.getId(), expenseOne.getEventId());
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
    public void getTag() {
        assertEquals(new Tag("Test", 0, 0, 0), expenseOne.getTag());
    }

    @Test
    public void setId() {
        expenseOne.setId(100);
        assertEquals(100, expenseOne.getId());
    }

    @Test
    public void setEventID() {
        expenseOne.setEventId(12L);
        assertEquals(12L, expenseOne.getEventId());
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
    public void setTag() {
        expenseOne.setTag(new Tag("Changed", 1, 1, 1));
        assertEquals(new Tag("Changed", 1, 1, 1), expenseOne.getTag());
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
        String toString = expenseOne.toString();
        // assertEquals("Expense{id=1, event=null, purchase='Food', amount=30.5, payer=Participant{id=0, name='First One', bankAccount=null}, debtors=[Participant{id=0, name='Second One', bankAccount=null}, Participant{id=0, name='Third One', bankAccount=null}], date=2024-04-01, tag=commons.Tag@bf580fb2}",expenseOne.toString());
    }
}
