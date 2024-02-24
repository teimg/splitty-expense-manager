package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class EventTest {

    private Event firstEvent;
    private Event secondEvent;
    private Event thirdEvent;
    private List<Participant> participants;

    @BeforeEach
    public void setUp() {
        Participant participant1 = new Participant("Tester Fester");
        this.participants = new ArrayList<>();
        participants.add(participant1);
        this.firstEvent = new Event(1, "Test", "InviteCode", participants,
                new Date(2024, 1, 10), new Date(2024, 2, 10));
        this.secondEvent = new Event(1, "Test", "InviteCode", participants,
                new Date(2024, 1, 10), new Date(2023, 2, 10));
        this.thirdEvent = new Event(1, "Test", "InviteCode", participants,
                new Date(2024, 1, 10), new Date(2024, 2, 10));
    }


    @Test
    public void testEventGetters() {
        int testID = firstEvent.getId();
        String testName = firstEvent.getName();
        String testInviteCode = firstEvent.getInviteCode();
        List<Participant> testParticipants = firstEvent.getParticipants();
        Date testCreationDate = firstEvent.getCreationDate();
        Date testLastActivityDate = firstEvent.getLastActivity();
        assertEquals(testID, 1);
        assertEquals(testName, "Test");
        assertEquals(testInviteCode, "InviteCode");
        assertEquals(testParticipants, participants);
        assertEquals(testCreationDate, new Date(2024, 1, 10));
        assertEquals(testLastActivityDate, new Date(2024, 2, 10));
    }

    @Test
    public void testEventEquals() {
        assertNotEquals(firstEvent, secondEvent);
        assertEquals(firstEvent, firstEvent);
        assertNotEquals(firstEvent, null);
        assertNotEquals("Hello", firstEvent);
        assertEquals(firstEvent, thirdEvent);
    }

    @Test
    public void testEventHashCode() {
        assertEquals(firstEvent.hashCode(), firstEvent.hashCode());
        assertEquals(firstEvent.hashCode(), thirdEvent.hashCode());
        assertNotEquals(firstEvent.hashCode(), secondEvent.hashCode());
    }

}
