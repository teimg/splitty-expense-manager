package server.api;

import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.EventService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private final EventService service;

    /**
     * constructor for event controller
     * @param service event service
     */
    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            Event savedEvent = service.createEvent(event);
            return ResponseEntity.ok(savedEvent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * standard
     * @return all events
     */
    @GetMapping(path = { "", "/" })
    public List<Event> getAll() {
        return service.getAll();
    }

    @GetMapping("/byInviteCode/{inviteCode}")
    public ResponseEntity<Event> getByInviteCode(@PathVariable String inviteCode) {
        return service.getByInviteCode(inviteCode)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Searches for an event by id and deletes if such event exists
     * @param id the ID of the event to be deleted
     * @return a {@link ResponseEntity} ok if event is found and deleted and notFound otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return service.getById(id).map(event -> {
            service.delete(id);
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
    @PutMapping("/update/{id}")
    public ResponseEntity<Event> update(@PathVariable long id, @RequestBody Event newEvent) {
        Optional<Event> oldEvent = service.getById(id);
        if (oldEvent.isPresent()) {
            Event e = oldEvent.get();
            e.setName(newEvent.getName());
            e.setCreationDate(newEvent.getCreationDate());
            e.setInviteCode(newEvent.getInviteCode());
            e.setLastActivity(newEvent.getLastActivity());

            Event updated = service.save(e);
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
    @PutMapping("/update/lastactivity/{id}")
    public ResponseEntity<Event>
    updateLastActivity(@PathVariable long id, @RequestBody Date newLastActivity) {
        Optional<Event> event = service.getById(id);
        if (event.isPresent()) {
            Event e = event.get();
            e.setLastActivity(newLastActivity);

            Event updated = service.save(e);
            return ResponseEntity.ok(updated);
        } else return ResponseEntity.notFound().build();
    }
}
