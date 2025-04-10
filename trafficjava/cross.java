package trafficjava;

import java.util.Random;

import javax.swing.text.StyledEditorKit.BoldAction;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Cross extends Group {

    public enum Direction {
        RIGHT, LEFT, UP, DOWN
    }

    // statiske variabler
    private static int lengde = 80; // lengden på veiene i krysset
    private static int width = 60; // bredden på veiene i krysset
    public static long GTI = 3500; // tiden på lyset i millisekunder
    public static long yT = 2500; // tiden på gult lys

    private Direction state; // hvilken retning som har grønnt lys
    // farge på lys

    // lys-variabler
    private int x, y; // x og y posisjonen til krysset
    private int lysBredde = width / 3; // bredden på rektangelet rundt lysene
    private int lysLengde = 25; // lengden på rektangelet rundt lysene
    private int radius = 5; // radius på sirkelen som representerer lyset
    public Circle sirkelopp, sirkelhøyre, sirkelvenstre, sirkelned; // sirkel objekt til kryssene
    // lys i lampene
    private Light.Distant greenlight = new Light.Distant();
    private Light.Distant redlight = new Light.Distant();
    private Light.Distant yellowLight = new Light.Distant();
    private Lighting grøntlys = new Lighting(greenlight);
    private Lighting rødtlys = new Lighting(redlight);
    private Lighting gultlys = new Lighting(yellowLight);
    // gult lys, true hvis gult
    Boolean gult;
    // region som biler ikke stopper i
    private Rectangle noStopZone;
    // region som biler stopper i
    private Rectangle stopZone;
    // region som biler kjører sakte i
    private Rectangle slowZone;
    // region som biler svinger i
    private Rectangle turnZone;

    /**
     * konsturktør for kryss
     * 
     * @param x start x posisjon, midt i krysset
     * @param y start y posisjon,midt i krysset
     */
    public Cross(int midtX, int midtY) {

        this.x = midtX;
        this.y = midtY;
        randomState();
        // Create the no stop zone if a car is inside, it will continue even after the
        // light changes
        noStopZone = new Rectangle(x - 40, y - 40, 80, 80);
        // create a stop zone, where cars will stop if the light is red
        stopZone = new Rectangle(x - 80, y - 80, 160, 160);
        stopZone.setFill(Color.YELLOW);
        // create a slow zone, where cars will be slowed
        slowZone = new Rectangle(x - 140, y - 140, 280, 280);
        slowZone.setFill(Color.ALICEBLUE);
        slowZone.setVisible(false);
        // create a turn zone,where cars will get a new direction
        turnZone = new Rectangle(x - 10, y - 10, 20, 20);
        getChildren().add(turnZone);
        getChildren().add(slowZone);
        getChildren().add(noStopZone);
        getChildren().add(stopZone);
        turnZone.setVisible(false);// debug
        turnZone.setFill(Color.BLACK);
        stopZone.setVisible(false); // debug
        noStopZone.setFill(Color.BLACK);
        noStopZone.setVisible(false); // or false in production

        greenlight.setAzimuth(45);
        greenlight.setElevation(60);
        greenlight.setColor(Color.LIGHTGREEN);
        redlight.setAzimuth(45);
        redlight.setElevation(60);
        redlight.setColor(Color.RED);
        yellowLight.setAzimuth(45);
        yellowLight.setElevation(60);
        yellowLight.setColor(Color.YELLOW);
        // varibler for roadmarkings
        int xtilvenstre = x - (width / 2);
        int yoppover = y - (width / 2);
        int xlangttilvenstre = x - lengde;
        int ylangtoppover = y - lengde;

        // grå boks for å skjule midten

        Rectangle box = new Rectangle(xtilvenstre, yoppover, width, width);
        box.setFill(Color.GRAY);
        getChildren().addAll(box);

        // opp
        // x må stå -(halvparten av bredden) til venstre

        Rectangle roadR3 = new Rectangle(xtilvenstre, ylangtoppover, width, lengde);
        roadR3.setFill(Color.GRAY);
        getChildren().addAll(roadR3);
        Line mopp = new Line(x, y, x, ylangtoppover);
        mopp.getStrokeDashArray().addAll(11d);
        mopp.setStroke(Color.YELLOW);
        mopp.setStrokeWidth(width / 20);
        getChildren().addAll(mopp);

        // lys-opp
        Group lysopp = new Group();
        int lox = x - width / 2;
        int loy = y - width;
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
        Rectangle roadR1 = new Rectangle(x, yoppover, lengde, width);
        roadR1.setFill(Color.GRAY);
        getChildren().addAll(roadR1);
        Line mhøyre = new Line(x, y, endx, y);
        mhøyre.getStrokeDashArray().addAll(11d);
        mhøyre.setStroke(Color.YELLOW);
        mhøyre.setStrokeWidth(width / 20);
        getChildren().addAll(mhøyre);
        // lys-høyre
        Group lyshøyre = new Group();
        int lhx = x + width;
        int lhy = y - width / 2;
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
        Rectangle roadR4 = new Rectangle(xtilvenstre, y, width, lengde);
        roadR4.setFill(Color.GRAY);
        getChildren().addAll(roadR4);
        Line mned = new Line(x, y, x, endy);
        mned.getStrokeDashArray().addAll(11d);
        mned.setStroke(Color.YELLOW);
        mned.setStrokeWidth(width / 20);
        getChildren().addAll(mned);
        // lys-ned
        Group lysned = new Group();
        int lnx = x + width / 2;
        int lny = y + width;
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

        Rectangle roadR2 = new Rectangle(xlangttilvenstre, yoppover, lengde, width);
        roadR2.setFill(Color.GRAY);
        getChildren().addAll(roadR2);
        Line mvenstre = new Line(x, y, xlangttilvenstre, y);
        mvenstre.getStrokeDashArray().addAll(11d);
        mvenstre.setStroke(Color.YELLOW);
        mvenstre.setStrokeWidth(width / 20);
        getChildren().addAll(mvenstre);
        // lys-venstre
        Group lysvenstre = new Group();
        int lvx = x - width;
        int lvy = y + width / 2;
        int rvy = lvy - lysLengde / 2;
        int rvx = lvx - lysBredde / 2;
        Rectangle boksvenstre = new Rectangle(rvx, rvy, lysBredde, lysLengde);
        boksvenstre.setFill(Color.BLACK);
        lysvenstre.getChildren().addAll(boksvenstre);
        sirkelvenstre = new Circle(lvx, lvy, radius);
        sirkelvenstre.setFill(Color.WHITE);
        lysvenstre.getChildren().addAll(sirkelvenstre);
        getChildren().addAll(lysvenstre);
        turnZone.toFront();
        stopZone.toFront();
        noStopZone.toFront();
        slowZone.toFront();
        setState(state);
        startLys();
    }

    /** setter en tilfeldig retning på lyset */
    private void randomState() {
        Random random = new Random();
        int tall = random.nextInt(4) + 1;
        switch (tall) {
            case 1:
                this.state = Direction.UP;
                break;
            case 2:
                this.state = Direction.DOWN;
                break;
            case 3:
                this.state = Direction.LEFT;
                break;
            case 4:
                this.state = Direction.RIGHT;
                break;

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

    /** metode for å få bredden på veien/veien i krysset */
    public static int getWidth() {
        return width;
    }

    /**
     * metode for å få lengden på veien i krysset
     * 
     * @return int
     */
    public static int getLength() {
        return lengde;
    }

    /** metode for å sette state til lysene */
    public void setState(Direction retning) {
        this.state = retning;
        updateColor();
    }

    /**
     * get merode for staten til lyset
     * 
     * @return Diretion enum
     */
    public Direction getState() {
        return this.state;
    }

    /** metode for å sette gult lys */
    private void gult() {
        gult = true;
        Platform.runLater(() -> {
            switch (state) {
                case RIGHT:
                    sirkelhøyre.setFill(Color.YELLOW);
                    sirkelhøyre.setEffect(gultlys);
                    sirkelned.setFill(Color.RED);
                    sirkelned.setEffect(rødtlys);
                    sirkelopp.setFill(Color.RED);
                    sirkelopp.setEffect(rødtlys);
                    sirkelvenstre.setFill(Color.RED);
                    sirkelvenstre.setEffect(rødtlys);
                    break;
                case LEFT:
                    sirkelhøyre.setFill(Color.RED);
                    sirkelhøyre.setEffect(rødtlys);
                    sirkelned.setFill(Color.RED);
                    sirkelned.setEffect(rødtlys);
                    sirkelopp.setFill(Color.RED);
                    sirkelopp.setEffect(rødtlys);
                    sirkelvenstre.setFill(Color.YELLOW);
                    sirkelvenstre.setEffect(gultlys);
                    break;
                case UP:
                    sirkelhøyre.setFill(Color.RED);
                    sirkelhøyre.setEffect(rødtlys);
                    sirkelned.setFill(Color.RED);
                    sirkelned.setEffect(rødtlys);
                    sirkelopp.setFill(Color.YELLOW);
                    sirkelopp.setEffect(gultlys);
                    sirkelvenstre.setFill(Color.RED);
                    sirkelvenstre.setEffect(rødtlys);

                    break;
                case DOWN:
                    sirkelhøyre.setFill(Color.RED);
                    sirkelhøyre.setEffect(rødtlys);
                    sirkelned.setFill(Color.YELLOW);
                    sirkelned.setEffect(gultlys);
                    sirkelopp.setFill(Color.RED);
                    sirkelopp.setEffect(rødtlys);
                    sirkelvenstre.setFill(Color.RED);
                    sirkelvenstre.setEffect(rødtlys);

                    break;

                default:
            }
        });
    }

    /** oppdater fargene til lysene på ui tråden */
    private void updateColor() {
        gult = false;
        Platform.runLater(() -> {
            switch (state) {
                case RIGHT:
                    sirkelhøyre.setFill(Color.GREEN);
                    sirkelhøyre.setEffect(grøntlys);
                    sirkelned.setFill(Color.RED);
                    sirkelned.setEffect(rødtlys);
                    sirkelopp.setFill(Color.RED);
                    sirkelopp.setEffect(rødtlys);
                    sirkelvenstre.setFill(Color.RED);
                    sirkelvenstre.setEffect(rødtlys);
                    break;
                case LEFT:
                    sirkelhøyre.setFill(Color.RED);
                    sirkelhøyre.setEffect(rødtlys);
                    sirkelned.setFill(Color.RED);
                    sirkelned.setEffect(rødtlys);
                    sirkelopp.setFill(Color.RED);
                    sirkelopp.setEffect(rødtlys);
                    sirkelvenstre.setFill(Color.GREEN);
                    sirkelvenstre.setEffect(grøntlys);
                    break;
                case UP:
                    sirkelhøyre.setFill(Color.RED);
                    sirkelhøyre.setEffect(rødtlys);
                    sirkelned.setFill(Color.RED);
                    sirkelned.setEffect(rødtlys);
                    sirkelopp.setFill(Color.GREEN);
                    sirkelopp.setEffect(grøntlys);
                    sirkelvenstre.setFill(Color.RED);
                    sirkelvenstre.setEffect(rødtlys);
                    break;
                case DOWN:
                    sirkelhøyre.setFill(Color.RED);
                    sirkelhøyre.setEffect(rødtlys);
                    sirkelned.setFill(Color.GREEN);
                    sirkelned.setEffect(grøntlys);
                    sirkelopp.setFill(Color.RED);
                    sirkelopp.setEffect(rødtlys);
                    sirkelvenstre.setFill(Color.RED);
                    sirkelvenstre.setEffect(rødtlys);
                    break;
                default:
                    break;

            }
        });
    }

    /*
     * set tiden for grønt lys
     * 
     * @param tid antall millisekunder
     */
    public static void setGTI(long tid) {
        Cross.GTI = tid;
        System.out.println("tid på grønt lys er:" + GTI);
    }

    /** metode med logikk for lysene, skal rulere hvilken retning som er grønn */
    private void lyslogikk() {
        switch (state) {
            case LEFT:
                setState(Direction.UP);
                break;
            case RIGHT:
                setState(Direction.DOWN);
                break;
            case UP:
                setState(Direction.RIGHT);
                break;
            case DOWN:
                setState(Direction.LEFT);
                break;
            default:
                break;

        }

    }

    /**
     * metode for å starte lysene
     */
    private void startLys() {
        lyslogikk(); // bytt farge på lysene
        Timeline lightSwitcher = new Timeline(new KeyFrame(Duration.millis(Cross.GTI), event -> startYellowPhase()));
        lightSwitcher.setCycleCount(1);
        lightSwitcher.play();
    }

    /**
     * metode for å kjøre gule lys
     */
    private void startYellowPhase() {
        gult(); // Turn on yellow light
        Timeline yellowTimer = new Timeline(new KeyFrame(Duration.millis(yT), event -> startLys()));
        yellowTimer.setCycleCount(1);
        yellowTimer.play();
    }

    /**
     * get metode for ikke-stopp sonen
     * 
     * @return Rectangle
     */
    public Rectangle getnoStopZone() {
        return noStopZone;
    }

    /**
     * get metode for sakte sonen
     * 
     * @return Rectangle
     */
    public Rectangle getSlowZone() {
        return slowZone;
    }

    /**
     * metode for å få stopZone
     * 
     * @return Rectangle
     */
    public Rectangle getStopZone() {
        return stopZone;
    }

    /**
     * get metode for turnzone
     * 
     * @return Rectangle
     */
    public Rectangle getTurnZone() {
        return turnZone;
    }

    public Boolean getGult() {
        return this.gult;
    }
}
