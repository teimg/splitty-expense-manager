package client.ModelView;


import client.utils.JoinableEvent;
import client.utils.RecentEventTracker;
import client.utils.communicators.interfaces.IEventCommunicator;
import commons.Event;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import javafx.beans.property.SimpleListProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StartScreenMvTest {

    StartScreenMv startScreenMv;

    @Mock
    RecentEventTracker mockTracker;

    @Mock
    IEventCommunicator mockEventCommunicator;

    @BeforeEach
    void setup(){
        startScreenMv = new StartScreenMv(mockEventCommunicator, mockTracker);
    }

    @Test
    void testOffline(){

        Mockito.when(mockEventCommunicator.createEvent(Mockito.any(Event.class)))
            .thenThrow(new ProcessingException("Could not reach server"));

        Mockito.when(mockEventCommunicator.getEventByInviteCode(Mockito.anyString()))
            .thenThrow(new ProcessingException("Could not reach server"));

        assertThrows(ProcessingException.class, () ->{
            startScreenMv.joinEventProperty().setValue("Not empty");
            startScreenMv.joinEvent();
        }, "ServerOffline");

        assertThrows(ProcessingException.class, () ->{
            startScreenMv.newEventProperty().setValue("Not empty");
            startScreenMv.createEvent();

        }, "ServerOffline");

        assertThrows(ProcessingException.class, () ->{
            startScreenMv.getRecentEvent("abc");

        }, "ServerOffline");
    }

    @Test
    void createEvent() {
        Mockito.when(mockEventCommunicator.createEvent(Mockito.any(Event.class)))
            .thenAnswer( i -> i.getArguments()[0]);

        startScreenMv.newEventProperty().setValue("created event");
        assertEquals(new Event("created event"), startScreenMv.createEvent());


        startScreenMv.newEventProperty().setValue("");
        assertThrows(IllegalArgumentException.class, () ->{
            startScreenMv.createEvent();
        }, "EventNameEmpty");
    }

    @Test
    void joinEvent() {
        Event ext = new Event("exists");
        Mockito.when(mockEventCommunicator.getEventByInviteCode("kTwF02c2"))
            .thenReturn(ext);

        startScreenMv.joinEventProperty().setValue("kTwF02c2");
        Event res = startScreenMv.joinEvent();
        assertEquals(ext, res);
    }

    @Test
    void joinNotExistsEvent() {
        Mockito.when(mockEventCommunicator.getEventByInviteCode("notValidCode"))
                .thenThrow(new NotFoundException("404 error??"));

        startScreenMv.joinEventProperty().setValue("notValidCode");
        assertThrows(NotFoundException.class, ()->{
            Event res = startScreenMv.joinEvent();
        }, "CodeNotFound");

    }


    @Test
    void newEventProperty() {
        assertEquals("", startScreenMv.newEventProperty().getValue());
    }

    @Test
    void joinEventProperty() {
        assertEquals("", startScreenMv.joinEventProperty().getValue());
    }

    @Test
    void deleteEvent(){
        assertDoesNotThrow(() ->{
            startScreenMv.deleteEvent(new JoinableEvent("abc", "Henk"));
        });
    }

    @Test
    void init(){
        assertDoesNotThrow(() ->{
            startScreenMv.init();
        });
    }

    @Test
    void getRecentEvent(){
        Event ext = new Event("testEvent");
        ext.setId(1);

        var mockMeth = Mockito.when(mockEventCommunicator.getEventByInviteCode("abc"))
            .thenThrow(new NotFoundException("blbal bla not found"))
            .thenReturn(ext);

        assertThrows(NotFoundException.class, ()->{
           startScreenMv.getRecentEvent("abc");
        }, "CodeNotFound");

        mockMeth.thenReturn(ext);

        assertEquals(ext, startScreenMv.getRecentEvent("abc"));
    }

    @Test
    void recentEventsProperty(){
        assertEquals(new SimpleListProperty<>(),
            startScreenMv.recentEventsProperty().get());
    }
}