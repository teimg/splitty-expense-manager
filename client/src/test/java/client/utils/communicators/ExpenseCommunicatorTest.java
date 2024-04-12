package client.utils.communicators;

import client.utils.ClientConfiguration;
import client.utils.communicators.implementations.ExpenseCommunicator;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseCommunicatorTest {

    @Mock
    private ClientConfiguration configuration;

    @Mock
    private ClientSupplier supplier;

    @Mock
    private Client client;

    private String origin;

    private ExpenseCommunicator communicator;

    @BeforeEach
    public void setUp() {
        origin = "";
        MockitoAnnotations.openMocks(this);
        lenient().when(configuration.getServer()).thenReturn(origin);
        lenient().when(supplier.getClient()).thenReturn(client);
        communicator = new ExpenseCommunicator(configuration, supplier);
    }

    @Test
    public void testCreateExpense() {
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Expense expense = new Expense();
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.post(any(Entity.class), eq(Expense.class))).thenReturn(expense);
        assertEquals(expense, communicator.createExpense(expense));
        verify(client).target(anyString());
    }

    @Test
    public void testCreatePieces() {
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Expense expense = new Expense();
        Event event = new Event();
        event.setId(1L);
        String purchase = "a";
        double amount = 10.0;
        Participant payer = new Participant();
        List<Participant> debtors = List.of(new Participant());
        LocalDate date = LocalDate.of(1, 1, 1);
        expense.setEventId(event.getId());
        expense.setPurchase(purchase);
        expense.setAmount(amount);
        expense.setPayer(payer);
        expense.setDebtors(debtors);
        expense.setDate(date);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(webTarget);
        when(webTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.post(any(Entity.class), eq(Expense.class))).thenReturn(expense);
        assertEquals(expense, communicator.createExpense(
                event, purchase, amount, payer, debtors, date));
        verify(client).target(anyString());
    }

    @Test
    public void testGet() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Expense expense = new Expense();
        expense.setId(1L);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(Expense.class)).thenReturn(expense);
        assertEquals(expense, communicator.getExpense(1L));
        verify(client).target(anyString());
    }

    @Test
    public void testUpdate() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Expense expense = new Expense();
        expense.setId(1L);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.put(any(Entity.class), eq(Expense.class))).thenReturn(expense);
        assertEquals(expense, communicator.updateExpense(1L, expense));
        verify(client).target(anyString());
    }

    @Test
    public void testDelete() {
        WebTarget webTarget = mock(WebTarget.class);
        WebTarget pathTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Expense expense = new Expense();
        expense.setId(1L);
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.path(anyString())).thenReturn(pathTarget);
        when(pathTarget.resolveTemplate(eq("id"), any())).thenReturn(pathTarget);
        when(pathTarget.request(APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(APPLICATION_JSON)).thenReturn(builder);
        when(builder.delete(Expense.class)).thenReturn(expense);
        assertEquals(expense, communicator.deleteExpense(1L));
        verify(client).target(anyString());
    }
}
