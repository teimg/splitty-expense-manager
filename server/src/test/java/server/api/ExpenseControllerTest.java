package server.api;

import commons.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import server.service.ExpenseService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    private Expense expense;
    private Event event;
    private List<Participant> participants;
    private BankAccount mockAccount;

    @BeforeEach
    void setUp() {

        mockAccount = new BankAccount("NL62INGB088899966","6589743");
        participants = new ArrayList<>();
        participants.add(new Participant("David",mockAccount));
        participants.add(new Participant("Jason"));
        participants.add(new Participant("Shakira"));
        event = new Event("Jackson", "lfy2024",
                participants, new Date(2023,05,17),new Date(2023,05,26));
        expense = new Expense(event.getId(), "Lunch", 60.0, new Participant("Jackson"), participants, LocalDate.now(), new Tag());
    }
    @Test
    public void addExpenseTest() {
        when(expenseService.saveExpense(expense)).thenReturn(expense);
        ResponseEntity<Expense> response = expenseController.addExpense(expense);
        assertEquals(expense, response.getBody());
    }

    @Test
    public void getAllExpensesTest() {
        List<Expense> expenses = List.of(expense);
        when(expenseService.getAllExpenses()).thenReturn(expenses);
        List<Expense> response = expenseController.getAllExpenses();
        assertEquals(1, response.size());
        assertEquals(expense, response.get(0));
    }

    @Test
    public void getExpenseByIdTest() {
        when(expenseService.getExpenseById(1L)).thenReturn(Optional.of(expense));
        ResponseEntity<Expense> response = expenseController.getExpenseById(1L);
        assertEquals(expense, response.getBody());
    }

    @Test
    public void updateExpenseSuccessTest() {
        Expense updatedExpense = new Expense(null, "Dinner", 25.0, null, new ArrayList<>(), LocalDate.now(), null);
        when(expenseService.getExpenseById(1L)).thenReturn(Optional.of(expense));
        when(expenseService.saveExpense(expense)).thenReturn(updatedExpense);

        ResponseEntity<Expense> response = expenseController.updateExpense(1L, updatedExpense);
        assertEquals(updatedExpense.getAmount(), response.getBody().getAmount());
        assertEquals(updatedExpense.getPurchase(), response.getBody().getPurchase());
    }

    @Test
    public void updateExpenseFailTest() {
        Expense updatedExpense = new Expense(null, "Dinner", 25.0, null, new ArrayList<>(), LocalDate.now(), null);
        when(expenseService.getExpenseById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Expense> response = expenseController.updateExpense(1L, updatedExpense);
        assertEquals(ResponseEntity.notFound().build(), response);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    public void deleteExpenseTest() {
        when(expenseService.getExpenseById(1L)).thenReturn(Optional.of(expense));
        ResponseEntity<?> response = expenseController.deleteExpense(1L);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    public void testHandle() {
        try {
            expenseController.handle(new IllegalArgumentException());
        }
        catch (Exception ignored) {
            System.out.println("Check");
        }
    }

}
