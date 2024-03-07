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

    public void save(BankAccount bankAccount) {
        repo.saveAndFlush(bankAccount);
    }

    public void remove(BankAccount bankAccount) {
        repo.delete(bankAccount);
    }
}
