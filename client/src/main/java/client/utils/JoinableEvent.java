package client.utils;

import commons.Event;

import java.io.Serializable;

public record JoinableEvent(long id, String name) implements Serializable {
    public static JoinableEvent fromEvent(Event event) {
        return new JoinableEvent(event.getId(), event.getName());
    }
}