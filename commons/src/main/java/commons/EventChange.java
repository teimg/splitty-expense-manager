package commons;

import java.util.Objects;

public class EventChange {
    public enum Type {
        CREATION,
        MODIFICATION,
        DELETION
    }

    private Type type;
    private Event event;

    public EventChange() {
    }

    public EventChange(Type type, Event event) {
        this.type = type;
        this.event = event;
    }

    public Type getType() {
        return type;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "EventChange{" +
                "type=" + type +
                ", event=Event{id=" + event.getId() +
                ", name='" + event.getName() + '\'' +
                ", inviteCode='" + event.getInviteCode() + "'}" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventChange that = (EventChange) o;

        if (type != that.type) return false;
        return Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (event != null ? event.hashCode() : 0);
        return result;
    }
}
