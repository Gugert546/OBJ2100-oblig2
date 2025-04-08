package com.gresskar.trafficsimulator;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Car implements Runnable {
    public static List<Car> carList = new ArrayList<>();
    public static final int CAR_WIDTH = 40;
    public static final int CAR_HEIGHT = 20;

    private final Rectangle shape;
    private double x, y;
    private Speed speed;
    private Direction direction;

    public Car(double startX, double startY, Direction direction) {
        // Create the car's physical shape
        this.shape = new Rectangle(CAR_WIDTH, CAR_HEIGHT);

        // Generate a random color for the car
        final Random random = new Random();
        this.shape.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        this.shape.setStroke(Color.BLACK);

        // Set the car's position, both for the object itself and for its shape
        this.x = startX;
        this.y = startY;
        this.shape.setX(x);
        this.shape.setY(y);

        // Set the car's direction and speed
        this.direction = direction;
        this.speed = Speed.HIGH;

        // Add the car object to the car list
        carList.add(this);

        // Multi-thread the car
        Thread car = new Thread(this);
        car.start();
    }

    public Rectangle getShape() {
        return shape;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Speed getSpeed() {
        return speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setX(double x) {
        this.x = x;
        this.shape.setX(x);
    }

    public void setY(double y) {
        this.y = y;
        this.shape.setY(y);
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void move() {
        switch (direction) {
            case UP:
                y -= speed.getValue();
                break;
            case DOWN:
                y += speed.getValue();
                break;
            case LEFT:
                x -= speed.getValue();
                break;
            case RIGHT:
                x += speed.getValue();
                break;
            default:
                break;
        }
    }

    public void updateUI() {
        Platform.runLater(() -> shape.setX(x));
        Platform.runLater(() -> shape.setY(y));
    }

    private Car findFrontCar() {
        Car frontCar = null;

        for (Car car : carList) {
            // Quit early if car is null
            if (car == null) {
                continue;
            }

            // The car can't be in front of itself
            if (this == car) {
                continue;
            }

            // Ignore cars that aren't driving in the same direction
            if (direction != car.getDirection()) {
                continue;
            }

            switch (direction) {
                case UP:
                    if (frontCar == null && car.getY() < y) {
                        frontCar = car;
                    }
                    break;
                case DOWN:
                    if (frontCar == null && car.getY() > y) {
                        frontCar = car;
                    }
                    break;
                case LEFT:
                    if (frontCar == null && car.getX() < x) {
                        frontCar = car;
                    }
                    break;
                case RIGHT:
                    if (frontCar == null && car.getX() > x) {
                        frontCar = car;
                    }
                    break;
                default:
                    break;
            }
        }

        return frontCar;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 60fps (1000 / 60)
                Thread.sleep(16);
                move();
                updateUI();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
