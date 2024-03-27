package server.service;

import commons.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.ExpenseRepository;
import server.service.ExpenseService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private EventService eventService;


    @InjectMocks
    private ExpenseService expenseService;

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
    void saveExpenseTest() {
        when(expenseRepository.saveAndFlush(expense)).thenReturn(expense);
        Expense savedExpense = expenseService.saveExpense(expense);
        assertEquals(expense, savedExpense);
        verify(expenseRepository).saveAndFlush(expense);
    }

    @Test
    void getExpenseByIdTest() {
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
        Optional<Expense> foundExpense = expenseService.getExpenseById(expense.getId());
        assertTrue(foundExpense.isPresent());
        assertEquals(expense, foundExpense.get());
        verify(expenseRepository).findById(expense.getId());
    }

    @Test
    void getAllExpensesTest() {
        List<Expense> allExpenses = Arrays.asList(expense); // Assume multiple expenses
        when(expenseRepository.findAll()).thenReturn(allExpenses);
        List<Expense> foundExpenses = expenseService.getAllExpenses();
        assertEquals(allExpenses, foundExpenses);
        verify(expenseRepository).findAll();
    }

    @Test
    void getExpensesByEventIdTest() {
        List<Expense> eventExpenses = Arrays.asList(expense); // Assume multiple expenses for an event
        when(expenseRepository.findByEventId((long) event.getId())).thenReturn(eventExpenses);
        List<Expense> foundExpenses = expenseService.getExpensesByEventId((long) event.getId());
        assertEquals(eventExpenses, foundExpenses);
        verify(expenseRepository).findByEventId((long) event.getId());
    }

//    TODO: fix the test for the new version of the service
//    @Test
//    void deleteExpenseTest() {
//        doNothing().when(expenseRepository).deleteById(expense.getId());
//        expenseService.deleteExpense(expense.getId());
//        verify(expenseRepository).deleteById(expense.getId());
//    }
}
