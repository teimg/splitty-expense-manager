package server.service;

import commons.Event;
import commons.EventChange;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventChangeServiceTest {

    @Mock
    private SimpMessagingTemplate websocketMsgs;

    private EventChangeService eventChangeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        eventChangeService = new EventChangeService(websocketMsgs);
    }

    @Test
    public void sendChange() {
        Participant p1 = new Participant("name1", "email1");
        Participant p2 = new Participant("name2", "email2");
        List<Participant> participants = List.of(p1, p2);
        Date creationDate = new Date(2024, 2, 10);
        Date lastActivity = new Date(2024, 10, 10);
        Event e = new Event("name", "inviteCode", participants, creationDate, lastActivity);
        doNothing().when(websocketMsgs).convertAndSend((String) any(), (EventChange) any());
        eventChangeService.sendChange(new EventChange(EventChange.Type.CREATION, e));
        eventChangeService.addLongPoll(e.getId(), new DeferredResult<>());
        eventChangeService.sendChange(new EventChange(EventChange.Type.CREATION, e));
    }

//    @Test
//    public void addLongPoll() {
//        DeferredResult<EventChange> dr = new DeferredResult<>();
//        eventChangeService.addLongPoll(new Event(), dr);
//        assertEquals(1, eventChangeService.getLongPolls().size());
//    }

}

