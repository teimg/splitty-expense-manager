package client.ModelView;

import client.utils.communicators.interfaces.IEventUpdateProvider;
import com.google.inject.Inject;
import commons.Event;
import commons.EventChange;
import commons.Expense;
import commons.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class StatisticsScreenMv {

    private final IEventUpdateProvider eventUpdateProvider;

    @Inject
    public StatisticsScreenMv(IEventUpdateProvider eventUpdateProvider) {
        this.eventUpdateProvider = eventUpdateProvider;
    }

    private double totalPrice;

    public Event getEvent() {
        return eventUpdateProvider.event();
    }

    public void onUpdate(Consumer<EventChange> handler) {
        eventUpdateProvider.setUpdateHandler(handler);
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
        for (Expense expense : getEvent().getExpenses()) {
            Tag tag = expense.getTag();
            if (tag != null) {
                totalPrice = totalPrice + expense.getAmount();
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

    public double rounder(double amount) {
        return Math.round(amount * 100.0)/100.0;
    }

}
