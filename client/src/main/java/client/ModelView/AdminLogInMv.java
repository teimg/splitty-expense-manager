package client.ModelView;

import client.utils.communicators.interfaces.IAdminCommunicator;
import jakarta.inject.Inject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminLogInMv {

    private IAdminCommunicator adminCommunicator;

    private StringProperty passwordField;

    private boolean isLoggedIn;

    @Inject
    public AdminLogInMv(IAdminCommunicator adminCommunicator) {
        this.adminCommunicator = adminCommunicator;
        this.passwordField = new SimpleStringProperty("");
    }

    public void validatePassword(){
        if(adminCommunicator.validatePassword(passwordField.getValue())){
            isLoggedIn = true;
            return;
        }
        passwordField.setValue("");
        throw  new IllegalArgumentException("InvalidPass");

        // For debugging the Admin page just comment out everything above except this line.
        //  return true;
    }

    public StringProperty passwordFieldProperty() {
        return passwordField;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
