package trafficjava;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private HBox biler = new HBox(10);
    public static List<Car> carList = new ArrayList<Car>();
    public static List<Cross> crossList = new ArrayList<Cross>();
    private int CAR_COUNT = 2;
    private double randomX = 0;
    private double randomY = 0;
    private Car.Direction randomDirection;
    private Cross chosenIntersection;
    // lanes i nedovergående retning
    public static double laneNedoverVenstre = 98;
    public static double laneNedoverHøyre = 473;
    // lanes i høyregående retning
    public static double laneHøyreNede = 453;
    public static double laneHøyreOppe = 153;
    // lanes i venstregående retning
    public static double laneVenstreOppe = 150 - 23;
    public static double laneVenstreNede = 450 - 23;
    // lanes i oppovergående retning
    public static double laneOppoverVenstre = 125 + 3;
    public static double laneOppoverHøyre = 500 + 3;

    // pane
    static Pane TrafficPane;

    /** start metode, spawner biler og setter stagen */
    public void start(Stage primaryStage) {
        Main.TrafficPane = drawScreen();

        Timeline carSpawner = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (carList.size() >= CAR_COUNT) {
                return;
            }

            setTilfeldigSpawn(); // setter randomX, randomY, randomDirection brukes til bilobjektet
            Car car = new Car(randomX, randomY, randomDirection);
            carList.add(car);
            TrafficPane.getChildren().add(car.getShape());
            car.toFront();
            System.out.println("Spawned car. Total: " + carList.size());

        }));
        carSpawner.setCycleCount(Timeline.INDEFINITE); // fortsetter å spawne hele tiden
        carSpawner.play(); // Start the spawning timeline
        Timeline guiUpdater = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateGUI();
        }));
        guiUpdater.setCycleCount(Timeline.INDEFINITE);
        guiUpdater.play();
        TrafficPane.getChildren().add(biler);
        Scene trafficSim = new Scene(TrafficPane, 800, 600);
        primaryStage.setTitle("traffic sim");
        primaryStage.setScene(trafficSim);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    /** main metode */
    public static void main(String[] args) {
        Application.launch(args);

    }

    /** metode for å sette en tilfeldig spawn lokasjon til bilen */
    // TODO, fjern unødig venstre
    private void setTilfeldigSpawn() {
        Random random = new Random();
        int tall = random.nextInt(9) + 1;
        switch (tall) {
            case 1:
                this.randomDirection = Car.Direction.DOWN;
                this.randomX = laneNedoverVenstre;
                this.randomY = 0;
                break;
            case 2:
                this.randomDirection = Car.Direction.DOWN;
                this.randomX = laneNedoverHøyre;
                this.randomY = 0;
                break;
            case 3:
                this.randomDirection = Car.Direction.RIGHT;
                this.randomX = 0;
                this.randomY = laneHøyreNede;
                break;
            case 4:
                this.randomDirection = Car.Direction.RIGHT;
                this.randomX = 0;
                this.randomY = laneHøyreOppe;
                break;
            case 5:
                this.randomDirection = Car.Direction.LEFT;
                this.randomX = 600;
                this.randomY = laneVenstreNede;
                break;
            case 6:
                this.randomDirection = Car.Direction.LEFT;
                this.randomX = 600;
                this.randomY = laneVenstreOppe;
                break;
            case 7:
                this.randomDirection = Car.Direction.LEFT;
                this.randomX = 600;
                this.randomY = laneVenstreNede;
                break;
            case 8:
                this.randomDirection = Car.Direction.UP;
                this.randomX = laneOppoverVenstre;
                this.randomY = 600;
                break;
            case 9:
                this.randomDirection = Car.Direction.UP;
                this.randomX = laneOppoverHøyre;
                this.randomY = 600;

            default:
                break;
        }

    }

    /** metode for å tegne veisystemet, lager også kryss objekter */
    public Pane drawScreen() {
        Pane pane = new Pane();

        // bakgrunn
        pane.setStyle("-fx-background-color: lightgreen;");
        // gui
        VBox gui = new VBox();
        Rectangle guifelt = new Rectangle(600, 30, 200, 540);
        guifelt.setFill(Color.GRAY);
        pane.getChildren().add(guifelt);
        // kanpp for å slette bilene
        Label sletteTekst = new Label("slett alle bilene");
        Button slettButton = new Button("slett");
        slettButton.setOnAction(e -> {
            // kode for knappen
            deleteAllcars();
        });
        gui.getChildren().addAll(sletteTekst, slettButton);

        // gui for å sette ønsket tid på lysene
        Label Ttekst = new Label("ønsket tid på grønt lys(millisec):");
        TextField tid = new TextField();
        Button Tknapp = new Button("OK");
        Tknapp.setOnAction(e -> {
            if (chosenIntersection != null) {
                Integer GTI = Integer.parseInt(tid.getText());
                if (GTI > 0) {
                    chosenIntersection.setGTI(GTI);
                } else
                    System.out.println("ulovlig verdi for tid");
            } else
                System.out.println("velg et kryss");
        });
        gui.getChildren().addAll(Ttekst, tid, Tknapp);

        // gui for å sette ønsket antall biler
        Label tekst = new Label("ønsket antall biler:" + CAR_COUNT);
        TextField bilAntall = new TextField();
        bilAntall.setPromptText("skriv ønsket antall biler");
        Button knapp = new Button("OK");
        knapp.setOnAction(e -> {
            updateGUI();
            Integer antallbil = Integer.parseInt(bilAntall.getText());
            this.CAR_COUNT = antallbil;

        });
        gui.getChildren().addAll(tekst, bilAntall, knapp);

        // legger til gui elementer på skjermen
        gui.setLayoutX(610);
        gui.setLayoutY(165);
        pane.getChildren().addAll(gui);
        gui.toFront();

        // kryss og vei

        // oppbygning av vei system
        Cross c0 = new Cross(125, 150);// øverst til venstre
        c0.setOnMouseClicked(e -> {
            this.chosenIntersection = c0;
            System.out.println("Selected c0");

        });
        Cross c1 = new Cross(125, 450);// nederst til venstre
        c1.setOnMouseClicked(e -> {
            this.chosenIntersection = c1;
            System.out.println("Selected c1");

        });
        Cross c2 = new Cross(500, 150);// øverst til høyre
        c2.setOnMouseClicked(e -> {
            this.chosenIntersection = c2;
            System.out.println("Selected c2");

        });
        Cross c3 = new Cross(500, 450);// nederst til høyre
        c3.setOnMouseClicked(e -> {
            this.chosenIntersection = c3;
            System.out.println("Selected c3");

        });
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

    /**
     * metode for å tegne bil ui, tegner Rectangle med bilens farge og farten
     * printet oppå
     */
    public void drawCarGui() {
        biler.getChildren().clear();
        VBox vbox = new VBox(10); // VBox to contain multiple rows of cars
        HBox hbox = new HBox(10); // HBox to hold a row of up to 4 cars

        int maxCarsPerRow = 4; // Maximum number of cars in a row

        for (Car car : carList) {
            Rectangle bil = new Rectangle();
            Text tekst = new Text("speed is: \n" + " " + car.speed);

            bil.setWidth(20);
            bil.setHeight(40);
            bil.setFill(car.getColor());

            StackPane bilPane = new StackPane(bil, tekst);
            tekst.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
            StackPane.setAlignment(tekst, Pos.CENTER);

            // If the current HBox has 4 cars, add it to the VBox and create a new HBox
            if (hbox.getChildren().size() >= maxCarsPerRow) {
                vbox.getChildren().add(hbox); // Add current HBox to VBox
                hbox = new HBox(10); // Create a new HBox for the next row
            }

            hbox.getChildren().add(bilPane); // Add car to the current row (HBox)
        }

        // Add the last HBox to the VBox if there are cars left in it
        if (!hbox.getChildren().isEmpty()) {
            vbox.getChildren().add(hbox);
        }

        this.biler.getChildren().add(vbox); // Add the VBox to the main container
        this.biler.setLayoutX(600);
        this.biler.setLayoutY(450);
    }

    /** metode for å oppdatere ui */
    public void updateGUI() {
        Platform.runLater(() -> {
            drawCarGui();

        });
    }

    /** metode for å slette bilene fra trafficpane */
    public static void deleteCar(Car car) {
        Platform.runLater(() -> {

            TrafficPane.getChildren().remove(car);

        });
    }

    public void deleteAllcars() {

        for (Car car : carList) {
            TrafficPane.getChildren().remove(car.getShape());
        }
        carList.clear();

    }

}
