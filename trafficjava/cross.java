package trafficjava;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
    public static long GTI = 4000; // tiden på lyset i millisekunder

    private Direction state; // hvilken retning som har grønnt lys
    private Direction lastState; // hvilken retning som hadde grønt sist-- kan brukes til gult lys
    private Timeline lightSwitcher; // timeline for å bytte farge på lys

    // lys-variabler
    private int x, y; // x og y posisjonen til krysset
    private int lysBredde = width / 3; // bredden på rektangelet rundt lysene
    private int lysLengde = 25; // lengden på rektangelet rundt lysene
    private int radius = 5; // radius på sirkelen som representerer lyset
    public Circle sirkelopp, sirkelhøyre, sirkelvenstre, sirkelned; // sirkel objekt til kryssene
    // lys i lampene
    private Light.Distant greenlight = new Light.Distant();
    private Light.Distant redlight = new Light.Distant();
    private Lighting grøntlys = new Lighting(greenlight);
    private Lighting rødtlys = new Lighting(redlight);
    private Car greenCar;
    private List<Car> frontcar;

    /**
     * konsturktør for kryss
     * 
     * @param x start x posisjon, midt i krysset
     * @param y start y posisjon,midt i krysset
     */
    public Cross(int midtX, int midtY) {

        this.x = midtX;
        this.y = midtY;
        this.state = Direction.DOWN;
        // this.state = randomState();
        greenlight.setAzimuth(45);
        greenlight.setElevation(60);
        greenlight.setColor(Color.LIGHTGREEN);
        redlight.setAzimuth(45);
        redlight.setElevation(60);
        redlight.setColor(Color.RED);
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

        start();
    }

    private Direction randomState() {
        // TODO gir en tilfeldig state til lysene som start, mer dynamisk program
        throw new UnsupportedOperationException("Unimplemented method 'randomState'");
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
    public static void setWidth(int width) {
        Cross.width = width;
    }

    /** metode for å få bredden på veien/veien i krysset */
    public static int getWidth() {
        return width;
    }

    /** metode for å sette lengden på veien i krysset */
    public void setLength(int lenght) {
        Cross.lengde = lenght;
    }

    /** metode for å få lengden på veien i krysset */
    public static int getLength() {
        return lengde;
    }

    /**
     * metode for å få en tilfeldig ny retning
     * 
     * @param direction settes til retningen bilen kjører(Car.getDirection())
     */
    public int randomDirection(int direction) {
        int retning;
        Random random = new Random();
        do {
            retning = random.nextInt(4) + 1;
        } while (retning == direction);
        return retning;

    }

    /** metode for å sette state til lysene */
    public void setState(Direction retning) {
        this.lastState = this.state;
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

    private void gul() {
        switch (lastState) {
            case RIGHT:

                break;
            case LEFT:
                // TODO lag gult lys i javafx
                // gult lys, resten rødt
                break;
            case UP:

                break;
            case DOWN:

                break;

            default:
                break;
        }

    }

    // TODO, kall metodene
    public Optional<Car> carComingFromLeft() {
        return Main.carList.stream()
                .filter(car -> Math.abs(car.y() - y) < 5 && car.x() < x
                        && car.getDirection() == trafficjava.Car.Direction.RIGHT) // Coming from left
                .filter(car -> Car.calculateDistance(this, car) <= 100) // Within 100px
                .min(Comparator.comparingDouble(car -> Car.calculateDistance(this, car))); // Closest car
    }

    public Optional<Car> carComingFromRight() {
        return Main.carList.stream()
                .filter(car -> Math.abs(car.y() - y) < 30 && car.x() > x
                        && car.getDirection() == trafficjava.Car.Direction.LEFT) // Coming from right
                .filter(car -> Car.calculateDistance(this, car) <= 100) // Within 100px
                .min(Comparator.comparingDouble(car -> Car.calculateDistance(this, car))); // Closest car
    }

    public Optional<Car> carComingFromTop() {
        return Main.carList.stream()
                .filter(car -> Math.abs(car.x() - x) < 30 && car.y() < y
                        && car.getDirection() == trafficjava.Car.Direction.DOWN) // Coming from top
                .filter(car -> Car.calculateDistance(this, car) <= 100) // Within 100px
                .min(Comparator.comparingDouble(car -> Car.calculateDistance(this, car))); // Closest car
    }

    public Optional<Car> carComingFromBottom() {
        return Main.carList.stream()
                .filter(car -> Math.abs(car.x() - x) < 5 && car.y() > y
                        && car.getDirection() == trafficjava.Car.Direction.UP) // Coming from bottom
                .filter(car -> Car.calculateDistance(this, car) <= 100) // Within 100px
                .min(Comparator.comparingDouble(car -> Car.calculateDistance(this, car))); // Closest car
    }

    /** oppdater fargene til lysene på ui tråden */
    private void updateColor() {
        Platform.runLater(() -> {
            switch (state) {
                case RIGHT:
                    høyre();
                    break;
                case LEFT:
                    venstre();
                    break;
                case UP:
                    opp();
                    break;
                case DOWN:
                    ned();
                    break;
                default:
                    break;

            }
        });
    }

    /**
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

    /** oppdaterer lista over biler, sender også info til bilene om lysets status */
    private void updateList() {
        Optional<Car> carFromLEFT = carComingFromLeft();
        Optional<Car> carFromRIGHT = carComingFromRight();
        Optional<Car> carFromUP = carComingFromTop();
        Optional<Car> carFromDOWN = carComingFromBottom();
        switch (state) {
            case RIGHT:// lyset på høyre side sett ovenfra
                carFromLEFT.ifPresent(car -> car.redLight(this, true));// stop cars from the left
                carFromRIGHT.ifPresent(car -> car.redLight(this, false)); // allow cars from the right
                carFromDOWN.ifPresent(car -> car.redLight(this, true)); // Stop cars from bottom
                carFromUP.ifPresent(car -> car.redLight(this, true)); // stop cars from top
                break;
            case LEFT:// lyset på venstre side sett ovenfra
                carFromLEFT.ifPresent(car -> car.redLight(this, false));// allow cars from the left
                carFromRIGHT.ifPresent(car -> car.redLight(this, true)); // stop cars from the right
                carFromDOWN.ifPresent(car -> car.redLight(this, true)); // Stop cars from bottom
                carFromUP.ifPresent(car -> car.redLight(this, true)); // stop cars from top

                break;
            case DOWN:// lyset nede, sett ovefra
                carFromLEFT.ifPresent(car -> car.redLight(this, true));// stop cars from the left
                carFromRIGHT.ifPresent(car -> car.redLight(this, true)); // stop cars from the right
                carFromDOWN.ifPresent(car -> car.redLight(this, false)); // allow cars from bottom
                carFromUP.ifPresent(car -> car.redLight(this, true)); // stop cars from top
                break;
            case UP:// lyset opp, sett ovefra
                carFromLEFT.ifPresent(car -> car.redLight(this, true));// stop cars from the left
                carFromRIGHT.ifPresent(car -> car.redLight(this, true)); // stop cars from the right
                carFromDOWN.ifPresent(car -> car.redLight(this, true)); // Stop cars from bottom
                carFromUP.ifPresent(car -> car.redLight(this, false)); // Allow cars from top

                break;

            default:
                break;
        }

    }

    /** metode for å starte lysene */
    private void startLys() {
        Timeline lightSwitcher = new Timeline(new KeyFrame(Duration.millis(Cross.GTI), event -> lyslogikk()));
        lightSwitcher.setCycleCount(Timeline.INDEFINITE);
        lightSwitcher.play();
    }

    /**
     * metode for å oppdatere hvor ofte noe skjer i timelinene, ikke fungerende enda
     */
    // Method to change GTI dynamically
    public void updateGTI() {

        // Stop the current timeline
        lightSwitcher.stop();

        // Update GTI and restart the timeline with the new interval
        lightSwitcher.getKeyFrames().set(0, new KeyFrame(Duration.millis(GTI), event -> lyslogikk()));
        lightSwitcher.play();
    }

    /** metode for å starte lysene, og updater som går hvert 100millis */
    private void start() {
        startLys();
        Timeline updater = new Timeline(new KeyFrame(Duration.millis(100), event -> updateList()));
        updater.setCycleCount(Timeline.INDEFINITE); // Runs forever
        updater.play();
    }
}
