package server.service;

import commons.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.database.BankAccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {
    private final BankAccountRepository repo;

    @Autowired
    public BankAccountService(BankAccountRepository repo) {
        this.repo = repo;
    }

    public List<BankAccount> getAll() {
        return repo.findAll();
    }

    public Optional<BankAccount> getById(long id) {
        return repo.findById(id);
    }

    public boolean exists(long id) {
        return repo.existsById(id);
    }

    public BankAccount save(BankAccount bankAccount) {
        return repo.saveAndFlush(bankAccount);
    }

    public Optional<BankAccount> remove(long id) {
        Optional<BankAccount> bankAccount = getById(id);
        if (bankAccount.isPresent()) repo.deleteById(id);
        return bankAccount;
    }
}
