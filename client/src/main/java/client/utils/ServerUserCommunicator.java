package client.utils;

import com.google.inject.Inject;

import commons.BankAccount;
import commons.Event;
import commons.Expense;
import commons.Participant;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUserCommunicator implements IServerUserCommunicator {

    private final String origin;

    @Inject
    public ServerUserCommunicator(ClientConfiguration config) {
        origin = config.getServer();
    }

    @Override
    public Event createEvent(String name) {
        return null;
    }

    public Event createEvent(Event event) {
        event.addParticipant(new Participant("Alice"));
        event.addParticipant(new Participant("Bob"));
        event.setInviteCode("XYZ");
        return event;
//        return ClientBuilder.newClient(new ClientConfig())
//                .target(origin).path("api/event")
//                .request(APPLICATION_JSON)
//                .accept(APPLICATION_JSON)
//                .post(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    @Override
    public Event getEvent(long id) {
        return null;
    }

    @Override
    public Event updateEvent(Event event) {
        return null;
    }

    @Override
    public Event deleteEvent(long id) {
        return null;
    }

    @Override
    public Participant createParticipant(Event event, String name,
                                         String email, BankAccount bankAccount) {
        Participant toServer = new Participant(name, email);
        toServer.setEvent(event);
        toServer.setBankAccount(bankAccount);
        return ClientBuilder.newClient()
                .target(origin).path("api/participants")
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .post(Entity.entity(toServer, APPLICATION_JSON), Participant.class);
    }

    @Override
    public Participant getParticipant(long id) {
        return ClientBuilder.newClient()
                .target(origin).path("api/participants/{id}")
                .resolveTemplate("id", id)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .get(Participant.class);
    }

    @Override
    public Participant updateParticipant(Participant participant) {
        return ClientBuilder.newClient()
                .target(origin).path("api/participants/{id}")
                .resolveTemplate("id", participant.getId())
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .put(Entity.entity(participant, APPLICATION_JSON), Participant.class);
    }

    @Override
    public Participant deleteParticipant(long id) {
        return ClientBuilder.newClient()
                .target(origin).path("api/participants/{id}")
                .resolveTemplate("id", id)
                .request(APPLICATION_JSON).accept(APPLICATION_JSON)
                .delete(Participant.class);
    }

    @Override
    public Expense createExpense(Event event, String purchase, double amount, Participant payer,
                                 List<Participant> debtors, LocalDate date) {
        Expense toServer = new Expense();
        toServer.setEvent(event);
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
