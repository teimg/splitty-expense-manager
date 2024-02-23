package client.utils;

import commons.Event;
import commons.Expense;
import commons.Participant;

import java.util.ArrayList;
import java.util.List;

/**
 * A configurable dummy IServerUserCommunicator for unit testing.
 */
public class DummyServerUserCommunicator implements IServerUserCommunicator {
    private Participant registerParticipantOutput;
    private Participant getParticipantOutput;
    private Event registerEventOutput;
    private Event getEventOutput;
    private Event joinEventOutput;
    private Expense addExpenseOutput;
    private List<Expense> getExpensesOutput;

    private final ArrayList<String> operationsLog;

    /**
     * Constructs a new dummy IServerUserCommunicator with no dummy outputs set.
     */
    public DummyServerUserCommunicator() {
        operationsLog = new ArrayList<>();
    }

    /**
     * Sets dummy output for the registerParticipant method.
     * @param registerParticipantOutput dummy value to return
     */
    public void setRegisterParticipantOutput(Participant registerParticipantOutput) {
        this.registerParticipantOutput = registerParticipantOutput;
    }

    /**
     * Sets dummy output for the getParticipant method.
     * @param getParticipantOutput dummy value to return
     */
    public void setGetParticipantOutput(Participant getParticipantOutput) {
        this.getParticipantOutput = getParticipantOutput;
    }

    /**
     * Sets dummy output for the registerEvent method.
     * @param registerEventOutput dummy value to return
     */
    public void setRegisterEventOutput(Event registerEventOutput) {
        this.registerEventOutput = registerEventOutput;
    }

    /**
     * Sets dummy output for the getEvent method.
     * @param getEventOutput dummy value to return
     */
    public void setGetEventOutput(Event getEventOutput) {
        this.getEventOutput = getEventOutput;
    }

    /**
     * Sets dummy output for the joinEvent method.
     * @param joinEventOutput dummy value to return
     */
    public void setJoinEventOutput(Event joinEventOutput) {
        this.joinEventOutput = joinEventOutput;
    }

    /**
     * Sets dummy output for the addExpense method.
     * @param addExpenseOutput dummy value to return
     */
    public void setAddExpenseOutput(Expense addExpenseOutput) {
        this.addExpenseOutput = addExpenseOutput;
    }

    /**
     * Sets dummy output for the getExpenses method.
     * @param getExpensesOutput dummy value to return
     */
    public void setGetExpensesOutput(List<Expense> getExpensesOutput) {
        this.getExpensesOutput = getExpensesOutput;
    }

    /**
     * Returns set dummy output, or null if no dummy output was set
     * @param firstName user's first name
     * @param lastName user's last name
     * @return dummy output
     */
    @Override
    public Participant registerParticipant(String firstName, String lastName) {
        operationsLog.add("registerParticipant " + firstName + " " + lastName);
        return registerParticipantOutput;
    }

    /**
     * Returns set dummy output, or null if no dummy output was set
     * @param id Participant unique id
     * @return dummy output
     */
    @Override
    public Participant getParticipant(int id) {
        operationsLog.add("getParticipant " + id);
        return getParticipantOutput;
    }

    /**
     * Returns set dummy output, or null if no dummy output was set
     * @param name the name of the Event to create
     * @param creator the creator Participant should be added automatically
     * @return dummy output
     */
    @Override
    public Event registerEvent(String name, Participant creator) {
        operationsLog.add("registerEvent " + name + " " + creator.toString());
        return registerEventOutput;
    }

    /**
     * Returns set dummy output, or null if no dummy output was set
     * @param id Event unique id
     * @return dummy output
     */
    @Override
    public Event getEvent(int id) {
        operationsLog.add("getEvent " + id);
        return getEventOutput;
    }

    /**
     * Returns set dummy output, or null if no dummy output was set
     * @param inviteCode the invite code for the Event
     * @param toJoin the Participant to join the Event
     * @return dummy output
     */
    @Override
    public Event joinEvent(String inviteCode, Participant toJoin) {
        operationsLog.add("joinEvent " + inviteCode + " " + toJoin.toString());
        return joinEventOutput;
    }

    /**
     * Returns set dummy output, or null if no dummy output was set
     * @param event the Event to add the expense to
     * @param purchase what was purchased with this expense
     * @param amount the amount paid
     * @param payer the Participant who paid
     * @param debtors the Participants who have accrued debt with this expense
     * @return dummy output
     */
    @Override
    public Expense addExpense(Event event, String purchase, double amount, Participant payer,
                              List<Participant> debtors) {
        operationsLog.add("addExpense %s %s %s %s %s"
                .formatted(event.toString(), purchase, amount, payer.toString(), debtors.toString()));
        return addExpenseOutput;
    }

    /**
     * Returns set dummy output, or null if no dummy output was set
     * @param event the Event to query Expenses for
     * @return dummy output
     */
    @Override
    public List<Expense> getExpenses(Event event) {
        operationsLog.add("getExpenses " + event.toString());
        return getExpensesOutput;
    }

    /**
     * Get a log of all operations called on this object.
     * @return a List of Strings representing operations
     */
    public List<String> getOperationsLog() {
        return operationsLog;
    }
}
