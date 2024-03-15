package server.service;

import commons.Debt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.DebtRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DebtService {

    private final DebtRepository repo;

    @Autowired
    public DebtService(DebtRepository debtRepository) {
        this.repo = debtRepository;
    }

    public Debt getById(long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Debt not found"));
    }

    public Debt createDebt(Debt debt) {
        // Additional validation logic can be added here
        return repo.save(debt);
    }

    public Debt updateDebt(long id, Debt newDetails) {
        Debt debt = getById(id);
        debt.setCreditor(newDetails.getCreditor());
        debt.setDebtor(newDetails.getDebtor());
        debt.setAmount(newDetails.getAmount());
        debt.setHasPaid(newDetails.isHasPaid());
        debt.setSummary(newDetails.getSummary());
        debt.setDescription(newDetails.getDescription());
        return repo.save(debt);
    }

    public List<Debt> getAll() {
        return repo.findAll();
    }
}
