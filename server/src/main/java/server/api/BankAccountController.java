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

    /**
     * Getter Method for all bank accounts
     * @return List of bank accounts
     */
    @GetMapping(path = { "", "/" })
    public List<BankAccount> getAll() {
        return repo.findAll();
    }

    /**
     * Getter Method for a specific bank account through id
     * @param id - id to be fetched
     * @return Bank Account
     */
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<BankAccount> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Post Method for Bank accounts (Adder)
     * @param bankAccount - To be Added
     * @return saved entity / badRequest in the case of failure
     */
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

    /**
     * Delete Method for Bank account by id
     * @param id - id to be deleted
     * @return deleted object / badRequest in case of failure
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<BankAccount> delete(@PathVariable long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        BankAccount deleted = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(deleted);
    }
    

    /**
     * Private utility method
     * @param s - string to be checked
     * @return boolean (true/false)
     */
    private static boolean isNullOrEmpty(String s) {
        return (s == null || s.isEmpty());
    }

}
