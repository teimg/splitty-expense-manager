package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PasswordRequestTest {

    private PasswordRequest pr1;

    private PasswordRequest pr2;

    private PasswordRequest pr3;

    @BeforeEach
    public void setUp() {
        this.pr1 = new PasswordRequest("Password");
        this.pr2 = new PasswordRequest("Password");
        this.pr3 = new PasswordRequest("NotSame");
    }

    @Test
    public void testStandardConstructor() {
        assertNotNull(pr1);
    }

    @Test
    public void testJPAConstructor() {
        PasswordRequest pr = new PasswordRequest();
        assertNotNull(pr);
    }

    @Test
    public void getPassword() {
        assertEquals("Password", pr1.getPassword());
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(pr1, null);
    }

    @Test
    public void testEqualsSame() {
        assertEquals(pr1, pr1);
    }

    @Test
    public void testEqualsEqual() {
        assertEquals(pr1, pr2);
    }

    @Test
    public void testEqualsDifferent() {
        assertNotEquals(pr1, pr3);
    }

    @Test
    public void testHashCodeSame() {
        assertEquals(pr1.hashCode(), pr1.hashCode());
    }

    @Test
    public void testHashCodeEqual() {
        assertEquals(pr1.hashCode(), pr2.hashCode());
    }

    @Test
    public void testHashCodeDifferent() {
        assertNotEquals(pr1.hashCode(), pr3.hashCode());
    }

    @Test
    public void testHashCodeDifferentClass() {
        assertNotEquals(pr1.hashCode(), "Hello".hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("PasswordRequest{password='Password'}", pr1.toString());
    }

}
