package client.ModelView;


import client.utils.JoinableEvent;
import client.utils.RecentEventTracker;
import client.utils.communicators.interfaces.IEventCommunicator;
import commons.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class StartScreenMv {

    private final IEventCommunicator server;

    private final RecentEventTracker tracker;

    private StringProperty newEvent;

    private StringProperty joinEvent;

    private ObjectProperty<ObservableList<JoinableEvent>> recentEvents;

    @Inject
    public StartScreenMv(IEventCommunicator server, RecentEventTracker tracker) {
        this.server = server;
        this.tracker = tracker;

        recentEvents = new SimpleObjectProperty<>(new SimpleListProperty<>());
        newEvent =  new SimpleStringProperty("");
        joinEvent = new SimpleStringProperty("");
    }

    public void init(){
        recentEvents.set(tracker.getEvents());
    }

    public Event createEvent() {
        String name = newEvent.getValue();
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("EventNameEmpty");
        }
        try{
            Event event = new Event(name);
            event = server.createEvent(event);
            return event;
        }catch (ProcessingException e){
            throw new ProcessingException("ServerOffline");
        }

    }

    public Event joinEvent() {
        String inviteCode = joinEvent.getValue();

        try{
            return server.getEventByInviteCode(inviteCode);
        }catch (NotFoundException e){
            throw new NotFoundException("CodeNotFound");
        }catch (ProcessingException e){
            throw new ProcessingException("ServerOffline");
        }
    }

    public Event getRecentEvent(String inviteCode){
        try {
            return server.getEventByInviteCode(inviteCode);
        }catch (NotFoundException | NullPointerException e){
            throw new NotFoundException("CodeNotFound");
        }catch (ProcessingException e){
            throw new ProcessingException("ServerOffline");
        }
    }

    public void updateRecents() throws ProcessingException {
        List<JoinableEvent> oldEvents = new ArrayList<>(tracker.getEvents());

        oldEvents.reversed().forEach(oldEvent -> {
            try {
                Event newEvent = getRecentEvent(oldEvent.inviteCode());
                tracker.registerEvent(newEvent);
            } catch (NotFoundException e) {
                tracker.deleteEvent(oldEvent);
            }
        });
    }

    public void deleteEvent(JoinableEvent event){
        tracker.deleteEvent(event);
    }

    public StringProperty newEventProperty() {
        return newEvent;
    }

    public StringProperty joinEventProperty() {
        return joinEvent;
    }

    public ObjectProperty<ObservableList<JoinableEvent>> recentEventsProperty() {
        return recentEvents;
    }
}
