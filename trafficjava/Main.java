package trafficjava;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) {
        Pane TrafficPane = new Pane();
        // gui
        Rectangle guifelt = new Rectangle(600, 30, 200, 540);
        guifelt.setFill(Color.GRAY);
        Label tekst = new Label("ønsket antall biler:");
        TextField bilAntall = new TextField();
        bilAntall.setPromptText("skriv ønsket antall biler");
        Button knapp = new Button("OK");
        VBox gui = new VBox(10);
        gui.getChildren().addAll(tekst, bilAntall, knapp);
        gui.setLayoutX(610);
        gui.setLayoutY(400);
        TrafficPane.getChildren().addAll(guifelt, gui);

        // oppbygning av vei system
        cross c0 = new cross(125, 150, true, true, true, true);// øverst til venstre
        cross c1 = new cross(125, 450, true, true, true, true);// nederst til venstre
        vei vV = new vei(true, c0.getX(), c0.getY(), c1.getY());
        cross c2 = new cross(500, 150, true, true, true, true);// øverst til høyre
        cross c3 = new cross(500, 450, true, true, true, true);// nederst til høyre
        vei vH = new vei(true, c2.getX(), c2.getY(), c3.getY());
        TrafficPane.getChildren().addAll(c0, c1, c2, c3, vV, vH);
        vei vT = new vei(false, c0.getX(), c0.getY(), c2.getX());
        vei vB = new vei(false, c1.getX(), c1.getY(), c3.getX());
        TrafficPane.getChildren().addAll(vT, vB);

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
