package server.suppliers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.*;

@Configuration
public class HttpUrlConnectionSupplier {

    @Bean
    public HttpURLConnection getConnection() {
        HttpURLConnection conn = null;
        try {
            URI uri = new URI("https://openexchangerates.org/api/");
            conn = (HttpURLConnection) uri.toURL().openConnection();
        }
        catch (Exception e) {
            System.out.println("Network Error");
        }
        return conn;
    }

}
