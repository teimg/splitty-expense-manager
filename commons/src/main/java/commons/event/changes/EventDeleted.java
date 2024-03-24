package commons.event.changes;

import commons.Event;

public class EventDeleted extends EventChange {
    public EventDeleted() {}
    public EventDeleted(Event event) {
        super(event);
    }
}
