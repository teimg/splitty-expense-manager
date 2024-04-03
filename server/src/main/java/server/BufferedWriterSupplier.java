package server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;

@Component
@Scope("prototype")
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
