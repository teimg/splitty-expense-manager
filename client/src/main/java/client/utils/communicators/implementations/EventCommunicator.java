package client.utils.communicators.implementations;

import client.utils.ClientConfiguration;
import client.utils.communicators.interfaces.IEventCommunicator;
import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class EventCommunicator implements IEventCommunicator {
    private final String origin;
    private final StompSession session;

    @Inject
    public EventCommunicator(ClientConfiguration config) {
        origin = config.getServer();
        session = connect("ws://localhost:8080/websocket");
    }

    @Override
    public Event createEvent(String name) {
        return null;
    }

    /**
     * Gets an existing event by id.
     *
     * @param id event id
     * @return the event
     */
    @Override
    public Event getEvent(long id) {
        return ClientBuilder.newClient()
                .target(origin).path("api/event/{id}")
                .resolveTemplate("id", id)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(Event.class);
    }

    @Override
    public Event createEvent(Event event) {
        return ClientBuilder.newClient()
                .target(origin).path("api/event")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    @Override
    public Event getEventByInviteCode(String inviteCode) {
        return ClientBuilder.newClient()
                    .target(origin).path("api/event/byInviteCode/{inviteCode}")
                    .resolveTemplate("inviteCode", inviteCode)
                    .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                    .get(Event.class);
    }

    public void requestEventUpdates(long eventId, Consumer<Event> onUpdate) {
        Response response = ClientBuilder.newClient()
                .target(origin).path("api/event/checkUpdates/{id}")
                .resolveTemplate("id", eventId)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Event updatedEvent = response.readEntity(Event.class);
            onUpdate.accept(updatedEvent);
        }
    }

    @Override
    public Event updateEvent(Event event) {
        return ClientBuilder.newClient()
            .target(origin).path("api/event/update/{id}")
            .resolveTemplate("id", event.getId())
            .request(APPLICATION_JSON).accept(APPLICATION_JSON)
            .put(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    @Override
    public Event deleteEvent(long id) {
        return null;
    }

    @Override
    public List<Event> getAll() {
        return ClientBuilder.newClient()
                .target(origin).path("api/event")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Event>>() {});
    }


    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.getObjectMapper().registerModule(new JavaTimeModule());
        stomp.setMessageConverter(converter);
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    @Override
    public <T> void registerForWebSocketMessages(String dest, Class<T> type, Consumer<T> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }
}
