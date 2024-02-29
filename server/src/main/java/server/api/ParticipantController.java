package server.api;

import commons.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ParticipantRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
    private final ParticipantRepository repo;

    /**
     * constructor for participant controller
     *
     * @param repo participant repository
     */
    public ParticipantController(ParticipantRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping
    public ResponseEntity<Participant> createParticipant(@RequestBody Participant participant) {
        if (isNullOrEmpty(participant.getName()) || participant.getEvent() == null) {
            return ResponseEntity.badRequest().build();
        }
        Participant saved = repo.save(participant);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable("id") long id, 
                                                         @RequestBody Participant newDetails) {
        if (isNullOrEmpty(newDetails.getName()) || newDetails.getEvent() == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Participant> participantData = repo.findById(id);
        if (participantData.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Participant participant = participantData.get();
        participant.setName(newDetails.getName());
        participant.setEvent(newDetails.getEvent());
        participant.setBankAccount(newDetails.getBankAccount());
        Participant saved = repo.save(participant);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable("id") long id) {
        Optional<Participant> participantData = repo.findById(id);
        if (participantData.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
