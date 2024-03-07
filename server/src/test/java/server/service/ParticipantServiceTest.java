package server.service;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    ParticipantRepository mockedRepo;

    ParticipantService ps;

    Participant participant;

    Participant participant1;


    @BeforeEach
    public void setup(){
        ps = new ParticipantService(mockedRepo);
        participant = new Participant("Henk");
        participant.setEvent(new Event());

        participant1 = new Participant("Piet");

    }

    @Test
    void getById() {
        Mockito.when(mockedRepo.findById(1L)).thenReturn(Optional.of(participant));

        assertEquals(participant, ps.getById(1L));
    }

    @Test
    void getByIdNNotFound() {
        Mockito.when(mockedRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->{
            ps.getById(1L);
        }, "No such participant" );
    }

    @Test
    void createParticipant() {
        Mockito.when(mockedRepo.save(participant)).thenReturn(participant);

        assertEquals(participant, ps.createParticipant(participant));
    }

    @Test
    void createParticipantNoEvent() {
        participant.setEvent(null);
        assertThrows(IllegalArgumentException.class, () ->{
           ps.createParticipant(participant);
        }, "Event not specified");
    }

    @Test
    void createParticipantNoName() {
        participant.setName(null);
        assertThrows(IllegalArgumentException.class, () ->{
            ps.createParticipant(participant);
        }, "Name not specified");
    }

    @Test
    void updateParticipant() {
    }

    @Test
    void deleteParticipant() {
    }

    @Test
    void getAll() {
        Mockito.when(mockedRepo.)
    }
}