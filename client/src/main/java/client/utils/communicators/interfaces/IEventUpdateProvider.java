package client.utils.communicators.interfaces;

import commons.Event;
import commons.EventChange;

import java.util.function.Consumer;

public interface IEventUpdateProvider {
    /**
     * Starts keeping event with id updated.
     * Needs to be called before event is queried.
     * @param id event id
     */
    void start(long id);

    /**
     * Get the up-to-date event.
     * @return the event
     */
    Event event();

    /**
     * Stops keeping event updated.
     * You need to call `start` again before you can query the event again.
     */
    void stop();

    /**
     * Set a function to be executed on event update.
     * Call with `null` to clear handler.
     * @param handler the function
     */
    void setUpdateHandler(Consumer<EventChange> handler);

    /**
     * Set a function to be executed on event deletion.
     * Call with `null` to clear handler.
     * @param handler the function
     */
    void setDeleteHandler(Consumer<EventChange> handler);
}
