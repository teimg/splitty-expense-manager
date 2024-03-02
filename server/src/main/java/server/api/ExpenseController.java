package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import commons.Expense;
import server.service.ExpenseService;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
    private final ExpenseService expenseService;

    /**
     * Constructor for expense controller
     *
     * @param expenseService expense repository
     */
    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * Creates a new expense object in the database.
     * @param expense
     * @return the created expense
     */
    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseService.saveExpense(expense);
        return ResponseEntity.ok(savedExpense);
    }

    /**
     * Retrieves all expenses from the database.
     * @return a list of all expenses
     */
    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    /**
     * Gets expense by expense ID.
     * @param id the ID of the expense to retrieve
     * @return the expense if found, or a {@link ResponseEntity} with not found status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing expense based on expense id
     * @param id the ID of the expense to update
     * @param expenseDetails the new details of expense
     * @return the updated expense if the operation was successful,
     * or a {@link ResponseEntity} with not found status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Expense>
    updateExpense(@PathVariable long id, @RequestBody Expense expenseDetails) {
        Optional<Expense> expenseData = expenseService.getExpenseById(id);
        if (expenseData.isPresent()) {
            Expense expense = expenseData.get();
            expense.setEvent(expenseDetails.getEvent());
            expense.setPurchase(expenseDetails.getPurchase());
            expense.setAmount(expenseDetails.getAmount());
            expense.setPayer(expenseDetails.getPayer());
            expense.setDebtors(expenseDetails.getDebtors());
            Expense updatedExpense = expenseService.saveExpense(expense);
            return ResponseEntity.ok(updatedExpense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an expense by expense ID.
     * @param id the ID of the expense to delete
     * @return a {@link ResponseEntity} indicating if deletion is successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable long id) {
        return expenseService.getExpenseById(id).map(expense -> {
            expenseService.deleteExpense(id);
            return ResponseEntity.ok().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}