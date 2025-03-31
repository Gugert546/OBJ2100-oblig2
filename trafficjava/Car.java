package trafficjava;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.util.Random;

public double maxSpeed;

class Car implements Runnable {
    enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    public static final int CAR_WIDTH = 40;
    public static final int CAR_HEIGHT = 20;

    private static List<Car> cars = new ArrayList<>();

    private final Rectangle shape;
    private Color color;
    private final double initialSpeed;
    private double currentSpeed;
    private double x;
    private double y;
    public Direction direction;
    public double acceleration; 
    

    public Car(Color color, double initialSpeed, double x, double y, Direction direction) {
        this.initialSpeed = initialSpeed;
        this.currentSpeed = initialSpeed;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.shape = new Rectangle(CAR_WIDTH, CAR_HEIGHT, color);
        this.acceleration = acceleration;
    }

    public Rectangle getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

        public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setSpeed() {
        if (currentSpeed > 0) 
            currentSpeed -= acceleration;
        if  (currentSpeed < 0) 
            currentSpeed = 0;
            return;

        if (currentSpeed == 0)
            currentSpeed += acceleration;
        if (currentSpeed == maxSpeed)
            return; 
    }

    public void setRandomColor() {
        Random random = new Random();
        Color randomColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }


    // Hvordan endre hastighet på acceleration eller deccelleration.
     /* public void update() {
     
    if (speed > 0) {
            speed *= 0.85; // Reduce speed by 15% each update
            if (speed < 0.1) speed = 0; // Stop completely when very slow
        }
        y += speed;
    }

    */
    
    // this.currentSpeed = speed;

    

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void moveCar() {

    }

    public void updateUI() {
        Platform.runLater(() -> shape.setX(x));
        Platform.runLater(() -> shape.setY(y));
    }

    @Override
    public void run() {

    }

    private Car findFrontCar() {
        Car frontCar = null;

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


    private Cross findFrontCross() {
        List<Cross> kryss = Main.getList();
        Cross frontCross = null;
        for (Cross cross : kryss) {
            if ()
        }

        int nyRetning = frontCross.newDirection();
        }

}

