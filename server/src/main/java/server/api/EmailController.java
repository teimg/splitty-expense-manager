package server.api;

import commons.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService service;

    @Autowired
    public EmailController(EmailService emailService) {
        this.service = emailService;
    }

    @PostMapping
    public ResponseEntity<EmailRequest> sendEmail(@RequestBody EmailRequest request) {
        service.sendEmail(request.getTo(), request.getSubject(), request.getBody());
        return ResponseEntity.ok(request);
    }

    @GetMapping
    public EmailRequest getAll() {
        return service.getAll();
    }

}
