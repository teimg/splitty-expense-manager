package client.utils.communicators;

import client.utils.ClientConfiguration;
import client.utils.communicators.implementations.EventCommunicator;
import client.utils.communicators.implementations.TagCommunicator;
import commons.Event;
import commons.Tag;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventCommunicatorTest {

    /**
     * Excludes testing of websocket Methods
     */

    @Mock
    private ClientConfiguration configuration;

    @Mock
    private ClientSupplier supplier;

    @Mock
    private Client client;

    private String origin;

    private EventCommunicator communicator;

    @BeforeEach
    public void setUp() {
        origin = "";
        MockitoAnnotations.openMocks(this);
        lenient().when(configuration.getServer()).thenReturn(origin);
        lenient().when(supplier.getClient()).thenReturn(client);
        communicator = new EventCommunicator(configuration, supplier);
    }

    @Test
    public void testCreate() {
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Event event = new Event();
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.post(any(Entity.class), eq(Event.class))).thenReturn(event);
        assertEquals(event, communicator.createEvent(event));
        verify(client).target(anyString());
    }

    @Test
    public void testGet() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Event event = new Event();
        event.setId(1L);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(Event.class)).thenReturn(event);
        assertEquals(event, communicator.getEvent(1L));
        verify(client).target(anyString());
    }

    @Test
    public void testGetByInviteCode() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Event event = new Event();
        event.setInviteCode("1234");
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.queryParam(eq("inviteCode"), anyString())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(Event.class)).thenReturn(event);
        assertEquals(event, communicator.getEventByInviteCode("1234"));
        verify(client).target(anyString());
    }

    @Test
    public void testUpdate() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Event event = new Event();
        event.setId(1L);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.put(any(Entity.class), eq(Event.class))).thenReturn(event);
        assertEquals(event, communicator.updateEvent(event));
        verify(client).target(anyString());
    }

    @Test
    public void testRestore() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Event event = new Event();
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.post(any(Entity.class), eq(Event.class))).thenReturn(event);
        assertEquals(event, communicator.restoreEvent(event));
        verify(client).target(anyString());
    }

    @Test
    public void testDelete() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        communicator.deleteEvent(1L);
        verify(client).target(anyString());
    }

    @Test
    public void testGetAll() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Event event = new Event();
        Event event2 = new Event();
        event.setId(1L);
        event2.setId(2L);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        GenericType<List<Event>> genericType = new GenericType<List<Event>>() {};
        when(builder.get(eq(genericType))).thenReturn(List.of(event, event2));
        assertEquals(List.of(event, event2), communicator.getAll());
        verify(client).target(anyString());
    }

    @Test
    public void testRename() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Event event = new Event();
        event.setId(1L);
        event.setName("NameChanged");
        when(client.target(anyString())).thenReturn(webTarget).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder).thenReturn(builder);
        when(builder.get(Event.class)).thenReturn(event);
        when(builder.put(any(Entity.class), eq(Event.class))).thenReturn(event);
        assertEquals(event, communicator.renameEvent(1L, "NameChanged"));
        verify(client, times(2)).target(anyString());
    }

    @Test
    public void testRenameFail() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);;
        when(client.target(anyString())).thenReturn(webTarget).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder).thenReturn(builder);
        when(builder.get(Event.class)).thenReturn(null);
        assertNull(communicator.renameEvent(1L, "NameChanged"));
        verify(client).target(anyString());
    }
}
