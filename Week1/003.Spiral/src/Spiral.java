import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Spiral extends Application {
    private Canvas canvas;
    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Spiral");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics) {
        // Start
        graphics.translate(this.canvas.getWidth()/2 ,this.canvas.getHeight()/2);
        graphics.scale(1, -1);
        graphics.setColor(new Color(1,1,1));
        // Grafiek
        double resolution = 0.1;
        double scale = 50.0;
        double lastY = 0;
        double lastX = 0;
        double n = 0.1;

        for(double angle = 0; angle < 20 * Math.PI; angle+=0.1)
        {
            double radius = n * angle;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);

            graphics.draw(new Line2D.Double(lastX*scale, lastY*scale, x*scale, y*scale));
            lastX = x;
            lastY = y;
        }
    }
    
    
    
    public static void main(String[] args) {
        launch(Spiral.class);
    }

}
