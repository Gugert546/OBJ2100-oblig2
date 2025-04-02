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

    enum Speed {
        LOW, STOP, HIGH
    }

    public Speed speed;
    public static final int CAR_WIDTH = 24;
    public static final int CAR_HEIGHT = 50;

    public static int maxSpeed = 2;

    private Rectangle shape;
    public Paint color;

    private double currentSpeed;
    private double x;
    private double y;
    public Direction direction;
    public double acceleration;
    // driving logic
    private int hvorLangtRetning;

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
                break;
            case HIGH:
                moveCar(maxSpeed);
                break;
            case STOP:
                moveCar(0);
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

    private void drivingLogic() {
        outOfBoundsCheck();
        Car frontcar = findFrontCar();
        if (frontcar == null) {
            setSpeed(Speed.HIGH);
        }
        switch (hvorLangtRetning) {
            case 1:// høyre retning frontcar x minus min x
                if (frontcar.getX() - x < 75) {
                    setSpeed(Speed.LOW);
                }
                if (frontcar.getX() - x < 55) {
                    setSpeed(Speed.STOP);
                }
                if (frontcar.getX() - x >= 75) {
                    setSpeed(Speed.HIGH);
                }
                break;
            case 2:// vensre retning min x minus frontcar x
                if (x - frontcar.getX() < 75) {
                    setSpeed(Speed.LOW);
                }
                if (frontcar.getX() - x < 55) {
                    setSpeed(Speed.STOP);
                }
                if (frontcar.getX() - x >= 75) {
                    setSpeed(Speed.HIGH);
                }
                break;
            case 3:// oppover retning min y minus frontcar y
                if (y - frontcar.getY() < 75) {
                    setSpeed(Speed.LOW);
                }
                if (y - frontcar.getY() < 55) {
                    setSpeed(Speed.STOP);
                }
                if (y - frontcar.getY() >= 75) {
                    setSpeed(Speed.HIGH);
                }
                break;
            case 4:// nedover retning front car y minus min y
                if (frontcar.getY() - y < 75) {
                    setSpeed(Speed.LOW);
                }
                if (frontcar.getY() - y < 55) {
                    setSpeed(Speed.STOP);
                }
                if (frontcar.getY() - y >= 75) {
                    setSpeed(Speed.HIGH);
                }
                break;

            default:
                break;
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
                    hvorLangtRetning = 1;
                }
            }

            if (direction == Direction.LEFT && car.getY() == y && car.getX() < x) {
                if (frontCar == null || Math.abs(car.getX() - x) > Math.abs(frontCar.getX() - x)) {
                    frontCar = car;
                    hvorLangtRetning = 2;
                }
            }

            if (direction == Direction.UP && car.getX() == x && car.getY() < y) {
                if (frontCar == null || Math.abs(car.getY() - y) < Math.abs(frontCar.getY() - y)) {
                    frontCar = car;
                    hvorLangtRetning = 3;
                }
            }

            if (direction == Direction.DOWN && car.getX() == x && car.getY() > y) {
                if (frontCar == null || Math.abs(car.getY() - y) > Math.abs(frontCar.getY() - y)) {
                    frontCar = car;
                    hvorLangtRetning = 4;
                }

            }

        }
        if (frontCar != null) {
            System.out.println("funnet nærmeste bil");
        }
        return frontCar;

    }

    public static void setMaxSpeed(Integer speed) {
        Car.maxSpeed = speed;

    }

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

    public void stoppAtLight(Cross cross) {

        switch (hvorLangtRetning) {
            case 1:// høyre retning frontcar x minus min x
                if (cross.getX() - x < 75) {
                    setSpeed(Speed.LOW);
                }
                if (cross.getX() - x < 55) {
                    setSpeed(Speed.STOP);
                }
                if (cross.getX() - x >= 75) {
                    setSpeed(Speed.HIGH);
                }
                break;
            case 2:// vensre retning min x minus frontcar x
                if (x - cross.getX() < 75) {
                    setSpeed(Speed.LOW);
                }
                if (cross.getX() - x < 55) {
                    setSpeed(Speed.STOP);
                }
                if (cross.getX() - x >= 75) {
                    setSpeed(Speed.HIGH);
                }
                break;
            case 3:// oppover retning min y minus frontcar y
                if (y - cross.getY() < 75) {
                    setSpeed(Speed.LOW);
                }
                if (y - cross.getY() < 55) {
                    setSpeed(Speed.STOP);
                }
                if (y - cross.getY() >= 75) {
                    setSpeed(Speed.HIGH);
                }
                break;
            case 4:// nedover retning front car y minus min y
                if (cross.getY() - y < 75) {
                    setSpeed(Speed.LOW);
                }
                if (cross.getY() - y < 55) {
                    setSpeed(Speed.STOP);
                }
                if (cross.getY() - y >= 75) {
                    setSpeed(Speed.HIGH);
                }
                break;

            default:
                break;
        }

    }

}