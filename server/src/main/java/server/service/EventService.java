package server.service;

import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;

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

    public Optional<Event> getById(Long id) {
        return repo.findById(id);
    }

    public List<Event> getAll() {
        return repo.findAll();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

}
