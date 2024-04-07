package client.scenes;

import client.ModelView.StatisticsScreenMv;
import client.keyBoardCtrl.KeyBoardListeners;
import client.keyBoardCtrl.ShortCuts;
import client.language.LanguageSwitch;
import client.utils.scene.SceneController;
import com.google.inject.Inject;
import commons.Event;
import commons.Tag;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class StatisticsScreenCtrl implements LanguageSwitch, SceneController, ShortCuts {

    @FXML
    private Button backButton;

    @FXML
    private Label statisticsLabel;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label totalCostLabel;

    @FXML
    private Label noExpensesLabel;

    private final MainCtrl mainCtrl;

    private final StatisticsScreenMv statisticsScreenMv;

    @Inject
    public StatisticsScreenCtrl(MainCtrl mainCtrl, StatisticsScreenMv statisticsScreenMv) {
        this.mainCtrl = mainCtrl;
        this.statisticsScreenMv = statisticsScreenMv;
    }

    public void loadInfo(Event event) {
        statisticsScreenMv.setEvent(event);
        fillChart();
        toggleVisibility();
    }

    private void toggleVisibility() {
        pieChart.setVisible(!statisticsScreenMv.getEvent().getExpenses().isEmpty());
        noExpensesLabel.setVisible(statisticsScreenMv.getEvent().getExpenses().isEmpty());
    }

    private void fillChart() {
        pieChart.getData().clear();
        Map<Tag, Double> entries = statisticsScreenMv.fillEntries();
        addLabels(entries);
        pieChart.setLegendVisible(false);
        totalCostLabel.setText(mainCtrl.getTranslator().getTranslation(
                "StatisticsScreen.Total-Cost-label") + " "
                + statisticsScreenMv.rounder(mainCtrl.getExchanger().getStandardConversion(
                statisticsScreenMv.getTotalPrice(), LocalDate.now())) +
                mainCtrl.getExchanger().getCurrentSymbol());
    }

    private void addLabels(Map<Tag, Double> entries) {
        ArrayList<PieChart.Data> data = new ArrayList<>();
        for (Map.Entry<Tag, Double>  entry: entries.entrySet()) {
            String legend = entry.getKey().getName() + " "
                    + (double) Math.round((entry.getValue()
                        /statisticsScreenMv.getTotalPrice())*10000)/100
                    + "% " +
                    statisticsScreenMv.rounder(mainCtrl.getExchanger().
                            getStandardConversion(entry.getValue(),
                            LocalDate.now())) + mainCtrl.getExchanger().getCurrentSymbol();
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
                        "StatisticsScreen.Total-Cost-label") + " "
                + statisticsScreenMv.rounder(mainCtrl.getExchanger().getStandardConversion(
                statisticsScreenMv.getTotalPrice(), LocalDate.now())) +
                mainCtrl.getExchanger().getCurrentSymbol());
        pieChart.setTitle(mainCtrl.getTranslator().getTranslation(
                "StatisticsScreen.PieChart-Title"));
        backButton.setText(mainCtrl.getTranslator().getTranslation(
                "StatisticsScreen.Back-Button"));
        noExpensesLabel.setText(mainCtrl.getTranslator().getTranslation(
                "StatisticsScreen.NoExpense-label"));
        loadInfo(statisticsScreenMv.getEvent());
    }

    public void handleBack(ActionEvent actionEvent) {
        mainCtrl.showEventOverview(statisticsScreenMv.getEvent());
    }

    @Override
    public void listeners() {
        Scene s = pieChart.getScene();
        KeyBoardListeners.addListener(s, KeyCode.B, () -> handleBack(new ActionEvent()));
    }
}
