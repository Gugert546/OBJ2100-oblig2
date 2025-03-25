package trafficjava;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) {
        cross cross = new cross(50, 500, true, true, false, false);
        cross cross2 = new cross(50, 20, false, true, true, false);
        cross cross3 = new cross(400, 20, false, true, true, true);
        cross cross4 = new cross(720, 20, false, false, true, true);
        vei vei1 = new vei(true, 50, 40, 400);
        vei vei2 = new vei(true, 400, 40, 400);

        Pane TrafficPane = new Pane(cross, cross2, vei1, cross3, cross4, vei2);
        Scene trafficSim = new Scene(TrafficPane, 800, 600);
        primaryStage.setTitle("traffic sim");
        primaryStage.setScene(trafficSim);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void main(String[] args) {
        Application.launch(args);

    }
}
