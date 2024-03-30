package client.ModelView;

import client.utils.communicators.interfaces.IAdminCommunicator;
import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminLogInMvTest {

    AdminLogInMv adminLogInMv;

    @Mock
    IAdminCommunicator mockAdminCommunicator;

    @BeforeEach
    void setup(){
        adminLogInMv = new AdminLogInMv(mockAdminCommunicator);

    }
    @Test
    void validatePassword() {
        String pass = "Password";
        Mockito.when(mockAdminCommunicator.validatePassword(Mockito.anyString()))
            .thenReturn(false);
        Mockito.when(mockAdminCommunicator.validatePassword(pass))
            .thenReturn(true);

        adminLogInMv.passwordFieldProperty().setValue(pass);
        assertDoesNotThrow(() ->
            adminLogInMv.validatePassword());
    }

    @Test
    void validateWrongPassword() {
        String pass = "Password";
        Mockito.when(mockAdminCommunicator.validatePassword(Mockito.anyString()))
            .thenReturn(false);

        adminLogInMv.passwordFieldProperty().setValue("notPass");
        assertThrows(IllegalArgumentException.class, () ->{
            adminLogInMv.validatePassword();

        }, "InvalidPass");
    }

    @Test
    void passwordFieldProperty() {
        assertEquals("", adminLogInMv.passwordFieldProperty().get());
    }

    @Test
    void isLoggedIn() {
        assertFalse(adminLogInMv.isLoggedIn());

        this.validatePassword();

        assertTrue(adminLogInMv.isLoggedIn());

    }
}