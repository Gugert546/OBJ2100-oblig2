package trafficjava;

import java.util.ArrayList;
import java.util.List;

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
import javafx.util.Duration;

//lyskryss sjekker for nærmeste bil, gir beskejed om nedbremsing/stopping.
//gul lys, er en buffer som gir bilen tid til å kommme ut av krysset
//car.setspeed(int newspeed) går gradvis til den nye farten, brukes for nedbremsing og starting 
//nedbremsingshastighet avhenger av distansen mellom bil og kryss, må også ha en limit på minste avstand før bremsing

public class Main extends Application {

    public static List<Car> cars = new ArrayList<>();
    public static List<cross> kryss = new ArrayList<>();
    private int CAR_COUNT = 6;

    public void start(Stage primaryStage) {
        Pane TrafficPane = new Pane();

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
                cross.setGTI(GTI);
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

        });
        
        //legger til gui elementer på skjermen
        VBox gui = new VBox(10);
        gui.getChildren().addAll(Ftekst, fart, Fknapp, Ttekst, tid, Tknapp, tekst, bilAntall, knapp);
        gui.setLayoutX(610);
        gui.setLayoutY(300);
        TrafficPane.getChildren().addAll(guifelt, gui);

        // bakgrunn
        TrafficPane.setStyle("-fx-background-color: lightgreen;");
        // oppbygning av vei system
        cross c0 = new cross(125, 150);// øverst til venstre
        cross c1 = new cross(125, 450);// nederst til venstre
        vei vV = new vei(true, c0.getX(), c0.getY(), c1.getY()); 
        vV.toBack();

        cross c2 = new cross(500, 150);// øverst til høyre
        cross c3 = new cross(500, 450);// nederst til høyre
        vei vH = new vei(true, c2.getX(), c2.getY(), c3.getY());
        vH.toBack();

        TrafficPane.getChildren().addAll(c0, c1, c2, c3, vV, vH);
        kryss.add(c0);
        kryss.add(c1);
        kryss.add(c2);
        kryss.add(c3);

        vei vT = new vei(false, c0.getX(), c0.getY(), c2.getX());
        vT.toBack();
        vei vB = new vei(false, c1.getX(), c1.getY(), c3.getX());
        vB.toBack();

        TrafficPane.getChildren().addAll(vT, vB);
        
        
        
        

       // Bil del

        Timeline carSpawner = new Timeline(new KeyFrame(Duration.seconds(4), event -> {
            if (cars.size() >= CAR_COUNT) return; // Stop spawning if the limit is reached

                Car car = new Car(200, 100, LEFT); 
                cars.add(car);
                TrafficPane.getChildren().add(car);

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

}
