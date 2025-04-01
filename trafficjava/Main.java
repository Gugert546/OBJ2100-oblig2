package trafficjava;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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

//lyskryss sjekker for nærmeste bil, gir beskejed om nedbremsing/stopping.
//gul lys, er en buffer som gir bilen tid til å kommme ut av krysset
//car.setspeed(int newspeed) går gradvis til den nye farten, brukes for nedbremsing og starting 
//nedbremsingshastighet avhenger av distansen mellom bil og kryss, må også ha en limit på minste avstand før bremsing

// Liste over biler

public class Main extends Application {
    public static List<Car> cars = new ArrayList<>();
    private int CAR_COUNT = 6;
    // public List<cross> kryss;

    public void start(Stage primaryStage) {
        Pane TrafficPane = new Pane();
        // gui
        Rectangle guifelt = new Rectangle(600, 30, 200, 540);
        guifelt.setFill(Color.GRAY);
        Label tekst = new Label("ønsket antall biler:");
        TextField bilAntall = new TextField();
        bilAntall.setPromptText("skriv ønsket antall biler");
        Button knapp = new Button("OK");

        Label tekst2 = new Label("ønsket fart");
        TextField newSpeed = new TextField();
        newSpeed.setPromptText("Skriv ønsket fart");
        Button knapp2 = new Button("OK");
        
        knapp.setOnAction(e -> {
            CAR_COUNT = Integer.parseInt(bilAntall.getSelectedText());
        });

    
        knapp2.setOnAction(e -> {
            initialSpeed = Integer.parseInt(newSpeed.getSelectedText());
        });
        
        VBox gui = new VBox(10);
        gui.getChildren().addAll(tekst, bilAntall, knapp);
        gui.setLayoutX(610);
        gui.setLayoutY(400);
        TrafficPane.getChildren().addAll(guifelt, gui);

        // oppbygning av vei system
        Cross c0 = new Cross(125, 150);// øverst til venstre
        // kryss.add(c0);
        // TrafficLight lysc0 = new TrafficLight(x,y);
        Cross c1 = new Cross(125, 450);// nederst til venstre
        Road vV = new Road(true, c0.getX(), c0.getY(), c1.getY());
        // kryss.add(c1);
        Cross c2 = new Cross(500, 150);// øverst til høyre
        // kryss.add(c2);
        Cross c3 = new Cross(500, 450);// nederst til høyre
        // kryss.add(c3);
        vei vH = new vei(true, c2.getX(), c2.getY(), c3.getY());
        TrafficPane.getChildren().addAll(c0, c1, c2, c3, vV, vH);
        vei vT = new vei(false, c0.getX(), c0.getY(), c2.getX());
        vei vB = new vei(false, c1.getX(), c1.getY(), c3.getX());
        TrafficPane.getChildren().addAll(vT, vB);
        vV.toBack();
        vH.toBack();
        vT.toBack();
        vB.toBack();

// Bil del

Timeline carSpawner = new Timeline(new KeyFrame(Duration.seconds(4), event -> {
        if (cars.size() >= CAR_COUNT) return; // Stop spawning if the limit is reached


Car car1 = new Car(setRandomColor, 200, 100, LEFT); 
Car car2 = new Car(setRandomColor, 300, 200, RIGHT); // Endre koordinater
Car car3 = new Car(setRandomColor, 400, 300, UP);

cars.addAll(car1, car2, car3);
TrafficPane.getChildren().addAll(bil1, bil2, bil3);

new Thread(car1).start();
new Thread(car2).start();
new Thread(car3).start();

carSpawner.setCycleCount(CAR_COUNT); // Set limit for spawning
carSpawner.play(); // Start the spawning timeline
           

        Scene trafficSim = new Scene(TrafficPane, 800, 600);
        primaryStage.setTitle("traffic sim");
        primaryStage.setScene(trafficSim);
        primaryStage.setResizable(false);
        primaryStage.show();
        // spawne bil, må i ny thread
        // bil bil1 = new bil();

        // for(i=)
    }

    public static void main(String[] args) {
        Application.launch(args);

    }

    // public List<cross> getList() {
    // return kryss;
    // }
}