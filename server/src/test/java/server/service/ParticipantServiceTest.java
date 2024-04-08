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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    ParticipantRepository mockedRepo;

    @Mock
    EventService mockedEventService;

    ParticipantService ps;

    Participant participant;

    Participant participant1;


    @BeforeEach
    public void setup(){
        ps = new ParticipantService(mockedRepo, mockedEventService);
        participant = new Participant("Henk");
        participant.setEventId(new Event().getId());

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
        Mockito.when(mockedRepo.saveAndFlush(participant)).thenReturn(participant);

        assertEquals(participant, ps.createParticipant(participant));
    }

    @Test
    void createParticipantNoEvent() {
        participant.setEventId(null);
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
        participant.setEmail("test@gmail.com");
        Mockito.when(mockedRepo.findById(participant.getId())).thenReturn(Optional.of(participant));
        Mockito.when(mockedRepo.saveAndFlush(participant)).thenReturn(participant);

        assertEquals(participant, ps.updateParticipant(participant.getId(), participant));

    }

    @Test
    void deleteParticipant() {
        Mockito.when(mockedRepo.findById(participant.getId())).thenReturn(Optional.of(participant));

        ps.deleteParticipant(participant.getId());
    }

    @Test
    void deleteParticipantNotFound() {
        Mockito.when(mockedRepo.findById(participant.getId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->{
            ps.deleteParticipant(participant.getId());
        }, "No such participant" );;
    }


    @Test
    void getAll() {

        Mockito.when(mockedRepo.findAll()).thenReturn(List.of(participant, participant1));

        assertEquals(List.of(participant, participant1), ps.getAll());
    }

    @Test
    public void testRestore() {
        when(mockedRepo.saveAndFlush(participant)).thenReturn(participant);
        ps.restore(participant);
        verify(mockedRepo).saveAndFlush(participant);
    }

}