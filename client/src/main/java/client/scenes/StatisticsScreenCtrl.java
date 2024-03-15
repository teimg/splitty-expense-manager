package client.scenes;

import client.language.LanguageSwitch;
import client.utils.SceneController;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StatisticsScreenCtrl implements Initializable, LanguageSwitch, SceneController {


    @FXML
    private Label statisticsLabel;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label totalCostLabel;

    private final MainCtrl mainCtrl;

    @Inject
    public StatisticsScreenCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /**
         * NEED TO USE BACKEND API TO FIND THE PAYMENTS
         * OF AN EVENT (GETTING THEM ALL)
         */

        /**
         * ArrayList<Pair<String, Integer>> = ... ---> here we add the
         * end point of getting all 'subjects' and their assigned weight.
         */

        /**
         * int totalPrice = ... --> here we add the endpoint for the total
         * price of the event/trip
         */

        /**
         * For now, I will use a hardCoded event:
         */

        ArrayList<Pair<String, Integer>> tags = new ArrayList<>();

        tags.add(new Pair<>("Test 30", 30));
        tags.add(new Pair<>("Test 70", 70));

        int totalPrice = 100;

        totalCostLabel.setText("The total cost of this event is: " + totalPrice);

        ArrayList<PieChart.Data> data = new ArrayList<>();

        for (Pair<String, Integer> map : tags) {
            data.add(new PieChart.Data(map.getKey(), map.getValue()));
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
                "StatisticsScreen.Total-Cost-label"));
        pieChart.setTitle(mainCtrl.getTranslator().getTranslation(
                "StatisticsScreen.PieChart-Title"));

    }

}
