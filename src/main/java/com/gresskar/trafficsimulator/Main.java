package com.gresskar.trafficsimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("traffic-simulator.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setResizable(false);
        stage.setTitle("Traffic Simulator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }
}