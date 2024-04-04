package client.ModelView;

import client.currency.Exchanger;
import client.utils.ExpenseBuilder;
import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IExpenseCommunicator;
import client.utils.communicators.interfaces.ITagCommunicator;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AddEditExpenseMv {

    private StringProperty priceField;

    private StringProperty currencyField;

    private StringProperty descriptionField;

    private ObjectProperty<LocalDate> dateField;

    private ObjectProperty<Participant> whoPaidField;

    private BooleanProperty evenlyCheckbox;

    private ObjectProperty<ObservableList<Pair<Participant, BooleanProperty>>> debtors;

    private ObjectProperty<Tag> tagField;

    private ExpenseBuilder expenseBuilder;

    private Event event;

    // Only used when there is an edit going on
    private Expense expense;

    private final IExpenseCommunicator expenseCommunicator;
    private final IEventCommunicator eventCommunicator;
    private final ITagCommunicator tagCommunicator;

    private Exchanger exchanger;

    @Inject
    public AddEditExpenseMv(IExpenseCommunicator expenseCommunicator,
                            IEventCommunicator eventCommunicator,
                            ITagCommunicator tagCommunicator) {
        this.expenseCommunicator = expenseCommunicator;
        this.eventCommunicator = eventCommunicator;
        this.tagCommunicator = tagCommunicator;

        priceField = new SimpleStringProperty("");
        descriptionField = new SimpleStringProperty("");
        currencyField = new SimpleStringProperty("");
        evenlyCheckbox = new SimpleBooleanProperty(true);
        dateField = new SimpleObjectProperty<>(LocalDate.now());
        whoPaidField = new SimpleObjectProperty<>();
        tagField = new SimpleObjectProperty<>(null);
        debtors = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    }

    public void loadInfo(Event event, Exchanger exchanger) {
        this.event = event;
        this.exchanger = exchanger;
        expenseBuilder = new ExpenseBuilder();

        for(var x : event.getParticipants()){
            debtors.get().add(new Pair<>(x,  new SimpleBooleanProperty(false)));
        }
    }

    public void loadExpense(Expense expense, Exchanger exchanger) {
        this.event = event;
        this.expense = expense;
        this.exchanger = exchanger;

        for(var x : debtors.get()){

            if(expense.getDebtors().contains(x.getKey())){
                x.getValue().setValue(true);
            }
        }

        setBuilder();
        setFields();

    }

    public void setBuilder(){
        expenseBuilder = new ExpenseBuilder();
        expenseBuilder.setDebtors(expense.getDebtors());
        expenseBuilder.setTag(expense.getTag());
        expenseBuilder.setDate(expense.getDate());
        expenseBuilder.setPurchase(expense.getPurchase());
        expenseBuilder.setPayer(expense.getPayer());
        expenseBuilder.setAmount((long)(expense.getAmount() * 100));
    }

    public void setFields(){
        descriptionField.setValue(expense.getPurchase());
        whoPaidField.setValue(expense.getPayer());
        descriptionField.setValue(expense.getPurchase());
        priceField.setValue(String.valueOf(
                Math.round(exchanger.getStandardConversion(
                        expense.getAmount(), LocalDate.now()) * 100.0)/100.0
        ));
        dateField.set(expense.getDate());
        tagField.set(expense.getTag());
        currencyField.setValue(exchanger.getCurrentCurrency());
    }

    @SuppressWarnings("unchecked")
    public void clear(){
        for(var x : event.getParticipants()){
            debtors.get().removeLast();
        }

        descriptionField.setValue("");
        priceField.setValue("");
        currencyField.setValue("");
        evenlyCheckbox.setValue(true);
        dateField.setValue(LocalDate.now());
        whoPaidField.setValue(null);

        debtors.getValue().removeAll();

        this.event = null;
        this.expense = null;
    }

    /**
     * get the value of the priceField
     *
     * @return the amount in euro cents right now
     */
    private long getPriceFieldValue() {
        long res = 0;
        String value = priceField.getValue();
        String[] values = value.split(",|\\.");
        try{
            res += Long.parseLong(values[0]) * 100;

            if(values.length == 2){
                res += Long.parseLong(values[1]);
            }

            if(values.length > 2 || res < 0){
                throw new NumberFormatException();
            }

        }
        catch (NumberFormatException e){
            throw new IllegalArgumentException("PriceFieldInvalid");
        }
        return res;
    }

    /**
     * Get the value in date field
     * @return the Date set in the date field
     */

    public LocalDate getDateFieldValue(){
        try{
            return  dateField.getValue();

        }
        catch (DateTimeParseException e){
            return LocalDate.now();
        }
    }

    public  Participant getPayer(){
        Participant res = whoPaidField.getValue();

        if(res == null){
            throw new IllegalArgumentException("PayerFieldInvalid");
        }

        return res;
    }

    public String getPurchase(){
        String res = descriptionField.getValue();

        if(res == null || res.isEmpty()){
            throw new IllegalArgumentException("PurchaseLeftEmpty");
        }
        return  res;
    }

    public List<Participant> getDebtors(){
        if(evenlyCheckbox.getValue()){
            return this.event.getParticipants();
        }
        List<Participant> res = new ArrayList<>();

        for(var x : debtors.get()){
            if(x.getValue().getValue()){
                res.add(x.getKey());
            }
        }

        return res;
    }

    public Tag getTag(){
        Tag res = tagField.getValue();

        if(res == null){
            throw new IllegalArgumentException("TagInvalid");
        }

        return res;
    }

    public String getCur(){
        String cur = currencyField.getValue();

        if(cur == null || cur.isEmpty()){
            throw new IllegalArgumentException("SelectACurrency");
        }

        return cur;
    }

    private Expense updateExpense() {
        Expense res = expenseBuilder.build();

        expense.setAmount(getPriceFieldValue());
        expenseCommunicator.deleteExpense(expense.getId());
        res = expenseCommunicator.createExpense(res);
//        res = expenseCommunicator.updateExpense(expense.getId(), res);
        this.event = eventCommunicator.updateEvent(event);

        return res;
    }


    /**
     * Creates expense
     * @return expense
     */
    public Expense createExpense(){
        expenseBuilder.setPayer(getPayer());
        expenseBuilder.setPurchase(getPurchase());
        expenseBuilder.setDate(getDateFieldValue());
        expenseBuilder.setAmount(
                (long) exchanger.getExchangeAgainstNew(
                        getPriceFieldValue(), getCur(), getDateFieldValue())
        );
        expenseBuilder.setDebtors(getDebtors());
        expenseBuilder.setTag(getTag());
        expenseBuilder.setEventId(event.getId());

        if(this.expense != null){
            return updateExpense();
        }

        Expense res = expenseBuilder.build();


        res = expenseCommunicator.createExpense(res);
        this.event = eventCommunicator.updateEvent(event);

        return res;
    }

    public void deleteTag() {
        this.tagCommunicator.deleteTag(getTag().getId());
    }

    public StringProperty priceFieldProperty() {
        return priceField;
    }

    public StringProperty currencyFieldProperty() {
        return currencyField;
    }

    public StringProperty descriptionFieldProperty() {
        return descriptionField;
    }

    public ObjectProperty<LocalDate> dateFieldProperty() {
        return dateField;
    }

    public ObjectProperty<Participant> whoPaidFieldProperty() {
        return whoPaidField;
    }

    public BooleanProperty evenlyCheckboxProperty() {
        return evenlyCheckbox;
    }

    public ObjectProperty<ObservableList<Pair<Participant, BooleanProperty>>> debtorsProperty() {
        return debtors;
    }

    public ObjectProperty<Tag> tagFieldProperty() {
        return tagField;
    }

    public List<Participant> getParticipants(){
        return this.event.getParticipants();
    }

    public List<Tag> getTags(){
        return tagCommunicator.getAllTags();
    }

    public Event getEvent() {
        return event;
    }

}
