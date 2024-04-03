package server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class CurrencyService {

    private HttpURLConnection connection;

    private BufferedReader bufferedReader;

    private BufferedWriter bufferedWriter;

    @Autowired
    public CurrencyService(HttpURLConnection httpURLConnection,
                           BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        this.connection = httpURLConnection;
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
    }

    public void setUrl(String url) throws URISyntaxException, IOException {
        URI uri = new URI(url);
        this.connection = (HttpURLConnection) uri.toURL().openConnection();
    }

    public Optional<Double> getExchangeRate(double amount, String currency, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        String dateString = formatter.format(date);
        Optional<Double> cache = fetchInCache(amount, currency, dateString);
        if (cache.isPresent()) {
            return cache;
        }
        String url = "https://openexchangerates.org/api/historical/" + dateString
                + ".json?app_id=78d7f7ea8bc34ebea95de1cb9cb5887b&base=" + "USD";
        StringBuilder jsonResponse = new StringBuilder();
        try {
            setUrl(url);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
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

    private Optional<Double> fetchInCache(double amount, String currency, String dateString) {
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith(dateString+"/"+currency + ":")) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        double value = Double.parseDouble(parts[1]);
                        return Optional.of(value);
                    }
                    else {
                        return Optional.empty();
                    }
                }
            }
        }
        catch (IOException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    private void saveToCache(String currency, String dateString, double val) {
        try {
            bufferedWriter.newLine();
            bufferedWriter.write(dateString+"/"+currency + ":" + val);
            bufferedWriter.flush();
        }
        catch (IOException e) {
            System.out.println("Caching Error - Could not save");
        }
    }

}
