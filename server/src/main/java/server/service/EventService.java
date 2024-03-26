package server.service;

import commons.Event;
import commons.event.changes.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository repo;
    private final EventChangeService eventChangeService;

    @Autowired
    public EventService(EventRepository repo, EventChangeService eventChangeService) {
        this.repo = repo;
        this.eventChangeService = eventChangeService;
    }

    private Event save(Event event) {
        return repo.saveAndFlush(event);
    }

    public Event createEvent(Event event) {
        if (isNullOrEmpty(event.getName())) {
            throw new IllegalArgumentException();
        }
        event.setCreationDate(new Date());
        event.setLastActivity(event.getCreationDate());
        event = save(event);
        event.setInviteCode(generateInviteCode(event.getId()));
        event = save(event);
        eventChangeService.sendChange(new EventCreated(event));
        return event;
    }

    public Event update(Event event) {
        event.setLastActivity(new Date());
        event = save(event);
        eventChangeService.sendChange(new EventModified(event));
        return event;
    }

    public String generateInviteCode(long id) {
        String randomPart = RandomStringUtils.randomAlphanumeric(4);
        String idPart = String.format("%04x", id);
        return randomPart + idPart;
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public Optional<Event> getById(Long id) {
        return repo.findById(id);
    }

    public List<Event> getAll() {
        return repo.findAll();
    }

    public Optional<Event> getByInviteCode(String inviteCode) {
        return repo.findByInviteCode(inviteCode);
    }

    public void delete(Long id) {
        Optional<Event> getRes = getById(id);
        if (getRes.isEmpty()) {
            return;
        }
        Event event = getRes.get();
        eventChangeService.sendChange(new EventDeleted(event));
        repo.delete(event);
    }

    public void updateLastActivity(Long id) {
        Optional<Event> getRes = getById(id);
        if (getRes.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Event event = getRes.get();
        update(event);
    }
}
