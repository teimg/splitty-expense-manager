package server.suppliers;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.*;

@Component
public class HttpUrlConnectionSupplier {

    @Bean
    public HttpURLConnection getConnection(String url) {
        HttpURLConnection conn = null;
        try {
            URI uri = new URI(url);
            conn = (HttpURLConnection) uri.toURL().openConnection();
        }
        catch (Exception e) {
            System.out.println("Network Error");
        }
        return conn;
    }

}
