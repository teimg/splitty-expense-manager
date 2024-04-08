package client.scenes;

import static org.mockito.Mockito.*;

import commons.BankAccount;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AdminScreenCtrlTest {

//    static {
//        new JFXPanel();
//    }

    @Mock
    private MainCtrl mainCtrlMock;

    @InjectMocks
    private AdminScreenCtrl adminScreenCtrl;

    private ObservableList<Event> testEvents;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        BankAccount johnsAccount = new BankAccount("DE123456789", "BIC123");
        BankAccount emmasAccount = new BankAccount("DE987654321", "BIC321");

        Participant john = new Participant("John", johnsAccount);
        Participant emma = new Participant("Emma", emmasAccount);
        Participant mike = new Participant("Mike");

        List<Participant> participants = Arrays.asList(john, emma, mike);

        Expense dinnerExpense = new Expense();
        dinnerExpense.setPurchase("Dinner");
        dinnerExpense.setAmount(120.0);
        dinnerExpense.setPayer(john); // John paid for dinner
        dinnerExpense.setDebtors(Arrays.asList(emma, mike)); // Emma and Mike owe John
        dinnerExpense.setDate(LocalDate.now());

        List<Expense> expenses = new ArrayList<>();
        expenses.add(dinnerExpense);

        Date a = new Date(20L);
        Date b = new Date(30L);
        Event dinnerEvent = new Event("Dinner Party", "INV123", participants, a, b);
        for (Expense expense : expenses) {
            dinnerEvent.addExpense(expense);
        }

        testEvents = FXCollections.observableArrayList(
                dinnerEvent
        );
    }

//    empty tests failed build???!!!
//    @Test
//    public void testHandleBack() {
//        //adminScreenCtrl.handleBack(new ActionEvent());
//
//        //verify(mainCtrlMock).showStartScreen();
//
//    }

}