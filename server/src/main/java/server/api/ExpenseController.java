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
     * @param repo expense repository
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
     * Deletes an expense by its ID.
     * @param id the ID of the expense to delete
     * @return a {@link ResponseEntity} indicating the operation result
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable long id) {
        return expenseService.findById(id).map(expense -> {
            expenseService.deleteById(id);
            return ResponseEntity.ok().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Filters expenses based on provided criteria.
     * @param eventId optional filter by event ID
     * @return a list of expenses that match the filtering criteria
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Expense>> getExpensesByCriteria(
            @RequestParam(value = "eventId", required = false) Long eventId) {
        List<Expense> expenses;
        if (eventId != null) {
            expenses = expenseService.findByEventId(eventId);
        } else {
            expenses = expenseService.findAll();
        }
        return ResponseEntity.ok(expenses);
    }

    /**
     * Performs a partial update on an expense, updating only specified fields.
     * @param id the ID of the expense to update
     * @param updates a map of the fields to update and their new values
     * @return the updated expense if found and updated,
     * or a {@link ResponseEntity} with not found status
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Expense>
    partialUpdateExpense(@PathVariable long id, @RequestBody Map<String, Object> updates) {
        Optional<Expense> expenseData = expenseService.findById(id);
        if (expenseData.isPresent()) {
            Expense expense = expenseData.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "event":
                        // Assuming Event is passed by ID or another simple identifier
                        // You need to fetch the Event entity and set it here
                        break;
                    case "purchase":
                        expense.setPurchase((String) value);
                        break;
                    case "amount":
                        expense.setAmount((Double) value);
                        break;
                    case "payer":
                        // Similar to "event", fetch and set the payer Participant entity
                        break;
                    case "debtors":
                        // Handle updating the list of debtors appropriately
                        break;
                    // Add more case statements as needed
                }
            });
            Expense updatedExpense = expenseService.save(expense);
            return ResponseEntity.ok(updatedExpense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
