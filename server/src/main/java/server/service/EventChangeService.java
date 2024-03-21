package server.service;

import commons.Event;
import commons.event.changes.EventChange;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventChangeService {
    private final SimpMessagingTemplate websocketMsgs;
    private final Map<Event, Set<DeferredResult<EventChange>>> longPolling;

    EventChangeService(SimpMessagingTemplate websocketMsgs) {
        this.websocketMsgs = websocketMsgs;
        this.longPolling = new HashMap<>();
    }

    public void addLongPolling(Event event, DeferredResult<EventChange> result) {
        longPolling.getOrDefault(event, new HashSet<>()).add(result);
    }

    public void sendChange(EventChange change) {
        websocketMsgs.convertAndSend(change);

        var results = longPolling.get(change.getEvent());
        if (results == null) {
            return;
        }
        for (var result : results) {
            result.setResult(change);
        }
    }
}
