package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EventChangeTest {

    private EventChange ec1;

    private EventChange ec2;

    private EventChange ec3;

    @BeforeEach
    public void setUp() {
        this.ec1 = new EventChange(EventChange.Type.DELETION, new Event());
        this.ec2 = new EventChange(EventChange.Type.DELETION, new Event());
        this.ec3 = new EventChange(EventChange.Type.CREATION, new Event());
    }

    @Test
    public void testStandardConstructor() {
        EventChange ec = new EventChange(EventChange.Type.MODIFICATION, new Event());
        assertNotNull(ec);
    }

    @Test
    public void testJPAConstructor() {
        EventChange ec = new EventChange();
        assertNotNull(ec);
    }

    @Test
    public void testGetType() {
        assertEquals(EventChange.Type.DELETION, ec1.getType());
    }

    @Test
    public void testGetEvent() {
        assertEquals(new Event(), ec1.getEvent());
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(ec1, null);
    }

    @Test
    public void testEqualsSame() {
        assertEquals(ec1, ec1);
    }

    @Test
    public void testEqualsEqual() {
        assertEquals(ec1, ec2);
    }

    @Test
    public void testEqualsDifferent() {
        assertNotEquals(ec1, ec3);
    }

    @Test
    public void testHashCodeSame() {
        assertEquals(ec1.hashCode(), ec1.hashCode());
    }

    @Test
    public void testHashCodeEqual() {
        assertEquals(ec1.hashCode(), ec2.hashCode());
    }

    @Test
    public void testHashCodeDifferent() {
        assertNotEquals(ec1.hashCode(), ec3.hashCode());
    }

    @Test
    public void testHashCodeDifferentClass() {
        assertNotEquals(ec1.hashCode(), "Hello".hashCode());
    }
}
