package client.utils;

import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WhichTagSelectorTest {

    private WhichTagSelector wts;

    private WhichTagSelector wts2;

    @BeforeEach
    public void setUp() {
        Tag t1 = new Tag("One", 1, 1, 1, null);
        Tag t2 = new Tag("Two", 2, 2, 2, null);
        Tag t3 = new Tag("Three", 3, 3, 3, null);
        Tag t4 = new Tag("Four", 4, 4, 4, null);
        Tag t11 = new Tag("OneOne", 4, 4, 4, null);
        this.wts = new WhichTagSelector(List.of(t1, t2, t3, t4, t11));
        this.wts2 = new WhichTagSelector(List.of(t1, t2, t3));
    }

    @Test
    public void testConstructor() {
        assertNotNull(wts);
    }

    @Test
    public void nullQuery() {
        Tag t1 = new Tag("One", 1, 1, 1, null);
        Tag t2 = new Tag("Two", 2, 2, 2, null);
        Tag t3 = new Tag("Three", 3, 3, 3, null);
        Tag t4 = new Tag("Four", 4, 4, 4, null);
        Tag t11 = new Tag("OneOne", 4, 4, 4, null);
        assertEquals(List.of(t1, t2, t3, t4, t11), wts.query(null));
    }

    @Test
    public void normalQuery() {
        List<Tag> res = wts.query("One");

        List<Tag> expected = new ArrayList<>();
        expected.add(new Tag("One", 1, 1, 1, null));
        expected.add(new Tag("OneOne", 4, 4, 4, null));

        expected.sort(Comparator.comparing(Tag::getName));
        res.sort(Comparator.comparing(Tag::getName));

        assertEquals(expected, res);
    }


    @Test
    public void toStringNull() {
        assertNull(wts.toString(null));
    }

    @Test
    public void toStringNotNull() {
        assertEquals("One", wts.toString(new Tag("One", 1, 1, 1, null)));
    }

    @Test
    public void fromStringNull() {
        assertNull(wts.fromString(null));
    }

    @Test
    public void fromStringNotNull() {
        assertEquals(new Tag("One", 1, 1, 1, null), wts.fromString("One"));
    }

    @Test
    public void getCurrentTagNameNull() {
        assertNull(wts.getCurrentTag(null));
    }

    @Test
    public void getCurrentTagSuccess() {
        assertEquals(new Tag("One", 1, 1, 1, null), wts.getCurrentTag("One"));
    }

    @Test
    public void getCurrentTagNotFound() {
        assertNull(wts.getCurrentTag("Not Here"));
    }

}
