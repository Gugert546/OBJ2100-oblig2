package trafficjava;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) {
        Pane TrafficPane = new Pane();
        // gui
        Rectangle guifelt = new Rectangle(600, 30, 200, 540);
        guifelt.setFill(Color.GRAY);
        TrafficPane.getChildren().addAll(guifelt);

        // kryss på venstre siden
        cross cVn = new cross(50, 550, true, true, false, false);// nederst til venstre
        cross cVo = new cross(50, 50, false, true, true, false);// øverst til venstre
        // cVm,
        // cross cVm = new cross(50, 300, true, true, true, false);// midten til venstre
        TrafficPane.getChildren().addAll(cVn, cVo);
        // kryss i midten venstre
        cross cVMo = new cross(225, 50, false, false, true, false);// venstre midten oppe
        cross cVM1 = new cross(225, 175, true, true, true, true);
        cross cVM2 = new cross(225, 425, true, true, true, true);
        // TrafficPane.getChildren().addAll(cVMo, cVM1, cVM2);

        // kryss midten høyre
        cross cMo = new cross(400, 50, false, true, true, true);// midten oppe
        cross cMn = new cross(400, 550, true, true, false, true);// midten nede
        cross cMm = new cross(400, 300, true, true, true, true); // i midten
        // TrafficPane.getChildren().addAll(cMo, cMn, cMm);

        // kryss på høyre siden
        cross cHo = new cross(575, 50, false, false, true, true); // øverst til høyre
        cross cHn = new cross(575, 550, true, false, false, true); // nederst til høyre
        cross cHm = new cross(575, 300, true, false, true, true); // midten til høyre cHm,
        TrafficPane.getChildren().addAll(cHo, cHn);

        // kryss med lys

        cross oppHøyre = new cross(400, 175, true, true, true, true);
        cross nedHøyre = new cross(400, 425, true, true, true, true);
        // TrafficPane.getChildren().addAll(oppvenstre, nedvenstre, oppHøyre, nedHøyre);
        // vertikale veier
        /*
         * vei vei1 = new vei(true, cVm.getX(), cVm.getY(), cVn.getY());
         * vei vei2 = new vei(true, cVo.getX(), cVo.getY(), cVm.getY());
         * vei vei3 = new vei(true, cMo.getX(), cMo.getY(), cMm.getY());
         * vei vei4 = new vei(true, cMm.getX(), cMm.getY(), cMn.getY());
         * vei vei5 = new vei(true, cHo.getX(), cHo.getY(), cHm.getY());
         * vei vei6 = new vei(true, cHm.getX(), cHm.getY(), cHn.getY());
         */
        // TrafficPane.getChildren().addAll(vei1, vei2, vei3, vei4, vei5, vei6);

        // horisontale veier
        /*
         * vei vei7 = new vei(false, cVn.getX(), cVn.getY(), cMn.getX());
         * vei vei8 = new vei(false, cVm.getX(), cVm.getY(), cMm.getX());
         * vei vei9 = new vei(false, cVo.getX(), cVo.getY(), cMo.getX());
         * vei vei10 = new vei(false, cMo.getX(), cMo.getY(), cHo.getX());
         * vei vei11 = new vei(false, cMm.getX(), cMm.getY(), cHm.getX());
         * vei vei12 = new vei(false, cMn.getX(), cMn.getY(), cHn.getX());
         */
        // TrafficPane.getChildren().addAll(vei7, vei8, vei9, vei10, vei11, vei12);

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
