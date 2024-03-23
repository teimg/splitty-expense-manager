package commons.event.changes;

import commons.Event;

public class EventChange {
    private Event event;

    public EventChange(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
