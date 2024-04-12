package client.utils.communicators;

import client.utils.ClientConfiguration;
import client.utils.communicators.implementations.TagCommunicator;
import commons.Tag;
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
public class TagCommunicatorTest {

    @Mock
    private ClientConfiguration configuration;

    @Mock
    private ClientSupplier supplier;

    @Mock
    private Client client;

    private String origin;

    private TagCommunicator communicator;

    @BeforeEach
    public void setUp() {
        origin = "";
        MockitoAnnotations.openMocks(this);
        lenient().when(configuration.getServer()).thenReturn(origin);
        lenient().when(supplier.getClient()).thenReturn(client);
        communicator = new TagCommunicator(configuration, supplier);
    }

    @Test
    public void testCreate() {
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Tag tag = new Tag("Name", 1, 1, 1, 0L);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.post(any(Entity.class), eq(Tag.class))).thenReturn(tag);
        assertEquals(tag, communicator.createTag(tag));
        verify(client).target(anyString());
    }

    @Test
    public void testGet() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Tag tag = new Tag("Name", 1, 1, 1, 0L);
        tag.setId(1L);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(Tag.class)).thenReturn(tag);
        assertEquals(tag, communicator.getTag(1L));
        verify(client).target(anyString());
    }

    @Test
    public void testUpdate() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Tag tag = new Tag("Name", 1, 1, 1, 0L);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.put(any(Entity.class), eq(Tag.class))).thenReturn(tag);
        assertEquals(tag, communicator.updateTag(tag));
        verify(client).target(anyString());
    }

    @Test
    public void testDelete() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Tag tag = new Tag("Name", 1, 1, 1, 0L);
        tag.setId(1L);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.delete(Tag.class)).thenReturn(tag);
        assertEquals(tag, communicator.deleteTag(1L));
        verify(client).target(anyString());
    }

    @Test
    public void testSaveOrUpdate() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Tag tag = new Tag("Name", 1, 1, 1, 0L);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.post(any(Entity.class), eq(Tag.class))).thenReturn(tag);
        assertEquals(tag, communicator.saveOrUpdateTag(tag));
        verify(client).target(anyString());
    }


}