package client.utils.communicators.interfaces;

import commons.Event;

import java.util.List;
import java.util.function.Consumer;

public interface IEventCommunicator {

    Event createEvent(Event event);

    /**
     * Creates a new event.
     * @param name name of the event
     * @return the created event
     */
    Event createEvent(String name);

    /**
     * Gets an existing event by id.
     * @param id event id
     * @return the event
     */
    Event getEvent(long id);

    Event getEventByInviteCode(String inviteCode);

    /**
     * Updates an existing event.
     * @param event the event with updated fields
     * @return the updated event
     */
    Event updateEvent(Event event);

    /**
     * Deletes an existing event.
     * @param id event id
     * @return the deleted event
     */
    Event deleteEvent(long id);

    List<Event> getAll();

    void establishWebSocketConnection();

    <T> void subscribeForWebSocketMessages(String dest, Class<T> type, Consumer<T> consumer);
    Event checkForEventUpdates(long id);

    Event renameEvent(long id, String name);

    Event restoreEvent(Event event);

    void closeWebSocketConnection();
}
