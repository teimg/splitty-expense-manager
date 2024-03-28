package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.Random;
import java.util.logging.Logger;

@Configuration
public class PasswordSupplier {

    private final RandomSupplier randomSupplier;

    private final String password;

    private static final Logger LOG
        = Logger.getLogger("Admin password");

    @Autowired
    public PasswordSupplier(RandomSupplier randomSupplier) {
        this.randomSupplier = randomSupplier;
        password = generatePassword();
    }

    private String generatePassword(){
        Random rand = randomSupplier.getRandom();
        int passSize = rand.nextInt(5) + 15;
        String[] allChars = "abcdeghijklmnopqrstuvwxyz1234567890$%&?!".split("");

        StringBuilder res = new StringBuilder();
        for (int i = 0; i < passSize; i++) {
            res.append(allChars[
                rand.nextInt(allChars.length)]);
        }

        return res.toString();
    }

    @Bean
    public String getPassword(){
        return password;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.info(this.password);
    }
}
