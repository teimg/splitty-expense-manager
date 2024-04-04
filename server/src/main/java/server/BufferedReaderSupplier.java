package server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;

@Component
@Scope("prototype")
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
