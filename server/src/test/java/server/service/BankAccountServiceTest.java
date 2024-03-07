package server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import commons.BankAccount;
import server.database.BankAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository repository;

    @InjectMocks
    private BankAccountService service;

    private BankAccount b1;
    private BankAccount b2;
    private BankAccount b3;

    @BeforeEach
    public void setUp() {
        BankAccount bankAccountOne = new BankAccount("NL12ABNA345678910", "HBUKGB4B");
        BankAccount bankAccountTwo = new BankAccount("NL12ABNA345678910", "HBUDGB4B");
        BankAccount bankAccountThree = new BankAccount("BE12ABNA345678910", "HBUKGB4B");
        this.b1 = bankAccountOne;
        this.b2 = bankAccountTwo;
        this.b3 = bankAccountThree;
    }

    @Test
    public void getAllTest() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(0, service.getAll().size());
        when(repository.findAll()).thenReturn(List.of(b2, b3, b1));
        assertTrue(service.getAll().containsAll(List.of(b1, b2, b3)));
    }

    @Test
    public void getByIdTest() {
        when(repository.findById(3L)).thenReturn(Optional.of(b3));
        Optional<BankAccount> gotten = service.getById(3L);
        assertTrue(gotten.isPresent());
        assertEquals(b3, gotten.get());
    }

    @Test
    public void existsTest() {
        when(repository.existsById(2L)).thenReturn(false);
        when(repository.existsById(3L)).thenReturn(true);
        boolean isThere3 = service.exists(3L);
        boolean isThere2 = service.exists(2L);
        assertTrue(isThere3);
        assertFalse(isThere2);
    }

    @Test
    public void saveTest() {
        when(repository.saveAndFlush(b3)).thenReturn(new BankAccount("BE12ABNA345678910", "HBUKGB4B"));
        BankAccount saved = service.save(b3);
        assertEquals(b3, saved);
        assertNotSame(b3, saved);
    }

    @Test
    public void removeTest() {
        when(repository.findById(3L)).thenReturn(Optional.of(b3));
        when(repository.findById(2L)).thenReturn(Optional.empty());
        Optional<BankAccount> rem3 = service.remove(3L);
        Optional<BankAccount> rem2 = service.remove(2L);
        verify(repository, times(1)).deleteById(3L);
        verify(repository, times(0)).deleteById(2L);
        assertTrue(rem3.isPresent());
        assertEquals(b3, rem3.get());
        assertTrue(rem2.isEmpty());
    }
}
