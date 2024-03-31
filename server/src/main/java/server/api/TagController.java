package server.api;

import commons.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.TagService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService service;

    @Autowired
    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping(path = { "", "/" })
    public List<Tag> getAll() {
        return service.getAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<Tag> getById(@PathVariable("id") long id) {
        if (id >= 0) {
            Optional<Tag> tag = service.getById(id);
            if (tag.isPresent()) {
                return ResponseEntity.ok(tag.get());
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {

        if (tag == null
                || isNullOrEmpty(tag.getName())) {
            return ResponseEntity.badRequest().build();
        }

        Tag saved = service.save(tag);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Tag> delete(@PathVariable long id) {
        if (id >= 0) {
            Optional<Tag> deleted = service.remove(id);
            if (deleted.isPresent()) {
                return ResponseEntity.ok(deleted.get());
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping(path = {"/{id}"})
    public ResponseEntity<Tag> update(@PathVariable long id,
                                              @RequestBody Tag tag) {
        if (id >= 0) {
            Optional<Tag> gotTag = service.getById(id);
            if (gotTag.isPresent()) {
                Tag toBeUpdated = gotTag.get();
                toBeUpdated.setName(tag.getName());
                toBeUpdated.setRed(tag.getRed());
                toBeUpdated.setGreen(tag.getGreen());
                toBeUpdated.setBlue(tag.getBlue());
                Tag updated = service.save(toBeUpdated);
                return ResponseEntity.ok(updated);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(path = {"/saveOrUpdate"})
    public ResponseEntity<Tag> saveOrUpdate(@RequestBody Tag tag) {
        List<Tag> tags = getAll();
        Tag existingTag = null;
        for (Tag standTag : tags) {
            if (standTag.standTagEquals(tag)) {
                existingTag = standTag;
                break;
            }
        }
        if (existingTag != null) {
            tag.setId(existingTag.getId());
            return update(tag.getId(), tag);
        } else {
            return add(tag);
        }
    }

    /**
     * Utility method
     * @param s - string to be checked
     * @return boolean (true/false)
     */
    public static boolean isNullOrEmpty(String s) {
        return (s == null || s.isEmpty());
    }

}
