package client.utils;

import commons.Expense;
import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseBuilderTest {

    private ExpenseBuilder eb;

    private ExpenseBuilder eb2;

    @BeforeEach
    public void setUp() {
        this.eb = new ExpenseBuilder();
        this.eb2 = new ExpenseBuilder();
    }

    @Test
    public void testConstructor() {
        assertNotNull(eb);
    }

    @Test
    public void idTest() {
        eb.setId(1);
        assertEquals(1, eb.getId());
    }

    @Test
    public void eventIdTest() {
        eb.setEventId(1L);
        assertEquals(1L, eb.getEventId());
    }

    @Test
    public void purchaseTest() {
        eb.setPurchase("Purchase");
        assertEquals("Purchase", eb.getPurchase());
    }

    @Test
    public void amountTest() {
        eb.setAmount(100L);
        assertEquals(100L, eb.getAmount());
    }

    @Test
    public void payerTest() {
        Participant p = new Participant("Name");
        eb.setPayer(p);
        assertEquals(p, eb.getPayer());
    }

    @Test
    public void debtorsTest() {
        Participant p = new Participant("Name");
        Participant p2 = new Participant("Second");
        eb.setDebtors(List.of(p, p2));
        assertEquals(List.of(p, p2), eb.getDebtors());
    }

    @Test
    public void tagTest() {
        Tag t = new Tag("Tag", 1,1 , 1);
        eb.setTag(t);
        assertEquals(t, eb.getTag());
    }

    @Test
    public void dateTest() {
        LocalDate ld = LocalDate.now();
        eb.setDate(ld);
        assertEquals(ld, eb.getDate());
    }

    @Test
    public void testSame() {
        assertEquals(eb, eb);
    }

    @Test
    public void testEquals() {
        assertEquals(eb, eb2);
    }

    @Test
    public void testNotEquals() {
        eb2.setDate(LocalDate.now());
        assertNotEquals(eb, eb2);
    }

    @Test
    public void testHashCodeSame() {
        assertEquals(eb.hashCode(), eb.hashCode());
    }

    @Test
    public void testHashCodeEquals() {
        assertEquals(eb.hashCode(), eb2.hashCode());
    }

    @Test
    public void testHashCodeNotEquals() {
        eb2.setDate(LocalDate.now());
        assertNotEquals(eb.hashCode(), eb2.hashCode());
    }

    @Test
    public void testToString() {
        eb.setId(1);
        eb.setEventId(1L);
        eb.setPurchase("Purchase");
        eb.setAmount(100L);
        Participant p = new Participant("Chill");
        eb.setPayer(p);
        Participant p2 = new Participant("Name");
        Participant p3 = new Participant("Second");
        Tag t = new Tag("Tag", 1,1 , 1);
        eb.setTag(t);
        eb.setDebtors(List.of(p2, p3));
        eb.setDate(LocalDate.of(1, 1, 1));
        assertEquals("ExpenseBuilder{id=1, eventId=1, payer=Participant{id=0, name='Chill', bankAccount=null}, purchase='Purchase', amount=100, date=0001-01-01\n" +
                "debtors: \n" +
                "Name\n" +
                "Second\n" +
                "}", eb.toString());
    }

    @Test
    public void testBuild() {
        eb.setId(1);
        eb.setEventId(1L);
        eb.setPurchase("Purchase");
        eb.setAmount(10000);
        Participant p = new Participant("Chill");
        eb.setPayer(p);
        Participant p2 = new Participant("Name");
        Participant p3 = new Participant("Second");
        Tag t = new Tag("Tag", 1,1 , 1);
        eb.setTag(t);
        eb.setDebtors(List.of(p2, p3));
        eb.setDate(LocalDate.of(1, 1, 1));
        Expense expected = new Expense(1L, "Purchase", 100, p, List.of(p2, p3), LocalDate.of(1, 1, 1), t);
        assertEquals(expected, eb.build());
    }

}
