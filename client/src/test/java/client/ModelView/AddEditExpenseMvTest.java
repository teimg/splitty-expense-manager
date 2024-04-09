package client.ModelView;

import client.currency.Exchanger;
import client.utils.communicators.interfaces.IEventCommunicator;
import client.utils.communicators.interfaces.IExpenseCommunicator;
import client.utils.communicators.interfaces.ITagCommunicator;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AddEditExpenseMvTest {


    @Mock
    private IExpenseCommunicator mockExpenseCommunicator;

    @Mock
    private IEventCommunicator mockEventCommunicator;

    @Mock
    private ITagCommunicator mockTagCommunicator;

    @Mock
    private Exchanger mockExchanger;

    private AddEditExpenseMv  addEditExpenseMv;

    @BeforeEach
    void setup(){
        addEditExpenseMv =
            new AddEditExpenseMv(mockExpenseCommunicator, mockEventCommunicator, mockTagCommunicator);
    }

    @Test
    void updateEvent() {
        List<Participant> participants = List.of(
            new Participant("Henk"),
            new Participant("Piet")
        );
        Event event = new Event("test", "CODE", participants, null, null);
        event.setId(1L);

        addEditExpenseMv.loadInfo(event, mockExchanger);

        Mockito.when(mockEventCommunicator.getEvent(1L))
            .thenReturn(event);

        addEditExpenseMv.updateEvent(event);


        Mockito.verify(mockEventCommunicator).getEvent(1L);

        assertEquals(event, addEditExpenseMv.getEvent());

    }

    @Test
    void loadInfo() {
        List<Participant> participants = List.of(new Participant("Henk"), new Participant("Piet"));
        Event event = new Event("test", "CODE", participants, null, null);

        addEditExpenseMv.loadInfo(event, mockExchanger);

        assertEquals(event, addEditExpenseMv.getEvent());

    }

    @Test
    void loadExpense() {

        List<Participant> participants = List.of(new Participant("Henk"), new Participant("Piet"));
        Event event = new Event("test", "CODE", participants, null, null);
        var t = new Tag("Test Tag", 1,1,1,1L);
        Expense expense =
            new Expense(1L, "Test purchase", 1.0,
                participants.getFirst(), participants, LocalDate.of(1,1,1), t);

        addEditExpenseMv.loadInfo(event, mockExchanger);
        addEditExpenseMv.loadExpense(expense, mockExchanger);

        assertEquals(event, addEditExpenseMv.getEvent());

        assertEquals(t, addEditExpenseMv.tagFieldProperty().get());
        assertEquals(participants.getFirst(), addEditExpenseMv.whoPaidFieldProperty().get());
        assertEquals("Test purchase", addEditExpenseMv.descriptionFieldProperty().get());
        assertEquals(LocalDate.of(1,1,1),  addEditExpenseMv.dateFieldProperty().get());

    }

    @Test
    void clear() {
        List<Participant> p = List.of(
            new Participant("p0")
        );

        Event event = new Event("test", "CODE", p, null, null);
        addEditExpenseMv.loadInfo(event, mockExchanger);

        addEditExpenseMv.descriptionFieldProperty().setValue("test Desc");
        addEditExpenseMv.priceFieldProperty().setValue("100.10");
        addEditExpenseMv.currencyFieldProperty().setValue("EUR");
        addEditExpenseMv.whoPaidFieldProperty().setValue(new Participant("Henk"));
        addEditExpenseMv.debtorsProperty().get().add(
            new Pair<>(p.get(0), new SimpleBooleanProperty(true)));

        addEditExpenseMv.clear();

        assertEquals("", addEditExpenseMv.descriptionFieldProperty().getValue());
        assertEquals("", addEditExpenseMv.priceFieldProperty().getValue());
        assertEquals("", addEditExpenseMv.currencyFieldProperty().getValue());
        assertNull(addEditExpenseMv.whoPaidFieldProperty().get());

    }

    @Test
    void getDateFieldValue() {
        addEditExpenseMv.dateFieldProperty().set(LocalDate.of(1, 1,1 ));

        assertEquals(LocalDate.of(1, 1,1), addEditExpenseMv.getDateFieldValue());
    }

    @Test
    void getNotDateField() {
        addEditExpenseMv.dateFieldProperty().set(null);

        assertEquals(LocalDate.now(), addEditExpenseMv.getDateFieldValue());
    }

    @Test
    void getPayer() {
        addEditExpenseMv.whoPaidFieldProperty().set(new Participant("Henk"));

        assertEquals("Henk", addEditExpenseMv.getPayer().getName());
    }

    @Test
    void getNotPayer() {
        addEditExpenseMv.whoPaidFieldProperty().set(null);

       assertThrows(IllegalArgumentException.class, () ->{
           addEditExpenseMv.getPayer();
       }, "PayerFieldInvalid");
    }

    @Test
    void getPurchase() {
        addEditExpenseMv.descriptionFieldProperty().setValue("Expense");

        assertEquals("Expense", addEditExpenseMv.getPurchase());

    }


    @Test
    void getNotPurchase() {
        addEditExpenseMv.descriptionFieldProperty().setValue("");

        assertThrows(IllegalArgumentException.class, () ->{
            addEditExpenseMv.getPurchase();
        }, "PurchaseLeftEmpty");

    }

    @Test
    void getPriceFieldValue(){
        addEditExpenseMv.priceFieldProperty().setValue("12.0");
        assertEquals(1200, addEditExpenseMv.getPriceFieldValue());

        addEditExpenseMv.priceFieldProperty().setValue("12,0");
        assertEquals(1200, addEditExpenseMv.getPriceFieldValue());
    }

    @Test
    void getNotPriceFieldValue(){
        addEditExpenseMv.priceFieldProperty().setValue("-12.0");

        assertThrows(IllegalArgumentException.class, () ->{
            addEditExpenseMv.getPriceFieldValue();
        }, "PriceFieldInvalid");

        addEditExpenseMv.priceFieldProperty().setValue("not a valid price");

        assertThrows(IllegalArgumentException.class, () ->{
            addEditExpenseMv.getPriceFieldValue();
        }, "PriceFieldInvalid");
    }

    @Test
    void getDebtors() {
        List<Participant> p = List.of(
            new Participant("p0"),
            new Participant("p1"),
            new Participant("p2"),
            new Participant("p3")
        );

        Event event = new Event("test", "CODE", p, null, null);
        addEditExpenseMv.loadInfo(event, mockExchanger);
        addEditExpenseMv.evenlyCheckboxProperty().setValue(false);

        addEditExpenseMv.debtorsProperty().get().add(
            new Pair<>(p.get(0), new SimpleBooleanProperty(true)));
        addEditExpenseMv.debtorsProperty().get().add(
            new Pair<>(p.get(1), new SimpleBooleanProperty(true)));
        addEditExpenseMv.debtorsProperty().get().add(
            new Pair<>(p.get(2), new SimpleBooleanProperty(false)));
        addEditExpenseMv.debtorsProperty().get().add(
            new Pair<>(p.get(3), new SimpleBooleanProperty(true)));

        assertEquals(
            List.of(p.get(0), p.get(1), p.get(3)),
            addEditExpenseMv.getDebtors()
        );

    }

    @Test
    void getTag() {
        var t = new Tag("test", 0,0,0, 1L);
        addEditExpenseMv.tagFieldProperty().setValue(t);

        assertEquals(t,addEditExpenseMv.getTag());

    }

    @Test
    void getEvenlyDebtors() {
        List<Participant> p = List.of(
            new Participant("p0"),
            new Participant("p1"),
            new Participant("p2"),
            new Participant("p3")
        );

        Event event = new Event("test", "CODE", p, null, null);
        addEditExpenseMv.loadInfo(event, mockExchanger);
        addEditExpenseMv.evenlyCheckboxProperty().setValue(true);

        addEditExpenseMv.debtorsProperty().get().add(
            new Pair<>(p.get(0), new SimpleBooleanProperty(true)));
        addEditExpenseMv.debtorsProperty().get().add(
            new Pair<>(p.get(1), new SimpleBooleanProperty(true)));
        addEditExpenseMv.debtorsProperty().get().add(
            new Pair<>(p.get(2), new SimpleBooleanProperty(false)));
        addEditExpenseMv.debtorsProperty().get().add(
            new Pair<>(p.get(3), new SimpleBooleanProperty(true)));

        assertEquals(p, addEditExpenseMv.getDebtors());

    }

    @Test
    void getNotTag() {
        addEditExpenseMv.tagFieldProperty().setValue(null);

        assertThrows(IllegalArgumentException.class, () ->{
            addEditExpenseMv.getTag();
        }, "TagInvalid");

    }



    @Test
    void getCur() {
        addEditExpenseMv.currencyFieldProperty().setValue("EUR");

        assertEquals("EUR", addEditExpenseMv.getCur());
    }

    @Test
    void getNotCur() {
        addEditExpenseMv.priceFieldProperty().setValue("");

        assertThrows(IllegalArgumentException.class, () ->{
            addEditExpenseMv.getCur();
        }, "SelectACurrency");
    }

    @Test
    void createExpense() {
        List<Participant> p = List.of(
            new Participant("p0"),
            new Participant("p1"),
            new Participant("p2"),
            new Participant("p3")
        );

        Tag t = new Tag("tag", 0,0,0, 1L);
        Event event = new Event("test","CODE", p, null, null);
        event.setId(1L);

        addEditExpenseMv.loadInfo(event, mockExchanger);
        addEditExpenseMv.descriptionFieldProperty().setValue("test Desc");
        addEditExpenseMv.priceFieldProperty().setValue("1.00");
        addEditExpenseMv.evenlyCheckboxProperty().setValue(true);
        addEditExpenseMv.currencyFieldProperty().setValue("EUR");
        addEditExpenseMv.whoPaidFieldProperty().setValue(p.get(0));
        addEditExpenseMv.dateFieldProperty().setValue(LocalDate.now());
        addEditExpenseMv.tagFieldProperty().setValue(t);

        addEditExpenseMv.loadInfo(event, mockExchanger);

        Mockito.when(mockExpenseCommunicator.createExpense(
          Mockito.any(Expense.class)
        )).thenAnswer(i -> i.getArguments()[0]);

        Expense res = addEditExpenseMv.createExpense();

        assertEquals("test Desc", res.getPurchase());
        assertEquals(p.get(0), res.getPayer());
        assertEquals(p, res.getDebtors());
        assertEquals(t, res.getTag());
        assertEquals(1L, res.getEventId());
    }

    @Test
    void updateExpense() {
        List<Participant> p = List.of(
            new Participant("p0"),
            new Participant("p1"),
            new Participant("p2"),
            new Participant("p3")
        );

        Tag t = new Tag("tag", 0,0,0, 1L);
        Event event = new Event("test","CODE", p, null, null);
        event.setId(1L);

        addEditExpenseMv.loadInfo(event, mockExchanger);
        addEditExpenseMv.loadExpense(
            new Expense(1L, "to update", 12.0, p.get(1), p, LocalDate.of(9,9,9), t),
                mockExchanger
        );

        addEditExpenseMv.loadInfo(event, mockExchanger);
        addEditExpenseMv.descriptionFieldProperty().setValue("test Desc");
        addEditExpenseMv.priceFieldProperty().setValue("1.00");
        addEditExpenseMv.evenlyCheckboxProperty().setValue(true);
        addEditExpenseMv.currencyFieldProperty().setValue("EUR");
        addEditExpenseMv.whoPaidFieldProperty().setValue(p.get(0));
        addEditExpenseMv.dateFieldProperty().setValue(LocalDate.now());
        addEditExpenseMv.tagFieldProperty().setValue(t);


        Mockito.when(mockExpenseCommunicator.createExpense(
            Mockito.any(Expense.class)
        )).thenAnswer(i -> i.getArguments()[0]);

        Expense res = addEditExpenseMv.createExpense();

        Mockito.verify(mockExpenseCommunicator).deleteExpense(Mockito.anyLong());

        assertEquals("test Desc", res.getPurchase());
        assertEquals(p.get(0), res.getPayer());
        assertEquals(p, res.getDebtors());
        assertEquals(t, res.getTag());
        assertEquals(1L, res.getEventId());
    }

    @Test
    void deleteTag() {
        var t = new Tag("Test Tag", 1,1,1,1L);
        addEditExpenseMv.tagFieldProperty().set(t);

        addEditExpenseMv.deleteTag();
        Mockito.verify(mockTagCommunicator).deleteTag(t.getId());
    }

    @Test
    void priceFieldProperty() {
        StringProperty val = new SimpleStringProperty();
        val.bindBidirectional(addEditExpenseMv.priceFieldProperty());
        var p = "TEST";
        val.setValue(p);
        assertEquals(p, addEditExpenseMv.priceFieldProperty().getValue());
    }

    @Test
    void currencyFieldProperty() {
        StringProperty val = new SimpleStringProperty();
        val.bindBidirectional(addEditExpenseMv.currencyFieldProperty());
        var p = "TEST";
        val.setValue(p);
        assertEquals(p, addEditExpenseMv.currencyFieldProperty().getValue());
    }

    @Test
    void descriptionFieldProperty() {
        StringProperty val = new SimpleStringProperty();
        val.bindBidirectional(addEditExpenseMv.descriptionFieldProperty());
        var p = "TEST";
        val.setValue(p);
        assertEquals(p, addEditExpenseMv.descriptionFieldProperty().getValue());
    }

    @Test
    void whoPaidFieldProperty() {
        ObjectProperty<Participant> val = new SimpleObjectProperty<>();
        val.bindBidirectional(addEditExpenseMv.whoPaidFieldProperty());
        var p = new Participant("Henk");
        val.setValue(p);
        assertEquals(p, addEditExpenseMv.whoPaidFieldProperty().getValue());
    }

    @Test
    void dateFieldProperty() {
        ObjectProperty<LocalDate> val = new SimpleObjectProperty<>();
        val.bindBidirectional(addEditExpenseMv.dateFieldProperty());
        var p = LocalDate.of(1 , 1, 1);
        val.setValue(p);
        assertEquals(p, addEditExpenseMv.dateFieldProperty().getValue());
    }

    @Test
    void evenlyCheckboxProperty() {
        BooleanProperty val = new SimpleBooleanProperty();
        val.bindBidirectional(addEditExpenseMv.evenlyCheckboxProperty());
        val.setValue(false);
        assertEquals(false, addEditExpenseMv.evenlyCheckboxProperty().getValue());
    }

    @Test
    void debtorsProperty() {
        ObjectProperty<ObservableList<Pair<Participant, BooleanProperty>>> val = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        val.bindBidirectional(addEditExpenseMv.debtorsProperty());
        val.get().add(new Pair<>(new Participant("testPar"), new SimpleBooleanProperty(true)));


        assertEquals(true, addEditExpenseMv.debtorsProperty().getValue().getFirst().getValue().getValue());
        assertEquals("testPar", addEditExpenseMv.debtorsProperty().get().getFirst().getKey().getName());
    }

    @Test
    void tagFieldProperty() {
        commons.Tag t = new Tag("test", 0, 0, 0, 1L);
        SimpleObjectProperty<Tag> val = new SimpleObjectProperty<>();
        val.bindBidirectional(addEditExpenseMv.tagFieldProperty());
        val.setValue(t);

        assertEquals(t, addEditExpenseMv.tagFieldProperty().getValue());
    }

    @Test
    void getParticipants() {
        List<Participant> participants = List.of(new Participant("Henk"), new Participant("Piet"));
        Event event = new Event("test", "CODE", participants, null, null);
        addEditExpenseMv.loadInfo(event, mockExchanger);

        assertEquals(participants, addEditExpenseMv.getParticipants());

    }

    @Test
    void getTags() {
        List<Tag> ex = List.of(
            new Tag("tag 1", 0,0,0,1L),
            new Tag("tag 2", 0,0,0,1L),
            new Tag("tag 3", 0,0,0,1L),
            new Tag("tag 4", 0,0,0,1L)
        );


        List<Participant> participants = List.of(new Participant("Henk"), new Participant("Piet"));
        Event event = new Event("test", "CODE", participants, null, null);
        event.setId(1L);
        event.setTags(ex);
        addEditExpenseMv.loadInfo(event, mockExchanger);


        Mockito.when(mockEventCommunicator.getEvent(1L))
            .thenReturn(event);

        assertEquals(ex, addEditExpenseMv.getTags());

    }

    @Test
    void getEvent() {
        List<Participant> participants = List.of(new Participant("Henk"), new Participant("Piet"));
        Event event = new Event("test", "CODE", participants, null, null);

        addEditExpenseMv.loadInfo(event, mockExchanger);

        assertEquals(event, addEditExpenseMv.getEvent());

    }
}