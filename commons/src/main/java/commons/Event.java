package commons;

import java.util.*;

public class Event {
    private int id;
    private String name;
    private String inviteCode;
    private List<Participant> participants;
    private Date creationDate;
    private Date lastActivity;

    /**
     *
     * @param id
     * @param name
     * @param inviteCode
     * @param participants
     * @param creationDate
     * @param lastActivity
     */
    public Event(int id, String name, String inviteCode, List<Participant> participants, Date creationDate, Date lastActivity) {
        this.id = id;
        this.name = name;
        this.inviteCode = inviteCode;
        this.participants = participants;
        this.creationDate = creationDate;
        this.lastActivity = lastActivity;
    }

}
