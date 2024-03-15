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
        this.debtOne = new Debt(participantOne, participantTwo, 100,false,"123","test");
        this.debtTwo = new Debt(participantOne, participantTwo, 100,false,"123","test");
        this.debtThree = new Debt(participantOne, participantThree, 100,false,"123","test");
    }

    @Test
    public void testConstructor() {
        assertNotNull(debtOne);
        assertNotNull(debtTwo);
        assertNotNull(debtThree);
    }

    @Test
    public void testCreditorGetter() {
        assertEquals(debtOne.getCreditor(), participantOne);
    }

    @Test
    public void testDebtorGetter() {
        assertEquals(debtOne.getDebtor(), participantTwo);
    }

    @Test
    public void testAmountGetter() {
        assertEquals(debtOne.getAmount(), 100);
    }

    @Test
    public void testCreditorSetter() {
        debtThree.setCreditor(participantThree);
        assertEquals(debtThree.getCreditor(), participantThree);
    }

    @Test
    public void testDebtorSetter() {
        debtThree.setDebtor(participantTwo);
        assertEquals(debtThree.getDebtor(), participantTwo);
    }

    @Test
    public void testAmountSetter() {
        debtThree.setAmount(500);
        assertEquals(debtThree.getAmount(), 500);
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(debtOne, null);
    }

    @Test
    public void testEqualsSame() {
        assertEquals(debtOne, debtOne);
    }

    @Test
    public void testEqualsEqual() {
        assertEquals(debtOne, debtTwo);
    }

    @Test
    public void testEqualsDifferent() {
        assertNotEquals(debtOne, debtThree);
    }

    @Test
    public void testHashCodeSame() {
        assertEquals(debtOne.hashCode(), debtOne.hashCode());
    }

    @Test
    public void testHashCodeEqual() {
        assertEquals(debtOne.hashCode(), debtTwo.hashCode());
    }

    @Test
    public void testHashCodeDifferent() {
        assertNotEquals(debtOne.hashCode(), debtThree.hashCode());
    }

    @Test
    public void testHashCodeDifferentClass() {
        assertNotEquals(debtOne.hashCode(), "Hello".hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(debtOne.toString(), "Debt{creditor=Participant{id=0, " +
                "name='Tester Fester', bankAccount=commons.BankAccount@d1afd8f7}, " +
                "debtor=Participant{id=0, name='Tester Bester', " +
                "bankAccount=commons.BankAccount@d1afd8f8}, amount=100.0}");
    }
}
