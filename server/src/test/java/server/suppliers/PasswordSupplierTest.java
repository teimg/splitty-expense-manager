package server.suppliers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import server.suppliers.PasswordSupplier;
import server.suppliers.RandomSupplier;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PasswordSupplierTest {

    PasswordSupplier passwordSupplier;

    @Mock
    RandomSupplier mockRandomSupplier;

    // Mockito did weird, but this works
    Random random;
    @BeforeEach
    void setup(){

        random = new Random(){
            int[] list = new int[]{0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
            int counter = 0;
            @Override
            public int nextInt(int bound) {
                return list[counter++];
            }
        };

        Mockito.when(mockRandomSupplier.getRandom())
            .thenReturn(random);

        passwordSupplier = new PasswordSupplier(mockRandomSupplier);
    }

    @Test
    void getPassword() {
        assertEquals("abcdefghijklmno", passwordSupplier.getPassword());
    }

    @Test
    void onApplicationEvent() {
    }
}