package server.api;

import commons.BankAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BankAccountRepository;

import java.util.List;

@RestController
@RequestMapping("/api/bankAccounts")
public class BankAccountController {

    private final BankAccountRepository repo;

    public BankAccountController(BankAccountRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<BankAccount> getAll() {
        return repo.findAll();
    }

    @GetMapping(path = {"{/personId}"})
    public ResponseEntity<BankAccount> getById(@PathVariable("personId") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    private static boolean isNullOrEmpty(String s) {
        return (s == null || s.isEmpty());
    }

}
