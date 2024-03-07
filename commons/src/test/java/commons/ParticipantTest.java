package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParticipantTest {

    private Participant participantOne;
    private Participant participantTwo;
    private Participant participantThree;

    @BeforeEach
    public void setUp() {
        Participant participantOne = new Participant("Tester Fester");
        Participant participantTwo = new Participant("Fester Aester");
        Participant participantThree = new Participant("Fester Aester");
        participantOne.setEmail("Test");
        participantOne.setBankAccount(new BankAccount("A", "A"));
        participantOne.setEvent(new Event());
        this.participantOne = participantOne;
        this.participantTwo = participantTwo;
        this.participantThree = participantThree;
    }

    @Test
    public void testConstructor() {
        Participant testConstructorParticipantOne = new Participant();
        Participant testConstructorParticipantTwo = new Participant("Test");
        Participant testConstructorParticipantThree = new Participant("Test", "FakeEmail");
        Participant testConstructorParticipantFour = new Participant("Test",
                new BankAccount("123", "123"));
        assertNotNull(testConstructorParticipantOne);
        assertNotNull(testConstructorParticipantTwo);
        assertNotNull(testConstructorParticipantThree);
        assertNotNull(testConstructorParticipantFour);
    }

    @Test
    public void getId() {
        Long id = participantOne.getId();
        assertNotNull(id);
    }

    @Test
    public void setId() {
        participantOne.setId(100);
        assertEquals(100, participantOne.getId());
    }

    @Test
    public void getName() {
        assertEquals( "Tester Fester", participantOne.getName());
    }

    @Test
    public void setName() {
        participantOne.setName("New Name");
        assertEquals( "New Name", participantOne.getName());
    }

    @Test
    public void getBankAccount() {
        assertEquals(new BankAccount("A", "A"), participantOne.getBankAccount());
    }

    @Test
    public void setBankAccount() {
        participantOne.setBankAccount(new BankAccount("Changed", "C"));
        assertEquals(new BankAccount("Changed", "C"), participantOne.getBankAccount());
    }

    @Test
    public void getEvent() {
        assertEquals(new Event(), participantOne.getEvent());
    }

    @Test
    public void setEvent() {
        participantTwo.setEvent(new Event());
        assertEquals(new Event(), participantTwo.getEvent());
    }

    @Test
    public void getEmail() {
        Participant participant = new Participant("Alex", "alex@gmail.com");
        assertEquals("alex@gmail.com", participant.getEmail());
    }

    @Test
    public void setEmail() {
        Participant participant = new Participant("Alex");
        participant.setEmail("alex@gmail.com");
        assertEquals("alex@gmail.com", participant.getEmail());
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(participantOne, null);
    }

    @Test
    public void testEqualsSame() {
        assertEquals(participantOne, participantOne);
    }

    @Test
    public void testEqualsEqual() {
        assertEquals(participantTwo, participantThree);
    }

    @Test
    public void testEqualsDifferent() {
        assertNotEquals(participantOne, participantTwo);
    }

    @Test
    public void testHashCodeSame() {
        assertEquals(participantOne.hashCode(), participantOne.hashCode());
    }

    @Test
    public void testHashCodeEqual() {
        assertEquals(participantTwo.hashCode(), participantThree.hashCode());
    }

    @Test
    public void testHashCodeDifferent() {
        assertNotEquals(participantOne.hashCode(), participantTwo.hashCode());
    }

    @Test
    public void testHashCodeDifferentClass() {
        assertNotEquals(participantOne.hashCode(), "Hello".hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("Participant{id=0, name='Tester Fester', bankAccount=commons.BankAccount@820}", participantOne.toString());
    }

}
