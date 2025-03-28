package trafficjava;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Vei extends Group {

    int bredde = Cross.getbredde();

    /**
     * konstruktør for vei objekt
     * 
     * @param retning true for vertikal vei, false for horisontal vei
     */
    public Vei(boolean retning, int x, int y, int length) {
        /* vertikal vei */
        if (retning = true) {
            int startx = x - bredde / 2;
            Rectangle asfalt = new Rectangle(startx, y, bredde, length);
            asfalt.setFill(Color.GRAY);
            int sx = x - bredde / 2;
            int ey = y + length;
            int my = y + bredde / 2;
            Line markings = new Line(x, my, x, ey);
            markings.getStrokeDashArray().addAll(11d);
            markings.setStroke(Color.YELLOW);
            markings.setStrokeWidth(bredde / 20);
            getChildren().addAll(asfalt, markings);

        }

        /* metode for å legge til en horisontal vei */
        if (retning == false) {
            Rectangle asfalt = new Rectangle(x, y, length, bredde);
            asfalt.setFill(Color.GRAY);
            int sx = x - bredde / 2;
            int ex = x + length;
            int my = y + bredde / 2;
            Line markings = new Line(sx, my, ex, my);
            markings.getStrokeDashArray().addAll(11d);
            markings.setStroke(Color.YELLOW);
            markings.setStrokeWidth(bredde / 20);
            getChildren().addAll(asfalt, markings);
        }
    }
}
