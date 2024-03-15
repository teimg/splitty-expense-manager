package server.api;

import commons.Debt;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import server.service.DebtService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DebtControllerTest {

    @Mock
    private DebtService service;

    @InjectMocks
    private DebtController controller;

    private Debt d1;
    private Debt d2;
    private Participant creditor;
    private Participant debtor;

    @BeforeEach
    public void setUp() {
        creditor = new Participant("Creditor");
        debtor = new Participant("Debtor");
        d1 = new Debt(creditor, debtor, 100.0, false, "Loan", "Personal loan");
        d2 = new Debt(debtor, creditor, 200.0, true, "Repayment", "Repayment of loan");
    }

    @Test
    public void getAllTest() {
        when(service.getAll()).thenReturn(List.of(d1, d2));
        List<Debt> debts = controller.getAll();
        assertNotNull(debts);
        assertEquals(2, debts.size());
        assertTrue(debts.containsAll(List.of(d1, d2)));
        verify(service, times(1)).getAll();
    }

    @Test
    public void getByIdTest() {
        when(service.getById(1L)).thenReturn(d1);
        ResponseEntity<Debt> response = controller.getById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(d1, response.getBody());

        verify(service, times(1)).getById(1L);
    }

    @Test
    public void createDebtTest() {
        when(service.createDebt(any(Debt.class))).thenReturn(d1);
        ResponseEntity<Debt> response = controller.createDebt(d1);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(d1, response.getBody());

        verify(service, times(1)).createDebt(any(Debt.class));
    }

    @Test
    public void updateDebtTest() {
        when(service.updateDebt(eq(1L), any(Debt.class))).thenReturn(d2);
        ResponseEntity<Debt> response = controller.updateDebt(1L, d2);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(d2, response.getBody());

        verify(service, times(1)).updateDebt(eq(1L), any(Debt.class));
    }

    @Test
    public void deleteDebtTest() {
        doNothing().when(service).deleteDebt(1L);
        ResponseEntity<?> response = controller.deleteDebt(1L);
        assertEquals(200, response.getStatusCodeValue());

        verify(service, times(1)).deleteDebt(1L);
    }

    @Test
    public void createDebtWithInvalidDataTest() {
        Debt invalidDebt = new Debt(null, null, -1, false, "", "");
        when(service.createDebt(invalidDebt)).thenThrow(new IllegalArgumentException("Invalid debt data"));
        assertThrows(IllegalArgumentException.class, () -> controller.createDebt(invalidDebt));

        verify(service, times(1)).createDebt(invalidDebt);
    }
}
