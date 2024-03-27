package client.utils;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import commons.Event;
import commons.Expense;
import commons.Participant;
import client.utils.communicators.implementations.ExpenseCommunicator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
public class ExpenseCommunicatorTest {

    @Mock
    private ClientConfiguration config;

    @Mock
    private Client client;

    @Mock
    private WebTarget webTarget;

    @Mock
    private Invocation.Builder invocationBuilder;

    @Mock
    private Response response;

    @InjectMocks
    private ExpenseCommunicator communicator;

    private Expense dummyExpense;

    @BeforeEach
    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        when(config.getServer()).thenReturn("http://localhost:8080");
//        when(client.target("http://localhost:8080")).thenReturn(webTarget);
//        when(webTarget.path(anyString())).thenReturn(webTarget);
//        when(webTarget.request(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilder);
//        when(invocationBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilder);
//
//        when(invocationBuilder.post(any(Entity.class))).thenReturn(response);
//        when(invocationBuilder.get()).thenReturn(response);
//        when(invocationBuilder.put(any(Entity.class))).thenReturn(response);
//        when(invocationBuilder.delete()).thenReturn(response);
//
//        when(response.readEntity(Expense.class)).thenReturn(dummyExpense);

        dummyExpense = new Expense();
        dummyExpense.setId(1);
        dummyExpense.setEventId(null);
        dummyExpense.setPurchase("Test Purchase");
        dummyExpense.setAmount(100.0);
        dummyExpense.setPayer(new Participant("Test Payer"));
        dummyExpense.setDebtors(Collections.singletonList(new Participant("Test Debtor")));
        dummyExpense.setDate(LocalDate.now());
    }

    @Test
    public void createExpenseTest() {
        assertFalse(dummyExpense.equals(new Expense()));
        // TODO create replacement objects and write some tests
    }
}
