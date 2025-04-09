package trafficjava;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.Random;

class Car extends Rectangle implements Runnable {

    enum Direction {
        UP, RIGHT, DOWN, LEFT;

        private static final Random PRNG = new Random();

        public static Direction randomDirection() {
            Direction[] directions = values();
            return directions[PRNG.nextInt(directions.length)];
        }

        public static Direction randomDirectionExcludingOpposite(Direction current) {
            List<Direction> options = new ArrayList<>(List.of(values()));
            options.remove(oppositeOf(current));
            return options.get(PRNG.nextInt(options.size()));
        }

        public static Direction oppositeOf(Direction dir) {
            return switch (dir) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }
    }

    public enum Speed {
        STOP(0), LOW(1), HIGH(3);

        private final int value;

        Speed(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Speed speed;
    public static final int CAR_WIDTH = 24;
    public static final int CAR_HEIGHT = 50;

    public static int maxSpeed = 3;

    private Rectangle shape;
    public Paint color;

    public double x;
    public double y;
    public Direction direction;
    public double acceleration;
    private boolean rødt; // rødt lys, true for rødt, false for grønt.
    private boolean gult; // gult lys, true for gult, false for ikke gult
    private boolean hasTurned = false;
    // krysset foran bilen, settes av findFrontcross()
    private Cross currentIntersection;

    /**
     * konstruktør for bil, lager en bil(Rectangle), setter x og y, og starter å
     * kjøre
     * hver bil kjører i en egen tråd
     *
     * 
     * @param x         x-verdien til bilen
     * @param y         y-verdien til bilen
     * @param direction hvilken retning bilen starter å kjøre, settes til enum
     *                  Direction
     */
    public Car(double x, double y, Direction direction) {
        this.x = x;
        this.y = y;
        setSpeed(Speed.LOW);
        this.shape = new Rectangle(CAR_WIDTH, CAR_HEIGHT, setRandomColor());
        this.shape.setStroke(Color.BLACK);
        this.color = shape.getFill();
        System.err.println("bilokject lagd med x" + x + "y" + y);
        this.shape.setX(x);
        this.shape.setY(y);
        Thread car = new Thread(this);
        setDirection(direction);
        car.start();

    }

    /**
     * get metode for å få formen til bilen
     * 
     * @return Rectangle
     */
    public Rectangle getShape() {
        return shape;
    }

    /**
     * get metode for å få fargen til bilen
     * 
     * @return Paint
     */
    public Paint getColor() {
        return color;
    }

    /**
     * get metode for å få retningen bilen kjører
     * 
     * @return Direction enum over retninger
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * set metode for farten
     * 
     * @param fart Speed enum,stop,low,high
     */
    public void setSpeed(Speed fart) {
        this.speed = fart;
    }

    /**
     * metode for å få en tilfeldig farge
     * 
     * @return Color
     */
    public Color setRandomColor() {
        Random random = new Random();
        Color color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return color;
    }

    /**
     * set metode for retning, roterer også bilen
     * 
     * @param nyretning Direction enum, nye retningen
     */
    public void setDirection(Direction nyretning) {
        this.direction = nyretning;
        if (nyretning == Direction.DOWN) {
            this.shape.setWidth(CAR_WIDTH);
            this.shape.setHeight(CAR_HEIGHT);
        }
        if (nyretning == Direction.RIGHT) {
            this.shape.setWidth(CAR_HEIGHT);
            this.shape.setHeight(CAR_WIDTH);
        }
        if (nyretning == Direction.UP) {
            this.shape.setWidth(CAR_WIDTH);
            this.shape.setHeight(CAR_HEIGHT);
        }
        if (nyretning == Direction.LEFT) {
            this.shape.setWidth(CAR_HEIGHT);
            this.shape.setHeight(CAR_WIDTH);
        }

    }

    /**
     * metode for å flytte bilen, kjøres i run() flytter bilen basert på verdien i
     * speed(enum)
     */
    public void moveCar() {

        switch (direction) {
            case UP:
                y += -speed.getValue();
                break;
            case RIGHT:
                x += speed.getValue();

                break;
            case DOWN:
                y += speed.getValue();
                break;
            case LEFT:
                x += -speed.getValue();

                break;
            default:
                break;
        }
    }

    /**
     * sjekker om bilen er utenfor skjermen,kjøres i drivingLogic
     * sletter også bilene fra carList, og fjerner de fra skjermen
     */
    public void outOfBoundsCheck() {
        if (x > 600) {
            Main.carList.remove(this);
            Main.deleteCar(this);
            this.shape.setVisible(false);
        }
        if (x < -50) {
            Main.carList.remove(this);
            Main.deleteCar(this);
            this.shape.setVisible(false);
        }
        if (y > 750) {
            Main.carList.remove(this);
            Main.deleteCar(this);
            this.shape.setVisible(false);
        }
        if (y < -50) {
            Main.carList.remove(this);
            Main.deleteCar(this);
            this.shape.setVisible(false);
        }
    }

    /** kalkulerer distansen mellom to bil objekter */
    private double calculateDistance(Car car1, Car car2) {
        return Math.sqrt(Math.pow(car1.x - car2.x, 2) +
                Math.pow(car1.y - car2.y, 2));
    }

    /** logikk for kjøring,kjøres fra run() */
    private void drivingLogic() {
        lightCheck();
        outOfBoundsCheck();

        Car frontCar = findFrontCar(); // finner bilen foran
        // if (frontCar != null) {
        // System.out.println("fant bil foran");
        // }
        findFrontCross(); // finner nærmeste kryss foran bilen
        double avstand = frontCar != null ? calculateDistance(this, frontCar) : Double.MAX_VALUE;
        // sving i midten av krysset
        if (currentIntersection != null) {
            Rectangle turnZone = currentIntersection.getTurnZone();
            Bounds carBounds = this.shape.localToParent(this.shape.getBoundsInLocal());
            Bounds centerBounds = turnZone.localToParent(turnZone.getBoundsInLocal());

            // Turn only once while in center
            if (carBounds.intersects(centerBounds)) {
                if (!hasTurned) {
                    Direction newDirection = Direction.randomDirectionExcludingOpposite(direction);
                    // finner en ny retning(eksluder nåverende retning)
                    setDirection(newDirection);
                    // setter ny direction(håndterer også flipping av biler)
                    System.out.println("endret retning til" + newDirection);
                    switch (newDirection) {
                        case Direction.RIGHT -> {
                            // Moving to the right — snap to one of the right-going lanes
                            this.y = (y < 170 ? Main.laneHøyreOppe : Main.laneHøyreNede);
                            this.x = (currentIntersection.getX() + 20);

                        }
                        case Direction.LEFT -> {
                            // Moving to the left — snap to one of the left-going lane
                            this.y = (y < 170 ? Main.laneVenstreOppe : Main.laneVenstreNede);
                            this.x = (currentIntersection.getX() - 55);

                        }
                        case Direction.DOWN -> {
                            // Moving down — snap to one of the down-going lanes
                            this.x = (x < 170 ? Main.laneNedoverVenstre : Main.laneNedoverHøyre);
                            this.y = (currentIntersection.getY());

                        }
                        case Direction.UP -> {
                            // Moving up — snap to one of the up-going lanes
                            this.x = (x < 170 ? Main.laneOppoverVenstre : Main.laneOppoverHøyre);
                            this.y = (currentIntersection.getY() - 50);

                        }
                    }
                    setSpeed(Speed.HIGH);
                    hasTurned = true;

                }
            } else {
                hasTurned = false; // Reset once outside center
            }

        }

        // kjører alltid i no stop zone)midten av krysset
        if (currentIntersection != null) {
            Rectangle noStopzone = currentIntersection.getnoStopZone();
            Bounds carBounds = this.shape.localToParent(this.shape.getBoundsInLocal());
            Bounds zoneBounds = noStopzone.localToParent(noStopzone.getBoundsInLocal());
            // Always drive if inside the no-stop zone
            if (carBounds.intersects(zoneBounds)) {
                // System.out.println("bilen er i ikkestoppzone");
                setSpeed(Speed.HIGH);
                return;
            }
        }
        // sjekker om bilen er på vei inn i et kryss, isApproaching settes til
        // true/false
        boolean isApproaching = false;
        if (currentIntersection != null) {
            isApproaching = switch (direction) {
                case RIGHT -> currentIntersection.getX() > x;
                case LEFT -> currentIntersection.getX() < x;
                case UP -> currentIntersection.getY() < y;
                case DOWN -> currentIntersection.getY() > y;

            };
        }
        // lyssjekk
        if (currentIntersection != null && (rødt || gult) && isApproaching) {
            Rectangle rslowZone = currentIntersection.getSlowZone();
            Rectangle rstopZone = currentIntersection.getStopZone();
            Bounds carBounds = this.shape.localToParent(this.shape.getBoundsInLocal());
            Bounds stopZone = rstopZone.localToParent(rstopZone.getBoundsInLocal());
            Bounds slowZone = rslowZone.localToParent(rslowZone.getBoundsInLocal());
            if (carBounds.intersects(stopZone) && avstand > 100) {
                setSpeed(Speed.STOP);
                // System.out.println(this.color + "bil er i stoppzone");
                return;
            } else if (carBounds.intersects(slowZone) && avstand > 100) {
                // System.out.println(this.color + "bil er i slowZone");
                setSpeed(Speed.LOW);
                return;
            }
        }
        // kollisjonsdeteksjon
        if (frontCar != null) {
            System.out.println("bil:" + getColor() + "  har bil foran");
            if (avstand <= 60) {
                System.out.println("avstand: " + avstand);
                setSpeed(Speed.STOP);
                return;

            } else if (avstand <= 80) {
                System.out.println("avstand: " + avstand);
                setSpeed(Speed.LOW);
                return;

            }
        }
        // default behavior
        setSpeed(Speed.HIGH);
        // System.out.println(this.color + "bil har ingen hindringer"); //debug
    }

    /** sjekker lysets status på krysset foran bilen */
    private void lightCheck() {
        if (currentIntersection != null) {
            trafficjava.Cross.Direction lys = currentIntersection.getState();
            if (lys == trafficjava.Cross.Direction.RIGHT && this.direction == Direction.LEFT ||
                    lys == trafficjava.Cross.Direction.LEFT && this.direction == Direction.RIGHT ||
                    lys == trafficjava.Cross.Direction.UP && this.direction == Direction.DOWN ||
                    lys == trafficjava.Cross.Direction.DOWN && this.direction == Direction.UP) {
                redLight(false);
                return;
            } else
                redLight(true);
        }
    }

    /**
     * finner nærmeste kryss foran bilen
     */
    private void findFrontCross() {

        this.currentIntersection = Main.crossList.stream()

                .filter(cross -> {

                    switch (direction) {
                        case RIGHT:
                            return Math.abs(cross.getY() - y) < 30 && cross.getX() > x &&
                                    Math.abs(cross.getX() - x) <= 100;
                        case LEFT:
                            return Math.abs(cross.getY() - y) < 30 && cross.getX() < x &&
                                    Math.abs(cross.getX() - x) <= 100;
                        case UP:
                            return Math.abs(cross.getX() - x) < 30 && cross.getY() < y &&
                                    Math.abs(cross.getY() - y) <= 100;
                        case DOWN:
                            return Math.abs(cross.getX() - x) < 30 && cross.getY() > y &&
                                    Math.abs(cross.getY() - y) <= 100;
                        default:
                            return false;
                    }

                })
                .min(Comparator.comparingDouble(cross -> {
                    switch (direction) {
                        case RIGHT:
                        case LEFT:
                            return Math.abs(cross.getX() - x);
                        case UP:
                        case DOWN:
                            return Math.abs(cross.getY() - y);
                        default:
                            return Double.MAX_VALUE;
                    }
                }))
                .orElse(null);

    }

    /** get metode for fart(i enum datatype) */
    private Speed getSpeed() {
        return speed;
    }

    /**
     * finner bilen foran
     * 
     * @return Car nærmeste bil
     */
    private Car findFrontCar() {

        return Main.carList.stream()
                .filter(car -> car != this)
                // .peek(car -> System.out.println("Checking car at: " + car.x + ", " + car.y))
                .filter(car -> {
                    switch (direction) {
                        case RIGHT:
                            return Math.abs(car.y - y) < 5 && car.x > x;
                        case LEFT:
                            return Math.abs(car.y - y) < 5 && car.x < x;
                        case UP:
                            return Math.abs(car.x - x) < 5 && car.y < y;
                        case DOWN:
                            return Math.abs(car.x - x) < 5 && car.y > y;
                        default:
                            return false;
                    }
                })
                .min(Comparator.comparingDouble(car -> {
                    switch (direction) {
                        case RIGHT:
                            return Math.abs(car.getX() - x);
                        case LEFT:
                            return Math.abs(car.getX() - x);
                        case UP:
                            return Math.abs(car.getY() - y);
                        case DOWN:
                            return Math.abs(car.getY() - y);
                        default:
                            return Double.MAX_VALUE;
                    }
                }))
                .orElse(null);
    }

    /**
     * setter max farten for bilene
     * 
     * @param speed int
     */
    public static void setMaxSpeed(Integer speed) {
        Car.maxSpeed = speed;

    }

    /** oppdaterer bilene på UI-tråden, gjør av vi kan se forflyttelse */
    public void updateUI() {
        Platform.runLater(() -> shape.setX(x));
        Platform.runLater(() -> shape.setY(y));
    }

    /** run metode, kjører drivinglocig() og updateUI() */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
                drivingLogic();
                updateUI();
                moveCar();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * metode for å fortelle bilen at lyset er rødt
     * 
     * @param rødt boolean, settes til true hvis rødt
     */
    public void redLight(boolean rødt) {
        this.rødt = rødt;
    }

    /**
     * set metode for currentIntersection
     * 
     * @param cross Cross objekt
     */
    public void setCurrentIntersection(Cross cross) {
        this.currentIntersection = cross;
    }

    /** get metode for currentIntersection */
    public Cross getCurrentIntersection() {
        return currentIntersection;
    }

}