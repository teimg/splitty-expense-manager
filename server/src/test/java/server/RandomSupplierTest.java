package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomSupplierTest {

    @Test
    void getRandom() {
        assertDoesNotThrow(() -> (new RandomSupplier()).getRandom());
    }
}