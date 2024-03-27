package client.ModelView;

import client.utils.ExpenseBuilder;
import client.utils.WhoPaidSelector;
import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IExpenseCommunicator;
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
//    private ObjectProperty<ObservableList<Participant>> whoPaidItems;

    private BooleanProperty evenlyCheckbox;

    private ObjectProperty<ObservableList<Pair<Participant, BooleanProperty>>> debtors;

    private ObjectProperty<Tag> tagField;


    private ExpenseBuilder expenseBuilder;

    private WhoPaidSelector whoPaidSelector;

    private Event event;


    // true if input is an edit meaning that the current expense is being edited
    private boolean isEdit;

    // Only used when there is an edit going on
    private Expense expense;


    private final IExpenseCommunicator expenseCommunicator;
    private final IEventCommunicator eventCommunicator;


    @Inject
    public AddEditExpenseMv(IExpenseCommunicator expenseCommunicator, IEventCommunicator eventCommunicator) {
        this.expenseCommunicator = expenseCommunicator;
        this.eventCommunicator = eventCommunicator;

        priceField = new SimpleStringProperty("");
        descriptionField = new SimpleStringProperty("");
        currencyField = new SimpleStringProperty("");
        evenlyCheckbox = new SimpleBooleanProperty(true);
        dateField = new SimpleObjectProperty<>(LocalDate.now());
        whoPaidField = new SimpleObjectProperty<>();
        tagField = new SimpleObjectProperty<>(null);
        debtors = new SimpleObjectProperty<>(FXCollections.observableArrayList());


    }

    public void loadInfo(Event event) {
        this.event = event;
        expenseBuilder = new ExpenseBuilder();

        for(var x : event.getParticipants()){
            debtors.get().add(new Pair<>(x,  new SimpleBooleanProperty(false)));
        }
    }

    public void loadExpense( Expense expense) {
        this.event = event;
        this.expense = expense;

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
        priceField.setValue(String.valueOf(expense.getAmount()));
        dateField.set(expense.getDate());
    }



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
        // Code should be moved to a different class, so it can be tested
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

        }catch (NumberFormatException e){
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

        }catch (DateTimeParseException e){
            // Not a very nice solution, but will work for now
            return LocalDate.now();
        }
    }

    public  Participant getPayer(){
        System.out.println(whoPaidField.getValue());

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
     * create the final expense
     *
     * @return an expense
     */
    public Expense createExpense(){

        expenseBuilder.setPayer(getPayer());
        expenseBuilder.setPurchase(getPurchase());
        expenseBuilder.setDate(getDateFieldValue());
        expenseBuilder.setAmount(getPriceFieldValue());
        expenseBuilder.setDebtors(getDebtors());
        expenseBuilder.setEvent(event);

        if(this.expense != null){
            return updateExpense();
        }

//        System.out.println(expenseBuilder.toString());
        Expense res = expenseBuilder.build();


        res = expenseCommunicator.createExpense(res);
        this.event = eventCommunicator.updateEvent(event);

        return res;

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

//    public ObjectProperty<ObservableList<Participant>> whoPaidItemsProperty() {
//        return whoPaidItems;
//    }

    public List<Participant> getParticipants(){
        return this.event.getParticipants();
    }

    public Event getEvent() {
        return event;
    }

}
