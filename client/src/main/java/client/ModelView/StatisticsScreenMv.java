package client.ModelView;

import commons.Event;
import commons.Expense;
import commons.Tag;

import java.util.HashMap;
import java.util.Map;

public class StatisticsScreenMv {

    private Event event;

    private double totalPrice;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Map<Tag, Double> fillEntries() {
        Map<Tag, Double> entries = new HashMap<>();
        this.totalPrice = 0;
        for (Expense expense : event.getExpenses()) {
            totalPrice = totalPrice + expense.getAmount();
            Tag tag = expense.getTag();
            if (tag != null) {
                if (entries.containsKey(tag)) {
                    double currentSum = entries.get(tag);
                    entries.put(tag, currentSum + expense.getAmount());
                } else {
                    entries.put(tag, expense.getAmount());
                }
            }
        }
        return entries;
    }
}
