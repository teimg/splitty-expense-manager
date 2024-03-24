package commons.event.changes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import commons.Event;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EventCreated.class, name = "EventCreated"),
    @JsonSubTypes.Type(value = EventModified.class, name = "EventModified"),
    @JsonSubTypes.Type(value = EventDeleted.class, name = "EventDeleted"),
})
public class EventChange {
    private Event event;

    public EventChange() {}

    public EventChange(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
