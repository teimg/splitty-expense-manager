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

    @GetMapping(path = {"/personId"})
    public ResponseEntity<BankAccount> getById(@PathVariable("personId") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<BankAccount> add(@RequestBody BankAccount bankAccount) {

        if (bankAccount == null
                || isNullOrEmpty(bankAccount.getBic())
                || isNullOrEmpty(bankAccount.getIban())) {
            return ResponseEntity.badRequest().build();
        }

        BankAccount saved = repo.save(bankAccount);
        return ResponseEntity.ok(saved);
    }


    private static boolean isNullOrEmpty(String s) {
        return (s == null || s.isEmpty());
    }

}
