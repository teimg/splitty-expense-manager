package client.utils;


import commons.Participant;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DebtorSelector {

    private boolean allSelected;

    private List<Participant> participants;
    private Set<Participant> debitors;

    /**
     * Creates debtor selector for the add/edit Expenses
     */
    public DebtorSelector(List<Participant> participants) {
        this.participants = participants;
        this.debitors = new HashSet<>();
        this.allSelected = true;
    }

    /**
     * get the participant from list of participant
     * @param participantName name of participant
     * @return participant
     */

    private Participant getParticipant(String participantName){

        for(Participant x : participants){
            if(x.getName().equals(participantName)){
                return  x;
            }
        }

        throw new  IllegalArgumentException();
    }

    /**
     * Add one participant to the debitors list
     * @param participant participants name that needs to be added to debitors list
     */
    public void add(String participant){
        if(participant == null){
            return;
        }
        debitors.add(getParticipant(participant));
    }

    /**
     * Add all particapents to
     */
    public void addAll(){
        this.debitors.addAll(participants);
    }

    /**
     * removes a participant
     * @param participant that gets unchecked in the add menu
     */
    public void remove(String participant){
        if(participant == null){
            return;
        }
        debitors.remove(getParticipant(participant));
    }

    /**
     * Removes all debitors
     */
    public void removeAll(){
        this.debitors = new HashSet<Participant>();
    }

    public List<Participant> getDebitors(){
        if(allSelected){
            return participants;
        }
        return new ArrayList<>(debitors);

    }

    public boolean isAllSelected() {
        return allSelected;
    }

    public void setAllSelected(boolean allSelected) {
        this.allSelected = allSelected;
    }


}
