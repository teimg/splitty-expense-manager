package server.suppliers;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class BufferedReaderSupplierTest {

    @Test
    public void testSupplier() {
        try (MockedConstruction<FileReader> fileReaderConstruction = mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> bufferedReaderConstruction = mockConstruction(BufferedReader.class)) {
            BufferedReaderSupplier supplier = new BufferedReaderSupplier();
            BufferedReader bufferedReader = supplier.getBufferedReader();
            assertNotNull(bufferedReader);
        }
    }

    @Test
    public void testFail() {
        try (MockedConstruction<FileReader> fileReaderrConstruction = mockConstruction(
                FileReader.class, (mock, context) -> {throw new IOException("Test exception");})) {
            BufferedReaderSupplier supplier = new BufferedReaderSupplier();
            BufferedReader bufferedReader = supplier.getBufferedReader();
            assertNull(bufferedReader);
        }
    }

}
