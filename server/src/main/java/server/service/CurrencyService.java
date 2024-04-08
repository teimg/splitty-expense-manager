package server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.suppliers.BufferedClearerSupplier;
import server.suppliers.BufferedReaderSupplier;
import server.suppliers.BufferedWriterSupplier;
import server.suppliers.HttpUrlConnectionSupplier;

import java.io.*;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class CurrencyService {

    private final HttpUrlConnectionSupplier connectionSupplier;

    private final BufferedReaderSupplier bufferedReaderSupplier;

    private final BufferedWriterSupplier bufferedWriterSupplier;

    private final BufferedClearerSupplier bufferedClearerSupplier;

    @Autowired
    public CurrencyService(HttpUrlConnectionSupplier connectionSupplier,
                           BufferedReaderSupplier bufferedReader,
                           BufferedWriterSupplier bufferedWriter,
                           BufferedClearerSupplier bufferedClearer) {
        this.bufferedReaderSupplier = bufferedReader;
        this.bufferedWriterSupplier = bufferedWriter;
        this.bufferedClearerSupplier = bufferedClearer;
        this.connectionSupplier = connectionSupplier;
    }

    public Optional<Double> getExchangeRate(double amount, String currency, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        String dateString = formatter.format(date);
        Optional<Double> cache = fetchInCache(currency, dateString);
        if (cache.isPresent()) {
            return Optional.of(cache.get()*amount);
        }
        String url;
        if (date.equals(LocalDate.now())) {
            url = "https://openexchangerates.org/api/latest.json?" +
                    "app_id=78d7f7ea8bc34ebea95de1cb9cb5887b&base=" + "USD";
        }
        else {
            url = "https://openexchangerates.org/api/historical/" + dateString
                    + ".json?app_id=78d7f7ea8bc34ebea95de1cb9cb5887b&base=" + "USD";
        }

        StringBuilder jsonResponse = new StringBuilder();
        try {
            HttpURLConnection connection = connectionSupplier.getConnection(url);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(is));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    jsonResponse.append(inputLine);
                }
                in.close();
            }
            else {
                return Optional.empty();
            }
        }
        catch (Exception e) {
            return Optional.empty();
        }
        try {
            double val = findRate(jsonResponse.toString(), currency);
            saveToCache(currency, dateString, val);
            return Optional.of((val*amount));
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    public double findRate(String string, String currency) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(string);
        JsonNode ratesNode = jsonNode.get("rates");
        return ratesNode.get(currency).asDouble();
    }

    public Optional<Double> fetchInCache(String currency, String dateString) {
        try {
            BufferedReader bufferedReader = bufferedReaderSupplier.getBufferedReader();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith(dateString+"/"+currency)) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        double value = Double.parseDouble(parts[1]);
                        bufferedReader.close();
                        return Optional.of(value);
                    }
                    else {
                        bufferedReader.close();
                        return Optional.empty();
                    }
                }
            }
            bufferedReader.close();
        }
        catch (IOException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    public void saveToCache(String currency, String dateString, double val) {
        try {
            BufferedWriter bufferedWriter = bufferedWriterSupplier.getBufferedWriter();
            bufferedWriter.write(dateString+"/"+currency + ":" + val);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (IOException e) {
            System.out.println("Caching Error - Could not save");
        }
    }

    public Optional<String> clearCache() {
        try {
            BufferedWriter bufferedWriter = bufferedClearerSupplier.getBufferedClearer();
            bufferedWriter.write("");
            bufferedWriter.flush();
            bufferedWriter.close();
            return Optional.of("Success");
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
