package server.service;

import commons.Event;
import commons.EventChange;
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
        longPolls.putIfAbsent(event.getId(), new HashSet<>());
        longPolls.get(event.getId()).add(result);
    }

    public void sendChange(EventChange change) {
        System.out.println("Change sent: " + change.toString());
        websocketMsgs.convertAndSend("/topic/events", change);

        var results = longPolls.get(change.getEvent().getId());
        if (results == null) {
            return;
        }
        for (var result : results) {
            result.setResult(change);
        }
    }

    public Map<Long, Set<DeferredResult<EventChange>>> getLongPolls() {
        return longPolls;
    }

}
