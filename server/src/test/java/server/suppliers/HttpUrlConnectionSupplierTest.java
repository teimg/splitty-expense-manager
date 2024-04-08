package server.suppliers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HttpUrlConnectionSupplierTest {

    @Test
    public void testSuccess() {
        HttpUrlConnectionSupplier hucs = new HttpUrlConnectionSupplier();
        assertNotNull(hucs.getConnection("https://openexchangerates.org/api/latest.json?app_id=78d7f7ea8bc34ebea95de1cb9cb5887b&base=USD"));
    }

    @Test
    public void testFailure() {
        HttpUrlConnectionSupplier hucs = new HttpUrlConnectionSupplier();
        assertNull(hucs.getConnection("failedConnection"));
    }

}
