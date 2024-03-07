package server.api;

import commons.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.database.ParticipantRepository;
import server.service.ParticipantService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
//    private final ParticipantRepository repo;

    private final ParticipantService service;

    @Autowired
    public ParticipantController(ParticipantService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getById(@PathVariable("id") long id) {

        try {
            Participant res = service.getById(id);
            return ResponseEntity.ok(res);
        }catch (IllegalArgumentException e){
            System.out.println("not found");
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Participant> deleteParticipant(@PathVariable("id") long id) {
        try {
            service.deleteParticipant(id);
            return ResponseEntity.ok().build();
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping
    public List<Participant> getAll() {
        return service.getAll();
    }

}
