package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

/**
 * The Tag Class used to store
 * the tag information regarding an expense
 */
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private int red;

    private int green;

    private int blue;

    @JsonIgnore
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Event event;
    @Column(name = "event_id")
    private Long eventId;

    public Tag(String name, int red, int green, int blue, Long eventId) {
        this.name = name;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.eventId = eventId;
    }

    public Tag(Tag tag) {
        this.name = tag.name;
        this.red = tag.red;
        this.green = tag.green;
        this.blue = tag.blue;
        this.eventId = tag.eventId;
    }

    /**
     * No arg constructor for jakarta framework
     */
    public Tag() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if ((id != tag.id) || (red != tag.red)|| (green != tag.green)) return false;
        if (blue != tag.blue) return false;
        return Objects.equals(name, tag.name);
    }

    public boolean standTagEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + red;
        result = 31 * result + green;
        result = 31 * result + blue;
        return result;
    }
}
