package client.utils;

import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class EventCommunicator implements IEventCommunicator{
    private final String origin;

    @Inject
    public EventCommunicator(ClientConfiguration config) {
        origin = config.getServer();
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

    @Override
    public Event updateEvent(Event event) {
        return null;
    }

    @Override
    public Event deleteEvent(long id) {
        return null;
    }


}
