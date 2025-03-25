import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TrafficLight implements Runnable {
    public enum LightState {
        RED, YELLOW, GREEN
    }

    private LightState state;
    private final Rectangle lightRectangle; // JavaFX GUI-element

    public TrafficLight(Rectangle lightRectangle) {
        this.state = LightState.RED;
        this.lightRectangle = lightRectangle;
        updateColor();
    }

    public synchronized void setState(LightState newState) {
        this.state = newState;
        updateColor();
    }

    public synchronized LightState getState() {
        return state;
    }

    private void updateColor() {
        Platform.runLater(() -> {
            switch (state) {
                case RED -> lightRectangle.setFill(Color.RED);
                case YELLOW -> lightRectangle.setFill(Color.YELLOW);
                case GREEN -> lightRectangle.setFill(Color.GREEN);
            }
        });
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(4000); // Tid mellom skifter
                setState(LightState.GREEN);
                Thread.sleep(3000);
                setState(LightState.YELLOW);
                Thread.sleep(2000);
                setState(LightState.RED);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
