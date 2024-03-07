package server.api;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.EventRepository;
import server.service.EventService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository repo;

    @InjectMocks
    private EventService service;

    @Test
    public void saveTest() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);

        when(repo.saveAndFlush(e1)).thenReturn(e1);
        Event actual = service.save(e1);
        assertEquals(e1, actual);
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

    @Test
    public void deleteTest() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e1 = new Event("name", "inviteCode", participants, creationDate, lastActivity);

        doNothing().when(repo).deleteById(e1.getId());
        service.delete(e1.getId());
        verify(repo).deleteById(e1.getId());
    }
}
