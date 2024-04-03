package server.api;

import commons.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.service.CurrencyService;

import java.util.Optional;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(path = {"{amount}/{baseCurrency}/{conversionCurrency}"})
    public ResponseEntity<String> getExchangeRate(
            @PathVariable("amount") double amount,
            @PathVariable("baseCurrency") String base,
            @PathVariable("conversionCurrency") String conversion) {

        if (base == null || !base.isEmpty()
                || conversion == null || !conversion.isEmpty()) {
            Optional<String> rate = currencyService.
                    getExchangeRate(amount, base, conversion);
            if (rate.isPresent()) {
                return ResponseEntity.ok(rate.get());
            }
        }
        return ResponseEntity.badRequest().build();

    }

}
