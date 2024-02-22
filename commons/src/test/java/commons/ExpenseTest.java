import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Test suite for the Expense class.
 */
public class ExpenseTest {

    /**
     * Tests the constructor and the getters of the Expense class.
     * Ensures that each field is correctly assigned and retrieved.
     */
//    @Test
//    public void testExpenseConstructorAndGetters() {
//        Event event = new Event(); // Stub implementation for testing
//        Participant payer = new Participant(); // Stub implementation for testing
//        List<Participant> debtors = Arrays.asList(new Participant(), new Participant());
//
//        Expense expense = new Expense(1, event, "Lunch", 100.0, payer, debtors);
//
//        assertEquals(1, expense.getId());
//        assertEquals(event, expense.getEvent());
//        assertEquals("Lunch", expense.getPurchase());
//        assertEquals(100.0, expense.getAmount(), 0.01);
//        assertEquals(payer, expense.getPayer());
//        assertEquals(debtors, expense.getDebtors());
//    }
//
//    /**
//     * Tests the setters of the Expense class.
//     * Ensures that each field can be modified and is correctly updated.
//     */
//    @Test
//    public void testSetters() {
//        Expense expense = new Expense(1, null, null, 0, null, null);
//
//        Event event = new Event(); // Stub implementation for testing
//        Participant payer = new Participant(); // Stub implementation for testing
//        List<Participant> debtors = Arrays.asList(new Participant(), new Participant());
//
//        expense.setEvent(event);
//        expense.setPurchase("Dinner");
//        expense.setAmount(200.0);
//        expense.setPayer(payer);
//        expense.setDebtors(debtors);
//
//        assertEquals(event, expense.getEvent());
//        assertEquals("Dinner", expense.getPurchase());
//        assertEquals(200.0, expense.getAmount(), 0.01);
//        assertEquals(payer, expense.getPayer());
//        assertEquals(debtors, expense.getDebtors());
//    }
//
//    /**
//     * Tests the equals method of the Expense class.
//     * Ensures that it correctly identifies equal and non-equal instances.
//     */
//    @Test
//    public void testEquals() {
//        Event event = new Event(); // Stub implementation for testing
//        Participant payer = new Participant(); // Stub implementation for testing
//        List<Participant> debtors = Arrays.asList(new Participant(), new Participant());
//
//        Expense expense1 = new Expense(1, event, "Lunch", 100.0, payer, debtors);
//        Expense expense2 = new Expense(1, event, "Lunch", 100.0, payer, debtors);
//        Expense expense3 = new Expense(2, event, "Lunch", 100.0, payer, debtors);
//
//        assertEquals(expense1, expense2, "Expenses with the same data should be equal.");
//        assertNotEquals(expense1, expense3, "Expenses with different IDs should not be equal.");
//    }
//
//    /**
//     * Tests the hashCode method of the Expense class.
//     * Ensures that equal instances produce the same hash code.
//     */
//    @Test
//    public void testHashCode() {
//        Event event = new Event(); // Stub implementation for testing
//        Participant payer = new Participant(); // Stub implementation for testing
//        List<Participant> debtors = Arrays.asList(new Participant(), new Participant());
//
//        Expense expense1 = new Expense(1, event, "Lunch", 100.0, payer, debtors);
//        Expense expense2 = new Expense(1, event, "Lunch", 100.0, payer, debtors);
//
//        assertEquals(expense1.hashCode(), expense2.hashCode(), "Equal expenses should have the same hash code.");
//    }
//
//    /**
//     * Tests the toString method of the Expense class.
//     * Ensures that it returns the correct string representation of the Expense instance.
//     */
//    @Test
//    public void testToString() {
//        Event event = new Event(); // Stub implementation for testing
//        Participant payer = new Participant(); // Stub implementation for testing
//        List<Participant> debtors = Arrays.asList(new Participant(), new Participant());
//
//        Expense expense = new Expense(1, event, "Lunch", 100.0, payer, debtors);
//        String expected = "Expense{id=1, event=" + event.toString() + ", purchase='Lunch', amount=100.0, payer=" + payer.toString() + ", debtors=" + debtors.toString() + "}";
//
//        assertEquals(expected, expense.toString(), "toString should return the correct string representation of the Expense instance.");
//    }
}