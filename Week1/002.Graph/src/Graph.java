import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Graph extends Application {
    private Canvas canvas;
    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Graph");
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
        double lastY = Math.sin(-10);

        for(double x = -10; x < 10; x += resolution)
        {
            float y = (float)Math.pow(x,3);
            graphics.draw(new Line2D.Double(x*scale, y*scale, (x-resolution)*scale, lastY*scale));
            lastY = y;
        }
    }
    
    
    
    public static void main(String[] args) {
        launch(Graph.class);
    }

}
