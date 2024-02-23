package client.utils;

import commons.Event;
import commons.Expense;
import commons.Participant;

import java.util.List;

/**
 * An interface for client communications with the server for user requirements.
 */
public interface IServerUserCommunicator {

    /**
     * Registers a new user to the application.
     * @param firstName user's first name
     * @param lastName user's last name
     * @return the new Participant
     */
    Participant registerParticipant(String firstName, String lastName);

    /**
     * Gets an existing Participant by id.
     * @param id Participant unique id
     * @return the corresponding Participant
     */
    Participant getParticipant(int id);

    /**
     * Creates a new Event with a specified name. The name does not have to be unique.
     * @param name the name of the Event to create
     * @param creator the creator Participant should be added automatically
     * @return the created Event
     */
    Event registerEvent(String name, Participant creator);

    /**
     * Gets an existing Event by id.
     * @param id Event unique id
     * @return the corresponding Event
     */
    Event getEvent(int id);

    /**
     * Makes a Participant join an Event by its invite code.
     * @param inviteCode the invite code for the Event
     * @param toJoin the Participant to join the Event
     * @return the Event that was joined
     */
    Event joinEvent(String inviteCode, Participant toJoin);

    /**
     * Adds a made expense to an Event.
     * @param event the Event to add the expense to
     * @param purchase what was purchased with this expense
     * @param amount the amount paid
     * @param payer the Participant who paid
     * @param debtors the Participants who have accrued debt with this expense
     * @return the created Expense
     */
    Expense addExpense(Event event, String purchase, double amount, Participant payer,
                              List<Participant> debtors);

    /**
     * Gets a List of Expenses registered for an Event.
     * @param event the Event to query Expenses for
     * @return the List of Expenses
     */
    List<Expense> getExpenses(Event event);
}
