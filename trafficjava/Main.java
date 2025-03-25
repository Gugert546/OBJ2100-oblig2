package trafficjava;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) {
        Pane TrafficPane = new Pane();

        cross cross = new cross(50, 550, true, true, false, false);// nederst til venstre
        cross cross2 = new cross(50, 50, false, true, true, false);// øverst til venstre
        cross cross7 = new cross(50, 300, true, true, true, false);// midten til venstre
        TrafficPane.getChildren().addAll(cross, cross2, cross7);
        cross cross3 = new cross(400, 50, false, true, true, true);// midten oppe
        cross crossbm = new cross(400, 550, true, true, false, true);// midten nede
        cross midten = new cross(400, 300, true, true, true, true); // i midten
        TrafficPane.getChildren().addAll(cross3, crossbm, midten);
        cross cross4 = new cross(750, 50, false, false, true, true); // øverst til høyre
        cross cross5 = new cross(750, 550, true, false, false, true); // nederst til høyre
        cross cross6 = new cross(750, 300, true, false, true, true); // midten til høyre
        TrafficPane.getChildren().addAll(cross4, cross5, cross6);

        // vertikale veier
        vei vei1 = new vei(true, cross2.getX(), cross2.getY(), cross7.getY());
        vei vei2 = new vei(true, cross3.getX(), cross3.getY(), midten.getY());
        vei vei3 = new vei(true, cross4.getX(), cross4.getY(), cross6.getY());
        vei vei4 = new vei(true, cross7.getX(), cross7.getY(), cross.getY());
        vei vei5 = new vei(true, midten.getX(), midten.getY(), crossbm.getY());
        vei vei6 = new vei(true, cross6.getX(), cross6.getY(), cross5.getY());
        TrafficPane.getChildren().addAll(vei1, vei2, vei3, vei4, vei5, vei6);

        // horisontale veier
        vei vei7 = new vei(false, cross2.getX(), cross2.getY(), cross3.getX());
        vei vei8 = new vei(false, cross7.getX(), cross7.getY(), midten.getX());
        vei vei9 = new vei(false, cross.getX(), cross.getY(), crossbm.getX());
        vei vei10 = new vei(false, cross3.getX(), cross3.getY(), cross4.getX());
        vei vei11 = new vei(false, midten.getX(), midten.getY(), cross6.getX());
        vei vei12 = new vei(false, crossbm.getX(), crossbm.getY(), cross5.getX());
        TrafficPane.getChildren().addAll(vei7, vei8, vei9, vei10, vei11, vei12);

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
