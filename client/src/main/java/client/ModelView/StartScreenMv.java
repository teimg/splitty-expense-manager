package client.ModelView;

import client.utils.IEventCommunicator;
import commons.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StartScreenMv {

    private  IEventCommunicator server;

    private StringProperty newEvent;

    private StringProperty joinEvent;


    @Inject
    public StartScreenMv(IEventCommunicator server) {
        this.server = server;
        newEvent =  new SimpleStringProperty("");
        joinEvent = new SimpleStringProperty("");
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

    public StringProperty newEventProperty() {
        return newEvent;
    }


    public StringProperty joinEventProperty() {
        return joinEvent;
    }

}
