package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.EventRepository;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private final EventRepository repo;

    /**
     * constructor for event controller
     * @param repo event repository
     */
    public EventController(EventRepository repo) {
        this.repo = repo;
    }
}
