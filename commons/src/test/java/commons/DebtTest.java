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
        this.participantOne = new Participant("Tester Fester",
                new BankAccount("NL12ABNA345678910", "HBUKGB4B"));
        this.participantTwo = new Participant("Tester Bester",
                new BankAccount("NL12ABNA345678910", "HBUKGB4C"));
        this.participantThree = new Participant("Tester Lester",
                new BankAccount("NL12ABNA345678910", "HBUKGB4C"));
        this.debtOne = new Debt(participantOne, participantTwo, 100);
        this.debtTwo = new Debt(participantOne, participantTwo, 100);
        this.debtThree = new Debt(participantOne, participantThree, 100);
    }

    @Test
    public void testConstructor() {
        assertDebtsNotNull();
    }

    @Test
    public void testCreditorGetter() {
        assertCreditor(debtOne, participantOne);
    }

    @Test
    public void testDebtorGetter() {
        assertDebtor(debtOne, participantTwo);
    }

    @Test
    public void testAmountGetter() {
        assertAmount(debtOne, 100);
    }

    @Test
    public void testCreditorSetter() {
        debtThree.setCreditor(participantThree);
        assertCreditor(debtThree, participantThree);
    }

    @Test
    public void testDebtorSetter() {
        debtThree.setDebtor(participantTwo);
        assertDebtor(debtThree, participantTwo);
    }

    @Test
    public void testAmountSetter() {
        debtThree.setAmount(500);
        assertAmount(debtThree, 500);
    }

    @Test
    public void testEquals() {
        assertEquality();
    }

    @Test
    public void testHashCode() {
        assertHashCodes();
    }

    @Test
    public void testToString() {
        String expected = buildExpectedToStringForDebt(debtOne);
        assertEquals(expected, debtOne.toString());
    }

    private String buildExpectedToStringForDebt(Debt debt) {
        return String.format("Debt{creditor=%s, debtor=%s, amount=%.1f, hasPaid=%b}",
                buildParticipantToString(debt.getCreditor()),
                buildParticipantToString(debt.getDebtor()),
                debt.getAmount(),
                debt.isHasPaid(),
                debt.getSummary());
    }

    private String buildParticipantToString(Participant participant) {
        return String.format("Participant{id=%d, name='%s', bankAccount=%s}",
                participant.getId(),
                participant.getName(),
                participant.getBankAccount().toString());
    }


    private void assertDebtsNotNull() {
        assertNotNull(debtOne);
        assertNotNull(debtTwo);
        assertNotNull(debtThree);
    }

    private void assertCreditor(Debt debt, Participant expected) {
        assertEquals(expected, debt.getCreditor());
    }

    private void assertDebtor(Debt debt, Participant expected) {
        assertEquals(expected, debt.getDebtor());
    }

    private void assertAmount(Debt debt, double expected) {
        assertEquals(expected, debt.getAmount());
    }

    private void assertEquality() {
        assertEquals(debtOne, debtOne); // Self
        assertNotEquals(debtOne, null); // Null
        assertEquals(debtOne, debtTwo); // Equal
        assertNotEquals(debtOne, debtThree); // Different
        assertNotEquals(debtOne, "Hello"); // Different class
    }

    private void assertHashCodes() {
        assertEquals(debtOne.hashCode(), debtOne.hashCode()); // Self
        assertEquals(debtOne.hashCode(), debtTwo.hashCode()); // Equal
        assertNotEquals(debtOne.hashCode(), debtThree.hashCode()); // Different
        assertNotEquals(debtOne.hashCode(), "Hello".hashCode()); // Different class
    }

    private void assertToStringContains(Debt debt, String expected) {
        assertEquals(expected, debt.toString());
    }
}
