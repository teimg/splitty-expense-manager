package server.api;

import commons.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.MockExpenseRepository;
import server.service.ExpenseService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpenseServiceTest {
    private ExpenseService expenseService;
    private MockExpenseRepository mockExpenseRepository;

    @BeforeEach
    void setUp() {
        mockExpenseRepository = new MockExpenseRepository();
        expenseService = new ExpenseService(mockExpenseRepository);
    }

    @Test
    void testSaveExpense() {
        Expense expense = new Expense(0, null, "Lunch", 20.0, null, List.of(), LocalDate.now());
        Expense savedExpense = expenseService.saveExpense(expense);

        assertThat(savedExpense.getId()).isGreaterThan(0);
        assertThat(savedExpense.getPurchase()).isEqualTo("Lunch");
    }

    @Test
    void testGetExpenseById() {
        Expense expense = new Expense(0, null, "Coffee", 5.0, null, List.of(), LocalDate.now());
        Expense savedExpense = expenseService.saveExpense(expense);

        Expense foundExpense = expenseService.getExpenseById(savedExpense.getId()).orElse(null);

        assertThat(foundExpense).isNotNull();
        assertThat(foundExpense.getPurchase()).isEqualTo("Coffee");
    }

    @Test
    void testDeleteExpense() {
        Expense expense = new Expense(0, null, "Snacks", 10.0, null, List.of(), LocalDate.now());
        Expense savedExpense = expenseService.saveExpense(expense);

        expenseService.deleteExpense(savedExpense.getId());
        Expense foundExpense = expenseService.getExpenseById(savedExpense.getId()).orElse(null);

        assertThat(foundExpense).isNull();
    }

    // Add more tests as needed to cover the functionality of ExpenseService
}
