package server.service;

import commons.Debt;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.DebtRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DebtServiceTest {

    @Mock
    private DebtRepository repository;

    @InjectMocks
    private DebtService service;

    private Debt d1;
    private Debt d2;
    private Participant creditor;
    private Participant debtor;

    @BeforeEach
    public void setUp() {
        creditor = new Participant("Creditor Name", "creditor@example.com");
        debtor = new Participant("Debtor Name", "debtor@example.com");
        d1 = new Debt(creditor, debtor, 100.0, false, "Loan from Creditor", "Initial loan amount");
        d2 = new Debt(debtor, creditor, 150.0, true, "Repayment to Creditor", "Partial repayment");
    }

    @Test
    public void getAllTest() {
        when(repository.findAll()).thenReturn(List.of(d1, d2));
        List<Debt> allDebts = service.getAll();
        assertNotNull(allDebts);
        assertFalse(allDebts.isEmpty());
        assertEquals(2, allDebts.size());
        assertTrue(allDebts.containsAll(List.of(d1, d2)));
    }

    @Test
    public void getByIdTest() {
        when(repository.findById(1L)).thenReturn(Optional.of(d1));
        Debt found = service.getById(1L);
        assertNotNull(found);
        assertEquals(d1, found);
    }

    @Test
    public void createDebtTest() {
        when(repository.save(any(Debt.class))).thenReturn(d1);
        Debt created = service.createDebt(d1);
        assertNotNull(created);
        assertEquals(d1.getAmount(), created.getAmount());
        assertEquals(d1.getCreditor(), created.getCreditor());
    }

    @Test
    public void updateDebtTest() {
        Debt updatedInfo = new Debt(creditor, debtor, 200.0, true, "Updated Loan", "Loan has been fully paid");
        when(repository.findById(1L)).thenReturn(Optional.of(d1));
        when(repository.save(any(Debt.class))).thenReturn(updatedInfo);
        Debt updated = service.updateDebt(1L, updatedInfo);
        assertNotNull(updated);
        assertEquals(updatedInfo.getAmount(), updated.getAmount());
        assertEquals(updatedInfo.isHasPaid(), updated.isHasPaid());
        assertEquals("Updated Loan", updated.getSummary());
    }


    @Test
    public void getNonExistentDebtTest() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.getById(999L));
    }

}
