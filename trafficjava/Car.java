package trafficjava;

import javafx.scene.shape.Rectangle;

import java.util.Comparator;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

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
    private final ReentrantLock lock = new ReentrantLock();

    public static int maxSpeed = 2;

    private Rectangle shape;
    public Paint color;

    private double currentSpeed;
    private double x;
    private double y;
    public Direction direction;
    public double acceleration;
    private boolean rødt;

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
        }
        if (x < 0) {
            Main.carList.remove(this);
        }
        if (y > 800) {
            Main.carList.remove(this);
        }
        if (y < 0) {
            Main.carList.remove(this);
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

        if (frontCar != null) {
            avstand = calculateDistance(this, frontCar);
            if (avstand <= 60) {
                System.out.println("avstand: " + avstand);
                setSpeed(Speed.STOP);

            } else if (avstand <= 80) {
                System.out.println("avstand: " + avstand);
                setSpeed(Speed.LOW);

            }
        }

        if (rødt && currentIntersection != null) {
            double distToCross = calculateDistance(currentIntersection, this);
            if (distToCross < 30 && distToCross > 10) {
                System.out.println("disttocross: " + distToCross);
                setSpeed(Speed.STOP);

            } else if (distToCross < 60 && distToCross > 30) {
                System.out.println("disttocross: " + distToCross);
                setSpeed(Speed.LOW);

            }
        }
        if (frontCar == null && rødt == false) {
            setSpeed(Speed.HIGH);
        }
    }

    private void lightCheck() {
        if (currentIntersection != null) {
            trafficjava.Cross.Direction lys = currentIntersection.getState();
            if (lys == trafficjava.Cross.Direction.RIGHT && this.direction == Direction.LEFT ||
                    lys == trafficjava.Cross.Direction.LEFT && this.direction == Direction.RIGHT ||
                    lys == trafficjava.Cross.Direction.UP && this.direction == Direction.DOWN ||
                    lys == trafficjava.Cross.Direction.DOWN && this.direction == Direction.UP) {
                redLight(true);
                return;
            } else
                redLight(false);
        }
    }

    private void findFrontCross() {

        this.currentIntersection = Main.crossList.stream()

                .filter(cross -> {

                    switch (direction) {
                        case RIGHT:
                            return Math.abs(cross.getY() - y) < 30 && cross.getX() > x;
                        case LEFT:
                            return Math.abs(cross.getY() - y) < 30 && cross.getX() < x;
                        case UP:
                            return Math.abs(cross.getX() - x) < 30 && cross.getY() < y;
                        case DOWN:
                            return Math.abs(cross.getX() - x) < 30 && cross.getY() > y;
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

    private Speed getSpeed() {
        return speed;
    }

    public double x() {
        return x;
    }

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

    public static void setMaxSpeed(Integer speed) {
        Car.maxSpeed = speed;

    }

    /** oppdaterer bilene på UI-tråden, gjør av vi kan se forflyttelse */
    public void updateUI() {
        Platform.runLater(() -> shape.setX(x));
        Platform.runLater(() -> shape.setY(y));
    }

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
     * @param cross settes til krysset som har rødt lys
     * @param rødt  boolean, settes til true hvis rødt
     */
    public void redLight(boolean rødt) {
        this.rødt = rødt;
    }

    public static double calculateDistance(Cross cross, Car car) {
        return Math.sqrt(Math.pow(cross.getX() - car.y(), 2) +
                Math.pow(cross.getX() - car.y(), 2));
    }

    public void setCurrentIntersection(Cross cross) {
        this.currentIntersection = cross;
    }

    public Cross getCurrentIntersection() {
        return currentIntersection;
    }
    /*
     * public boolean tryClaimIntersection(Cross intersection) {
     * if (lock.tryLock()) { // Try to acquire the lock without blocking
     * try {
     * if (currentIntersection == null || currentIntersection == intersection) {
     * currentIntersection = intersection;
     * return true; // Successfully claimed
     * }
     * } finally {
     * lock.unlock(); // Always unlock
     * }
     * }
     * return false; // Another intersection is controlling the car
     * }
     * 
     * public Cross findClosestIntersection(List<Cross> intersections) {
     * return intersections.stream()
     * .min(Comparator.comparingDouble(cross -> Car.calculateDistance(cross, this)))
     * .orElse(null);
     * }
     */
}