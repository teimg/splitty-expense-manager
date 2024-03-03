package client.utils;

import commons.BankAccount;
import commons.Event;
import commons.Expense;
import commons.Participant;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * An interface for client communications with the server for user requirements.
 */
public interface IServerUserCommunicator {

    /**
     * Creates a new event.
     * @param name name of the event
     * @return the created event
     */
    Event createEvent(String name);

    /**
     * Gets an existing event by id.
     * @param id event id
     * @return the event
     */
    Event getEvent(long id);

    /**
     * Updates an existing event.
     * @param event the event with updated fields
     * @return the updated event
     */
    Event updateEvent(Event event);

    /**
     * Deletes an existing event.
     * @param id event id
     * @return the deleted event
     */
    Event deleteEvent(long id);

    /**
     * Creates a new participant.
     * @param event event the participant belongs to
     * @param name name of the participant
     * @param email email of the participant or null
     * @param bankAccount bank account of the participant or null
     * @return the created participant
     */
    Participant createParticipant(Event event, String name, String email, BankAccount bankAccount);

    /**
     * Gets an existing participant by id.
     * @param id participant id
     * @return the participant
     */
    Participant getParticipant(long id);

    /**
     * Updates an existing participant.
     * @param participant the participant with updated fields
     * @return the updated participant
     */
    Participant updateParticipant(Participant participant);

    /**
     * Deletes an existing participant.
     * @param id participant id
     * @return the deleted participant
     */
    Participant deleteParticipant(long id);

    /**
     * Creates a new expense.
     * @param event event the expense belongs to
     * @param purchase name of the purchase
     * @param amount amount of money spent
     * @param payer participant who paid
     * @param debtors participants who profited
     * @param date date the expense was made
     * @return the created expense
     */
    Expense createExpense(Event event, String purchase, double amount,
                          Participant payer, List<Participant> debtors, LocalDate date);

    /**
     * Gets an existing expense by id.
     * @param id expense id
     * @return the expense
     */
    Expense getExpense(long id);

    /**
     * Updates an existing expense.
     * @param expense the expense with updated fields
     * @return the updated expense
     */
    Expense updateExpense(Expense expense);

    /**
     * Deletes an existing expense.
     * @param id expense id
     * @return the deleted expense
     */
    Expense deleteExpense(long id);

    /**
     * Gets a list of all expenses in an event.
     * @param eventId event id
     * @return the expenses in that event
     */
    Collection<Expense> getAllExpensesForEvent(long eventId);
}
