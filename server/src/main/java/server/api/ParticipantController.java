package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ParticipantRepository;

@RestController
@RequestMapping("/api/participant")
public class ParticipantController {

    @Autowired
    private final ParticipantRepository repo;

    /**
     * constructor for participant controller
     * @param repo participant repository
     */
    public ParticipantController(ParticipantRepository repo) {
        this.repo = repo;
    }
}
