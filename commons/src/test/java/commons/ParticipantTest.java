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
        Participant participantTwo = new Participant("Fester Tester");
        Participant participantThree = new Participant("Tester Fester");
        this.participantOne = participantOne;
        this.participantTwo = participantTwo;
        this.participantThree = participantThree;
    }

    @Test
    public void testConstructor() {
        Participant testConstructorParticipantOne = new Participant();
        Participant testConstructorParticipantTwo = new Participant("Test");
        assertNotNull(testConstructorParticipantOne);
        assertNotNull(testConstructorParticipantTwo);
    }

    @Test
    public void testNameGetter() {
        assertEquals(participantOne.getName(), "Tester Fester");
    }

    @Test
    public void testNameSetter() {
        participantOne.setName("New Name");
        assertEquals(participantOne.getName(), "New Name");
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
        assertEquals(participantOne, participantThree);
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
        assertEquals(participantOne.hashCode(), participantThree.hashCode());
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
    public void testEmailGetter() {
        Participant participant = new Participant("Alex", "alex@gmail.com");
        assertEquals("alex@gmail.com", participant.getEmail());
    }

    @Test
    public void testEmailSetter() {
        Participant participant = new Participant("Alex");
        participant.setEmail("alex@gmail.com");
        assertEquals("alex@gmail.com", participant.getEmail());
    }

}
