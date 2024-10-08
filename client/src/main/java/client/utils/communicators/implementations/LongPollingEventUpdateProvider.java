package client.utils.communicators.implementations;

import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IEventUpdateProvider;
import com.google.inject.Inject;
import commons.Event;
import commons.EventChange;

import jakarta.ws.rs.ProcessingException;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.function.Consumer;

public class LongPollingEventUpdateProvider implements IEventUpdateProvider {
    private Task<Void> longPollingTask;
    private boolean active = false;
    private Event event;

    private Consumer<EventChange> updateHandler;
    private Consumer<EventChange> deleteHandler;

    private final IEventCommunicator eventCommunicator;

    @Inject
    public LongPollingEventUpdateProvider(IEventCommunicator eventCommunicator) {
        this.eventCommunicator = eventCommunicator;
    }

    private void runInGUIThread(Consumer<EventChange> handler, EventChange change) {
        if (handler != null)
            Platform.runLater(() -> handler.accept(change));
    }

    @Override
    public void start(long id) {
        if (active) throw new RuntimeException("was already updating another event");
        // Initialize held event
        event = eventCommunicator.getEvent(id);
        if (event == null) throw new RuntimeException("invalid event id");
        // Make sure event stays up to date
        longPollingTask = new Task<>() {
            @Override
            protected Void call() {
                while (!isCancelled()) {
                    // Poll for a change
                    EventChange change;
                    try {
                        change = eventCommunicator.checkForEventUpdates(id);
                    } catch (ProcessingException e) {
                        stop();
                        break;
                    }
                    if (change == null) continue;
                    // Process the change
                    switch (change.getType()) {
                        case MODIFICATION -> {
                            event = change.getEvent();
                            runInGUIThread(updateHandler, change);
                        }
                        case DELETION -> {
                            runInGUIThread(deleteHandler, change);
                            stop();
                        }
                    }
                }
                return null;
            }
        };
        // Start polling
        Thread longPollingThread = new Thread(longPollingTask);
        longPollingThread.setDaemon(true);
        longPollingThread.start();
        // Mark as active
        active = true;
    }

    @Override
    public Event event() {
        if (active) return event;
        throw new RuntimeException("event is not getting updated");
    }

    @Override
    public void stop() {
        if (!active) return;
        longPollingTask.cancel();
        setUpdateHandler(null);
        setDeleteHandler(null);
        active = false;
    }

    @Override
    public void setUpdateHandler(Consumer<EventChange> handler) {
        updateHandler = handler;
    }

    @Override
    public void setDeleteHandler(Consumer<EventChange> handler) {
        deleteHandler = handler;
    }
}
