package server.api;

import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private final EventRepository repo;

    /**
     * constructor for event controller
     * @param repo event repository
     */
    public EventController(EventRepository repo) {
        this.repo = repo;
    }

    /**
     * standard
     * @return all events
     */
    @GetMapping(path = { "", "/" })
    public List<Event> getAll() {
        return repo.findAll();
    }

    /**
     * Searches for an event by id and deletes if such event exists
     * @param id the ID of the event to be deleted
     * @return a {@link ResponseEntity} ok if event is found and deleted and notFound otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return repo.findById(id).map(event -> {
            repo.deleteById(id);
            return ResponseEntity.ok().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * updates an existing event with new details
     * @param id the id of the expense to be updated
     * @param newEvent the new event object that holds the values for the old event
     * to be updated with
     * @return the updated event if the operation was successful,
     * or a {@link ResponseEntity} with notFound status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> update(@PathVariable long id, @RequestBody Event newEvent) {
        Optional<Event> oldEvent = repo.findById(id);
        if (oldEvent.isPresent()) {
            Event e = oldEvent.get();
            e.setName(newEvent.getName());
            e.setCreationDate(newEvent.getCreationDate());
            e.setInviteCode(newEvent.getInviteCode());
            e.setLastActivity(newEvent.getLastActivity());

            Event updated = repo.save(e);
            return ResponseEntity.ok(updated);
        } else return ResponseEntity.notFound().build();
    }

    /**
     * updates an existing events last activity field
     * @param id the ID of the event for the last activity to be updated
     * @param newLastActivity the Date that the last activity should be updated to
     * @return the updated event if the operation was successful,
     * or a {@link ResponseEntity} with notFound status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event>
    updateLastActivity(@PathVariable long id, @RequestBody Date newLastActivity) {
        Optional<Event> event = repo.findById(id);
        if (event.isPresent()) {
            Event e = event.get();
            e.setLastActivity(newLastActivity);

            Event updated = repo.save(e);
            return ResponseEntity.ok(updated);
        } else return ResponseEntity.notFound().build();
    }
}
