package client.utils.communicators;

import client.utils.ClientConfiguration;
import client.utils.communicators.implementations.AdminCommunicator;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminCommunicatorTest {

    @Mock
    private ClientConfiguration configuration;

    @Mock
    private ClientSupplier supplier;

    @Mock
    private Client client;

    private String origin;

    private AdminCommunicator communicator;

    @BeforeEach
    public void setUp() {
        origin = "";
        MockitoAnnotations.openMocks(this);
        lenient().when(configuration.getServer()).thenReturn(origin);
        lenient().when(supplier.getClient()).thenReturn(client);
        communicator = new AdminCommunicator(configuration, supplier);
    }

    @Test
    public void testValidatePassword() {
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.post(any())).thenReturn(Response.accepted().build());
        assertTrue(communicator.validatePassword("password"));
        verify(client).target(anyString());
    }
}
