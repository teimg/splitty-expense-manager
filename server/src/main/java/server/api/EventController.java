package server.api;

import commons.Event;
import commons.Participant;
import commons.Tag;
import commons.EventChange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.service.EventChangeService;
import server.service.EventService;
import server.service.ExpenseService;
import server.service.ParticipantService;
import server.service.TagService;

import java.util.*;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;
    private final ParticipantService participantService;
    private final ExpenseService expenseService;
    private final TagService tagService;
    private final EventChangeService eventChangeService;

    /**
     * constructor for event controller
     *
     * @param eventService       event service
     * @param participantService -
     * @param expenseService     -
     * @param tagService         -
     * @param eventChangeService -
     */
    public EventController(EventService eventService,
                           ParticipantService participantService,
                           ExpenseService expenseService,
                           TagService tagService, EventChangeService eventChangeService) {
        this.eventService = eventService;
        this.participantService = participantService;
        this.expenseService = expenseService;
        this.tagService = tagService;
        this.eventChangeService = eventChangeService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            Event savedEvent = eventService.createEvent(event);
            return ResponseEntity.ok(savedEvent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/restoreEvent")
    public ResponseEntity<Event> restoreEvent(@RequestBody Event event) {
        if (eventService.existsByInviteCode(event.getInviteCode())) {
            return ResponseEntity.badRequest().build();
        }

        var participants = event.getParticipants();
        event.setParticipants(null);
        var tags = event.getTags();
        event.setTags(null);
        var expenses = event.getExpenses();
        event.setExpenses(null);

        Event savedEvent = eventService.save(event);

        Map<Long, Participant> newParticipants = new HashMap<>();

        participants.replaceAll(participant -> {
            participant.setEventId(savedEvent.getId());
            Long oldId = participant.getId();
            Participant newParticipant = participantService.restore(participant);
            newParticipants.put(oldId, newParticipant);
            return newParticipant;
        });

        Map<Long, Tag> newTags = new HashMap<>();
        tags.replaceAll(tag -> {
            tag.setEventId(savedEvent.getId());
            Long oldId = tag.getId();
            Tag newTag = tagService.restore(tag);
            newTags.put(oldId, newTag);
            return newTag;
        });

        expenses.replaceAll(expense -> {
            expense.setEventId(savedEvent.getId());
            expense.setTag(newTags.get(expense.getTag().getId()));
            expense.setPayer(newParticipants.get(expense.getPayer().getId()));
            expense.getDebtors().replaceAll(debtor -> newParticipants.get(debtor.getId()));
            return expenseService.restore(expense);
        });

        savedEvent.setParticipants(participants);
        savedEvent.setTags(tags);
        savedEvent.setExpenses(expenses);

        Event restoredEvent = eventService.restore(savedEvent);

        return ResponseEntity.ok(restoredEvent);
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

    @PutMapping("/rename/{id}")
    public ResponseEntity<Event> renameEvent(@PathVariable long id, @RequestBody String newName) {
        if(newName == null || newName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Return 400 Bad Request for invalid names
        }

        return eventService.getById(id)
                .map(event -> {
                    event.setName(newName.trim());
                    Event updatedEvent = eventService.update(event);
                    return ResponseEntity.ok(updatedEvent);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
        // Return 404 Not Found if the event doesn't exist
    }
    /**
     * standard
     * @return all events
     */
    @GetMapping(path = { "", "/" })
    public List<Event> getAll() {
        return eventService.getAll();
    }

    /**
     * Get by ID
     * @param id - id
     * @return event with ID
     */
    @GetMapping(path = { "/{id}" })
    public ResponseEntity<Event> getByID(@PathVariable long id) {
        return eventService.getById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/byInviteCode/{inviteCode}")
    public ResponseEntity<Event> getByInviteCode(@PathVariable String inviteCode) {
        return eventService.getByInviteCode(inviteCode)
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
        return eventService.getById(id).map(event -> {
            eventService.delete(id);
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
        Optional<Event> oldEvent = eventService.getById(id);
        if (oldEvent.isPresent()) {
            Event e = oldEvent.get();
            e.setName(newEvent.getName());
            e.setCreationDate(newEvent.getCreationDate());
            e.setInviteCode(newEvent.getInviteCode());

            Event updated = eventService.update(e);

            return ResponseEntity.ok(updated);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
