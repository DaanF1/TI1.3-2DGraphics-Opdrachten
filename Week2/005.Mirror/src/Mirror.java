import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;
import sun.java2d.opengl.OGLRenderQueue;

public class Mirror extends Application {
    ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Mirror");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.translate(this.canvas.getWidth()/2, this.canvas.getHeight()/2);
        graphics.scale(0.5,-0.5);

        // Assenstelsel
        graphics.drawLine(-400, 0, 400, 0);
        graphics.drawLine(0, -400, 0, 400);
        // Lijn: Y=2.5*X
        graphics.drawLine(-200, (int) (2.5*(-200)), 200, (int) (2.5*200));
        // Vierkant
        Rectangle2D rectangle = new Rectangle2D.Double(-50, 150, 100, 100);
        graphics.draw(rectangle);
        // Vierkant spiegelen
        // Heb 0.7 gebruikt, anders werd het vierkant te groot
        graphics.transform(new AffineTransform(0.7, -0.7, 0.7, 0.7, -0.7, 0.7));
        graphics.draw(rectangle);
    }


    public static void main(String[] args)
    {
        launch(Mirror.class);
    }

}
