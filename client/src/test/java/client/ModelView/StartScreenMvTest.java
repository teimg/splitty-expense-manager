package client.ModelView;


import client.utils.RecentEventTracker;
import client.utils.communicators.interfaces.IEventCommunicator;
import commons.Event;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;

import static org.junit.jupiter.api.Assertions.*;

class StartScreenMvTest {

    StartScreenMv startScreenMv;

    IEventCommunicator eventCommunicator;

    @BeforeEach
    void setup(){
        eventCommunicator = new IEventCommunicator() {
            @Override
            public Event createEvent(Event event) {
                return event;
            }

            @Override
            public Event createEvent(String name) {
                return new Event(name);
            }

            @Override
            public Event getEvent(long id) {
                return null;
            }

            @Override
            public Event getEventByInviteCode(String inviteCode) {
                if(inviteCode.equals("kTwF02c2")){
                    return new Event("exists");
                }
                throw  new NotFoundException();
            }

            @Override
            public Event updateEvent(Event event) {
                return null;
            }

            @Override
            public Event deleteEvent(long id) {
                return null;
            }
        };

        startScreenMv = new StartScreenMv(eventCommunicator, new RecentEventTracker());


    }

    @Test
    void testOffline(){
        eventCommunicator = new IEventCommunicator() {
            @Override
            public Event createEvent(Event event) {
                throw new ProcessingException("");
            }

            @Override
            public Event createEvent(String name) {
                throw new ProcessingException("");
            }

            @Override
            public Event getEvent(long id) {
                throw new ProcessingException("");
            }

            @Override
            public Event getEventByInviteCode(String inviteCode) {
                throw new ProcessingException("");
            }

            @Override
            public Event updateEvent(Event event) {
                throw new ProcessingException("");
            }

            @Override
            public Event deleteEvent(long id) {
                throw new ProcessingException("");
            }
        };

        startScreenMv = new StartScreenMv(eventCommunicator, new RecentEventTracker());

        assertThrows(ProcessingException.class, () ->{
            startScreenMv.joinEventProperty().setValue("Not empty");
            startScreenMv.joinEvent();
        }, "ServerOffline");

        assertThrows(ProcessingException.class, () ->{
            startScreenMv.newEventProperty().setValue("Not empty");
            startScreenMv.createEvent();
        }, "ServerOffline");
    }

    @Test
    void createEvent() {
        startScreenMv.newEventProperty().setValue("created event");
        assertEquals(new Event("created event"), startScreenMv.createEvent());

        startScreenMv.newEventProperty().setValue("");
        assertThrows(IllegalArgumentException.class, () ->{
           startScreenMv.createEvent();
        }, "EventNameEmpty");
    }

    @Test
    void joinEvent() {
        startScreenMv.joinEventProperty().setValue("kTwF02c2");
        Event res = startScreenMv.joinEvent();
        assertEquals(new Event("exists"), res);
    }

    @Test
    void joinNotExistsEvent() {
        startScreenMv.joinEventProperty().setValue("notValidCode");
        assertThrows(NotFoundException.class, ()->{
            Event res = startScreenMv.joinEvent();
        }, "CodeNotFound");

    }


    @Test
    void newEventProperty() {
        assertEquals(startScreenMv.newEventProperty().getValue(), "");
    }

    @Test
    void joinEventProperty() {
        assertEquals(startScreenMv.joinEventProperty().getValue(), "");
    }
}