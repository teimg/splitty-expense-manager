package server.api;

import commons.Debt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.DebtService;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
public class DebtController {

    private final DebtService service;

    @Autowired
    public DebtController(DebtService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Debt> getById(@PathVariable("id") long id) {
        Debt debt = service.getById(id);
        return ResponseEntity.ok(debt);
    }

    @PostMapping
    public ResponseEntity<Debt> createDebt(@RequestBody Debt debt) {
        Debt createdDebt = service.createDebt(debt);
        return ResponseEntity.ok(createdDebt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Debt> updateDebt(@PathVariable("id") long id, @RequestBody Debt newDetails) {
        Debt updatedDebt = service.updateDebt(id, newDetails);
        return ResponseEntity.ok(updatedDebt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDebt(@PathVariable("id") long id) {
        service.deleteDebt(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<Debt> getAll() {
        return service.getAll();
    }
}
