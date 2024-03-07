package server.api;

import commons.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.service.BankAccountService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bankAccounts")
public class BankAccountController {

    private final BankAccountService service;

    @Autowired
    public BankAccountController(BankAccountService service) {
        this.service = service;
    }

    /**
     * Getter Method for all bank accounts
     * @return List of bank accounts
     */
    @GetMapping(path = { "", "/" })
    public List<BankAccount> getAll() {
        return service.getAll();
    }

    /**
     * Getter Method for a specific bank account through id
     * @param id - id to be fetched
     * @return Bank Account
     */
    @GetMapping(path = {"/{id}"})
    public ResponseEntity<BankAccount> getById(@PathVariable("id") long id) {
        if (id >= 0) {
            Optional<BankAccount> bankAccount = service.getById(id);
            if (bankAccount.isPresent()) {
                return ResponseEntity.ok(bankAccount.get());
            }
        }
        return ResponseEntity.badRequest().build();
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

        BankAccount saved = service.save(bankAccount);
        return ResponseEntity.ok(saved);
    }

    /**
     * Delete Method for Bank account by id
     * @param id - id to be deleted
     * @return deleted object / badRequest in case of failure
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<BankAccount> delete(@PathVariable long id) {
        if (id >= 0) {
            Optional<BankAccount> deleted = service.remove(id);
            if (deleted.isPresent()) {
                return ResponseEntity.ok(deleted.get());
            }
        }
        return ResponseEntity.badRequest().build();
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
        if (id >= 0) {
            Optional<BankAccount> gotBankAccount = service.getById(id);
            if (gotBankAccount.isPresent()) {
                BankAccount toBeUpdated = gotBankAccount.get();
                toBeUpdated.setBic(bankAccount.getBic());
                toBeUpdated.setIban(bankAccount.getIban());
                BankAccount updated = service.save(toBeUpdated);
                return ResponseEntity.ok(updated);
            }
        }
        return ResponseEntity.badRequest().build();
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
