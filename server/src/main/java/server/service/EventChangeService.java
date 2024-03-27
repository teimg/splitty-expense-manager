package server.service;

import commons.Event;
import commons.event.changes.EventChange;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class EventChangeService {
    private final SimpMessagingTemplate websocketMsgs;
    private final Map<Long, Set<DeferredResult<EventChange>>> longPolls;

    EventChangeService(SimpMessagingTemplate websocketMsgs) {
        this.websocketMsgs = websocketMsgs;
        this.longPolls = new HashMap<>();
    }

    public void addLongPoll(Event event, DeferredResult<EventChange> result) {
        longPolls.getOrDefault(event.getId(), new HashSet<>()).add(result);
    }

    public void sendChange(EventChange change) {
        websocketMsgs.convertAndSend("/topic/events", change);

        var results = longPolls.get(change.getEvent().getId());
        if (results == null) {
            return;
        }
        for (var result : results) {
            result.setResult(change);
        }
    }
}