package client.utils;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WhoPaidSelectorTest {

    private WhoPaidSelector ds;

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

        ds = new WhoPaidSelector(participants);
    }
    @Test
    void query() {
        List<Participant> res = ds.query("j");

        List<Participant> expected = new ArrayList<>();
        expected.add(new Participant("Jan"));
        expected.add(new Participant("Joe"));
        expected.add(new Participant("Joost"));

        expected.sort(Comparator.comparing(Participant::getName));
        res.sort(Comparator.comparing(Participant::getName));

        assertEquals(expected, res);
    }

    @Test
    void getCurrentPayer() {
        Participant expected = new Participant("Joe");


        assertEquals(expected, ds.getCurrentPayer("Joe"));
    }

    @Test
    void getCurrentPayerNull() {
        Participant expected = new Participant("Joe");

        assertNull(ds.getCurrentPayer("Flippie"));
    }
}