package client.utils;

import commons.Participant;

import java.util.ArrayList;
import java.util.List;

public class WhoPaidSelector  {

    private List<Participant> participants;
    private List<Participant> optionalParticipants;

    public WhoPaidSelector(List<Participant> participants) {
        this.participants = participants;
        optionalParticipants = new ArrayList<>();
    }

    public List<Participant> query(String query){
        if(query == null || query.isEmpty()){
            return new ArrayList<>(participants);
        }

        optionalParticipants = new ArrayList<>();
        query = query.toLowerCase();

        for (Participant participant: participants){
            String name = participant.getName().toLowerCase();

            if(name.startsWith(query)){
                optionalParticipants.add(participant);
            }
        }

        return optionalParticipants;
    }

    public Participant getCurrentPayer(String name) {
        if(name == null){
            return  null;
        }

        for (Participant participant : participants){
            if (participant.getName().equals(name)){
                return participant;
            }
        }

        return  null;
    }

}
