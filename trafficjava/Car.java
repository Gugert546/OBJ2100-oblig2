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
    private int midtY = CAR_WIDTH / 2;
    private int midtX = CAR_HEIGHT / 2;

    public static int maxSpeed = 3;

    private Rectangle shape;
    public Paint color;

    private double x;
    private double y;
    public Direction direction;
    public double acceleration;
    private boolean rødt; // rødt lys, true for rødt, false for grønt.
    private boolean gult; // gult lys, true for gult, false for ikke gult
    private boolean hasTurned = false;

    private Cross currentIntersection;

    public Car(double x, double y, Direction direction) {
        this.x = x;
        this.y = y;
        setSpeed(Speed.LOW);
        this.shape = new Rectangle(CAR_WIDTH, CAR_HEIGHT, setRandomColor());
        this.color = shape.getFill();
        System.err.println("bilokject lagd med x" + x + "y" + y);
        this.shape.setX(x);
        this.shape.setY(y);
        Thread car = new Thread(this);
        setDirection(direction);
        car.start();

    }

    public Rectangle getShape() {
        return shape;
    }

    public Paint getColor() {
        return color;
    }

    public int getintDirection() {
        int intDirection = 0;
        switch (direction) {
            case UP:
                intDirection = 3;
                break;
            case RIGHT:
                intDirection = 1;
                break;
            case DOWN:
                intDirection = 4;
                break;
            case LEFT:
                intDirection = 2;
                break;
            default:
                break;
        }
        return intDirection;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setSpeed(Speed fart) {
        this.speed = fart;
    }

    public Color setRandomColor() {
        Random random = new Random();
        Color color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return color;
    }

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

    public void outOfBoundsCheck() {
        if (x > 500) {
            Main.carList.remove(this);
            Main.deleteCar(this);
        }
        if (x < -50) {
            Main.carList.remove(this);
            Main.deleteCar(this);
        }
        if (y > 850) {
            Main.carList.remove(this);
            Main.deleteCar(this);
        }
        if (y < -50) {
            Main.carList.remove(this);
            Main.deleteCar(this);
        }
    }

    private double calculateDistance(Car car1, Car car2) {
        return Math.sqrt(Math.pow(car1.getX() - car2.getX(), 2) +
                Math.pow(car1.getY() - car2.getY(), 2));
    }

    /** logikk for kjøring */
    private void drivingLogic() {
        lightCheck();
        outOfBoundsCheck();

        Car frontCar = findFrontCar();
        if (frontCar != null) {
            System.out.println("fant bil foran");
        }
        findFrontCross();
        double avstand;
        boolean isApproaching = false;

        if (currentIntersection != null) {
            isApproaching = switch (direction) {
                case RIGHT -> currentIntersection.getX() > midtX;
                case LEFT -> currentIntersection.getX() < midtX;
                case UP -> currentIntersection.getY() < midtY;
                case DOWN -> currentIntersection.getY() > midtY;
            };
        }

        if (frontCar != null) {
            System.out.println("bil:" + getColor() + "  har bil foran");
            avstand = calculateDistance(this, frontCar);
            if (avstand <= 60) {
                System.out.println("avstand: " + avstand);
                setSpeed(Speed.STOP);
                return;
            } else if (avstand <= 100) {
                System.out.println("avstand: " + avstand);
                setSpeed(Speed.LOW);
                return;
            } else {
                setSpeed(Speed.HIGH);
            }
        } else {
            if (currentIntersection != null) {
                Rectangle noStopzone = currentIntersection.getnoStopZone();
                Rectangle rslowZone = currentIntersection.getSlowZone();
                Rectangle rstopZone = currentIntersection.getStopZone();
                Rectangle turnZone = currentIntersection.getTurnZone();

                Bounds carBounds = this.shape.localToParent(this.shape.getBoundsInLocal());
                Bounds zoneBounds = noStopzone.localToParent(noStopzone.getBoundsInLocal());
                Bounds stopZone = rstopZone.localToParent(rstopZone.getBoundsInLocal());
                Bounds slowZone = rslowZone.localToParent(rslowZone.getBoundsInLocal());
                Bounds centerBounds = turnZone.localToParent(turnZone.getBoundsInLocal());

                // 🔄 Turn only once while in center
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
                                this.x = (currentIntersection.getX() - 80);

                            }
                            case Direction.DOWN -> {
                                // Moving down — snap to one of the down-going lanes
                                this.x = (x < 170 ? Main.laneNedoverVenstre : Main.laneNedoverHøyre);
                                this.y = (currentIntersection.getY() + 80);

                            }
                            case Direction.UP -> {
                                // Moving up — snap to one of the up-going lanes
                                this.x = (x < 170 ? Main.laneOppoverVenstre : Main.laneOppoverHøyre);
                                this.y = (currentIntersection.getY() - 80);

                            }
                        }
                        setSpeed(Speed.HIGH);
                        hasTurned = true;

                    }
                } else {
                    hasTurned = false; // Reset once outside center
                }

                // Always drive if inside the no-stop zone
                if (carBounds.intersects(zoneBounds)) {
                    System.out.println("bilen er i ikkestoppzone");
                    setSpeed(Speed.HIGH);
                    return;
                }

                if ((rødt || gult) && isApproaching) {
                    if (carBounds.intersects(stopZone)) {
                        setSpeed(Speed.STOP);
                        System.out.println(this.color + "bil er i stoppzone");
                        return;
                    } else if (carBounds.intersects(slowZone)) {
                        System.out.println(this.color + "bil er i slowZone");
                        setSpeed(Speed.LOW);
                        return;
                    }
                }
            }
        }

        if (!rødt && !gult)

        {
            setSpeed(Speed.HIGH);
            // System.out.println(this.color + "bil har ingen hindringer");
        }

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
                .filter(car -> {
                    switch (direction) {
                        case RIGHT:
                            return Math.abs(car.getY() - y) < 5 && car.getX() > x;
                        case LEFT:
                            return Math.abs(car.getY() - y) < 5 && car.getX() < x;
                        case UP:
                            return Math.abs(car.getX() - x) < 5 && car.getY() < y;
                        case DOWN:
                            return Math.abs(car.getX() - x) < 5 && car.getY() > y;
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

    /** setter max farten for bilene */
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

    /** kalkulerer avstanden mellom midten av krysset og midten av bilen */
    public static double calculateDistance(Cross cross, Car car) {
        return Math.hypot(cross.getX() - car.getMidtX(), cross.getY() - car.getMidtY());
    }

    private int getMidtY() {
        return midtY;
    }

    private int getMidtX() {
        return midtX;
    }

    public void setCurrentIntersection(Cross cross) {
        this.currentIntersection = cross;
    }

    public Cross getCurrentIntersection() {
        return currentIntersection;
    }

}