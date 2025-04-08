package trafficjava;

import javafx.scene.shape.Rectangle;
import java.util.Comparator;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.Random;

class Car extends Rectangle implements Runnable {

    enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    enum Speed {
        LOW, STOP, HIGH
    }

    public Speed speed;
    public static final int CAR_WIDTH = 24;
    public static final int CAR_HEIGHT = 50;
    private int midtY = CAR_WIDTH / 2;
    private int midtX = CAR_HEIGHT / 2;

    public static int maxSpeed = 3;

    private Rectangle shape;
    public Paint color;

    private double currentSpeed;
    private double x;
    private double y;
    public Direction direction;
    public double acceleration;
    private boolean rødt; // rødt lys, true for rødt, false for grønt.
    private boolean gult; // gult lys, true for gult, false for ikke gult

    private Cross currentIntersection;

    public Car(double x, double y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.shape = new Rectangle(CAR_WIDTH, CAR_HEIGHT, setRandomColor());
        this.color = shape.getFill();
        System.err.println("bilokject lagd med x" + x + "y" + y);
        this.shape.setX(x);
        this.shape.setY(y);
        Thread car = new Thread(this);
        car.start();

    }

    public Rectangle getShape() {
        return shape;
    }

    public Paint getColor() {
        return color;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
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

    public void setSpeed(Speed speed) {
        switch (speed) {
            case LOW:
                moveCar(0.5);
                currentSpeed = 0.5;
                break;
            case HIGH:
                moveCar(maxSpeed);
                currentSpeed = maxSpeed;
                break;
            case STOP:
                moveCar(0);
                currentSpeed = 0;
                break;
            default:
                break;
        }
    }

    public Color setRandomColor() {
        Random random = new Random();
        Color color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return color;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void moveCar(double speed) {

        switch (direction) {
            case UP:
                y += -speed;
                break;
            case RIGHT:
                x += speed;
                shape.setWidth(CAR_HEIGHT);
                shape.setHeight(CAR_WIDTH);
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x += -speed;
                shape.setWidth(CAR_HEIGHT);
                shape.setHeight(CAR_WIDTH);
                break;
            default:
                break;
        }
    }

    public void outOfBoundsCheck() {
        // TODO fjern fra skjerm
        if (x > 600) {
            Main.carList.remove(this);
            Main.deleteCar(this);
        }
        if (x < 0) {
            Main.carList.remove(this);
            Main.deleteCar(this);
        }
        if (y > 800) {
            Main.carList.remove(this);
            Main.deleteCar(this);
        }
        if (y < 0) {
            Main.carList.remove(this);
            Main.deleteCar(this);
        }
    }

    private double calculateDistance(Car car1, Car car2) {
        return Math.sqrt(Math.pow(car1.x() - car2.x(), 2) +
                Math.pow(car1.y() - car2.y(), 2));
    }

    /** logikk for kjøring */
    private void drivingLogic() {
        lightCheck();
        outOfBoundsCheck();
        Car frontCar = findFrontCar();
        findFrontCross();
        double avstand;
        // Check if approaching intersection
        boolean isApproaching = false;
        double distToCross = Double.MAX_VALUE;

        if (currentIntersection != null) {

            distToCross = calculateDistance(currentIntersection, this);
            isApproaching = switch (direction) {
                case RIGHT -> currentIntersection.getX() > midtX;
                case LEFT -> currentIntersection.getX() < midtX;
                case UP -> currentIntersection.getY() < midtY;
                case DOWN -> currentIntersection.getY() > midtY;
            };
        }

        if (frontCar != null) {
            avstand = calculateDistance(this, frontCar);
            if (avstand <= 60) {
                System.out.println("avstand: " + avstand);
                setSpeed(Speed.STOP);

            } else if (avstand <= 100) {
                System.out.println("avstand: " + avstand);
                setSpeed(Speed.LOW);
            } else {
                setSpeed(Speed.HIGH);
            }
        } else {

            if (currentIntersection != null) {
                Rectangle zone = currentIntersection.getStopZone();
                Bounds carBounds = this.localToScene(this.getBoundsInLocal());
                Bounds zoneBounds = zone.localToScene(zone.getBoundsInLocal());

                // Always drive if inside the stop zone
                if (carBounds.intersects(zoneBounds)) {
                    setSpeed(Speed.HIGH);
                    return;
                }

                if (rødt && isApproaching) {
                    if (distToCross >= 65 && distToCross <= 75) {
                        setSpeed(Speed.STOP);
                    } else if (distToCross <= 100) {
                        setSpeed(Speed.LOW);
                    }
                }

                if (gult && isApproaching) {
                    if (distToCross <= 65 && distToCross <= 75) {
                        setSpeed(Speed.STOP);
                    } else if (distToCross < 100) {
                        setSpeed(Speed.LOW);
                    }
                }
            }
        }
        if (frontCar == null && !rødt && !gult) {
            setSpeed(Speed.HIGH);
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

    /** get metode for x */
    public double x() {
        return x;
    }

    /** get metode for y */
    public double y() {
        return y;
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
                            return Math.abs(car.y() - y) < 5 && car.x() > x;
                        case LEFT:
                            return Math.abs(car.y() - y) < 5 && car.x() < x;
                        case UP:
                            return Math.abs(car.x() - x) < 5 && car.y() < y;
                        case DOWN:
                            return Math.abs(car.x() - x) < 5 && car.y() > y;
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