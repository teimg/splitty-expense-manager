package commons.event.changes;

import commons.Event;

public class EventCreated extends EventChange {
    public EventCreated() {}
    public EventCreated(Event event) {
        super(event);
    }
}
