package trafficjava;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class JavaFXApp extends Application {
    public final int CAR_AMOUNT = 5;

    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        // Create a curved path instead of a straight line
        Path path = new Path();
        path.getElements().add(new MoveTo(50, 300)); // Start point
        path.getElements().add(new LineTo(150, 300));
        path.getElements().add(new CubicCurveTo(200, 300, 200, 200, 200, 200));
        path.getElements().add(new LineTo(200, 50));
        // path.getElements().add(new LineTo(450, 300));
        path.setStroke(Color.BLACK);
        path.setFill(null);

        // Create a rectangle that moves along the path
        Rectangle bil1 = new Rectangle(20, 20, 22, 10);
        Rectangle bil2 = new Rectangle(20, 20, 22, 10);
        Rectangle bil3 = new Rectangle(20, 20, 22, 10);
        Rectangle bil4 = new Rectangle(20, 20, 22, 10);
        Rectangle bil5 = new Rectangle(20, 20, 22, 10);
        bil1.setFill(getRandomColor());
        bil2.setFill(getRandomColor());
        bil3.setFill(getRandomColor());
        bil4.setFill(getRandomColor());
        bil5.setFill(getRandomColor());

        // Path transition for animation
        PathTransition pt = new PathTransition();
        pt.setDuration(Duration.millis(3500));
        pt.setPath(path);
        pt.setNode(bil1);
        pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pt.play();

        PathTransition pt2 = new PathTransition();
        pt2.setDuration(Duration.millis(5000));
        pt2.setPath(path);
        pt2.setNode(bil2);
        pt2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        PauseTransition pause1 = new PauseTransition(Duration.seconds(3));
        pause1.setOnFinished(event -> pt2.play());
        pause1.play();

        PathTransition pt3 = new PathTransition();
        pt3.setDuration(Duration.millis(5000));
        pt3.setPath(path);
        pt3.setNode(bil3);
        pt3.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        PauseTransition pause2 = new PauseTransition(Duration.seconds(6));
        pause2.setOnFinished(event -> pt3.play());
        pause2.play();

        PathTransition pt4 = new PathTransition();
        pt4.setDuration(Duration.millis(5000));
        pt4.setPath(path);
        pt4.setNode(bil4);
        pt4.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        PauseTransition pause3 = new PauseTransition(Duration.seconds(9));
        pause3.setOnFinished(event -> pt4.play());
        pause3.play();

        PathTransition pt5 = new PathTransition();
        pt5.setDuration(Duration.millis(5000));
        pt5.setPath(path);
        pt5.setNode(bil5);
        pt5.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        PauseTransition pause4 = new PauseTransition(Duration.seconds(12));
        pause4.setOnFinished(event -> pt5.play());
        pause4.play();

        pane.getChildren().addAll(path, bil1, bil2, bil3, bil4, bil5); // Add the path and rectangle to the pane

        Scene scene = new Scene(pane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Curved Path Animation");
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static Color getRandomColor() {
        Random rand = new Random();
        return Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
    }
}
