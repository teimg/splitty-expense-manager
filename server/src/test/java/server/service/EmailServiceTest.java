package server.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class EmailServiceTest {

    private EmailService emailService;
    private JavaMailSenderImpl javaMailSender;

    @BeforeEach
    void setUp() {
        javaMailSender = mock(JavaMailSenderImpl.class);

        emailService = new EmailService(javaMailSender);
    }

    @Test
    void testSendEmail() {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Test Email Content";
        String username = "sender@example.com";
        String password = "password123";

        when(javaMailSender.createMimeMessage()).thenReturn(helper().createMimeMessage());

        emailService.sendEmail(to, subject, text, username, password);

        verify(javaMailSender).setUsername(username);
        verify(javaMailSender).setPassword(password);
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send((MimeMessage) any());
        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(javaMailSender).setUsername(usernameCaptor.capture());
        verify(javaMailSender).setPassword(passwordCaptor.capture());
        assertEquals(username, usernameCaptor.getValue());
        assertEquals(password, passwordCaptor.getValue());
    }

    @Test
    void testSendEmailFail() {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Test Email Content";
        String username = "sender@example.com";
        String password = "password123";
        doThrow(new RuntimeException(new MessagingException("Simulated MessagingException")))
                .when(javaMailSender).createMimeMessage();
        emailService.sendEmail(to, subject, text, username, password);
        verify(javaMailSender, times(0)).send(any(MimeMessage.class));
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