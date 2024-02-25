package client.utils;


import commons.Participant;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.List;
import java.util.Set;

public class DebtorSelector {

    private Set<Participant> debitors;

    /**
     * Creates debtor selector for the add/edit Expenses
     */
    public DebtorSelector() {
    }


    /**
     * Add one participant to the debitors list
     * @param participant
     */
    public void add(Participant participant){
        if(participant == null){
            return;
        }
        debitors.add(participant);
    }

    /**
     * All particepatns get added to debitors list
     * @param participants that get added to the list
     */
    public void addAll(List<Participant> participants){
        if (participants == null){
            return;
        }
        debitors.addAll(participants);
    }

    /**
     * removes a participant
     * @param participant that gets unchecked in the add menu
     */
    public void remove(Participant participant){
        debitors.remove(participant);
    }

    /**
     *
     * @param participants that need to be removed (if they all get removed at once)
     */
    public void removeAll(List<Participant> participants){
        for (Participant x : participants){
            participants.remove(x);
        }
    }


}
