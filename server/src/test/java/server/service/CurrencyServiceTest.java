package server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import server.suppliers.BufferedClearerSupplier;
import server.suppliers.BufferedReaderSupplier;
import server.suppliers.BufferedWriterSupplier;
import server.suppliers.HttpUrlConnectionSupplier;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @Mock
    private HttpUrlConnectionSupplier connectionSupplier;

    @Mock
    private HttpURLConnection connection;

    @Mock
    private BufferedReaderSupplier bufferedReaderSupplier;

    @Mock
    private BufferedWriterSupplier bufferedWriterSupplier;

    @Mock
    private BufferedClearerSupplier bufferedClearerSupplier;

    @Mock
    private BufferedWriter mockBufferedWriter;

    @Mock
    private BufferedReader mockBufferedReader;

    @Mock
    private BufferedWriter mockBufferedClearer;

    private CurrencyService currencyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        lenient().when(bufferedWriterSupplier.getBufferedWriter()).thenReturn(mockBufferedWriter);
        lenient().when(bufferedReaderSupplier.getBufferedReader()).thenReturn(mockBufferedReader);
        lenient().when(bufferedClearerSupplier.getBufferedClearer()).thenReturn(mockBufferedClearer);
        currencyService = new CurrencyService(connectionSupplier, bufferedReaderSupplier, bufferedWriterSupplier, bufferedClearerSupplier);
    }

    @Test
    public void fetchCache() throws IOException {
        when(mockBufferedReader.readLine())
                .thenReturn("2022-10-10/USD:1.05")
                .thenReturn(null);
        doNothing().when(mockBufferedReader).close();
        assertEquals(Optional.of(1.05), currencyService.fetchInCache("USD", "2022-10-10"));
    }

    @Test
    public void fetchCacheNotFound() throws IOException {
        when(mockBufferedReader.readLine())
                .thenReturn("2022-10-10/USD:1.05")
                .thenReturn(null);
        doNothing().when(mockBufferedReader).close();
        assertEquals(Optional.empty(), currencyService.fetchInCache("USD", "2021-10-10"));
    }

    @Test
    public void fetchCacheException() throws IOException {
        when(mockBufferedReader.readLine())
                .thenReturn("2022-10-10/USD:1.05")
                .thenReturn(null);
        doThrow(IOException.class).when(mockBufferedReader).close();
        assertEquals(Optional.empty(), currencyService.fetchInCache("USD", "2021-10-10"));
    }

    @Test
    public void fetchCacheWeirdLine() throws IOException {
        when(mockBufferedReader.readLine())
                .thenReturn("2022-10-10/USD:1.05:0")
                .thenReturn(null);
        doNothing().when(mockBufferedReader).close();
        assertEquals(Optional.empty(), currencyService.fetchInCache("USD", "2022-10-10"));
    }

    @Test
    public void testFindRate() throws JsonProcessingException {
        double res = currencyService.findRate("{\"disclaimer\":\"Usage subject to terms: https://openexchangerates.org/terms\",\"license\":\"https://openexchangerates.org/license\",\"timestamp\":1097424000,\"base\":\"USD\",\"rates\":{\"AED\":1.05,\"ALL\":101.278582}}", "AED");
        assertEquals(res, 1.05);
    }

    @Test
    public void saveCache() throws IOException {
        doNothing().when(mockBufferedWriter).write((String) any());
        doNothing().when(mockBufferedWriter).newLine();
        doNothing().when(mockBufferedWriter).flush();
        doNothing().when(mockBufferedWriter).close();

        currencyService.saveToCache("Currency", "2022-01-01", 100);

        verify(mockBufferedWriter).write((String) any());
        verify(mockBufferedWriter).newLine();
        verify(mockBufferedWriter).flush();
        verify(mockBufferedWriter).close();
    }

    @Test
    public void saveCacheFail() throws IOException {
        doNothing().when(mockBufferedWriter).write((String) any());
        doThrow(IOException.class).when(mockBufferedWriter).newLine();

        currencyService.saveToCache("Currency", "2022-01-01", 100);

        verify(mockBufferedWriter, times(0)).close();
    }

    @Test
    public void clearCache() throws IOException {
        doNothing().when(mockBufferedClearer).write("");
        doNothing().when(mockBufferedClearer).flush();
        doNothing().when(mockBufferedClearer).close();

        Optional<String> result = currencyService.clearCache();

        verify(mockBufferedClearer).write("");
        verify(mockBufferedClearer).flush();
        verify(mockBufferedClearer).close();

        assertEquals(Optional.of("Success"), result);
    }

    @Test
    public void clearCacheFail() throws IOException {
        when(bufferedClearerSupplier.getBufferedClearer()).thenReturn(mockBufferedClearer);

        doNothing().when(mockBufferedClearer).write("");
        doNothing().when(mockBufferedClearer).flush();
        doThrow(IOException.class).when(mockBufferedClearer).close();

        Optional<String> result = currencyService.clearCache();
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void rateCacheHit() throws IOException {
        when(mockBufferedReader.readLine())
                .thenReturn("2022-10-10/USD:1.05")
                .thenReturn(null);
        doNothing().when(mockBufferedReader).close();
        assertEquals(Optional.of(105.0), currencyService.getExchangeRate(100, "USD", LocalDate.of(2022, 10, 10)));
    }

    @Test
    public void rateNow() throws IOException, URISyntaxException {
        String inputString = "{\"disclaimer\":\"Usage subject to terms: https://openexchangerates.org/terms\",\"license\":\"https://openexchangerates.org/license\",\"timestamp\":1097424000,\"base\":\"USD\",\"rates\":{\"AED\":1.05,\"ALL\":101.278582}}";
        when(mockBufferedReader.readLine())
                .thenReturn("2022-10-10/USD:1.05")
                .thenReturn(null);
        doNothing().when(mockBufferedReader).close();
        when(connectionSupplier.getConnection("https://openexchangerates.org/api/latest.json?" +
        "app_id=78d7f7ea8bc34ebea95de1cb9cb5887b&base=" + "USD")).thenReturn(connection);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(inputString.getBytes()));
        when(connection.getResponseCode()).thenReturn(200);
        doNothing().when(mockBufferedWriter).write((String) any());
        doNothing().when(mockBufferedWriter).newLine();
        doNothing().when(mockBufferedWriter).flush();
        doNothing().when(mockBufferedWriter).close();
        assertEquals(Optional.of(105.0), currencyService.getExchangeRate(100, "AED", LocalDate.now()));
    }

    @Test
    public void rateNowNotFound() throws IOException, URISyntaxException {
        when(mockBufferedReader.readLine())
                .thenReturn("2022-10-10/USD:1.05")
                .thenReturn(null);
        doNothing().when(mockBufferedReader).close();
        when(connectionSupplier.getConnection("https://openexchangerates.org/api/latest.json?" +
                "app_id=78d7f7ea8bc34ebea95de1cb9cb5887b&base=" + "USD")).thenReturn(connection);
        when(connection.getResponseCode()).thenReturn(100);
        assertEquals(Optional.empty(), currencyService.getExchangeRate(100, "AED", LocalDate.now()));
    }

    @Test
    public void rateOldException() throws IOException, URISyntaxException {
        when(mockBufferedReader.readLine())
                .thenReturn("2022-10-10/USD:1.05")
                .thenReturn(null);
        doNothing().when(mockBufferedReader).close();
        when(connectionSupplier.getConnection("https://openexchangerates.org/api/latest.json?" +
                "app_id=78d7f7ea8bc34ebea95de1cb9cb5887b&base=" + "USD")).thenReturn(connection);
        when(connection.getInputStream()).thenReturn(null);
        assertEquals(Optional.empty(), currencyService.getExchangeRate(100, "AED", LocalDate.of(2022, 1, 1)));
    }

    @Test
    public void rateFindRateException() throws IOException, URISyntaxException {
        String inputString = "\",\"lates\":{\"AED\":1.05,\"ALL\":101.278582}}";
        when(mockBufferedReader.readLine())
                .thenReturn("2022-10-10/USD:1.05")
                .thenReturn(null);
        doNothing().when(mockBufferedReader).close();
        when(connectionSupplier.getConnection("https://openexchangerates.org/api/latest.json?" +
                "app_id=78d7f7ea8bc34ebea95de1cb9cb5887b&base=" + "USD")).thenReturn(connection);
        when(connection.getInputStream()).thenReturn(new ByteArrayInputStream(inputString.getBytes()));
        when(connection.getResponseCode()).thenReturn(200);
        assertEquals(Optional.empty(), currencyService.getExchangeRate(100, "AED", LocalDate.now()));
    }

}
