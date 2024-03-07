package commons;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.*;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String inviteCode;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses;

    private Date creationDate;
    private Date lastActivity;


    /**
     *
     * @param name - string name of event
     * @param inviteCode - invite code for other participants
     * @param participants - list of participants
     * @param creationDate - creation date
     * @param lastActivity - last activity date
     */
    public Event(String name, String inviteCode,
                 List<Participant> participants, Date creationDate, Date lastActivity) {
        this.name = name;
        this.inviteCode = inviteCode;
        this.participants = participants;
        this.creationDate = creationDate;
        this.lastActivity = lastActivity;
        this.expenses = new ArrayList<>();
    }

    public Event() {

    }

    /**
     * Getter method for id
     * @return int id
     */
    public long getId() {
        return id;
    }

    /**
     * Setter method for id
     * @param id - int to set id
     */
    public void setId(long id) {
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
     * Method to add participants
     * @param participant - to be added
     */
    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    /**
     * Method to add expenses.
     * Mainly for testing. In production expenses are to be added by JPA.
     * @param expense - to be added
     */
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    /**
     * Equals function for Event
     * @param o Object for the Event to be compared to
     * @return true if the two objects are equal and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id && Objects.equals(name, event.name) &&
                Objects.equals(inviteCode, event.inviteCode) &&
                Objects.equals(participants, event.participants) &&
                Objects.equals(creationDate, event.creationDate) &&
                Objects.equals(lastActivity, event.lastActivity) &&
                Objects.equals(expenses, event.expenses);
    }

    /**
     * Hash code function for Event
     * @return the hash code for the specific object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, inviteCode, participants,
                creationDate, lastActivity, expenses);
    }

    /**
     * To String method
     * @return String representation of object
     */
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", participants=" + participants +
                ", creationDate=" + creationDate +
                ", lastActivity=" + lastActivity +
                '}';
    }
}
