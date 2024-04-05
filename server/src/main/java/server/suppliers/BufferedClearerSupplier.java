package server.suppliers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.springframework.boot.Banner.Mode.LOG;


@Component
@Scope("prototype")
public class BufferedClearerSupplier {

    private static final Logger LOG
        = Logger.getLogger("Cache cleared");

    @Bean
    public BufferedWriter getBufferedClearer() {
        return getBuffer();
    }

    private BufferedWriter getBuffer(){
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter("server/src/main/resources/cache/cache.txt"));
        } catch (Exception e) {
            System.out.println("Error");
        }
        return bf;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            BufferedWriter bufferedWriter = this.getBuffer();
            bufferedWriter.write("");
            bufferedWriter.flush();
            bufferedWriter.close();
            LOG.info("Success");
        } catch (IOException e) {
            LOG.info("Failed");
        }
    }

}
