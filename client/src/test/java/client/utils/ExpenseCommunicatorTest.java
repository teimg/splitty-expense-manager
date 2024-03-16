package client.utils;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import commons.Event;
import commons.Expense;
import commons.Participant;
import client.utils.ClientConfiguration;
import client.utils.ExpenseCommunicator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
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
        MockitoAnnotations.openMocks(this);
        when(config.getServer()).thenReturn("http://localhost:8080");
        when(client.target("http://localhost:8080")).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.request(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilder);
        when(invocationBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilder);

        // Setup mock response for different HTTP methods
        when(invocationBuilder.post(any(Entity.class))).thenReturn(response);
        when(invocationBuilder.get()).thenReturn(response);
        when(invocationBuilder.put(any(Entity.class))).thenReturn(response);
        when(invocationBuilder.delete()).thenReturn(response);

        // Mock entity response for deserialization
        when(response.readEntity(Expense.class)).thenReturn(dummyExpense);

        dummyExpense = new Expense();
        dummyExpense.setId(1);
        dummyExpense.setEvent(new Event());
        dummyExpense.setPurchase("Test Purchase");
        dummyExpense.setAmount(100.0);
        dummyExpense.setPayer(new Participant("Test Payer"));
        dummyExpense.setDebtors(Collections.singletonList(new Participant("Test Debtor")));
        dummyExpense.setDate(LocalDate.now());
    }

    @Test
    public void createExpenseTest() {
        communicator.createExpense(dummyExpense.getEvent(), dummyExpense.getPurchase(), dummyExpense.getAmount(),
                dummyExpense.getPayer(), dummyExpense.getDebtors(), dummyExpense.getDate());

        verify(invocationBuilder).post(Entity.entity(dummyExpense, MediaType.APPLICATION_JSON));
    }

    @Test
    public void getExpenseTest() {
        long expenseId = 1L;
        communicator.getExpense(expenseId);

        verify(webTarget).path("api/expense/{id}");
        verify(webTarget).resolveTemplate("id", expenseId);
        verify(invocationBuilder).get();
    }
}
