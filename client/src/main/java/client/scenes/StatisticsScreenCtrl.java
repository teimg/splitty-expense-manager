package client.scenes;

import client.language.LanguageSwitch;
import client.utils.SceneController;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Tag;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsScreenCtrl implements LanguageSwitch, SceneController {

    @FXML
    private Button backButton;

    @FXML
    private Label statisticsLabel;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label totalCostLabel;

    private final MainCtrl mainCtrl;

    private Event event;

    private double totalPrice;

    @Inject
    public StatisticsScreenCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void loadInfo(Event event) {
        this.event = event;
        fillChart();
    }

    private void fillChart() {
        pieChart.getData().clear();
        Map<Tag, Double> entries = new HashMap<>();
        this.totalPrice = 0;
        for (Expense expense : event.getExpenses()) {
            totalPrice = totalPrice + expense.getAmount();
            Tag tag = expense.getTag();
            if (entries.containsKey(tag)) {
                double currentSum = entries.get(tag);
                entries.put(tag, currentSum + expense.getAmount());
            }
            else {
                entries.put(tag, expense.getAmount());
            }
        }
        addLabels(entries);
        pieChart.setLegendVisible(false);
        totalCostLabel.setText("The total cost of this event is: " + totalPrice + "$");
    }

    private void addLabels(Map<Tag, Double> entries) {
        ArrayList<PieChart.Data> data = new ArrayList<>();
        for (Map.Entry<Tag, Double>  entry: entries.entrySet()) {
            String legend = entry.getKey().getName() + " "
                    + (double) Math.round((entry.getValue()/totalPrice)*10000)/100
                    + "% " + entry.getValue() + "$";
            PieChart.Data slice = new PieChart.Data(legend, entry.getValue());
            //For some reason this is required. This is as the PieChart.Data
            //is lazy and isn't immediately constructed. Very weird.
            Platform.runLater(() -> {
                Node node = slice.getNode();
                if (node != null) {
                    node.setStyle("-fx-pie-color: rgb(" + entry.getKey().getRed()
                            + ", " + entry.getKey().getGreen() + ", "
                            + entry.getKey().getBlue()  + ");");
                }
            });
            data.add(slice);
        }
        for (PieChart.Data aspect : data) {
            pieChart.getData().add(aspect);
        }
    }

    @Override
    public void setLanguage() {
        statisticsLabel.setText(mainCtrl.getTranslator().getTranslation(
                "StatisticsScreen.Title-label"));
        totalCostLabel.setText(mainCtrl.getTranslator().getTranslation(
                "StatisticsScreen.Total-Cost-label") + " " + this.totalPrice + "$");
        pieChart.setTitle(mainCtrl.getTranslator().getTranslation(
                "StatisticsScreen.PieChart-Title"));
        backButton.setText(mainCtrl.getTranslator().getTranslation(
                "StatisticsScreen.Back-Button"));
    }

    public void handleBack(ActionEvent actionEvent) {
        mainCtrl.showEventOverview(event);
    }
}
