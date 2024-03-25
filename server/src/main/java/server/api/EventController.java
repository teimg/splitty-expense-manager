package server.api;

import commons.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.service.EventService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventService service;
    private final SimpMessagingTemplate msgs;

    /**
     * constructor for event controller
     *
     * @param service event service
     * @param msgs
     */
    public EventController(EventService service, SimpMessagingTemplate msgs) {
        this.service = service;
        this.msgs = msgs;
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
     * Checks for updates to an event by its ID.
     * @param id The ID of the event.
     * @return ResponseEntity with the updated event if there's an update, or an empty body if not.
     */
    @GetMapping("/checkUpdates/{id}")
    public ResponseEntity<Event> checkForUpdates(@PathVariable long id) {
        Optional<Event> currentEvent = service.getById(id);
        if (currentEvent.isPresent()) {
            Event event = currentEvent.get();
            // Example logic: return the event if it has been updated within the last 5 minutes.
            long lastUpdateTime = event.getLastActivity().getTime();
            long now = new Date().getTime();
            if (now - lastUpdateTime < 300000) { // 5 minutes in milliseconds
                return ResponseEntity.ok(event);
            } else {
                return ResponseEntity.noContent().build();
            }
        } else {
            return ResponseEntity.notFound().build();
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

    /**
     * Get by ID
     * @param id - id
     * @return event with ID
     */
    @GetMapping(path = { "/{id}" })
    public ResponseEntity<Event> getByID(@PathVariable long id) {
        return service.getById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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

            Event updated = service.update(e);

            return ResponseEntity.ok(updated);
        } else return ResponseEntity.notFound().build();
    }
}
