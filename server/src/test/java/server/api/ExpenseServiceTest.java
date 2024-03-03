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
import server.database.ExpenseRepository;
import server.service.ExpenseService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;

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
        event = new Event(101, "Jackson", "lfy2024",
                participants, new Date(2023,05,17),new Date(2023,05,26));
        expense = new Expense(10086, event, "Lunch", 60.0, new Participant("Jackson"), participants, LocalDate.now());
    }

    @Test
    void saveExpenseTest() {
        when(expenseRepository.saveAndFlush(expense)).thenReturn(expense);
        Expense savedExpense = expenseService.saveExpense(expense);
        assertEquals(expense, savedExpense);
        verify(expenseRepository).saveAndFlush(expense);
    }
}
