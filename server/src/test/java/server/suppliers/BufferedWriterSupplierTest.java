package server.suppliers;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class BufferedWriterSupplierTest {

    @Test
    public void testSupplier() {
        try (MockedConstruction<FileWriter> fileWriterConstruction = mockConstruction(FileWriter.class);
             MockedConstruction<BufferedWriter> bufferedWriterConstruction = mockConstruction(BufferedWriter.class)) {
            BufferedWriterSupplier supplier = new BufferedWriterSupplier();
            BufferedWriter bufferedWriter = supplier.getBufferedWriter();
            assertNotNull(bufferedWriter);
        }
    }

    @Test
    public void testFail() {
        try (MockedConstruction<FileWriter> fileWriterConstruction = mockConstruction(
                FileWriter.class, (mock, context) -> {throw new IOException("Test exception");})) {
            BufferedWriterSupplier supplier = new BufferedWriterSupplier();
            BufferedWriter bufferedWriter = supplier.getBufferedWriter();
            assertNull(bufferedWriter);
        }
    }

}
