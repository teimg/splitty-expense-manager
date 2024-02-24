package commons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String inviteCode;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("event")
    private List<Participant> participants;
    private Date creationDate;
    private Date lastActivity;

    /**
     *
     * @param id - id to identify event
     * @param name - string name of event
     * @param inviteCode - invite code for other participants
     * @param participants - list of participants
     * @param creationDate - creation date
     * @param lastActivity - last activity date
     */
    public Event(int id, String name, String inviteCode,
                 List<Participant> participants, Date creationDate, Date lastActivity) {
        this.id = id;
        this.name = name;
        this.inviteCode = inviteCode;
        this.participants = participants;
        this.creationDate = creationDate;
        this.lastActivity = lastActivity;
    }

    /**
     * Getter method for id
     * @return int id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter method for id
     * @param id - int to set id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for name
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for name
     * @param name - String to set name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for inviteCode
     * @return String inviteCode
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * Setter method for inviteCode
     * @param inviteCode - String to set inviteCode
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * Getter method for participants
     * @return List of participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Getter method for creation date
     * @return Date creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Setter method for creation date
     * @param creationDate - Date to set creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Getter method for lastActivityDate
     * @return Date lastActivityDate
     */
    public Date getLastActivity() {
        return lastActivity;
    }

    /**
     * Setter method for lastActivityDate
     * @param lastActivity - Date to set lastActivityDate
     */
    public void setLastActivity(Date lastActivity) {
        this.lastActivity = lastActivity;
    }

    /**
     * Equals method for Event class
     * @param o - Other object
     * @return boolean based on equality.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id || !Objects.equals(name, event.name)
                || !Objects.equals(inviteCode, event.inviteCode)) return false;
        if (!Objects.equals(participants, event.participants)) return false;
        if (!Objects.equals(creationDate, event.creationDate)) return false;
        return Objects.equals(lastActivity, event.lastActivity);
    }

    /**
     * HashCode method for Event class
     * @return int hashcode
     */
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (inviteCode != null ? inviteCode.hashCode() : 0);
        result = 31 * result + (participants != null ? participants.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (lastActivity != null ? lastActivity.hashCode() : 0);
        return result;
    }
}
