package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ExpenseRepository;
import commons.Expense;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
    @Autowired
    private final ExpenseRepository repo;

    @Autowired
    public ExpenseController(ExpenseRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Expense createExpense(@RequestBody Expense expense) {
        return repo.save(expense);
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable long id) {
        Optional<Expense> expense = repo.findById(id);
        return expense.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable long id, @RequestBody Expense expenseDetails) {
        Optional<Expense> expenseData = repo.findById(id);
        if (expenseData.isPresent()) {
            Expense expense = expenseData.get();
            expense.setEvent(expenseDetails.getEvent());
            expense.setPurchase(expenseDetails.getPurchase());
            expense.setAmount(expenseDetails.getAmount());
            expense.setPayer(expenseDetails.getPayer());
            expense.setDebtors(expenseDetails.getDebtors());
            Expense updatedExpense = repo.save(expense);
            return ResponseEntity.ok(updatedExpense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable long id) {
        return repo.findById(id).map(expense -> {
            repo.deleteById(id);
            return ResponseEntity.ok().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Expense>> getExpensesByCriteria(
            @RequestParam(value = "eventId", required = false) Long eventId) {
        List<Expense> expenses;
        if (eventId != null) {
            expenses = repo.findByEventId(eventId);
        } else {
            expenses = repo.findAll();
        }
        return ResponseEntity.ok(expenses);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Expense> partialUpdateExpense(@PathVariable long id, @RequestBody Map<String, Object> updates) {
        Optional<Expense> expenseData = repo.findById(id);
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
            Expense updatedExpense = repo.save(expense);
            return ResponseEntity.ok(updatedExpense);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
