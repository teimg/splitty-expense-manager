package server.suppliers;

import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaMailSenderSupplierTest {

    @Test
    public void testSupplier() {
        JavaMailSenderSupplier jmss = new JavaMailSenderSupplier();
        assertEquals(jmss.getJavaMailSender().getJavaMailProperties(), helper().getJavaMailProperties());
    }

    private JavaMailSenderImpl helper() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }

}
