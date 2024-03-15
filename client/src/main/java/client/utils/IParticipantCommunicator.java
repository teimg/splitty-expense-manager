package client.utils;

import commons.BankAccount;
import commons.Event;
import commons.Participant;

public interface IParticipantCommunicator {
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

}
