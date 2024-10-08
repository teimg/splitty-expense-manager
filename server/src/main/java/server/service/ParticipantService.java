package server.service;

import commons.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    private  final ParticipantRepository repo;
    private final EventService eventService;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository,
                              EventService eventService) {
        this.repo = participantRepository;
        this.eventService = eventService;
    }
    
    public Participant getById( long id) throws IllegalArgumentException {
        var optionalParticipant = repo.findById(id);

        if(optionalParticipant.isPresent()){
            return optionalParticipant.get();

        }
        throw new IllegalArgumentException("No such participant");

    }

    public Participant save(Participant participant) {
        participant = repo.saveAndFlush(participant);
        eventService.updateLastActivity(participant.getEventId());
        return participant;
    }

    public Participant restore(Participant participant) {
        return repo.saveAndFlush(participant);
    }

    public Participant createParticipant(Participant participant)
        throws IllegalArgumentException{

        checkValidParticipant(participant);
        return save(participant);
    }

    /**
     *
     * @param id id of participant
     * @param newDetails the participant
     * @return updated participant
     * @throws IllegalArgumentException if event or name are null
     */
    public Participant updateParticipant(long id, Participant newDetails)
        throws IllegalArgumentException {

        checkValidParticipant(newDetails);
        
        Participant participant = getById(id);
        participant.setName(newDetails.getName());
        participant.setEventId(newDetails.getEventId());
        participant.setBankAccount(newDetails.getBankAccount());
        participant.setEmail(newDetails.getEmail());
        return save(participant);
    }

    /**
     * delete participant
     *
     * @param id id of the participant to delete
     * @throws IllegalArgumentException if participant is not found
     */

    public void deleteParticipant( long id) throws IllegalArgumentException{
        Optional<Participant> participantData = repo.findById(id);
        if (participantData.isEmpty()) {
            throw new IllegalArgumentException("No such participant");
        }
        Participant participant = participantData.get();
        repo.deleteById(id);
        eventService.updateLastActivity(participant.getEventId());
    }

    /**
     *
     * @return all participants
     */

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

    /**
     *
     * @param s string to check if null
     * @return true if s is empty
     */

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }


    /**
     *
     * @param participant to check invalidly
     * @return true if invalid
     */
    private static boolean isInvalid(Participant participant) {
        return isNullOrEmpty(participant.getName()) || participant.getEventId() == null;
    }

}
