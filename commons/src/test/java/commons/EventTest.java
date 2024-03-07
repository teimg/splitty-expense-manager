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
        this.firstEvent = new Event("Test", "InviteCode", participants,
                new Date(2024, 1, 10), new Date(2024, 2, 10));
        this.secondEvent = new Event("Test", "InviteCode", participants,
                new Date(2024, 1, 10), new Date(2023, 2, 10));
        this.thirdEvent = new Event("Test", "InviteCode", participants,
                new Date(2024, 1, 10), new Date(2024, 2, 10));
    }

    @Test
    public void testConstructors() {
        Event constructorTest = new Event("Test", "InviteCode", participants,
                new Date(2024, 1, 10), new Date(2024, 2, 10));
        assertNotNull(constructorTest);
        Event constructorTestTwo = new Event();
        assertNotNull(constructorTestTwo);
    }

    @Test
    public void getId() {
        Long testID = firstEvent.getId();
        assertNotNull(testID);
    }

    @Test
    public void getName() {
        String testName = firstEvent.getName();
        assertEquals(testName, "Test");
    }

    @Test
    public void getInviteCode() {
        String testInviteCode = firstEvent.getInviteCode();
        assertEquals(testInviteCode, "InviteCode");
    }

    @Test
    public void getParticipants() {
        List<Participant> testParticipants = firstEvent.getParticipants();
        assertEquals(testParticipants, participants);
    }

    @Test
    public void getCreationDate() {
        Date testCreationDate = firstEvent.getCreationDate();
        assertEquals(testCreationDate, new Date(2024, 1, 10));
    }

    @Test
    public void getLastActivityDate() {
        Date testLastActivityDate = firstEvent.getLastActivity();
        assertEquals(testLastActivityDate, new Date(2024, 2, 10));
    }

    @Test
    public void setId() {
        firstEvent.setId(100);
        assertEquals(firstEvent.getId(), 100);
    }

    @Test
    public void setName() {
        firstEvent.setName("Change");
        assertEquals(firstEvent.getName(), "Change");
    }

    @Test
    public void setInviteCode() {
        firstEvent.setInviteCode("Change");
        assertEquals(firstEvent.getInviteCode(), "Change");
    }

    @Test
    public void addParticipants() {
        Participant added = new Participant("Add");
        firstEvent.addParticipant(added);
        assertTrue(firstEvent.getParticipants().contains(added));
    }

    @Test
    public void setCreationDate() {
        Date creationDate = new Date(2000, 1, 11);
        firstEvent.setCreationDate(creationDate);
        assertEquals(firstEvent.getCreationDate(), creationDate);
    }

    @Test
    public void setLastActivityDate() {
        Date activityDate = new Date(2050, 10, 11);
        firstEvent.setLastActivity(activityDate);
        assertEquals(firstEvent.getLastActivity(), activityDate);
    }

    @Test
    public void addExpense() {
        assertEquals(firstEvent, thirdEvent);
        Expense expense = new Expense();
        firstEvent.addExpense(expense);
        assertNotEquals(firstEvent, thirdEvent);
        assertTrue(firstEvent.getExpenses().contains(expense));
        assertEquals(0, thirdEvent.getExpenses().size());
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(firstEvent, null);
    }

    @Test
    public void testEqualsSame() {
        assertEquals(firstEvent, firstEvent);
    }

    @Test
    public void testEqualsEqual() {
        assertEquals(firstEvent, thirdEvent);
    }

    @Test
    public void testEqualsDifferent() {
        assertNotEquals(firstEvent, secondEvent);
    }

    @Test
    public void testHashCodeSame() {
        assertEquals(firstEvent.hashCode(), firstEvent.hashCode());
    }

    @Test
    public void testHashCodeEqual() {
        assertEquals(firstEvent.hashCode(), thirdEvent.hashCode());
    }

    @Test
    public void testHashCodeDifferent() {
        assertNotEquals(firstEvent.hashCode(), secondEvent.hashCode());
    }

    @Test
    public void testEventHashCodeDifferentClass() {
        assertNotEquals(firstEvent.hashCode(), "Hello".hashCode());
    }

    @Test
    public void testToString() {
//        assertEquals("Event{name='Test', " +
//                "inviteCode='InviteCode', participants=[Participant{id=0, " +
//                "name='Tester Fester', bankAccount=null}], creationDate=Sun " +
//                "Feb 10 00:00:00 CET 3924, lastActivity=Mon Mar 10 00:00:00 CET 3924}",
//                firstEvent.toString());
    }

}
