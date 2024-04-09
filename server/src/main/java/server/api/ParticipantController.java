package server.api;

import commons.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.service.ParticipantService;
import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService service;

    @Autowired
    public ParticipantController(ParticipantService service) {
        this.service = service;
    }

    /**
     * endpoint to get participants by id
     *
     * @param id of participant
     * @return  participant or HttpStatus.BAD_REQUEST
     */
    @GetMapping("/{id}")
    public ResponseEntity<Participant> getById(@PathVariable("id") long id) {

        try {
            Participant res = service.getById(id);
            return ResponseEntity.ok(res);
        }
        catch (IllegalArgumentException e){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

    /**
     * Create participant based on id
     * @param participant to create
     * @return created participant or HttpStatus.BAD_REQUEST
     */
    @PostMapping
    public ResponseEntity<Participant> createParticipant(@RequestBody Participant participant) {
        try {
            Participant res = service.createParticipant(participant);
            return  ResponseEntity.ok(participant);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     *
     * @param id id of participant to update
     * @param newDetails participant to update
     * @return the updated participant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable("id") long id,
                                                            @RequestBody Participant newDetails) {
        try {
            Participant saved = service.updateParticipant(id, newDetails);
            return ResponseEntity.ok(saved);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete an participant
     *
     * @param id of participant
     * @return participant or HttpStatus.BAD_REQUEST
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Participant> deleteParticipant(@PathVariable("id") long id) {
        try {
            service.deleteParticipant(id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    /**
     *
     * @return all participants
     */
    @GetMapping
    public List<Participant> getAll() {
        return service.getAll();
    }

}
