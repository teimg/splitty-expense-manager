package server.api;

import commons.Expense;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MockExpenseRepository implements ExpenseRepository {
    private final List<Expense> expenses = new ArrayList<>();

    @Override
    public <S extends Expense> S save(S entity) {
        entity.setId(expenses.size() + 1); // Simple ID assignment
        expenses.add(entity);
        return entity;
    }

    @Override
    public void flush() {
        // No operation
        return;
    }

    @Override
    public <S extends Expense> S saveAndFlush(S entity) {
        return save(entity);
    }

    @Override
    public <S extends Expense> List<S> saveAllAndFlush(Iterable<S> entities) {
        entities.forEach(this::save);
        return (List<S>) expenses;
    }

    @Override
    public void deleteAllInBatch(Iterable<Expense> entities) {
        // This would normally delete all given entities in a batch operation
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        // This would normally delete all entities by their ID in a batch operation
    }

    @Override
    public void deleteAllInBatch() {
        // This would delete all entities in a batch operation
    }

    @Override
    public Expense getOne(Long id) {
        return findById(id).orElse(null);
    }

    @Override
    public Expense getById(Long id) {
        return findById(id).orElse(null);
    }

    @Override
    public Expense getReferenceById(Long id) {
        return findById(id).orElse(null);
    }

    @Override
    public <S extends Expense> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Expense> List<S> findAll(Example<S> example) {
        return new ArrayList<>();
    }

    @Override
    public <S extends Expense> List<S> findAll(Example<S> example, Sort sort) {
        return new ArrayList<>();
    }

    @Override
    public <S extends Expense> Page<S> findAll(Example<S> example, Pageable pageable) {
        return new PageImpl<>(new ArrayList<>());
    }

    @Override
    public <S extends Expense> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Expense> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Expense, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Optional<Expense> findById(Long id) {
        return expenses.stream().filter(expense -> expense.getId()==id).findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return expenses.stream().anyMatch(expense -> expense.getId()==id);
    }

    @Override
    public <S extends Expense> List<S> saveAll(Iterable<S> entities) {
        entities.forEach(this::save);
        return (List<S>) expenses;
    }

    @Override
    public List<Expense> findAll() {
        return new ArrayList<>(expenses);
    }

    @Override
    public List<Expense> findAllById(Iterable<Long> ids) {
        List<Expense> foundExpenses = new ArrayList<>();
        ids.forEach(id -> findById(id).ifPresent(foundExpenses::add));
        return foundExpenses;
    }

    @Override
    public long count() {
        return expenses.size();
    }

    @Override
    public void deleteById(Long id) {
        expenses.removeIf(expense -> expense.getId()==id);
    }

    @Override
    public void delete(Expense entity) {
        expenses.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        ids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Expense> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        expenses.clear();
    }

    @Override
    public List<Expense> findByEventId(Long eventId) {
        // This would find expenses by event ID
        return new ArrayList<>();
    }

    @Override
    public List<Expense> findAll(Sort sort) {
        // This method would return all expenses sorted
        return new ArrayList<>();
    }

    @Override
    public Page<Expense> findAll(Pageable pageable) {
        // This method would return a page of expenses
        return new PageImpl<>(new ArrayList<>());
    }

    // Implement any additional methods required by JpaRepository that have not been explicitly overridden
}
