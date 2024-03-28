package server.api;

import commons.Participant;
import commons.PasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.PasswordSupplier;
import server.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/auth")
    public ResponseEntity<Void> validatePassword (@RequestBody PasswordRequest password){
        if (adminService.validatePassword(password.getPassword())){
            return ResponseEntity.ok().build();
        }

        return  ResponseEntity.status(403).build();
    }
}
