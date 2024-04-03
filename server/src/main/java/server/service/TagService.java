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

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.repo = tagRepository;
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

    public Tag save(Tag tag) {
        return repo.saveAndFlush(tag);
    }

    public Optional<Tag> remove(long id) {
        Optional<Tag> tag = getById(id);
        if (tag.isPresent()) {
            repo.deleteById(id);
        }
        return tag;
    }

}
