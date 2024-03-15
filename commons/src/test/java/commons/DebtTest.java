package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DebtTest {

    private Debt debtOne;
    private Debt debtTwo;
    private Debt debtThree;

    private Participant participantOne;
    private Participant participantTwo;
    private Participant participantThree;

    @BeforeEach
    public void setUp() {
        this.participantOne = new Participant("Tester Fester", new BankAccount("NL12ABNA345678910", "HBUKGB4B"));
        this.participantTwo = new Participant("Tester Bester", new BankAccount("NL12ABNA345678910", "HBUKGB4C"));
        this.participantThree = new Participant("Tester Lester", new BankAccount("NL12ABNA345678910", "HBUKGB4D"));

        this.debtOne = new Debt(participantOne, participantTwo, 100, false, "Loan", "First loan");
        this.debtTwo = new Debt(participantOne, participantTwo, 100, false, "Loan", "First loan");
        this.debtThree = new Debt(participantOne, participantThree, 200, true, "Repayment", "Final repayment");
    }

    @Test
    public void testEqualityAndHashcode() {
        assertEquals(debtOne, debtTwo, "Debts with the same fields should be equal");
        assertNotEquals(debtOne, debtThree, "Debts with different fields should not be equal");

        assertEquals(debtOne.hashCode(), debtTwo.hashCode(), "Hashcode should match for equal objects");
        assertNotEquals(debtOne.hashCode(), debtThree.hashCode(), "Hashcode should not match for different objects");
    }

    @Test
    public void testGetters() {
        assertEquals(debtOne.getCreditor(), participantOne, "Creditor should match");
        assertEquals(debtOne.getDebtor(), participantTwo, "Debtor should match");
        assertEquals(debtOne.getAmount(), 100, "Amount should match");
        assertFalse(debtOne.isHasPaid(), "hasPaid should be false");
        assertEquals(debtOne.getSummary(), "Loan", "Summary should match");
        assertEquals(debtOne.getDescription(), "First loan", "Description should match");
    }

    @Test
    public void testSetters() {
        debtThree.setCreditor(participantTwo);
        debtThree.setDebtor(participantOne);
        debtThree.setAmount(150);
        debtThree.setHasPaid(false);
        debtThree.setSummary("Changed Loan");
        debtThree.setDescription("Updated loan details");

        assertEquals(participantTwo, debtThree.getCreditor(), "Creditor should be updated");
        assertEquals(participantOne, debtThree.getDebtor(), "Debtor should be updated");
        assertEquals(150, debtThree.getAmount(), "Amount should be updated");
        assertFalse(debtThree.isHasPaid(), "hasPaid should be updated to false");
        assertEquals("Changed Loan", debtThree.getSummary(), "Summary should be updated");
        assertEquals("Updated loan details", debtThree.getDescription(), "Description should be updated");
    }

    @Test
    public void testToString() {
        String expected = String.format("Debt{creditor=%s, debtor=%s, amount=100.0, hasPaid=false, summary='Loan', description='First loan'}", debtOne.getCreditor(), debtOne.getDebtor());
        assertEquals(expected, debtOne.toString(), "ToString should return the correct string representation");
    }
}
