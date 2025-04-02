package trafficjava;

import javafx.scene.shape.Rectangle;

import java.util.List;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Random;

class Car extends Rectangle implements Runnable {

    enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    public static final int CAR_WIDTH = 40;
    public static final int CAR_HEIGHT = 20;

    public final int maxSpeed = 10;

    private final Rectangle shape;
    public Paint color;
    // private final double initialSpeed;
    private double currentSpeed;
    private double x;
    private double y;
    public Direction direction;
    public double acceleration;

    public Car(double x, double y, Direction direction) {
        // this.currentSpeed = initialSpeed;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.shape = new Rectangle(CAR_WIDTH, CAR_HEIGHT, Color.GOLD);
        this.color = shape.getFill();
        System.err.println("bilokject lagd med x" + x + "y" + y);
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

    public Direction getDirection() {
        return direction;
    }

    public void setSpeed() {
        if (currentSpeed > 0) {
            currentSpeed -= acceleration;
        } else if (currentSpeed < 0) {

        } else if (currentSpeed == 0) {

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

    public void moveCar(int speed) {
        updateUI();
        switch (direction) {
            case UP:
                y += -speed;
                break;
            case RIGHT:
                x += speed;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x += -speed;
                break;
            default:
                break;
        }
    }

    public void updateUI() {
        Platform.runLater(() -> shape.setX(x));
        Platform.runLater(() -> shape.setY(y));
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
            moveCar(maxSpeed);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private Car findFrontCar() {
        Car frontCar = null;
        List<Car> cars = Main.carList;
        for (Car car : cars) {
            // Bilen foran kan ikke være seg selv
            if (car == this) {
                continue;
            }

            if (direction == Direction.RIGHT && car.getY() == y && car.getX() > x) {
                if (frontCar == null || Math.abs(car.getX() - x) < Math.abs(frontCar.getX() - x)) {
                    frontCar = car;
                }
            }

            if (direction == Direction.LEFT && car.getY() == y && car.getX() < x) {
                if (frontCar == null || Math.abs(car.getX() - x) > Math.abs(frontCar.getX() - x)) {
                    frontCar = car;
                }
            }

            if (direction == Direction.UP && car.getX() == x && car.getY() < y) {
                if (frontCar == null || Math.abs(car.getY() - y) < Math.abs(frontCar.getY() - y)) {
                    frontCar = car;
                }
            }

            if (direction == Direction.DOWN && car.getX() == x && car.getY() > y) {
                if (frontCar == null || Math.abs(car.getY() - y) > Math.abs(frontCar.getY() - y)) {
                    frontCar = car;
                }

            }

        }
        return frontCar;
    }
}