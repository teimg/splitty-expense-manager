package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailRequestTest {

    private EmailRequest er1;

    private EmailRequest er2;

    private EmailRequest er3;

    @BeforeEach
    public void setUp() {
        this.er1 = new EmailRequest("To", "Subject", "Body");
        this.er2 = new EmailRequest("To", "Subject", "Body");
        this.er3 = new EmailRequest("NotTo", "Subject", "Body");
    }

    @Test
    public void testStandardConstructor() {
        assertNotNull(er1);
    }

    @Test
    public void testJPAConstructor() {
        EmailRequest er = new EmailRequest();
        assertNotNull(er);
    }

    @Test
    public void getTo() {
        assertEquals("To", er1.getTo());
    }

    @Test
    public void setTo() {
        er1.setTo("Changed");
        assertEquals("Changed", er1.getTo());
    }

    @Test
    public void getSubject() {
        assertEquals("Subject", er1.getSubject());
    }

    @Test
    public void setSubject() {
        er1.setSubject("Changed");
        assertEquals("Changed", er1.getSubject());
    }

    @Test
    public void getBody() {
        assertEquals("Body", er1.getBody());
    }

    @Test
    public void setBody() {
        er1.setBody("Changed");
        assertEquals("Changed", er1.getBody());
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(er1, null);
    }

    @Test
    public void testEqualsSame() {
        assertEquals(er1, er1);
    }

    @Test
    public void testEqualsEqual() {
        assertEquals(er1, er2);
    }

    @Test
    public void testEqualsDifferent() {
        assertNotEquals(er1, er3);
    }

    @Test
    public void testHashCodeSame() {
        assertEquals(er1.hashCode(), er1.hashCode());
    }

    @Test
    public void testHashCodeEqual() {
        assertEquals(er1.hashCode(), er2.hashCode());
    }

    @Test
    public void testHashCodeDifferent() {
        assertNotEquals(er1.hashCode(), er3.hashCode());
    }

    @Test
    public void testHashCodeDifferentClass() {
        assertNotEquals(er1.hashCode(), "Hello".hashCode());
    }


}
