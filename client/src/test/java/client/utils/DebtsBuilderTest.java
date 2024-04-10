package client.utils;

import client.language.Translator;
import client.scenes.MainCtrl;
import client.utils.communicators.implementations.EmailCommunicator;
import client.utils.communicators.implementations.ExpenseCommunicator;
import com.sun.javafx.application.PlatformImpl;
import commons.*;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DebtsBuilderTest {

    private DebtsBuilder debtsBuilder;

    @Mock
    private Event event;

    @Mock
    private Translator translator;

    @Mock
    private EmailCommunicator emailCommunicator;

    @Mock
    private ExpenseCommunicator expenseCommunicator;

    @Mock
    private MainCtrl mainCtrl;

    private ArrayList<Expense> expenses;

    private ArrayList<Debt> debts;

    private ArrayList<Participant> participants;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

//    @BeforeAll
//    public static void FXsetUp() {
//        Platform.startup(() -> {
//        });
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        Platform.exit();
//    }

    @Test
    public void findDebts() {
        info();
        when(event.getExpenses()).thenReturn(expenses);
        debtsBuilder = new DebtsBuilder(event, translator,
                emailCommunicator, expenseCommunicator, mainCtrl);
        assertEquals(debts, debtsBuilder.findDebts());
    }

    @Test
    public void simplifyDebts() {
        info();
        when(event.getExpenses()).thenReturn(expenses);
        when(event.getParticipants()).thenReturn(participants);
        debtsBuilder = new DebtsBuilder(event, translator,
                emailCommunicator, expenseCommunicator, mainCtrl);
        ArrayList<Debt> simplifiedDebts = new ArrayList<>();
        simplifiedDebts.add(new Debt(participants.get(0), participants.get(1), 180));
        simplifiedDebts.add(new Debt(participants.get(1), participants.get(2), 73.33333333333333));
        assertEquals(simplifiedDebts, debtsBuilder.simplifyDebts());
        assertEquals(simplifiedDebts, debtsBuilder.getDebts());
    }

//    @Test
//    public void getSummary() {
//        info();
//        when(event.getExpenses()).thenReturn(expenses);
//        when(event.getParticipants()).thenReturn(participants);
//        when(translator.getTranslation("OpenDebts.Summary-owes")).thenReturn("Owes");
//        when(translator.getTranslation("OpenDebts.Summary-to")).thenReturn("To");
//        debtsBuilder = new DebtsBuilder(event, translator,
//                emailCommunicator, expenseCommunicator, mainCtrl);
//        assertEquals("Second One Owes 60.0$ To First One", debtsBuilder.getSummary(debts.get(0)));
//    }

    @Test
    public void settleDebt() {
        info();
        when(event.getExpenses()).thenReturn(expenses);
        when(event.getParticipants()).thenReturn(participants);
        debtsBuilder = new DebtsBuilder(event, translator,
                emailCommunicator, expenseCommunicator, mainCtrl);
        debtsBuilder.settleDebt(debts.get(0));
        ArrayList<Participant> d = new ArrayList<>();
        d.add(participants.get(0));
        Expense expense = new Expense(0L, "Debt Settlement", 60.0, participants.get(1), d, LocalDate.now(), null);
        verify(event).addExpense(expense);
        verify(expenseCommunicator).createExpense(expense);
        verify(mainCtrl).showOpenDebts();
    }

//    @Test
//    public void emailSuccess() throws InterruptedException {
//        info();
//        when(event.getExpenses()).thenReturn(expenses);
//        when(event.getParticipants()).thenReturn(participants);
//        debtsBuilder = new DebtsBuilder(event, translator,
//                emailCommunicator, expenseCommunicator, mainCtrl);
//        Debt debt = debts.get(1);
//        EmailRequest er = new EmailRequest(debt.getDebtor().getEmail(),
//                "Debt Reminder", "This is a reminder for " +
//                "an open debt. " + "You owe " + debt.getCreditor().getName()
//                + " " + debt.getAmount() + "$.");
//        CountDownLatch latch = new CountDownLatch(1);
//        debtsBuilder.handleEmailButtonClick(debts.get(1));
//        Thread verificationThread = new Thread(() -> {
//            verify(emailCommunicator, atLeastOnce()).sendEmail(any());
//            latch.countDown();
//        });
//        verificationThread.start();
//        boolean verificationCompleted = latch.await(15, TimeUnit.SECONDS);
//        if (!verificationCompleted) {
//            fail("Verification did not complete within the specified timeout");
//        }
//    }

//    @Test
//    public void emailFailure() {
//        info();
//        when(event.getExpenses()).thenReturn(expenses);
//        when(event.getParticipants()).thenReturn(participants);
//        debtsBuilder = new DebtsBuilder(event, translator,
//                emailCommunicator, expenseCommunicator, mainCtrl);
//        Debt debt = debts.get(0);
//        EmailRequest er = new EmailRequest(debt.getDebtor().getEmail(),
//                "Debt Reminder", "This is a reminder for " +
//                "an open debt. " + "You owe " + debt.getCreditor().getName()
//                + " " + debt.getAmount() + "$.");
//        lenient().doThrow(RuntimeException.class).when(emailCommunicator).sendEmail(er);
//        debtsBuilder.handleEmailButtonClick(debts.get(0));
//    }

//    @Test
//    public void buildPanes() {
//        info();
//        when(event.getExpenses()).thenReturn(expenses);
//        when(event.getParticipants()).thenReturn(participants);
//        debtsBuilder = new DebtsBuilder(event, translator,
//                emailCommunicator, expenseCommunicator, mainCtrl);
//        Platform.runLater(() -> {
//            debtsBuilder.buildPanes();
//        });
//        PlatformImpl.runAndWait(() -> {
//        });
//        assertNotNull(debtsBuilder.getPanes());
//        assertEquals(2, debtsBuilder.getPanes().size());
//    }
//
//    @Test
//    public void createButton() {
//        info();
//        when(event.getExpenses()).thenReturn(expenses);
//        when(event.getParticipants()).thenReturn(participants);
//        debtsBuilder = new DebtsBuilder(event, translator,
//                emailCommunicator, expenseCommunicator, mainCtrl);
//        Platform.runLater(() -> {
//            when(translator.getTranslation("OpenDebts.SettleDebt-Button")).thenReturn("Settle Debt");
//            assertEquals(expectedButton().getText(), debtsBuilder.createButton(debts.get(0)).getText());
//            assertEquals(expectedButton().getFont(), debtsBuilder.createButton(debts.get(0)).getFont());
//            assertEquals(expectedButton().getPrefHeight(), debtsBuilder.createButton(debts.get(0)).getPrefHeight());
//            assertEquals(expectedButton().getPrefWidth(), debtsBuilder.createButton(debts.get(0)).getPrefWidth());
//            assertEquals(expectedButton().getStyle(), debtsBuilder.createButton(debts.get(0)).getStyle());
//        });
//        PlatformImpl.runAndWait(() -> {
//        });
//    }
//
//    @Test
//    public void prepareIcon() {
//        info();
//        when(event.getExpenses()).thenReturn(expenses);
//        when(event.getParticipants()).thenReturn(participants);
//        debtsBuilder = new DebtsBuilder(event, translator,
//                emailCommunicator, expenseCommunicator, mainCtrl);
//        Platform.runLater(() -> {
//            assertEquals(expectedIcon().getFitHeight(), debtsBuilder.prepareIcons("NoMailIcon").getFitHeight());
//            assertEquals(expectedIcon().getFitWidth(), debtsBuilder.prepareIcons("NoMailIcon").getFitWidth());
//            assertEquals(expectedIcon().getImage().getUrl(), debtsBuilder.prepareIcons("NoMailIcon").getImage().getUrl());
//        });
//        PlatformImpl.runAndWait(() -> {
//        });
//    }

    private ImageView expectedIcon() {
        Image image = new Image("file:client/src/main/resources/client/icons/debt/NoMailIcon.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        return imageView;
    }

    private Button expectedButton() {
        Button settleButton = new Button("Settle Debt");
        settleButton.setOnAction(event -> debtsBuilder.settleDebt(debts.get(0)));
        settleButton.setFont(Font.font(12));
        settleButton.setPrefWidth(120);
        settleButton.setPrefHeight(5);
        settleButton.setMaxWidth(Double.MAX_VALUE);
        settleButton.setMaxHeight(Double.MAX_VALUE);
        settleButton.setStyle("-fx-background-color: rgb(" +
                "" + 180 + ", " + 180 + ", " + 180 + ");");
        return settleButton;
    }


    private void info() {
        ArrayList<Expense> expenses = new ArrayList<>();
        Participant participant1 = new Participant("First One", "t@gmail.com");
        participant1.setBankAccount(new BankAccount("123", "123"));
        Participant participant2 = new Participant("Second One");
        Participant participant3 = new Participant("Third One", "t@gmail.com");
        List<Participant> debtors = new ArrayList<>();
        debtors.add(participant2);
        debtors.add(participant3);
        List<Participant> debtors2 = new ArrayList<>();
        debtors.add(participant2);
        Event event = new Event("Test", "InviteCode", debtors,
                new Date(2024, 1, 10), new Date(2024, 2, 10));
        expenses.add(new Expense(event.getId(), "Food", 180,
                participant1, debtors, LocalDate.of(2024, Month.APRIL, 1),
                new Tag("Test", 0, 0, 0, event.getId())));
        expenses.add(new Expense(event.getId(), "Food", 40,
                participant2, debtors, LocalDate.of(2024, Month.APRIL, 2),
                new Tag("Test", 0, 0, 0, event.getId())));
        expenses.add(new Expense(event.getId(), "Food", 100,
                participant3, debtors2, LocalDate.of(2024, Month.APRIL, 1),
                new Tag("Test", 0, 0, 0, event.getId())));
        this.expenses = expenses;

        ArrayList<Debt> debts = new ArrayList<>();
        debts.add(new Debt(participant1, participant2, 60));
        debts.add(new Debt(participant1, participant3, 60));
        debts.add(new Debt(participant1, participant2, 60));
        debts.add(new Debt(participant2, participant3, 13.333333333333334));
        this.debts = debts;

        ArrayList<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);
        participants.add(participant3);
        this.participants = participants;
    }


}
