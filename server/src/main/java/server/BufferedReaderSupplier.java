package server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;

@Configuration
public class BufferedReaderSupplier {

    @Bean
    public BufferedReader getBufferedReader() {
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader("server/src/main/resources/cache/cache.txt"));
        } catch (Exception e) {
            System.out.println("Error");
        }
        return bf;
    }

}
