package sample;

import com.udojava.evalex.Expression;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class Main extends Application {

    private static double UPPER_LIMIT = 360;
    private static double LOWER_LIMIT = -360;
    private final NumberAxis xAxis = new NumberAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private double max = 0;
    private double min = 0;
    private boolean unset = true;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Line Chart Sample");
        xAxis.setAnimated(true);
        yAxis.setAnimated(true);
        xAxis.setLowerBound(LOWER_LIMIT);
        xAxis.setUpperBound(UPPER_LIMIT);
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Waves");
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(true);

        VBox base = new VBox();
        base.setPadding(new Insets(10, 10, 10, 10));
        base.setSpacing(10);

        Label errorLabel = new Label();

        TextField expField = new TextField();
        expField.setPromptText("Enter the expression");
        expField.setOnKeyTyped(keyEvent -> errorLabel.setText(""));

        CheckBox autoclear = new CheckBox("Clear on draw");
        autoclear.setSelected(true);
        autoclear.setAllowIndeterminate(false);

        Button evalButton = new Button("Draw");
        evalButton.setOnAction(e2 -> {
            if (UPPER_LIMIT < LOWER_LIMIT)
                errorLabel.setText("Upper limit is less than lower limit!");
            else if (UPPER_LIMIT == LOWER_LIMIT)
                errorLabel.setText("Both limits are equal!");
            else {
                try {
                    XYChart.Series points = generateFunction(expField.getText());
                    if (autoclear.isSelected())
                        lineChart.getData().clear();

                    lineChart.getData().add(points);
                } catch (Exception e) {
                    errorLabel.setText(e.getMessage());
                }
            }
        });

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            lineChart.getData().clear();
            expField.setText("");
        });

        HBox box = new HBox(expField, evalButton, clearButton, autoclear);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);

        TextField upperField = new TextField("360");
        makeNumberField(upperField);
        upperField.setPromptText("Upper value of x");
        TextField lowerField = new TextField("-360");
        makeNumberField(lowerField);
        lowerField.setPromptText("Lower value of x");
        Button modify = new Button("Modify");
        modify.setOnAction(e -> {
            UPPER_LIMIT = Double.parseDouble(upperField.getText());
            LOWER_LIMIT = Double.parseDouble(lowerField.getText());
            if (UPPER_LIMIT < LOWER_LIMIT)
                errorLabel.setText("Upper limit is less than lower limit!");
            else if (UPPER_LIMIT == LOWER_LIMIT)
                errorLabel.setText("Both limits are equal!");
            else{
                xAxis.setUpperBound(UPPER_LIMIT);
                xAxis.setLowerBound(LOWER_LIMIT);
            }
        });
        HBox rangeBox = new HBox(upperField, lowerField, modify);
        rangeBox.setSpacing(10);
        rangeBox.setAlignment(Pos.CENTER);

        base.setAlignment(Pos.CENTER);
        base.getChildren().addAll(lineChart, rangeBox, box, errorLabel);

        Scene scene = new Scene(base, 800, 600);

        stage.setScene(scene);
        stage.show();
    }

    private void makeNumberField(TextField field) {
        field.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("[-]\\d*"))
                field.setText(t1.replaceAll("[^\\d]", ""));
        });
    }

    private XYChart.Series generateFunction(String exp) throws Exception {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Custom function");
        Expression expression = new Expression(exp);

        for (double i = LOWER_LIMIT; i < UPPER_LIMIT; i++) {
            double val = expression.setVariable("x", new BigDecimal(i)).eval().doubleValue();
            if (unset) {
                unset = false;
                max = val;
                min = val;
            } else if (val > max)
                max = val;
            else if (min > val)
                min = val;
            series.getData().add(new XYChart.Data<>(i, val));
        }
        yAxis.setUpperBound(max);
        yAxis.setLowerBound(min);
        return series;
    }


    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        launch(args);
    }
}
