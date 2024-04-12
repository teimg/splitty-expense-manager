package client.utils.communicators;

import client.utils.ClientConfiguration;
import client.utils.communicators.implementations.CurrencyCommunicator;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyCommunicatorTest {

    @Mock
    private ClientConfiguration configuration;

    @Mock
    private ClientSupplier supplier;

    @Mock
    private Client client;

    private String origin;

    private CurrencyCommunicator communicator;

    @BeforeEach
    public void setUp() {
        origin = "";
        MockitoAnnotations.openMocks(this);
        lenient().when(configuration.getServer()).thenReturn(origin);
        lenient().when(supplier.getClient()).thenReturn(client);
        communicator = new CurrencyCommunicator(configuration, supplier);
    }

    @Test
    public void testGetConversion() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        double val = 1.2;
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("amount"), any())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("currency"), any())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("date"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(Double.class)).thenReturn(val);
        assertEquals(val, communicator.getConversion(
                1.5, "CHF", LocalDate.of(1, 1, 1)));
        verify(client).target(anyString());
    }

    @Test
    public void testGetConversionFail() {
        doThrow(new IllegalArgumentException()).when(client).target(anyString());
        assertThrows(IllegalArgumentException.class, () -> {
            communicator.getConversion(
                    1.5, "CHF", LocalDate.of(1, 1, 1));
        });
    }

    @Test
    public void testClearCache() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.delete(String.class)).thenReturn("Success");
        assertEquals("Success", communicator.clearCache());
        verify(client).target(anyString());
    }

}
