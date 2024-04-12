package client.utils.communicators.interfaces;

import commons.Event;
import commons.Expense;
import commons.Participant;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface IExpenseCommunicator {

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
     * Creates a new expense
     * @param expense expense object
     * @return the created expense
     */
    Expense createExpense(Expense expense);

    /**
     * Gets an existing expense by id.
     * @param id expense id
     * @return the expense
     */
    Expense getExpense(long id);

    /**
     * Updates an existing expense.
     * @param id of the event to update
     * @param expense the expense with updated fields
     * @return the updated expense
     */
    Expense updateExpense(long id, Expense expense);

    /**
     * Deletes an existing expense.
     * @param id expense id
     * @return the deleted expense
     */
    Expense deleteExpense(long id);

}
