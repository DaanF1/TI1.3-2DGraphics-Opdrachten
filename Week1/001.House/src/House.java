import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;

public class House extends Application {
    private Canvas canvas;
    @Override
    public void start(Stage primaryStage) throws Exception {
        canvas = new Canvas(1024, 768);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(new Group(canvas)));
        primaryStage.setTitle("House");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics) {
        // Start
        graphics.translate(this.canvas.getWidth()/2 ,this.canvas.getHeight()/2);
        graphics.scale(1, -1);
        graphics.setColor(new Color(1,1,1));
        // Huis
        graphics.draw(new Line2D.Double(-200, -200, 200, -200));
        graphics.draw(new Line2D.Double(200, -200, 200, 150));
        graphics.draw(new Line2D.Double(-200, -200, -200, 150));
        graphics.draw(new Line2D.Double(-200, 150, 0, 375));
        graphics.draw(new Line2D.Double(200, 150, 0, 375));
        // Deur
        graphics.draw(new Line2D.Double(-175, -200, -175, -25));
        graphics.draw(new Line2D.Double(-100, -200, -100, -25));
        graphics.draw(new Line2D.Double(-100, -25, -175, -25));
        // Raam
        graphics.draw(new Line2D.Double(150, -150, -50, -150));
        graphics.draw(new Line2D.Double(150, -50, -50, -50));
        graphics.draw(new Line2D.Double(150, -50, 150, -150));
        graphics.draw(new Line2D.Double(-50, -50, -50, -150));
    }



    public static void main(String[] args) {
        launch(House.class);
    }

}
