package server.api;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import server.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    private EventService service;

    @Mock
    private TagService tagService;

    @Mock
    private ParticipantService participantService;

    @Mock
    private ExpenseService expenseService;

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
        assertEquals(HttpStatusCode.valueOf(200), actual.getStatusCode());
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
        assertEquals(HttpStatusCode.valueOf(200), actual.getStatusCode());
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
        assertEquals(HttpStatusCode.valueOf(200), actual.getStatusCode());
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
        assertEquals(HttpStatusCode.valueOf(200), actual.getStatusCode());
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
        assertEquals(HttpStatusCode.valueOf(400), actual.getStatusCode());
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
        var actual = controller.checkForUpdates(e1.getId());
        assertNull(actual.getResult());
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
//        ResponseEntity<Event> actual = controller.checkForUpdates(e1.getId());
//        assertEquals(ResponseEntity.noContent().build(), actual);
//        assertEquals(HttpStatusCode.valueOf(204), actual.getStatusCode());
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
        var actual = controller.checkForUpdates(e1.getId());
        assertEquals( ResponseEntity.notFound().build(), actual);
        assertEquals(HttpStatusCode.valueOf(404), actual.getResult());
    }

    @Test
    public void restoreFail() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "CODE", participants, creationDate, lastActivity);
        when(service.existsByInviteCode("CODE")).thenReturn(true);
        assertEquals(ResponseEntity.badRequest().build(), controller.restoreEvent(e1));
        verify(service).existsByInviteCode("CODE");
    }

    @Test
    public void restoreSuccess() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = new ArrayList<>();
        participants.add(p1);
        participants.add(p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "CODE", participants, creationDate, lastActivity);
        e1.setId(0L);
        Expense e = new Expense(1L, "Lunch", 60.0, new Participant("Jackson"), participants, LocalDate.now(), new Tag());
        e1.addExpense(e);
        e1.addTag(new Tag("Tag", 1, 1, 1, 1L));
        when(service.existsByInviteCode("CODE")).thenReturn(false);
        when(service.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(service.restore(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(participantService.restore(any(Participant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(tagService.restore(new Tag("Tag", 1, 1, 1, 1L)))
                .thenReturn(new Tag("Tag", 1, 1, 1, 1L));
        lenient().when(expenseService.restore(e)).thenReturn(e);
        Event expected = new Event("name", "CODE", new ArrayList<>(), creationDate, lastActivity);
        expected.addParticipant(p2);
        expected.addParticipant(p2);
        expected.addExpense(e);
        expected.addTag(new Tag("Tag", 1, 1, 1, 1L));
        assertEquals(expected, controller.restoreEvent(e1).getBody());
        assertEquals(HttpStatusCode.valueOf(200), controller.restoreEvent(e1).getStatusCode());
    }

}
