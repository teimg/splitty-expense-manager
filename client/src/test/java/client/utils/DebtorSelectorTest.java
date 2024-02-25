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
    void add() {
        ds.add("Abel");
        assertArrayEquals( new Participant[]{new Participant("Abel")}, ds.getDebitors().toArray());
    }

    @Test
    void addAll() {
        ds.addAll();

        assertTrue(ds.getDebitors().contains(new Participant("Abel")));
        assertTrue(ds.getDebitors().contains(new Participant("Piet")));
        assertTrue(ds.getDebitors().contains(new Participant("Joost")));
        assertTrue(ds.getDebitors().contains(new Participant("Barry")));
        assertTrue(ds.getDebitors().contains(new Participant("Joe")));
    }

    @Test
    void remove() {
        ds.add("Jan");
        ds.add("Abel");
        ds.remove("Jan");
        assertArrayEquals(new Participant[]{new Participant("Abel")}, ds.getDebitors().toArray());
    }

    @Test
    void removeAll() {
        ds.add("Jan");
        ds.add("Abel");
        ds.removeAll();
        assertArrayEquals(new Participant[]{}, ds.getDebitors().toArray());

    }
}