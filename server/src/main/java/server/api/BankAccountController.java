package server.api;

import commons.BankAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.BankAccountRepository;

import java.util.List;
import java.util.Optional;

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
        Optional<BankAccount> bankAccount = repo.findById(id);
        return bankAccount.map(ResponseEntity::ok).orElseGet(()
                -> ResponseEntity.badRequest().build());
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
        Optional<BankAccount> deleted = repo.findById(id);
        repo.deleteById(id);
        return deleted.map(ResponseEntity::ok).orElseGet(()
                -> ResponseEntity.badRequest().build());
    }

    /**
     * Update method for Bank accounts based on id
     * @param id - id of bank account ot be updated
     * @param bankAccount - new Bank Account object
     * @return - Updated bank account or not found in case of failure.
     */
    @PutMapping(path = {"/{id}"})
    public ResponseEntity<BankAccount> update(@PathVariable long id,
                                              @RequestBody BankAccount bankAccount) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<BankAccount> bankAccountToBeUpdated = repo.findById(id);
        if (bankAccountToBeUpdated.isPresent()) {
            BankAccount bank = bankAccountToBeUpdated.get();
            bank.setBic(bankAccount.getBic());
            bank.setIban(bankAccount.getIban());
            BankAccount updateBankAccount = repo.save(bank);
            return ResponseEntity.ok((updateBankAccount));
        }
        return ResponseEntity.notFound().build();
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
