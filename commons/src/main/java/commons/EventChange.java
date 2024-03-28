package commons;

public class EventChange {
    public enum Type {
        CREATION,
        MODIFICATION,
        DELETION
    }
    private Type type;
    private Event event;

    public EventChange() {}

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
}
