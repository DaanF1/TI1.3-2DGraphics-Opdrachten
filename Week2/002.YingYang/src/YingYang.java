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

public class YingYang extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Ying Yang");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);
        graphics.scale(1, -1);

        Shape cirkel = new Ellipse2D.Double(-100, -100, 200,200);
        Shape binnenCirkel1 = new Ellipse2D.Double(-15, 35, 30, 30);
        Shape binnenCirkel2 = new Ellipse2D.Double(-15, -65, 30, 30);

        GeneralPath path = new GeneralPath();
        path.moveTo(0,100);
        path.curveTo(135, 95, 135, -95, 0, -100);
        path.curveTo(-65, -95, -65, -5, 0, 0);
        path.curveTo(65,5,65,95,0, 100);
        path.closePath();

        graphics.setColor(Color.black);
        graphics.draw(cirkel);
        graphics.fill(path);
        graphics.fill(binnenCirkel1);
        graphics.setColor(Color.white);
        graphics.fill(binnenCirkel2);
        graphics.setColor(Color.black);
    }


    public static void main(String[] args)
    {
        launch(YingYang.class);
    }

}
