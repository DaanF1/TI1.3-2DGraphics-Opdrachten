import java.awt.*;
import java.awt.geom.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class GradientPaintExercise extends Application {
    private ResizableCanvas canvas;
    private int size = 3;
    private Point2D focusPoint;
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
//        canvas.setOnMouseDragged(e -> {
//            this.point = new Point2D.Double(e.getX(),e.getY());
//        });
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("GradientPaint");
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        if (this.initialize){
            graphics.setTransform(new AffineTransform());
            graphics.setBackground(Color.white);
            graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
            graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);
            graphics.scale(1, -1);

            // Settings
            fractions[0] = 0.3F;
            fractions[1] = 0.7F;
            fractions[2] = 1F;
            colors[0] = Color.red;
            colors[1] = Color.white;
            colors[2] = Color.BLUE;

            // Draw initial RadialGradientPaint
            RadialGradientPaint rgp = new RadialGradientPaint(new Point2D.Double(0,0), 0.2F,
                    new Point2D.Double(100,100), fractions, colors, cycleMethod);


            this.initialize = false;
        }
//        RadialGradientPaint rgp = new RadialGradientPaint(new Point2D.Double(0,0), 0.2F,
//                new Point2D.Double(this.focusPoint.getX(),this.focusPoint.getY()), fractions, colors, cycleMethod);
    }


    public static void main(String[] args)
    {
        launch(GradientPaintExercise.class);
    }

}
