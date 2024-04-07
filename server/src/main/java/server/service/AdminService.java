package server.service;

import org.springframework.stereotype.Service;
import server.suppliers.PasswordSupplier;

@Service
public class AdminService {

    private final PasswordSupplier passwordSupplier;
    public AdminService(PasswordSupplier passwordSupplier) {
        this.passwordSupplier = passwordSupplier;
    }
    public  boolean validatePassword(String password){
        return passwordSupplier.getPassword().equals(password);
    }

}
