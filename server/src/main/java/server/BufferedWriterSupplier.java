package server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedWriter;
import java.io.FileWriter;

@Configuration
public class BufferedWriterSupplier {

    @Bean
    public BufferedWriter getBufferedWriter() {
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter("server/src/main/resources/cache/cache.txt"));
        } catch (Exception e) {
            System.out.println("Error");
        }
        return bf;
    }

}
