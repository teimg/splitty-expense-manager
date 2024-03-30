package server.api;

import commons.PasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import server.service.AdminService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    AdminController adminController;

    @Mock
    AdminService mockAdminService;

    @BeforeEach
    void setup(){
        adminController = new AdminController(mockAdminService);
    }

    @Test
    void validatePassword() {
        Mockito.when(mockAdminService.validatePassword("Password"))
                .thenReturn(true);
        assertEquals( ResponseEntity.ok().build(), adminController.validatePassword(new PasswordRequest("Password")));
    }

    @Test
    void validateWrongPassword() {
        Mockito.when(mockAdminService.validatePassword("WrongPassword"))
            .thenReturn(false);
        assertEquals( ResponseEntity.status(403).build(), adminController.validatePassword(new PasswordRequest("WrongPassword")));
    }
}