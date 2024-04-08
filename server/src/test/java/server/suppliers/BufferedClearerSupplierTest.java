package server.suppliers;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mockConstruction;

public class BufferedClearerSupplierTest {

    @Test
    public void testSupplier() {
        try (MockedConstruction<FileWriter> fileWriterConstruction = mockConstruction(FileWriter.class);
             MockedConstruction<BufferedWriter> bufferedWriterConstruction = mockConstruction(BufferedWriter.class)) {
            BufferedClearerSupplier supplier = new BufferedClearerSupplier();
            BufferedWriter bufferedClearer = supplier.getBufferedClearer();
            assertNotNull(bufferedClearer);
        }
    }

    @Test
    public void testFail() {
        try (MockedConstruction<FileWriter> fileWriterConstruction = mockConstruction(
                FileWriter.class, (mock, context) -> {throw new IOException("Test exception");})) {
            BufferedClearerSupplier supplier = new BufferedClearerSupplier();
            BufferedWriter bufferedClearer = supplier.getBufferedClearer();
            assertNull(bufferedClearer);
        }
    }

}
