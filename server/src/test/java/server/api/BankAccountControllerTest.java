package server.api;

import commons.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import server.service.BankAccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountControllerTest {

    @Mock
    private BankAccountService service;

    @InjectMocks
    BankAccountController controller;

    private BankAccount b1;
    private BankAccount b2;
    private BankAccount b3;


    @BeforeEach
    public void setUp() {
        this.b1 = new BankAccount("NL12ABNA345678910", "HBUKGB4B");
        this.b2 = new BankAccount("NL12ABNA345678910", "HBUDGB4B");
        this.b3 = new BankAccount("BE12ABNA345678910", "HBUKGB4B");
    }

    @Test
    public void getAllTest() {
        when(service.getAll()).thenReturn(new ArrayList<>());
        assertEquals(new ArrayList<>(), controller.getAll());
        assertEquals(0, controller.getAll().size());
        when(service.getAll()).thenReturn(List.of(b1, b2, b3));
        assertTrue(controller.getAll().containsAll(List.of(b1, b2, b3)));
    }

    @Test
    public void getByIdTest() {
        when(service.getById(1L)).thenReturn(Optional.of(b1));
        ResponseEntity<BankAccount> found = controller.getById(1L);
        verify(service).getById(1L);
        assertEquals(HttpStatusCode.valueOf(200), found.getStatusCode());
        assertEquals(b1, found.getBody());
        ResponseEntity<BankAccount> notFound = controller.getById(-1L);
        assertEquals(ResponseEntity.badRequest().build(), notFound);
        assertEquals(HttpStatusCode.valueOf(400), notFound.getStatusCode());
        assertNull(notFound.getBody());
    }

    @Test
    public void addTest() {
        when(service.save(b2)).thenReturn(
                new BankAccount("NL12ABNA345678910", "HBUDGB4B"));
        BankAccount savedAccount = controller.add(b2).getBody();
        assertEquals(b2, savedAccount);
        assertNotSame(b2, savedAccount);
        ResponseEntity<BankAccount> notFound = controller.add(null);
        assertEquals(ResponseEntity.badRequest().build(), notFound);
        assertEquals(HttpStatusCode.valueOf(400), notFound.getStatusCode());
    }

    @Test
    public void deleteTest() {
        when(service.remove(2L)).thenReturn(Optional.of(b2));
        ResponseEntity<BankAccount> ba2 = controller.delete(2L);
        assertEquals(HttpStatusCode.valueOf(200), ba2.getStatusCode());
        ResponseEntity<BankAccount> notFound = controller.delete(-1L);
        assertEquals(ResponseEntity.badRequest().build(), notFound);
        assertEquals(HttpStatusCode.valueOf(400), notFound.getStatusCode());
    }

    @Test
    public void updateTest() {
        BankAccount updatedAccount = new BankAccount("Updated", "Updated");
        when(service.getById(1L)).thenReturn(Optional.of(b1));
        when(service.save(b1)).thenReturn(updatedAccount);
        ResponseEntity<BankAccount> ba1 = controller.update(1L, new BankAccount("Test", "Test"));
        verify(service).getById(1L);
        verify(service).save(b1);
        assertEquals(updatedAccount.getBic(), ba1.getBody().getBic());
        assertEquals(updatedAccount.getIban(), ba1.getBody().getIban());
        assertEquals(HttpStatusCode.valueOf(200), ba1.getStatusCode());
        ResponseEntity<BankAccount> ba2 = controller.update(-1L, new BankAccount("Test", "Test"));
        assertEquals(ba2, ResponseEntity.badRequest().build());
        assertEquals(HttpStatusCode.valueOf(400), ba2.getStatusCode());
    }

    @Test
    public void nullOrEmptyTest() {
        String n = null;
        String e = "";
        String f = "full";
        assertTrue(BankAccountController.isNullOrEmpty(n));
        assertTrue(BankAccountController.isNullOrEmpty(e));
        assertFalse(BankAccountController.isNullOrEmpty(f));
    }

}