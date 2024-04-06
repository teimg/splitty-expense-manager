package client.utils.communicators;

import client.utils.ClientConfiguration;
import client.utils.communicators.implementations.EmailCommunicator;
import commons.EmailRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailCommunicatorTest {

    @Mock
    private ClientConfiguration configuration;

    @Mock
    private ClientSupplier supplier;

    @Mock
    private Client client;

    private String origin;

    private EmailCommunicator communicator;

    @BeforeEach
    public void setUp() {
        origin = "";
        MockitoAnnotations.openMocks(this);
        lenient().when(configuration.getServer()).thenReturn(origin);
        lenient().when(supplier.getClient()).thenReturn(client);
        communicator = new EmailCommunicator(configuration, supplier);
    }

    @Test
    public void testSend() {
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        EmailRequest emailRequest = new EmailRequest("To", "Subject", "From");
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.post(any(Entity.class), eq(EmailRequest.class))).thenReturn(emailRequest);
        assertEquals(emailRequest, communicator.sendEmail(emailRequest));
        verify(client).target(anyString());
    }

    @Test
    public void testGetAll() {
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        EmailRequest emailRequest = new EmailRequest("To", "Subject", "From");
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(EmailRequest.class)).thenReturn(emailRequest);
        assertEquals(emailRequest, communicator.getAll());
        verify(client).target(anyString());
    }

    @Test
    public void getOrigin() {
        assertEquals(communicator.getOrigin(), "");
    }



}

