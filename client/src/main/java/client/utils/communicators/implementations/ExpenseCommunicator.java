package client.utils.communicators.implementations;

import client.utils.ClientConfiguration;
import client.utils.communicators.interfaces.IExpenseCommunicator;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ExpenseCommunicator implements IExpenseCommunicator {

    private final String origin;

    @Inject
    public ExpenseCommunicator(ClientConfiguration config) {
        origin = config.getServer();
    }

    @Override
    public Expense createExpense(Event event, String purchase, double amount, Participant payer,
                                 List<Participant> debtors, LocalDate date) {
        Expense toServer = new Expense();
        toServer.setEventId(event.getId());
        toServer.setPurchase(purchase);
        toServer.setAmount(amount);
        toServer.setPayer(payer);
        toServer.setDebtors(debtors);
        toServer.setDate(date);
        return ClientBuilder.newClient()
                .target(origin).path("api/expense")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(toServer, APPLICATION_JSON), Expense.class);
    }

    @Override
    public Expense createExpense(Expense expense) {
        return ClientBuilder.newClient()
                .target(origin).path("api/expense")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }

    @Override
    public Expense getExpense(long id) {
        return ClientBuilder.newClient()
                .target(origin).path("api/expense/{id}")
                .resolveTemplate("id", id)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(Expense.class);
    }

    @Override
    public Expense updateExpense(Expense expense) {
        return ClientBuilder.newClient()
                .target(origin).path("api/expense/{id}")
                .resolveTemplate("id", expense.getId())
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .put(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }

    @Override
    public Expense deleteExpense(long id) {
        return ClientBuilder.newClient()
                .target(origin).path("api/expense/{id}")
                .resolveTemplate("id", id)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .delete(Expense.class);
    }

    // The necessary endpoint has not yet been implemented.
    // TODO
    @Override
    public Collection<Expense> getAllExpensesForEvent(long eventId) {
        return null;
    }


}
