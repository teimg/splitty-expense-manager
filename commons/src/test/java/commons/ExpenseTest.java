package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class ExpenseTest {

    @Test
    public void testExpenseConstructorAndGetters() {

        Participant payer = new Participant("John");
        List<Participant> debtors = Arrays.asList(
                new Participant("Jane"),
                new Participant("Alice")
        );
        Event event = new Event(1, "Birthday Party", "INV123", debtors,new Date(), new Date());
        Expense expense = new Expense(1, event, "Cake", 50.0, payer, debtors);

        assertEquals(1, expense.getId());
        assertEquals(event, expense.getEvent());
        assertEquals("Cake", expense.getPurchase());
        assertEquals(50.0, expense.getAmount(), 0.01);
        assertEquals(payer, expense.getPayer());
        assertEquals(debtors, expense.getDebtors());
    }

    @Test
    public void testExpenseEquals() {
        Participant payer = new Participant("John");
        List<Participant> debtors = Arrays.asList(
                new Participant("Jane"),
                new Participant("Alice")
        );
        Event event = new Event(1, "Birthday Party", "INV123", debtors,new Date(), new Date());
        Expense expense1 = new Expense(1, event, "Cake", 50.0, payer, debtors);
        Expense expense2 = new Expense(1, event, "Cake", 50.0, payer, debtors);
        Expense expense3 = new Expense(2, event, "Pizza", 30.0, payer, debtors);

        assertEquals(expense1, expense2);
        assertNotEquals(expense1, expense3);
    }

    @Test
    public void testExpenseHashCode() {
        Participant payer = new Participant("John");
        List<Participant> debtors = Arrays.asList(
                new Participant("Jane"),
                new Participant("Alice")
        );
        Event event = new Event(1, "Birthday Party", "INV123", debtors,new Date(), new Date());
        Expense expense1 = new Expense(1, event, "Cake", 50.0, payer, debtors);
        Expense expense2 = new Expense(1, event, "Cake", 50.0, payer, debtors);

        assertEquals(expense1.hashCode(), expense2.hashCode());
    }

    @Test
    public void testExpenseToString() {
        Participant payer = new Participant("John");
        List<Participant> debtors = Arrays.asList(
                new Participant("Jane"),
                new Participant("Alice")
        );
        Event event = new Event(1, "Birthday Party", "INV123", debtors,new Date(), new Date());
        Expense expense = new Expense(1, event, "Cake", 50.0, payer, debtors);
        String expenseString = expense.toString();
        assertTrue(expenseString.contains("Cake"));
        assertTrue(expenseString.contains("50.0"));
        // More assertions can be added to validate the complete toString output
    }
}
