package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
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

    public Optional<String> getExchangeRate(double amount, String base, String conversion) {
//        String url = "https://openexchangerates.org/api/" + "/convert/"
//                + amount + "/" + base + "/" + conversion + "?app_id=Required&app_id="
//                + "f6a144884bb44ed5ab06e0749ea59667";
        String url = "https://openexchangerates.org/api/latest.json?app_id=78d7f7ea8bc34ebea95de1cb9cb5887b";
        StringBuilder jsonResponse = new StringBuilder();
        try {
            setUrl(url);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
        return Optional.of(jsonResponse.toString());
    }

}
