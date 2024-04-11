package client.utils.communicators.implementations;

import client.utils.ClientConfiguration;
import client.utils.JsonUtils;
import client.utils.communicators.ClientSupplier;
import client.utils.communicators.interfaces.IEventCommunicator;
import com.google.inject.Inject;
import commons.Event;
import commons.EventChange;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class EventCommunicator implements IEventCommunicator {
    private final String origin;
    private final String webSocketURL;
    private StompSession session;

    private final ClientSupplier client;

    @Inject
    public EventCommunicator(ClientConfiguration config, ClientSupplier client) {
        this.origin = config.getServer();
        this.webSocketURL = config.getServerWS() + "/websocket";
        this.client = client;
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
        return client.getClient()
                .target(origin).path("api/event/{id}")
                .resolveTemplate("id", id)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(Event.class);
    }

    @Override
    public Event createEvent(Event event) {
        return client.getClient()
                .target(origin).path("api/event")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    @Override
    public Event restoreEvent(Event event) {
        return client.getClient()
                .target(origin).path("api/event/restoreEvent")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    @Override
    public Event renameEvent(long id, String newName) {
        Event eventToUpdate = getEvent(id);
        if (eventToUpdate != null) {
            eventToUpdate.setName(newName);
            return updateEvent(eventToUpdate);
        }
        return null;
    }


    @Override
    public Event getEventByInviteCode(String inviteCode) {
        return client.getClient()
                    .target(origin).path("api/event/byInviteCode/{inviteCode}")
                    .resolveTemplate("inviteCode", inviteCode)
                    .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                    .get(Event.class);
    }

    public EventChange checkForEventUpdates(long id) {
        try {
            Response response = client.getClient()
                    .target(origin).path("api/event/checkUpdates/{id}")
                    .resolveTemplate("id", id)
                    .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(EventChange.class);
            } else {
                // No updates or event not found, handle accordingly
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error checking for event updates: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Event updateEvent(Event event) {
        return client.getClient()
            .target(origin).path("api/event/update/{id}")
            .resolveTemplate("id", event.getId())
            .request(APPLICATION_JSON).accept(APPLICATION_JSON)
            .put(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    @Override
    public void deleteEvent(long id) {
        client.getClient()
                .target(origin).path("api/event/{id}")
                .resolveTemplate("id", id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    @Override
    public List<Event> getAll() {
        return client.getClient()
                .target(origin).path("api/event")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Event>>() {
                });
    }

    @Override
    public void establishWebSocketConnection() {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(JsonUtils.getObjectMapper());
        stomp.setMessageConverter(converter);
        try {
            session = stomp.connect(webSocketURL, new StompSessionHandlerAdapter() {
            }).get();
            System.out.println("websockets: connection established");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void subscribeForWebSocketMessages(String dest, Class<T> type,
                                                  Consumer<T> consumer) {
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

    @Override
    public void closeWebSocketConnection() {
        session.disconnect();
        System.out.println("websockets: connection closed");
    }
}
