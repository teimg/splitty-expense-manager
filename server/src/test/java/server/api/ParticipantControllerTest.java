package server.api;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import server.service.ParticipantService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        participant.setEvent(
            new Event( "Party", "xyz",
                List.of(participant), new Date(0), new Date(0)));
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
    public void getById() {
        var createResponse = pc.createParticipant(participant);
        Participant saved = createResponse.getBody();
        Mockito.when(mockService.getById(participant.getId())).thenReturn(participant);

        var actual = pc.getById(saved.getId());
        assertEquals(saved, actual.getBody());
    }

    @Test
    public void updateParticipant() {
        Mockito.when(mockService.createParticipant(participant)).thenReturn(participant);
        var createResponse = pc.createParticipant(participant);
        Participant saved = createResponse.getBody();
        saved.setName("Alex");
        Mockito.when(mockService.updateParticipant(saved.getId(), saved)).thenReturn(saved);
        var actual = pc.updateParticipant(saved.getId(), saved);
        assertEquals(saved, actual.getBody());
    }

    @Test
    public void deleteParticipant() {
        pc.deleteParticipant(participant.getId());
    }
}
