package server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
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

    @Autowired
    public CurrencyService(HttpURLConnection httpURLConnection) {
        this.connection = httpURLConnection;
    }

    public void setUrl(String url) throws URISyntaxException, IOException {
        URI uri = new URI(url);
        this.connection = (HttpURLConnection) uri.toURL().openConnection();
    }

    public Optional<String> getExchangeRate(double amount, String currency, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        String dateString = formatter.format(date);
        String url = "https://openexchangerates.org/api/historical/" + dateString  + ".json?app_id=78d7f7ea8bc34ebea95de1cb9cb5887b&base=" + "USD";
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
            return Optional.of((val*amount) +"");
        }
        catch (Exception e) {
            System.out.println("Error");
        }

        return Optional.of(jsonResponse.toString());
    }

    public double findRate(String string, String currency) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(string);
        JsonNode ratesNode = jsonNode.get("rates");
        return ratesNode.get(currency).asDouble();
    }

}
