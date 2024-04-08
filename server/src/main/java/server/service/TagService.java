package server.service;

import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository repo;

    private final EventService eventService;

    @Autowired
    public TagService(TagRepository tagRepository, EventService eventService) {
        this.repo = tagRepository;
        this.eventService = eventService;
    }

    public List<Tag> getAll() {
        return repo.findAll();
    }

    public Optional<Tag> getById(long id) {
        return repo.findById(id);
    }

    public boolean exists(long id) {
        return repo.existsById(id);
    }

    // TODO: update last activity of the event
    public Tag save(Tag tag) {
        eventService.updateLastActivity(tag.getEventId());
        return repo.saveAndFlush(tag);
    }

    public Tag restore(Tag tag) {
        return repo.saveAndFlush(tag);
    }

    public Optional<Tag> remove(long id) {
        Optional<Tag> tag = getById(id);
        if (tag.isPresent()) {
            repo.deleteById(id);
            eventService.updateLastActivity(tag.get().getEventId());
        }
        return tag;
    }

}
