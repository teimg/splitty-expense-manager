package server.api;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ParticipantControllerTest {
    private TestParticipantRepository repo;
    private ParticipantController sut;

    @BeforeEach
    public void setup() {
        repo = new TestParticipantRepository();
        sut = new ParticipantController(repo);
    }

    @Test
    public void createParticipant() {
        Participant participant = new Participant("Bob");
        Event event = new Event(1, "Party", "xyz", List.of(participant), new Date(0), new Date(0));
        participant.setEvent(event);
        var actual = sut.createParticipant(participant);
        assertEquals(participant, actual.getBody());
    }

    @Test
    public void cannotCreateWithNullName() {
        Participant participant = new Participant(null);
        Event event = new Event(1, "Party", "xyz", List.of(participant), new Date(0), new Date(0));
        participant.setEvent(event);
        var actual = sut.createParticipant(participant);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void cannotCreateWithNullEvent() {
        Participant participant = new Participant("Bob");
        var actual = sut.createParticipant(participant);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }
}
