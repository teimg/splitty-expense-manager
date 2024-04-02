package server.service;

import commons.Event;
import commons.EventChange;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.EventRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository repo;

    @Mock
    private EventChangeService eventChangeService;

    @InjectMocks
    private EventService service;

    private Event e1;

    private Event e2;

    @BeforeEach
    public void setUp() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        this.e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);
        this.e2 = new Event("name2", "inviteCode2", participants, creationDate, lastActivity);
    }

    @Test
    public void saveTest() {
        when(repo.saveAndFlush(e1)).thenReturn(e1);
        Event actual = service.save(e1);
        assertEquals(e1, actual);
        verify(repo).saveAndFlush(e1);
    }

    @Test
    public void getByInviteCodeTest() {
        when(repo.findByInviteCode(e1.getInviteCode())).thenReturn(Optional.ofNullable(e1));
        Optional<Event> actual = service.getByInviteCode(e1.getInviteCode());
        assertEquals(e1, actual.get());
        verify(repo).findByInviteCode(e1.getInviteCode());
    }

    @Test
    public void deleteSuccessTest() {
        when(repo.findById(e1.getId())).thenReturn(Optional.ofNullable(e1));
        service.delete(e1.getId());
        verify(eventChangeService).sendChange(new EventChange(EventChange.Type.DELETION, e1));
        verify(repo).delete(e1);
    }

    @Test
    public void deleteFailTest() {
        when(repo.findById(e1.getId())).thenReturn(Optional.empty());
        service.delete(e1.getId());
        verify(repo, times(0)).delete(e1);
    }

    @Test
    public void generateInviteCode() {
        long id = 12345;
        long id2 = 12345;
        assertNotEquals(service.generateInviteCode(id), service.generateInviteCode(id2));
    }

    @Test
    public void createSuccessTest() {
        when(repo.saveAndFlush(e1)).thenReturn(e1);
        Event event = service.createEvent(e1);
        verify(eventChangeService).sendChange(new EventChange(EventChange.Type.CREATION, e1));
        assertEquals(e1, event);
    }

    @Test
    public void createFailTest() {
        e1.setName("");
        assertThrows(IllegalArgumentException.class, () -> {
            service.createEvent(e1);
        });
    }

    @Test
    public void updateTest() {
        when(repo.saveAndFlush(e1)).thenReturn(e1);
        Event event = service.update(e1);
        verify(eventChangeService).sendChange(new EventChange(EventChange.Type.MODIFICATION, e1));
        assertEquals(e1, event);
    }

    @Test
    public void updateLastActivitySuccess() {
        when(repo.findById(e1.getId())).thenReturn(Optional.of(e1));
        when(repo.saveAndFlush(e1)).thenReturn(e1);
        service.updateLastActivity(e1.getId());
        verify(repo).saveAndFlush(any());
        verify(eventChangeService).sendChange(any());
    }

    @Test
    public void updateLastActivityFail() {
        when(repo.findById(e1.getId())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            service.updateLastActivity(e1.getId());
        });
    }

    @Test
    public void isNullOrEmpty() {
        assertTrue(service.isNullOrEmpty(""));
        assertTrue(service.isNullOrEmpty(null));
        assertFalse(service.isNullOrEmpty("Yo"));
    }

    @Test
    public void getByIdTest() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);

        when(repo.findById(1L)).thenReturn(Optional.of(e1));
        Optional<Event> actual = service.getById(1L);
        assertEquals(e1, actual.get());
    }

    @Test
    public void getAllTest() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);
        Event e2 = new Event("name2", "inviteCode2", participants, creationDate, lastActivity);

        List<Event> events = Arrays.asList(e1, e2);
        when(repo.findAll()).thenReturn(events);
        List<Event> actual = service.getAll();

        assertEquals(events, actual);
        verify(repo).findAll();
    }


//    TODO: fix the test for the new version of the service
//    @Test
//    public void deleteTest() {
//        Participant p1 = new Participant("name1", "email1");
//        Participant p2 = new Participant("name2", "email2");
//        List<Participant> participants = List.of(p1, p2);
//        Date creationDate = new Date(2024, 2, 10);
//        Date lastActivity = new Date(2024, 10, 10);
//        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);
//
//        doNothing().when(repo).deleteById(e1.getId());
//        service.delete(e1.getId());
//        verify(repo).deleteById(e1.getId());
//    }
}
