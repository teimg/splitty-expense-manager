package server.service;

import commons.Expense;
import server.database.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final EventService eventService;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, EventService eventService) {
        this.expenseRepository = expenseRepository;
        this.eventService = eventService;
    }

    public Expense saveExpense(Expense expense) {
        eventService.save(expense.getEvent());
        return expenseRepository.saveAndFlush(expense);
    }

    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public List<Expense> getExpensesByEventId(Long eventId) {
        return expenseRepository.findByEventId(eventId);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}