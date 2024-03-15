package server.service;

import commons.Event;
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

    @Autowired
    public EventService(EventRepository repo) {
        this.repo = repo;
    }

    public Event save(Event event) {
        return repo.saveAndFlush(event);
    }

    public Event createEvent(Event event) {
        if (isNullOrEmpty(event.getName())) {
            throw new IllegalArgumentException();
        }
        event.setCreationDate(new Date());
        event.setLastActivity(event.getCreationDate());
        event = repo.save(event);
        event.setInviteCode(generateInviteCode(event.getId()));
        event = repo.save(event);
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
        repo.deleteById(id);
    }

}
