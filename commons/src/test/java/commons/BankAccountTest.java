package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    private BankAccount bankAccountOne;
    private BankAccount bankAccountTwo;
    private BankAccount bankAccountThree;


    @BeforeEach
    public void setUp() {
        BankAccount bankAccountOne = new BankAccount("NL12ABNA345678910", "HBUKGB4B");
        BankAccount bankAccountTwo = new BankAccount("NL12ABNA345678910", "HBUDGB4B");
        BankAccount bankAccountThree = new BankAccount("NL12ABNA345678910", "HBUKGB4B");
        this.bankAccountOne = bankAccountOne;
        this.bankAccountTwo = bankAccountTwo;
        this.bankAccountThree = bankAccountThree;
    }

    @Test
    public void testConstructor() {
        BankAccount constructorTest = new BankAccount("NL12ABNA345678910", "HBUKGB4B");
        assertNotNull(constructorTest);
        BankAccount constructorTestTwo = new BankAccount();
        assertNotNull(constructorTestTwo);
    }

    @Test
    public void testIBANGetter() {
        assertEquals(bankAccountOne.getIban(), "NL12ABNA345678910");
    }

    @Test
    public void testBICGetter() {
        assertEquals(bankAccountOne.getBic(), "HBUKGB4B");
    }

    @Test
    public void testIBANSetter() {
        bankAccountThree.setIban("NL00ABNA0000000");
        assertEquals(bankAccountThree.getIban(), "NL00ABNA0000000");
    }

    @Test
    public void testBICSetter() {
        bankAccountThree.setBic("00000000");
        assertEquals(bankAccountThree.getBic(), "00000000");
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(bankAccountOne, null);
    }

    @Test
    public void testEqualsSame() {
        assertEquals(bankAccountOne, bankAccountOne);
    }

    @Test
    public void testEqualsEqual() {
        assertEquals(bankAccountOne, bankAccountThree);
    }

    @Test
    public void testEqualsDifferent() {
        assertNotEquals(bankAccountOne, bankAccountTwo);
    }

    @Test
    public void testHashCodeSame() {
        assertEquals(bankAccountOne.hashCode(), bankAccountOne.hashCode());
    }

    @Test
    public void testHashCodeEqual() {
        assertEquals(bankAccountOne.hashCode(), bankAccountThree.hashCode());
    }

    @Test
    public void testHashCodeDifferent() {
        assertNotEquals(bankAccountOne.hashCode(), bankAccountTwo.hashCode());
    }

    @Test
    public void testHashCodeDifferentClass() {
        assertNotEquals(bankAccountOne.hashCode(), "Hello".hashCode());
    }

}
