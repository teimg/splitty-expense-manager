package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import server.service.CurrencyService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyControllerTest {

    @Mock
    CurrencyService service;

    @InjectMocks
    CurrencyController controller;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testGetRate() {
        when(service.getExchangeRate(
                100, "EUR", LocalDate.now())).thenReturn(Optional.of(1.5));
        ResponseEntity<Double> res = controller.getExchangeRate(100, "EUR", LocalDate.now());
        verify(service).getExchangeRate(100, "EUR", LocalDate.now());
        assertEquals(1.5, res.getBody());
        assertEquals(HttpStatusCode.valueOf(200), res.getStatusCode());
    }

    @Test
    public void testGetRateFail() {
        when(service.getExchangeRate(
                100, "EUR", LocalDate.now())).thenReturn(Optional.empty());
        ResponseEntity<Double> res = controller.getExchangeRate(100, "EUR", LocalDate.now());
        verify(service).getExchangeRate(100, "EUR", LocalDate.now());
        assertEquals(ResponseEntity.badRequest().build(), res);
        assertEquals(HttpStatusCode.valueOf(400), res.getStatusCode());
    }

    @Test
    public void testCacheFail() {
        when(service.clearCache()).thenReturn(Optional.of("Confirmed"));
        ResponseEntity<String> res = controller.clearCache();
        verify(service).clearCache();
        assertEquals("Cache Cleared Successfully", res.getBody());
        assertEquals(HttpStatusCode.valueOf(200), res.getStatusCode());
    }

    @Test
    public void testCacheSuccess() {
        when(service.clearCache()).thenReturn(Optional.empty());
        ResponseEntity<String> res = controller.clearCache();
        verify(service).clearCache();
        assertEquals(ResponseEntity.badRequest().build(), res);
        assertEquals(HttpStatusCode.valueOf(400), res.getStatusCode());
    }

}
