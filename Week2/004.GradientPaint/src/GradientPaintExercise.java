import java.awt.*;
import java.awt.geom.*;

import com.sun.webkit.graphics.WCGraphicsContext;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class GradientPaintExercise extends Application {
    // Heb een tijdje vastgezeten bij deze opdracht, vandaar alle attributen
    private ResizableCanvas canvas;
    private FXGraphics2D g;
    private RadialGradientPaint paint;
    private int size = 3;
    private Point2D focusPoint;
    private float radius;
    private boolean initialize = true;
    private float[] fractions = new float[this.size];
    private Color[] colors = new Color[this.size];
    private MultipleGradientPaint.CycleMethod cycleMethod = MultipleGradientPaint.CycleMethod.REFLECT;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        // MouseDrag event
        canvas.setOnMouseDragged(e -> {
            this.focusPoint.setLocation(new Point2D.Float((float) e.getX(), (float) e.getY()));
            this.paint =
                    new RadialGradientPaint((float) this.focusPoint.getX(), (float) this.focusPoint.getY(), this.radius, this.fractions, this.colors);
            this.g.setPaint(this.paint);
            this.g.fill(new Ellipse2D.Double(0, 0, this.radius * 2, this.radius * 2));
        });
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("GradientPaint");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        //graphics.clearRect(- (int) canvas.getWidth()/2, - (int) canvas.getHeight()/2, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);

        if (this.initialize){
            // Settings
            this.g = graphics;
            graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);
            graphics.scale(1, -1);

            focusPoint = new Point2D.Float(0, 0);
            radius = 200;
            fractions[0] = 0.1F;
            fractions[1] = 0.6F;
            fractions[2] = 1F;
            colors[0] = Color.RED;
            colors[1] = Color.WHITE;
            colors[2] = Color.BLUE;

            this.paint =
                    new RadialGradientPaint((float) this.focusPoint.getX(), (float) this.focusPoint.getY(), this.radius, this.fractions, this.colors);

            this.initialize = false;
        }

        graphics.setPaint(this.paint);
        graphics.fill(new Ellipse2D.Double(0, 0, this.radius * 2, this.radius * 2));
    }


    public static void main(String[] args)
    {
        launch(GradientPaintExercise.class);
    }

}
