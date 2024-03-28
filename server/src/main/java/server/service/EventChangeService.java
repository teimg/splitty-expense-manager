package server.service;

import commons.EventChange;
import org.springframework.http.ResponseEntity;
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
    private final Map<Long, Set<DeferredResult<ResponseEntity<EventChange>>>> longPolls;

    EventChangeService(SimpMessagingTemplate websocketMsgs) {
        this.websocketMsgs = websocketMsgs;
        this.longPolls = new HashMap<>();
    }

    public void addLongPoll(Long eventId, DeferredResult<ResponseEntity<EventChange>> result) {
        longPolls.putIfAbsent(eventId, new HashSet<>());
        longPolls.get(eventId).add(result);
    }

    public void removeLongPoll(Long eventId, DeferredResult<ResponseEntity<EventChange>> result) {
        longPolls.get(eventId).remove(result);
    }

    public void sendChange(EventChange change) {
        System.out.println("Change sent: " + change.toString());
        websocketMsgs.convertAndSend("/topic/events", change);

        var results = longPolls.get(change.getEvent().getId());
        if (results == null) {
            return;
        }
        for (var result : results) {
            result.setResult(ResponseEntity.ok(change));
        }
    }
}
