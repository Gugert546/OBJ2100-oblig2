package trafficjava;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class cross extends Group {

    Pane gui = new Pane();
    int lengde = 120;
    private static int bredde = 80;

    public cross(int x, int y, boolean opp, boolean høyre, boolean ned, boolean venstre) {

        // varibler for roadmarkings
        int middlex = x;
        int middley = y + bredde / 2;
        int ox = x - bredde / 2;
        int ny = y + bredde / 2;
        int vx = x - lengde;
        // variabler linje y akse
        int ey = y + (lengde + bredde / 2);
        int oy = y - (lengde - bredde / 2);

        // variabler linje x akse
        int sx = x - lengde;
        int ex = x + lengde;

        // grå boks for å skjule midten
        int bx = x - bredde / 2;

        Rectangle box = new Rectangle(bx, y, bredde, bredde);
        box.setFill(Color.GRAY);
        getChildren().addAll(box);
        // opp
        if (opp == true) {
            Rectangle roadR3 = new Rectangle(ox, oy, bredde, lengde);
            roadR3.setFill(Color.GRAY);
            getChildren().addAll(roadR3);
            Line mopp = new Line(middlex, middley, middlex, oy);
            mopp.getStrokeDashArray().addAll(11d);
            mopp.setStroke(Color.YELLOW);
            mopp.setStrokeWidth(bredde / 20);
            getChildren().addAll(mopp);

            /*
             * trafficLight.addLight()
             */
        }
        // høyre
        if (høyre == true) {
            Rectangle roadR1 = new Rectangle(x, y, lengde, bredde);
            roadR1.setFill(Color.GRAY);
            getChildren().addAll(roadR1);
            Line mhøyre = new Line(middlex, middley, ex, middley);
            mhøyre.getStrokeDashArray().addAll(11d);
            mhøyre.setStroke(Color.YELLOW);
            mhøyre.setStrokeWidth(bredde / 20);
            getChildren().addAll(mhøyre);
            /*
             * trafficLight.addLight()
             */
        }
        // ned
        if (ned == true) {
            Rectangle roadR4 = new Rectangle(ox, ny, bredde, lengde);
            roadR4.setFill(Color.GRAY);
            getChildren().addAll(roadR4);
            Line mned = new Line(middlex, middley, middlex, ey);
            mned.getStrokeDashArray().addAll(11d);
            mned.setStroke(Color.YELLOW);
            mned.setStrokeWidth(bredde / 20);
            getChildren().addAll(mned);
            /*
             * trafficLight.addLight()
             */
        }
        // venstre
        if (venstre == true) {
            Rectangle roadR2 = new Rectangle(vx, y, lengde, bredde);
            roadR2.setFill(Color.GRAY);
            getChildren().addAll(roadR2);
            Line mvenstre = new Line(middlex, middley, sx, middley);
            mvenstre.getStrokeDashArray().addAll(11d);
            mvenstre.setStroke(Color.YELLOW);
            mvenstre.setStrokeWidth(bredde / 20);
            getChildren().addAll(mvenstre);
            /*
             * trafficLight.addLight()
             */
        }

    }

    /**
     * metode for å lage et veikryss, må skrive true/false for å bestemme hvilke
     * retninger det skal ha
     */
    public Group addCross(int x, int y, boolean opp, boolean høyre, boolean ned, boolean venstre) {
        Group cross = new Group();
        // varibler for roadmarkings
        int middlex = x;
        int middley = y + bredde / 2;
        int ox = x - bredde / 2;
        int ny = y + bredde / 2;
        int vx = x - lengde;
        // variabler linje y akse
        int ey = y + (lengde + bredde / 2);
        int oy = y - (lengde - bredde / 2);

        // variabler linje x akse
        int sx = x - lengde;
        int ex = x + lengde;

        // grå boks for å skjule midten
        int bx = x - bredde / 2;

        Rectangle box = new Rectangle(bx, y, bredde, bredde);
        box.setFill(Color.GRAY);
        cross.getChildren().addAll(box);
        // opp
        if (opp == true) {
            Rectangle roadR3 = new Rectangle(ox, oy, bredde, lengde);
            roadR3.setFill(Color.GRAY);
            cross.getChildren().addAll(roadR3);
            Line mopp = new Line(middlex, middley, middlex, oy);
            mopp.getStrokeDashArray().addAll(11d);
            mopp.setStroke(Color.YELLOW);
            mopp.setStrokeWidth(bredde / 20);
            cross.getChildren().addAll(mopp);

            /*
             * trafficLight.addLight()
             */
        }
        // høyre
        if (høyre == true) {
            Rectangle roadR1 = new Rectangle(x, y, lengde, bredde);
            roadR1.setFill(Color.GRAY);
            cross.getChildren().addAll(roadR1);
            Line mhøyre = new Line(middlex, middley, ex, middley);
            mhøyre.getStrokeDashArray().addAll(11d);
            mhøyre.setStroke(Color.YELLOW);
            mhøyre.setStrokeWidth(bredde / 20);
            cross.getChildren().addAll(mhøyre);
            /*
             * trafficLight.addLight()
             */
        }
        // ned
        if (ned == true) {
            Rectangle roadR4 = new Rectangle(ox, ny, bredde, lengde);
            roadR4.setFill(Color.GRAY);
            cross.getChildren().addAll(roadR4);
            Line mned = new Line(middlex, middley, middlex, ey);
            mned.getStrokeDashArray().addAll(11d);
            mned.setStroke(Color.YELLOW);
            mned.setStrokeWidth(bredde / 20);
            cross.getChildren().addAll(mned);
            /*
             * trafficLight.addLight()
             */
        }
        // venstre
        if (venstre == true) {
            Rectangle roadR2 = new Rectangle(vx, y, lengde, bredde);
            roadR2.setFill(Color.GRAY);
            cross.getChildren().addAll(roadR2);
            Line mvenstre = new Line(middlex, middley, sx, middley);
            mvenstre.getStrokeDashArray().addAll(11d);
            mvenstre.setStroke(Color.YELLOW);
            mvenstre.setStrokeWidth(bredde / 20);
            cross.getChildren().addAll(mvenstre);
            /*
             * trafficLight.addLight()
             */
        }
        return cross;
    }

    /** metode for å lage en vertikal vei */

    /** legger et trådkors på skjermen for å visualisere plasseringer */
    public void addCoordinates() {
        Line xakse = new Line(0, 300, 800, 300);
        xakse.setStroke(Color.BLACK);
        xakse.setStrokeWidth(10);
        Line yakse = new Line(400, 0, 400, 600);
        yakse.setStroke(Color.BLACK);
        yakse.setStrokeWidth(10);

        gui.getChildren().addAll(xakse, yakse);
    }

    /** metode for å sette bredden på veien/krysset */
    public void setBredde(int bredde) {
        this.bredde = bredde;
    }

    /** metode for å få bredden på veien/veien i krysset */
    public static int getbredde() {
        return bredde;
    }

    /** metode for å sette lengden på veien i krysset */
    public void setLength(int lenght) {
        this.lengde = lenght;
    }

    /** metode for å få lengden på veien i krysset */
    public int getLength() {
        return lengde;
    }
}