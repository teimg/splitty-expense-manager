package server.suppliers;

import org.junit.jupiter.api.Test;
import server.suppliers.RandomSupplier;

import static org.junit.jupiter.api.Assertions.*;

class RandomSupplierTest {

    @Test
    void getRandom() {
        assertDoesNotThrow(() -> (new RandomSupplier()).getRandom());
    }
}