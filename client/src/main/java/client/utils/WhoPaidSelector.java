package client.utils;

import commons.Participant;

import java.util.ArrayList;
import java.util.List;

public class WhoPaidSelector  {

    private List<Participant> participants;
    private List<String> optionalParticipants;

    public WhoPaidSelector(List<Participant> participants) {
        this.participants = participants;
        optionalParticipants = new ArrayList<>();
    }

    public List<String> query(String query){
        if(query == null || query.isEmpty()){
            return participants.stream().map(x -> x.getName()).toList();
        }

        optionalParticipants = new ArrayList<>();
        query = query.toLowerCase();

        for (Participant participant: participants){
            String name = participant.getName().toLowerCase();

            if(name.startsWith(query)){
                optionalParticipants.add(name.substring(0, 1).toUpperCase() + name.substring(1));
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
