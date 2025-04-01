package trafficjava;

import java.util.Random;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class cross extends Group implements Runnable {

    public enum Direction {
        HØYRE, VENSTRE, OPP, NED
    }

    private Direction state;
    private static int GTI = 1000;
    Pane gui = new Pane();
    int lengde = 40;
    private static int bredde = 40;
    int x;
    int y;
    private int lysBredde = 20;
    private int lysLengde = 25;
    private int radius = 5;
    public Circle sirkelopp;
    public Circle sirkelhøyre;
    public Circle sirkelvenstre;
    public Circle sirkelned;
    private Light.Distant greenlight = new Light.Distant();
    private Light.Distant redlight = new Light.Distant();

    private Lighting grøntlys = new Lighting(greenlight);
    private Lighting rødtlys = new Lighting(redlight);

    /**
     * konsturktør for kryss
     * 
     * @param x start x posisjon, midt i krysset
     * @param y start y posisjon,midt i krysset
     */
    public cross(int midtX, int midtY) {
        this.x = midtX;
        this.y = midtY;
        this.state = Direction.HØYRE;
        greenlight.setAzimuth(45);
        greenlight.setElevation(60);
        greenlight.setColor(Color.LIGHTGREEN);
        redlight.setAzimuth(45);
        redlight.setElevation(60);
        redlight.setColor(Color.RED);
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
        // x må stå -(halvparten av bredden) til venstre

        Rectangle roadR3 = new Rectangle(xtilvenstre, ylangtoppover, bredde, lengde);
        roadR3.setFill(Color.GRAY);
        getChildren().addAll(roadR3);
        Line mopp = new Line(x, y, x, ylangtoppover);
        mopp.getStrokeDashArray().addAll(11d);
        mopp.setStroke(Color.YELLOW);
        mopp.setStrokeWidth(bredde / 20);
        getChildren().addAll(mopp);

        // lys-opp
        Group lysopp = new Group();
        int lox = x - bredde / 2;
        int loy = y - bredde;
        int roy = loy - lysBredde / 2;
        int rox = lox - lysLengde / 2;
        Rectangle boksopp = new Rectangle(rox, roy, lysLengde, lysBredde);
        boksopp.setFill(Color.BLACK);
        lysopp.getChildren().addAll(boksopp);
        sirkelopp = new Circle(lox, loy, radius);
        sirkelopp.setFill(Color.WHITE);
        lysopp.getChildren().addAll(sirkelopp);
        getChildren().addAll(lysopp);
        // høyre
        // y må stå oppover

        int endx = x + lengde;
        Rectangle roadR1 = new Rectangle(x, yoppover, lengde, bredde);
        roadR1.setFill(Color.GRAY);
        getChildren().addAll(roadR1);
        Line mhøyre = new Line(x, y, endx, y);
        mhøyre.getStrokeDashArray().addAll(11d);
        mhøyre.setStroke(Color.YELLOW);
        mhøyre.setStrokeWidth(bredde / 20);
        getChildren().addAll(mhøyre);
        // lys-høyre
        Group lyshøyre = new Group();
        int lhx = x + bredde;
        int lhy = y - bredde / 2;
        int rhy = lhy - lysLengde / 2;
        int rhx = lhx - lysBredde / 2;
        Rectangle bokshøyre = new Rectangle(rhx, rhy, lysBredde, lysLengde);
        bokshøyre.setFill(Color.BLACK);
        lyshøyre.getChildren().addAll(bokshøyre);
        this.sirkelhøyre = new Circle(lhx, lhy, radius);
        sirkelhøyre.setFill(Color.WHITE);
        lyshøyre.getChildren().addAll(sirkelhøyre);
        getChildren().addAll(lyshøyre);

        // ned
        // x må stå til venstre

        int endy = y + lengde;
        Rectangle roadR4 = new Rectangle(xtilvenstre, y, bredde, lengde);
        roadR4.setFill(Color.GRAY);
        getChildren().addAll(roadR4);
        Line mned = new Line(x, y, x, endy);
        mned.getStrokeDashArray().addAll(11d);
        mned.setStroke(Color.YELLOW);
        mned.setStrokeWidth(bredde / 20);
        getChildren().addAll(mned);
        // lys-ned
        Group lysned = new Group();
        int lnx = x + bredde / 2;
        int lny = y + bredde;
        int rny = lny - lysBredde / 2;
        int rnx = lnx - lysLengde / 2;
        Rectangle boksned = new Rectangle(rnx, rny, lysLengde, lysBredde);
        boksned.setFill(Color.BLACK);
        lysned.getChildren().addAll(boksned);
        sirkelned = new Circle(lnx, lny, radius);
        sirkelned.setFill(Color.WHITE);
        lysned.getChildren().addAll(sirkelned);
        getChildren().addAll(lysned);

        // venstre
        // x må stå langt il venstre og y oppover

        Rectangle roadR2 = new Rectangle(xlangttilvenstre, yoppover, lengde, bredde);
        roadR2.setFill(Color.GRAY);
        getChildren().addAll(roadR2);
        Line mvenstre = new Line(x, y, xlangttilvenstre, y);
        mvenstre.getStrokeDashArray().addAll(11d);
        mvenstre.setStroke(Color.YELLOW);
        mvenstre.setStrokeWidth(bredde / 20);
        getChildren().addAll(mvenstre);
        // lys-venstre
        Group lysvenstre = new Group();
        int lvx = x - bredde;
        int lvy = y + bredde / 2;
        int rvy = lvy - lysLengde / 2;
        int rvx = lvx - lysBredde / 2;
        Rectangle boksvenstre = new Rectangle(rvx, rvy, lysBredde, lysLengde);
        boksvenstre.setFill(Color.BLACK);
        lysvenstre.getChildren().addAll(boksvenstre);
        sirkelvenstre = new Circle(lvx, lvy, radius);
        sirkelvenstre.setFill(Color.WHITE);
        lysvenstre.getChildren().addAll(sirkelvenstre);
        getChildren().addAll(lysvenstre);

    }

    /** metode for å få x posisjonen til krysset(midt i) */
    public int getX() {
        return x;

    }

    /** metode for å få y posisjonen til krysset(midt i) */
    public int getY() {
        return y;
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

    /**
     * metode for å få en tilfeldig ny retning
     * 
     * @param direction settes til retningen bilen kjører(Car.getDirection())
     */
    public int newDirection(int direction) {
        int retning;
        Random random = new Random();
        do {
            retning = random.nextInt(4) + 1;
        } while (retning == direction);
        return retning;

    }

    /** metode for å sette state til lysene */
    public void setState(Direction retning) {
        this.state = retning;
        updateColor();
    }

    /** metode for lys til høyre */
    private void høyre() {

        sirkelhøyre.setFill(Color.GREEN);
        sirkelhøyre.setEffect(grøntlys);
        sirkelned.setFill(Color.RED);
        sirkelned.setEffect(rødtlys);
        sirkelopp.setFill(Color.RED);
        sirkelopp.setEffect(rødtlys);
        sirkelvenstre.setFill(Color.RED);
        sirkelvenstre.setEffect(rødtlys);

    }

    /** metode for lys til venstre */
    private void venstre() {

        sirkelhøyre.setFill(Color.RED);
        sirkelhøyre.setEffect(rødtlys);
        sirkelned.setFill(Color.RED);
        sirkelned.setEffect(rødtlys);
        sirkelopp.setFill(Color.RED);
        sirkelopp.setEffect(rødtlys);
        sirkelvenstre.setFill(Color.GREEN);
        sirkelvenstre.setEffect(grøntlys);

    }

    /** metode for lys oppover */
    private void opp() {

        sirkelhøyre.setFill(Color.RED);
        sirkelhøyre.setEffect(rødtlys);
        sirkelned.setFill(Color.RED);
        sirkelned.setEffect(rødtlys);
        sirkelopp.setFill(Color.GREEN);
        sirkelopp.setEffect(grøntlys);
        sirkelvenstre.setFill(Color.RED);
        sirkelvenstre.setEffect(rødtlys);

    }

    /** metode for lys ned */
    private void ned() {

        sirkelhøyre.setFill(Color.RED);
        sirkelhøyre.setEffect(rødtlys);
        sirkelned.setFill(Color.GREEN);
        sirkelned.setEffect(grøntlys);
        sirkelopp.setFill(Color.RED);
        sirkelopp.setEffect(rødtlys);
        sirkelvenstre.setFill(Color.RED);
        sirkelvenstre.setEffect(rødtlys);

    }

    /** metode for å oppdatere lysene */
    private void updateColor() {
        Platform.runLater(() -> {
            switch (state) {
                case HØYRE:
                    høyre();
                    break;
                case VENSTRE:
                    venstre();
                    break;
                case OPP:
                    opp();
                    break;
                case NED:
                    ned();
                    break;
                default:
                    break;
            }
        });
    }

    private void gul() {
    }

    public static void setGTI(int tid) {
        cross.GTI = tid;
        System.out.println("tid på grønt lys er:" + GTI);
    }

    /** run metode, bytter farge på lysene */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(GTI);
                setState(Direction.VENSTRE);
                // legg til state GUL
                Thread.sleep(GTI);
                setState(Direction.OPP);
                Thread.sleep(GTI);
                setState(Direction.NED);
                Thread.sleep(GTI);
                setState(Direction.HØYRE);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

        }

    }
}