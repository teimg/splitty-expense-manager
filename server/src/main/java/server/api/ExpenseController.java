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

    
}
