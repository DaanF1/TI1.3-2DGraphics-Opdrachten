import java.awt.*;
import java.awt.geom.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

public class Rainbow extends Application {
    private Canvas canvas;
    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(1920, 1080);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("Rainbow");
        primaryStage.show();
    }
    
    
    public void draw(FXGraphics2D graphics) {
        // Start
        graphics.translate(this.canvas.getWidth()/2 ,this.canvas.getHeight()/2);
        graphics.scale(1, -1);
        // Grafiek
        double radiusBinnen = 500;
        double radiusBuiten = 400;
        for (int i = 0; i < 10000; i++){
            double hoek = (i/3141.5f); // i/PI
            double x1 = radiusBinnen * Math.cos(hoek);
            double y1 = radiusBinnen * Math.sin(hoek);
            double x2 = radiusBuiten * Math.cos(hoek);
            double y2 = radiusBuiten * Math.sin(hoek);

            graphics.setColor(Color.getHSBColor(i/10000.0f, 1, 1));
            graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }
    }
    
    
    
    public static void main(String[] args) {
        launch(Rainbow.class);
    }

}
