package trafficjava;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class cross extends Group {

    Pane gui = new Pane();
    int lengde = 60;
    private static int bredde = 40;
    int x;
    int y;
    boolean opp;
    boolean ned;
    boolean høyre;
    boolean venstre;

    // todo- lag get metoder for boolean variabler
    /**
     * konsturktør for kryss
     * 
     * @param x       start x posisjon, midt i krysset
     * @param y       start y posisjon,midt i krysset
     * @param opp     false/true for å ha med denne siden av krysset
     * @param høyre   false/true for å ha med denne siden av krysset
     * @param ned     false/true for å ha med denne siden av krysset
     * @param venstre false/true for å ha med denne siden av krysset
     * 
     */
    public cross(int midtX, int midtY, boolean opp, boolean høyre, boolean ned, boolean venstre) {
        this.x = midtX;
        this.y = midtY;
        this.opp = opp;
        this.ned = ned;
        this.høyre = høyre;
        this.venstre = venstre;
        // varibler for roadmarkings
        int halvbredde = bredde / 2;
        int xtilvenstre = x - halvbredde;
        int yoppover = y - halvbredde;
        int xlangttilvenstre = x - lengde;
        int ylangtoppover = y - lengde;

        // grå boks for å skjule midten

        Rectangle box = new Rectangle(xtilvenstre, yoppover, bredde, bredde);
        box.setFill(Color.GRAY);
        getChildren().addAll(box);

        // opp
        // x må stå -halvparten av bredden til venstre
        if (opp == true) {

            Rectangle roadR3 = new Rectangle(xtilvenstre, ylangtoppover, bredde, lengde);
            roadR3.setFill(Color.GRAY);
            getChildren().addAll(roadR3);
            Line mopp = new Line(x, y, x, ylangtoppover);
            mopp.getStrokeDashArray().addAll(11d);
            mopp.setStroke(Color.YELLOW);
            mopp.setStrokeWidth(bredde / 20);
            getChildren().addAll(mopp);

            /*
             * trafficLight.addLight()
             */
        }
        // høyre
        // y må stå oppover
        if (høyre == true) {
            int endx = x + lengde;
            Rectangle roadR1 = new Rectangle(x, yoppover, lengde, bredde);
            roadR1.setFill(Color.GRAY);
            getChildren().addAll(roadR1);
            Line mhøyre = new Line(x, y, endx, y);
            mhøyre.getStrokeDashArray().addAll(11d);
            mhøyre.setStroke(Color.YELLOW);
            mhøyre.setStrokeWidth(bredde / 20);
            getChildren().addAll(mhøyre);
            /*
             * trafficLight.addLight()
             */
        }
        // ned
        // x må stå til venstre
        if (ned == true) {
            int endy = y + lengde;
            Rectangle roadR4 = new Rectangle(xtilvenstre, y, bredde, lengde);
            roadR4.setFill(Color.GRAY);
            getChildren().addAll(roadR4);
            Line mned = new Line(x, y, x, endy);
            mned.getStrokeDashArray().addAll(11d);
            mned.setStroke(Color.YELLOW);
            mned.setStrokeWidth(bredde / 20);
            getChildren().addAll(mned);
            /*
             * trafficLight.addLight()
             */
        }
        // venstre
        // x må stå langt il venstre og y oppover
        if (venstre == true) {
            Rectangle roadR2 = new Rectangle(xlangttilvenstre, yoppover, lengde, bredde);
            roadR2.setFill(Color.GRAY);
            getChildren().addAll(roadR2);
            Line mvenstre = new Line(x, y, xlangttilvenstre, y);
            mvenstre.getStrokeDashArray().addAll(11d);
            mvenstre.setStroke(Color.YELLOW);
            mvenstre.setStrokeWidth(bredde / 20);
            getChildren().addAll(mvenstre);
            /*
             * trafficLight.addLight()
             */
        }

    }

    /** metode for å få x posisjonen til krysset(midt i) */
    public int getX() {
        return x;

    }

    /** metode for å få y posisjonen til krysset(midt i) */
    public int getY() {
        return y;
    }

    public void getDirections() {
        String retninger = "";

        if (opp == true) {
            retninger = "kryss oppover";
        }
        if (ned == true) {
            retninger = "kryss nedover";
        }
        if (venstre == true) {
            retninger = "kryss venstre";
        }
        if (høyre == true) {
            retninger = "kryss høyre";
        }
        System.out.println(retninger);

    }

    /** metode for å sette bredden på veien/krysset */
    public void setBredde(int bredde) {
        cross.bredde = bredde;
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