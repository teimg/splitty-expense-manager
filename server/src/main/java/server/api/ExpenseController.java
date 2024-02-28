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

    /**
     * constructor for expense controller
     * @param repo expense repository
     */
    public ExpenseController(ExpenseRepository repo) {
        this.repo = repo;
    }
}
