package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ExpenseRepository;
import commons.Expense;

import java.util.List;
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
}
