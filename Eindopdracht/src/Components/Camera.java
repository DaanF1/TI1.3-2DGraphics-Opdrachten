package Components;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;

public class Camera {
    private Canvas canvas;
    private double scale = 1;

    public Camera(Canvas canvas) {
        this.canvas = canvas;
    }

    public void handleScroll(ScrollEvent event) {
        double delta = event.getDeltaY();
        if (delta < 0 && scale > 0.2) {
            scale -= 0.1;
        } else if (delta > 0 && scale < 10){
            scale += 0.1;
        }
        // Prevent turning image upside-down
        if (scale < 0){
            scale = -scale;
        }
        canvas.setScaleX(scale);
        canvas.setScaleY(scale);
        event.consume();
    }
}
