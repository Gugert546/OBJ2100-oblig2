package trafficjava;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class vei extends Group {

    int bredde = cross.getbredde();

    /**
     * konstruktør for vei objekt
     * 
     * @param retning   true for vertikal vei, false for horisontal vei
     * @param x         settes til getX fra krysset der veien starter
     * @param y         settes til getY fra krysset der veien starter
     * @param stoppunkt settes til getX/Y fra krysset der veien stopper, x/y basert
     *                  på vertikal/horisontal vei
     */
    public vei(boolean retning, int x, int y, int stoppunkt) {

        /* vertikal vei */
        if (retning == true) {
            int startx = x - bredde / 2;
            int length = stoppunkt - y;
            Rectangle asfalt = new Rectangle(startx, y, bredde, length);
            asfalt.setFill(Color.GRAY);
            Line markings = new Line(x, y, x, stoppunkt);
            markings.getStrokeDashArray().addAll(11d);
            markings.setStroke(Color.YELLOW);
            markings.setStrokeWidth(bredde / 20);
            getChildren().addAll(asfalt, markings);
            System.out.println("vertikal veg lagd");

        }

        /* metode for å legge til en horisontal vei */
        if (retning == false) {
            int length = stoppunkt - x;
            int starty = y - bredde / 2;
            Rectangle asfalt = new Rectangle(x, starty, length, bredde);
            asfalt.setFill(Color.GRAY);
            Line markings = new Line(x, y, stoppunkt, y);
            markings.getStrokeDashArray().addAll(11d);
            markings.setStroke(Color.YELLOW);
            markings.setStrokeWidth(bredde / 20);
            getChildren().addAll(asfalt, markings);
            System.out.println("horisontal veg lagd");
        }
    }

}
