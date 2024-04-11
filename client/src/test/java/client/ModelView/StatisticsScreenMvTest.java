package client.ModelView;

import client.utils.communicators.interfaces.IEventUpdateProvider;
import commons.Event;
import commons.Expense;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsScreenMvTest {

    private StatisticsScreenMv statisticsScreenMv;

    @Mock
    private Event event;

    @Mock
    private IEventUpdateProvider eventUpdateProvider;

    @BeforeEach
    void setup() {
        statisticsScreenMv = new StatisticsScreenMv(eventUpdateProvider);
    }

    @Test
    public void testFillEntries() {
        when(eventUpdateProvider.event()).thenReturn(event);

        Tag tag1 = new Tag("Tag1", 1,1,1, event.getId());
        Tag tag2 = new Tag("Tag2",1,1,1, event.getId());

        Expense expense1 = new Expense(1L, "Expense 1", 100.0, null, null, null, tag1);
        Expense expense2 = new Expense(2L, "Expense 2", 200.0, null, null, null, tag2);
        Expense expense3 = new Expense(3L, "Expense 3", 150.0, null, null, null, tag1);

        when(event.getExpenses()).thenReturn(Arrays.asList(expense1, expense2, expense3));

        Map<Tag, Double> entries = statisticsScreenMv.fillEntries();

        Map<Tag, Double> expectedEntries = new HashMap<>();
        expectedEntries.put(tag1, 250.0);
        expectedEntries.put(tag2, 200.0);

        assertEquals(expectedEntries.size(), entries.size());
        for (Map.Entry<Tag, Double> entry : entries.entrySet()) {
            assertEquals(expectedEntries.get(entry.getKey()), entry.getValue());
        }

        assertEquals(450.0, statisticsScreenMv.getTotalPrice());
    }

    @Test
    void testGetEvent() {
        when(eventUpdateProvider.event()).thenReturn(event);
        assertEquals(event, statisticsScreenMv.getEvent());
    }

    @Test
    void testSetEvent() {
        when(eventUpdateProvider.event()).thenReturn(event);
        Event newEvent = new Event();
        when(eventUpdateProvider.event()).thenReturn(newEvent);
        assertEquals(newEvent, statisticsScreenMv.getEvent());
    }

    @Test
    void testSetTotalPrice() {
        double newTotalPrice = 200.0;
        statisticsScreenMv.setTotalPrice(newTotalPrice);
        assertEquals(newTotalPrice, statisticsScreenMv.getTotalPrice());
    }
}
