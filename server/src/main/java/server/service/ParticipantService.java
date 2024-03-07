package server.service;

import commons.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    private  final ParticipantRepository repo;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository) {
        this.repo = participantRepository;
    }
    
    public Participant getById( long id) throws IllegalArgumentException {
        var OptionalParticipant = repo.findById(id);

        if(OptionalParticipant.isPresent()){
            return OptionalParticipant.get();

        }
        throw new IllegalArgumentException("No such participant");

    }


    public Participant createParticipant(Participant participant) throws IllegalArgumentException{
        checkValidParticipant(participant);
        return repo.save(participant);
    }
    
    public Participant updateParticipant(long id, Participant newDetails) throws IllegalArgumentException {
        checkValidParticipant(newDetails);
        
        Participant participant = getById(id);
        participant.setName(newDetails.getName());
        participant.setEvent(newDetails.getEvent());
        participant.setBankAccount(newDetails.getBankAccount());
        participant.setEmail(newDetails.getEmail());
        return repo.save(participant);
    }

    public void deleteParticipant( long id) throws IllegalArgumentException{
        Optional<Participant> participantData = repo.findById(id);
        if (participantData.isEmpty()) {
            throw new IllegalArgumentException("No such participant");
        }
        repo.deleteById(id);
    }

    public List<Participant> getAll() {
        return repo.findAll();
    }

    private void checkValidParticipant(Participant participant){

        if( isNullOrEmpty(participant.getName())){
            throw new IllegalArgumentException("Name not specified");
        }

        if(isInvalid(participant)){
            throw new IllegalArgumentException("Event not specified");
        }

    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private static boolean isInvalid(Participant participant) {
        return isNullOrEmpty(participant.getName()) || participant.getEvent() == null;
    }

}
