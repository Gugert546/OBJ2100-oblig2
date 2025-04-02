package trafficjava;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
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
    // lanes i nedovergående retning
    public static int laneNedoverVenstre = 98;
    public static int laneNedoverHøyre = 473;
    // lanes i høyregående retning
    public static int laneHøyreNede = 453;
    public static int laneHøyreOppe = 153;
    // lanes i venstregående retning
    public static int laneVenstreOppe = 150 - 23;
    public static int laneVenstreNede = 450 - 23;
    // lanes i oppovergående retning
    public static int laneOppoverVenstre = 125 + 3;
    public static int laneOppoverHøyre = 450 + 3;

    public void start(Stage primaryStage) {
        Pane TrafficPane = drawScreen();

        Timeline carSpawner = new Timeline(new KeyFrame(Duration.seconds(4), event -> {
            if (carList.size() >= CAR_COUNT)
                return;
            setTilfeldigSpawn();
            Car car = new Car(randomX, randomY, randomDirection);
            carList.add(car);
            TrafficPane.getChildren().add(car.getShape());
            car.toFront();
            System.out.println(carList.size());

        }));
        carSpawner.setCycleCount(Timeline.INDEFINITE); // Set limit for spawning
        carSpawner.play(); // Start the spawning timeline

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
                this.randomX = 98;
                this.randomY = 0;
                break;
            case 2:
                this.randomDirection = Car.Direction.DOWN;
                this.randomX = 473;
                this.randomY = 0;
                break;

            case 3:
                this.randomDirection = Car.Direction.RIGHT;
                this.randomX = 0;
                this.randomY = 453;
                break;

            default:
                break;
        }

    }

    public Pane drawScreen() {
        Pane pane = new Pane();

        // bakgrunn
        pane.setStyle("-fx-background-color: lightgreen;");
        // gui
        VBox gui = new VBox();
        Rectangle guifelt = new Rectangle(600, 30, 200, 540);
        guifelt.setFill(Color.GRAY);
        pane.getChildren().add(guifelt);

        // gui for å velge ønsket fart på bilene
        Label Ftekst = new Label("ønsket fart på bilene:" + Car.maxSpeed);
        TextField fart = new TextField();
        Button Fknapp = new Button("OK");
        Fknapp.setOnAction(e -> {
            Integer speed = Integer.parseInt(fart.getText());
            if (speed > 0) {
                Car.setMaxSpeed(speed);
            } else
                System.out.println("ulovelig verdi for fart");
        });
        gui.getChildren().addAll(Ftekst, fart, Fknapp);

        // gui for å sette ønsket tid på lysene
        Label Ttekst = new Label("ønsket tid på grønt lys(millisec):" + Cross.GTI);
        TextField tid = new TextField();
        Button Tknapp = new Button("OK");
        Tknapp.setOnAction(e -> {
            Integer GTI = Integer.parseInt(tid.getText());
            if (GTI > 0) {
                Cross.setGTI(GTI);
            } else
                System.out.println("ulovlig verdi for tid");

        });
        gui.getChildren().addAll(Ttekst, tid, Tknapp);

        // gui for å sette ønsket antall biler
        Label tekst = new Label("ønsket antall biler:" + CAR_COUNT);
        TextField bilAntall = new TextField();
        bilAntall.setPromptText("skriv ønsket antall biler");
        Button knapp = new Button("OK");
        knapp.setOnAction(e -> {
            Integer antallbil = Integer.parseInt(bilAntall.getText());
            this.CAR_COUNT = antallbil;
        });
        gui.getChildren().addAll(tekst, bilAntall, knapp);

        // legger til gui elementer på skjermen
        gui.setLayoutX(610);
        gui.setLayoutY(200);
        pane.getChildren().addAll(gui);

        // kryss og vei

        // oppbygning av vei system
        Cross c0 = new Cross(125, 150);// øverst til venstre
        Cross c1 = new Cross(125, 450);// nederst til venstre
        Cross c2 = new Cross(500, 150);// øverst til høyre
        Cross c3 = new Cross(500, 450);// nederst til høyre
        crossList.add(c0);
        crossList.add(c1);
        crossList.add(c2);
        crossList.add(c3);
        pane.getChildren().addAll(c0, c1, c2, c3);
        Road hO = new Road(false, 0, 150, 600);// horisontalt oppe
        Road hN = new Road(false, 0, 450, 600);// horisontalt nede
        Road vV = new Road(true, 125, 0, 800); // vertikalt venstre
        Road vH = new Road(true, 500, 0, 800); // vertikalt høyre
        pane.getChildren().addAll(hO, hN, vV, vH);
        hO.toBack();
        hN.toBack();
        vV.toBack();
        vH.toBack();
        return pane;
    }
}
