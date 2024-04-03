package server.api;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import server.service.ParticipantService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
public class ParticipantControllerTest {

    @Mock
    private ParticipantService mockService;
    private ParticipantController pc;

    Participant participant;

    @BeforeEach
    public void setup() {
        pc = new ParticipantController(mockService);

        participant = new Participant("Henk");
    }

    @Test
    public void createParticipant() {
        Mockito.when(mockService.createParticipant(participant)).thenReturn(participant);

        assertEquals(ResponseEntity.ok(participant), pc.createParticipant(participant));
    }

    @Test
    public void cannotCreateWithNullName() {
        Mockito.when(mockService.createParticipant(participant)).thenThrow(IllegalArgumentException.class);

        assertEquals(BAD_REQUEST, pc.createParticipant(participant).getStatusCode());
    }


    @Test
    public void getByIdSuccess() {
        var createResponse = pc.createParticipant(participant);
        Participant saved = createResponse.getBody();
        Mockito.when(mockService.getById(participant.getId())).thenReturn(participant);

        var actual = pc.getById(saved.getId());
        assertEquals(saved, actual.getBody());
    }

    @Test
    public void getByIdFailure() {
        Participant saved = participant;
        Mockito.when(mockService.getById(saved.getId())).thenThrow(IllegalArgumentException.class);
        assertThrows(ResponseStatusException.class, () -> {
            pc.getById(saved.getId());
        });
    }

    @Test
    public void getAll() {
        Mockito.when(mockService.getAll()).thenReturn(List.of(participant));
        assertEquals(List.of(participant), pc.getAll());
    }

    @Test
    public void updateParticipantSuccess() {
        Mockito.when(mockService.createParticipant(participant)).thenReturn(participant);
        var createResponse = pc.createParticipant(participant);
        Participant saved = createResponse.getBody();
        saved.setName("Alex");
        Mockito.when(mockService.updateParticipant(saved.getId(), saved)).thenReturn(saved);
        var actual = pc.updateParticipant(saved.getId(), saved);
        assertEquals(saved, actual.getBody());
    }

    @Test
    public void updateParticipantFailure() {
        Participant saved = participant;
        saved.setName("Alex");
        Mockito.when(mockService.updateParticipant(saved.getId(), saved)).thenThrow(IllegalArgumentException.class);
        var actual = pc.updateParticipant(saved.getId(), saved);
        assertEquals(ResponseEntity.badRequest().build(), actual);
        assertEquals(HttpStatusCode.valueOf(400), actual.getStatusCode());
    }

    @Test
    public void deleteParticipantSuccess() {
        ResponseEntity<Participant> actual = pc.deleteParticipant(participant.getId());
        assertEquals(ResponseEntity.ok().build(), actual);
        assertEquals(HttpStatusCode.valueOf(200), actual.getStatusCode());
    }

    @Test
    public void deleteParticipantFailure() {
        doThrow(new IllegalArgumentException()).when(mockService).deleteParticipant(participant.getId());
        ResponseEntity<Participant> actual = pc.deleteParticipant(participant.getId());
        assertEquals(ResponseEntity.badRequest().build(), actual);
        assertEquals(HttpStatusCode.valueOf(400), actual.getStatusCode());
    }
}
