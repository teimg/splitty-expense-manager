package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ExpenseRepository;

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
