package trafficjava;

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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

//lyskryss sjekker for nærmeste bil, gir beskejed om nedbremsing/stopping.
//gul lys, er en buffer som gir bilen tid til å kommme ut av krysset
//car.setspeed(int newspeed) går gradvis til den nye farten, brukes for nedbremsing og starting 
//nedbremsingshastighet avhenger av distansen mellom bil og kryss, må også ha en limit på minste avstand før bremsing

public class Main extends Application {

    public static List<Car> carList = new ArrayList<Car>();
    public static List<Cross> crossList = new ArrayList<Cross>();
    private int CAR_COUNT = 6;
    private double randomX = 0;
    private double randomY = 0;
    private Car.Direction randomDirection;

    public void start(Stage primaryStage) {
        Pane TrafficPane = new Pane();

        Timeline carSpawner = new Timeline(new KeyFrame(Duration.seconds(4), event -> {
            if (carList.size() >= CAR_COUNT)
                return; // Stop spawning if the limit is reached
            setTilfeldigSpawn();
            Car car = new Car(randomX, randomY, randomDirection);
            carList.add(car);
            TrafficPane.getChildren().add(car);
            car.toFront();
            System.out.println("bil lagt til" + randomX + randomY + randomDirection + car.color);
        }));
        carSpawner.setCycleCount(Timeline.INDEFINITE); // Set limit for spawning
        carSpawner.play(); // Start the spawning timeline

        // gui
        Rectangle guifelt = new Rectangle(600, 30, 200, 540);
        guifelt.setFill(Color.GRAY);

        // ønsket fart på biler
        Label Ftekst = new Label("ønsket fart på bilene");
        TextField fart = new TextField();
        Button Fknapp = new Button("OK");
        Fknapp.setOnAction(e -> {
            Integer speed = Integer.parseInt(fart.getText());
            if (speed > 0) {
                // Car.setMaxSpeed(speed);
            } else
                System.out.println("ulovelig verdi for fart");
        });

        // ønsket tid på grønt lys
        Label Ttekst = new Label("ønsket tid på grønt lys(millisec):");
        TextField tid = new TextField();
        Button Tknapp = new Button("OK");
        Tknapp.setOnAction(e -> {
            Integer GTI = Integer.parseInt(tid.getText());
            if (GTI > 0) {
                Cross.setGTI(GTI);
            } else
                System.out.println("ulovlig verdi for tid");

        });

        // ønsket antall biler
        Label tekst = new Label("ønsket antall biler:");
        TextField bilAntall = new TextField();
        bilAntall.setPromptText("skriv ønsket antall biler");
        Button knapp = new Button("OK");
        knapp.setOnAction(e -> {
            Integer antallbil = Integer.parseInt(bilAntall.getText());
            this.CAR_COUNT = antallbil;
        });

        // legger til gui elementer på skjermen
        VBox gui = new VBox(10);
        gui.getChildren().addAll(Ftekst, fart, Fknapp, Ttekst, tid, Tknapp, tekst, bilAntall, knapp);
        gui.setLayoutX(610);
        gui.setLayoutY(300);
        TrafficPane.getChildren().addAll(guifelt, gui);

        // bakgrunn
        /// TrafficPane.setStyle("-fx-background-color: lightgreen;");
        // oppbygning av vei system
        Cross c0 = new Cross(125, 150);// øverst til venstre
        Cross c1 = new Cross(125, 450);// nederst til venstre
        Road vV = new Road(true, c0.getX(), c0.getY(), c1.getY());

        Cross c2 = new Cross(500, 150);// øverst til høyre
        Cross c3 = new Cross(500, 450);// nederst til høyre
        Road vH = new Road(true, c2.getX(), c2.getY(), c3.getY());

        TrafficPane.getChildren().addAll(c0, c1, c2, c3, vV, vH);
        vV.toBack();
        vH.toBack();
        crossList.add(c0);
        crossList.add(c1);
        crossList.add(c2);
        crossList.add(c3);

        Road vT = new Road(false, c0.getX(), c0.getY(), c2.getX());

        Road vB = new Road(false, c1.getX(), c1.getY(), c3.getX());

        TrafficPane.getChildren().addAll(vT, vB);
        vT.toBack();
        vB.toBack();

        Scene trafficSim = new Scene(TrafficPane, 800, 600);
        primaryStage.setTitle("traffic sim");
        primaryStage.setScene(trafficSim);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);

    }

    private void setTilfeldigSpawn() {
        Random random = new Random();
        int tall = random.nextInt(3) + 1;
        switch (tall) {
            case 1:
                this.randomDirection = Car.Direction.DOWN;
                this.randomX = 400;
                this.randomY = 300;
                break;
            case 2:
                this.randomDirection = Car.Direction.DOWN;
                this.randomX = 400;
                this.randomY = 300;
                break;

            case 3:
                this.randomDirection = Car.Direction.RIGHT;
                this.randomX = 400;
                this.randomY = 300;
                break;

            default:
                break;
        }

    }
}
