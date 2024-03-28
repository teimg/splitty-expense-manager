package server.api;

import commons.Event;
import commons.EventChange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.service.EventChangeService;
import server.service.EventService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventService service;
    private final EventChangeService eventChangeService;
    private final SimpMessagingTemplate msgs;

    /**
     * constructor for event controller
     *
     * @param service            event service
     * @param eventChangeService
     * @param msgs
     */
    public EventController(EventService service, EventChangeService eventChangeService,
                           SimpMessagingTemplate msgs) {
        this.service = service;
        this.eventChangeService = eventChangeService;
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

    @GetMapping("/checkUpdates/{id}")
    public DeferredResult<ResponseEntity<EventChange>> checkForUpdates(@PathVariable long id) {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<EventChange>>(5000L, noContent);

        eventChangeService.addLongPoll(id, res);
        res.onCompletion(()->{
            eventChangeService.removeLongPoll(id, res);
        });

        return res;
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
