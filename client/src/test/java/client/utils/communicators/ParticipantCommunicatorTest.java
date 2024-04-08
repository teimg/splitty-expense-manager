package client.utils.communicators;

import client.utils.ClientConfiguration;
import client.utils.communicators.implementations.ParticipantCommunicator;
import commons.BankAccount;
import commons.Event;
import commons.Participant;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParticipantCommunicatorTest {

    @Mock
    private ClientConfiguration configuration;

    @Mock
    private ClientSupplier supplier;

    @Mock
    private Client client;

    private String origin;

    private ParticipantCommunicator communicator;

    @BeforeEach
    public void setUp() {
        origin = "";
        MockitoAnnotations.openMocks(this);
        lenient().when(configuration.getServer()).thenReturn(origin);
        lenient().when(supplier.getClient()).thenReturn(client);
        communicator = new ParticipantCommunicator(configuration, supplier);
    }

    @Test
    public void testCreate() {
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Participant participant = new Participant("Hello", "t@gmail.com");
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.post(any(Entity.class), eq(Participant.class))).thenReturn(participant);
        assertEquals(participant, communicator.createParticipant(new Event(), "Hello", "t@gmail.com", new BankAccount()));
        verify(client).target(anyString());
    }

    @Test
    public void testGet() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Participant participant = new Participant("Hello", "t@gmail.com");
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(Participant.class)).thenReturn(participant);
        assertEquals(participant, communicator.getParticipant(1L));
        verify(client).target(anyString());
    }

    @Test
    public void testUpdate() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Participant participant = new Participant("Hello", "t@gmail.com");
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.put(any(Entity.class), eq(Participant.class))).thenReturn(participant);
        assertEquals(participant, communicator.updateParticipant(participant));
        verify(client).target(anyString());
    }

    @Test
    public void testDelete() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Participant participant = new Participant("Hello", "t@gmail.com");
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.delete(Participant.class)).thenReturn(participant);
        assertEquals(participant, communicator.deleteParticipant(1L));
        verify(client).target(anyString());
    }


}