package client.utils;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DebtorSelectorTest {

    private DebtorSelector ds;

    @BeforeEach
    void setup() {
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant("Henk"));
        participants.add(new Participant("Piet"));
        participants.add(new Participant("Joost"));
        participants.add(new Participant("Barry"));
        participants.add(new Participant("Joe"));
        participants.add(new Participant("Jan"));
        participants.add(new Participant("Abel"));
        participants.add(new Participant("Pietje"));
        participants.add(new Participant("Trien"));

        ds = new DebtorSelector(participants);
        ds.setAllSelected(false);
    }

    @Test
    void addNonExistentParticipant() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ds.add("NonExistent"));
        assertNull(exception.getMessage());
    }

    @Test
    void addNullParticipant() {
        assertDoesNotThrow(() -> ds.add(null));
    }

    @Test
    void removeNonExistentParticipant() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> ds.remove("NonExistent"));
        assertNull(exception.getMessage());
    }

    @Test
    void removeNullParticipant() {
        assertDoesNotThrow(() -> ds.remove(null));
    }

    @Test
    void getDebitorsWhenAllSelected() {
        ds.setAllSelected(true);
        assertEquals(9, ds.getDebitors().size());
    }

    @Test
    void getDebitorsWhenNoneSelected() {
        ds.setAllSelected(false);
        ds.removeAll();
        assertTrue(ds.getDebitors().isEmpty());
    }

    @Test
    void toggleAllSelected() {
        ds.setAllSelected(false);
        ds.add("Henk");
        assertEquals(1, ds.getDebitors().size(), "Should only have one debtor when allSelected is false");

        ds.setAllSelected(true);
        assertEquals(9, ds.getDebitors().size(), "Should have all participants as debtors when allSelected is true");
    }

    void testIsAllSelected() {
        ds.setAllSelected(true);
        assertTrue(ds.isAllSelected(), "isAllSelected should return true after being set to true");

        ds.setAllSelected(false);
        assertFalse(ds.isAllSelected(), "isAllSelected should return false after being set to false");
    }
}
