package server.api;

import commons.PasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;

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
