package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.service.BankAccountService;

@ExtendWith(MockitoExtension.class)
public class BankAccountControllerTest {

    @Mock
    private BankAccountService service;

    @InjectMocks
    BankAccountController controller;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void getAllTest() {

    }

    @Test
    public void getByIdTest() {

    }

    @Test
    public void addTest() {

    }

    @Test
    public void deleteTest() {

    }

    @Test
    public void updateTest() {

    }

    @Test
    public void nullOrEmptyTest() {

    }

}