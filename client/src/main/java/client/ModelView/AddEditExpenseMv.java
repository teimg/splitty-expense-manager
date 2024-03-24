package client.ModelView;

import client.utils.ExpenseBuilder;
import client.utils.WhoPaidSelector;
import client.utils.communicators.interfaces.IExpenseCommunicator;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
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

    private StringProperty whoPaidField;
    private ObjectProperty<ObservableList<String>> whoPaidItems;

    private BooleanProperty evenlyCheckbox;

    private ObjectProperty<ObservableList<Pair<Participant, BooleanProperty>>> debtors;


    private ExpenseBuilder expenseBuilder;


    private WhoPaidSelector whoPaidSelector;

    private Event event;


    // true if input is an edit meaning that the current expense is being edited
    private boolean isEdit;

    // Only used when there is an edit going on
    private Expense expense;


    private final IExpenseCommunicator expenseCommunicator;


    @Inject
    public AddEditExpenseMv(IExpenseCommunicator iExpenseCommunicator) {
        this.expenseCommunicator = iExpenseCommunicator;

        priceField = new SimpleStringProperty("");
        descriptionField = new SimpleStringProperty("");
        currencyField = new SimpleStringProperty("");
        evenlyCheckbox = new SimpleBooleanProperty(true);
        dateField = new SimpleObjectProperty<>(LocalDate.now());
        whoPaidField = new SimpleStringProperty("");
        whoPaidItems = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        debtors = new SimpleObjectProperty<>(FXCollections.observableArrayList());


    }

    public void loadInfo(Event event) {
        this.event = event;
        initWhoPaid();
        expenseBuilder = new ExpenseBuilder();

        for(var x : event.getParticipants()){
            debtors.get().add(new Pair<>(x,  new SimpleBooleanProperty(false)));
        }
    }

    public void clear(){
        descriptionField.setValue("");
        priceField.setValue("");
        currencyField.setValue("");
        evenlyCheckbox.setValue(true);
        dateField.setValue(LocalDate.now());
        whoPaidField.setValue("");
        whoPaidItems.setValue(FXCollections.observableArrayList());
        debtors.setValue(FXCollections.observableArrayList());
    }

    public void initWhoPaid() {
        this.whoPaidSelector = new WhoPaidSelector(this.event.getParticipants());
        whoPaidItems.get().addAll(
            this.event.getParticipants()
                .stream()
                .map(Participant::getName)
                .toList());

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

        Participant res = whoPaidSelector.getCurrentPayer(whoPaidField.getValue());

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

        System.out.println(expenseBuilder.toString());
        Expense res = expenseBuilder.build();

        res = expenseCommunicator.createExpense(res);
        this.event.addExpense(res);
        clear();
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

    public StringProperty whoPaidFieldProperty() {
        return whoPaidField;
    }

    public BooleanProperty evenlyCheckboxProperty() {
        return evenlyCheckbox;
    }

    public ObjectProperty<ObservableList<Pair<Participant, BooleanProperty>>> debtorsProperty() {
        return debtors;
    }

    public ObjectProperty<ObservableList<String>> whoPaidItemsProperty() {
        return whoPaidItems;
    }

    public Event getEvent() {
        return event;
    }
}
