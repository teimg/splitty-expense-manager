package server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import server.suppliers.PasswordSupplier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    AdminService adminService;

    @Mock
    PasswordSupplier mockPasswordSupplier;
    @BeforeEach
    void setup(){
        adminService = new AdminService(mockPasswordSupplier);
    }
    @Test
    void validatePassword() {
        Mockito.when(mockPasswordSupplier.getPassword())
            .thenReturn("password");

        assertTrue(adminService.validatePassword("password"));
    }

    @Test
    void validateWrongPassword() {
        Mockito.when(mockPasswordSupplier.getPassword())
            .thenReturn("password");

        assertFalse(adminService.validatePassword("Wrong"));
    }
}