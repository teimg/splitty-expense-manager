package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TagTest {

    private Tag tag;
    private Tag tagTwo;
    private Tag tagThree;

    @BeforeEach
    public void setUp() {
        this.tag = new Tag("Tag", 10, 10, 10, null);
        this.tagTwo = new Tag("Tag", 10, 10, 10, null);
        this.tagThree = new Tag("Tag2", 10, 10, 101, null);
    }

    @Test
    public void constructorStandardTest() {
        assertNotNull(tag);
    }

    @Test
    public void constructorJPATest() {
        Tag tag = new Tag();
        assertNotNull(tag);
    }

    @Test
    public void testNameGetter() {
        assertEquals(this.tag.getName(), "Tag");
    }

    @Test
    public void testRedGetter() {
        assertEquals(this.tag.getRed(), 10);
    }

    @Test
    public void testGreenGetter() {
        assertEquals(this.tag.getGreen(), 10);
    }

    @Test
    public void testBlueGetter() {
        assertEquals(this.tag.getBlue(), 10);
    }

    @Test
    public void testRedSetter() {
        this.tag.setRed(12);
        assertEquals(this.tag.getRed(), 12);
    }

    @Test
    public void testGreenSetter() {
        this.tag.setGreen(11);
        assertEquals(this.tag.getGreen(), 11);
    }

    @Test
    public void testBlueSetter() {
        this.tag.setBlue(13);
        assertEquals(this.tag.getBlue(), 13);
    }

    @Test
    public void testNameSetter() {
        this.tag.setName("meow");
        assertEquals(this.tag.getName(), "meow");
    }
    @Test
    public void testIDSetter() {
        this.tag.setId(873465087);
        assertEquals(this.tag.getId(), 873465087);
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(this.tag, null);
    }

    @Test
    public void testEqualsSame() {
        assertEquals(this.tag, this.tag);
    }

    @Test
    public void testEqualsEqual() {
        assertEquals(this.tag, this.tagTwo);
    }

    @Test
    public void testEqualsDifferent() {
        assertNotEquals(this.tag, this.tagThree);
    }

    @Test
    public void testHashCodeSame() {
        assertEquals(this.tag.hashCode(), this.tag.hashCode());
    }

    @Test
    public void testHashCodeEqual() {
        assertEquals(this.tag.hashCode(), this.tagTwo.hashCode());
    }

    @Test
    public void testHashCodeDifferent() {
        assertNotEquals(this.tag.hashCode(), this.tagThree.hashCode());
    }

    @Test
    public void testHashCodeDifferentClass() {
        assertNotEquals(this.tag.hashCode(), "Hello".hashCode());
    }

    @Test
    public void testStandardFalseEquals(){
        assertFalse(tag.standTagEquals(tagThree));
    }

    @Test
    public void testStandardTrueEquals(){
        assertTrue(tag.standTagEquals(tagTwo));
    }
}