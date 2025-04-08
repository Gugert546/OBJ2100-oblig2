package com.gresskar.trafficsimulator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class Controller {
    @FXML private Pane rootPane;

    @FXML private Label labelCarCount;
    @FXML private TextField textFieldCarCount;

    @FXML private Label labelCarSpeed;
    @FXML private TextField textFieldCarSpeed;

    @FXML private Label labelLightSpeed;
    @FXML private TextField textFieldLightSpeed;

    @FXML
    public void initialize() {
        drawCars();
    }

    @FXML protected void drawCars() {
        Car car1 = new Car(0, 155, Direction.RIGHT);
        Car car2 = new Car(0, 455, Direction.RIGHT);

        rootPane.getChildren().add(car1.getShape());
        rootPane.getChildren().add(car2.getShape());
    }

    @FXML
    protected void onButtonCarCount() {
        try {
            final int carCount = Integer.parseInt(textFieldCarCount.getText());
            if (carCount < 0) {
                System.err.println("Car count can't be negative");
                return;
            }
            labelCarCount.setText("Car count: " + carCount);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    @FXML
    protected void onButtonCarSpeed() {
        try {
            final int carSpeed = Integer.parseInt(textFieldCarSpeed.getText());
            if (carSpeed < 0) {
                System.err.println("Car speed can't be negative");
                return;
            }
            labelCarSpeed.setText("Car speed: " + carSpeed);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }
    @FXML
    protected void onButtonLightSpeed() {
        try {
            final int lightSpeed = Integer.parseInt(textFieldLightSpeed.getText());
            if (lightSpeed < 0) {
                System.err.println("Light speed can't be negative");
                return;
            }
            labelLightSpeed.setText("Light speed: " + lightSpeed);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }
}