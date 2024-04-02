package server.api;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import server.service.EventChangeService;
import server.service.EventService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    private EventService service;
    @Mock
    private EventChangeService eventChangeService;

    @InjectMocks
    EventController controller;

    @Test
    public void getAllTest() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);

        List<Event> events = List.of(e1);

        when(service.getAll()).thenReturn(events);
        List<Event> actual = controller.getAll();
        assertEquals(1, actual.size());
        assertEquals(e1, actual.get(0));
    }

    @Test
    public void getByIdTest() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);
        when(service.getById(e1.getId())).thenReturn(Optional.of(e1));
        ResponseEntity<Event> actual = controller.getByID(e1.getId());
        assertEquals(e1, actual.getBody());
        assertEquals(200, actual.getStatusCodeValue());
    }

    @Test
    public void getByInviteCodeTest() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);
        when(service.getByInviteCode("inviteCode")).thenReturn(Optional.of(e1));
        ResponseEntity<Event> actual = controller.getByInviteCode("inviteCode");
        assertEquals(e1, actual.getBody());
        assertEquals(200, actual.getStatusCodeValue());
    }

    @Test
    public void deleteTest() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);

        when(service.getById(1L)).thenReturn(Optional.of(e1));
        ResponseEntity<?> actual = controller.delete(1L);
        assertEquals(200, actual.getStatusCodeValue());
    }

    @Test
    public void updateTestSuccessful() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);
        Event e2 = new Event("name2", "inviteCode2", participants, creationDate, lastActivity);

        when(service.getById(1L)).thenReturn(Optional.of(e1));
        when(service.update(e1)).thenReturn(e2);

        ResponseEntity<Event> actual = controller.update(1L, e2);
        assertEquals(e2.getName(), actual.getBody().getName());
        assertEquals(e2.getInviteCode(), actual.getBody().getInviteCode());
    }

    @Test
    public void updateTestFailure() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e2 = new Event("name2", "inviteCode2", participants, creationDate, lastActivity);

        when(service.getById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Event> actual = controller.update(1L, e2);
        assertEquals( ResponseEntity.notFound().build(), actual);
    }

    @Test
    public void createEventSuccessful() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);
        when(service.createEvent(e1)).thenReturn(e1);
        ResponseEntity<Event> actual = controller.createEvent(e1);
        assertEquals(e1, actual.getBody());
        assertEquals(200, actual.getStatusCodeValue());
    }

    @Test
    public void createEventFailure() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);
        doThrow(IllegalArgumentException.class).when(service).createEvent(e1);
        ResponseEntity<Event> actual = controller.createEvent(e1);
        assertEquals(ResponseEntity.badRequest().build(), actual);
        assertEquals(400, actual.getStatusCodeValue());
    }

    @Test
    public void checkUpdatesSuccess() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);
        when(service.getById(e1.getId())).thenReturn(Optional.of(e1));
        ResponseEntity<Event> actual = controller.checkForUpdates(e1.getId());
        assertEquals(e1, actual.getBody());
        assertEquals(200, actual.getStatusCodeValue());
    }

    @Test
    public void checkUpdatesSuccessTiming() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);
        e1.setLastActivity(new Date(0L));
        when(service.getById(e1.getId())).thenReturn(Optional.of(e1));
        ResponseEntity<Event> actual = controller.checkForUpdates(e1.getId());
        assertEquals(ResponseEntity.noContent().build(), actual);
        assertEquals(204, actual.getStatusCodeValue());
    }

    @Test
    public void checkUpdatesFailure() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);
        e1.setLastActivity(new Date(0L));
        when(service.getById(e1.getId())).thenReturn(Optional.empty());
        ResponseEntity<Event> actual = controller.checkForUpdates(e1.getId());
        assertEquals( ResponseEntity.notFound().build(), actual);
        assertEquals(404, actual.getStatusCodeValue());
    }

}
