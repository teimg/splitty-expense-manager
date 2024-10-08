package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.CurrencyService;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(path = {"/{amount}/{currency}/{date}"})
    public ResponseEntity<Double> getExchangeRate(
            @PathVariable("amount") double amount,
            @PathVariable("currency") String currency,
            @PathVariable("date") LocalDate date) {

        if (!((currency == null || currency.isEmpty()) && amount != 0 && date != null)) {
            Optional<Double> rate = currencyService.
                    getExchangeRate(amount, currency, date);
            if (rate.isPresent()) {
                return ResponseEntity.ok(rate.get());
            }
        }
        return ResponseEntity.badRequest().build();

    }

    @DeleteMapping(path = {"/cache"})
    public ResponseEntity<String> clearCache() {
        Optional<String> res = currencyService.clearCache();
        if (res.isPresent()) {
            return ResponseEntity.ok("Cache Cleared Successfully");
        }
        return ResponseEntity.badRequest().build();
    }

}
