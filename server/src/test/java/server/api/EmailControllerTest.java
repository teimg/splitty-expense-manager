package server.api;

import commons.EmailRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.service.EmailService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailControllerTest {

    @InjectMocks
    private EmailController emailController;

    @Mock
    private EmailService mockEmailService;

    private EmailRequest er;

    @BeforeEach
    public void setup(){
        er = new EmailRequest("To", "Subject", "Body");
    }

    @Test
    public void sendEmail() {
        EmailRequest res = emailController.sendEmail(er).getBody();
        assertEquals(res, er);
        verify(mockEmailService).sendEmail(eq(er.getTo()), eq(er.getSubject()), eq(er.getBody()),
                any(), any());
    }

}
