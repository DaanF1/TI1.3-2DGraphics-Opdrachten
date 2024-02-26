import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Screensaver extends Application {
    private ResizableCanvas canvas;
    private ArrayList<ArrayList<Point>> points;
    private Color lineColor;
    private int amountOfBoxes = 80;
    private int timer;

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.setWidth(1920);
        canvas.setHeight(1000);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now)
            {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Screensaver");
        stage.show();
        draw(g2d);
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setBackground(Color.black);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.setColor(this.lineColor);

        for (ArrayList<Point> point : points){
            for (int i = 0; i < point.size(); i++){
                // Get points for drawing a box
                Point p1 = points.get(0).get(i);
                Point p2 = points.get(1).get(i);
                Point p3 = points.get(2).get(i);
                Point p4 = points.get(3).get(i);

                // Draw one box
                graphics.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                graphics.drawLine((int) p2.getX(), (int) p2.getY(), (int) p3.getX(), (int) p3.getY());
                graphics.drawLine((int) p3.getX(), (int) p3.getY(), (int) p4.getX(), (int) p4.getY());
                graphics.drawLine((int) p4.getX(), (int) p4.getY(), (int) p1.getX(), (int) p1.getY());

                // Amount of lines seen at once
                if (point.size() > amountOfBoxes){
                    point.remove(0);
                }
            }
//            for (int i = 0; i < point.size(); i++){
//                Point point1 = point.get(i);
//                Point point2 = point.get((i+1)%point.size());
//                graphics.draw(new Line2D.Double(point1.getX(), point1.getY(), point2.getX(), point2.getY()));
//            }
        }
    }

    public void init()
    {
        this.lineColor = Color.magenta;
        this.timer = 0;
        this.points = new ArrayList<>();
        // Adding lists & initial startingpoints
        ArrayList<Point> pointRB = new ArrayList<>();
        pointRB.add(new Point(500,100, true, false));
        this.points.add(pointRB);
        ArrayList<Point> pointLB = new ArrayList<>();
        pointLB.add(new Point(200, 300, false, true));
        this.points.add(pointLB);
        ArrayList<Point> pointLO = new ArrayList<>();
        pointLO.add(new Point(1300, 800, true, false));
        this.points.add(pointLO);
        ArrayList<Point> pointRO = new ArrayList<>();
        pointRO.add(new Point(600, 500, false, false));
        this.points.add(pointRO);
    }

    public void update(double deltaTime)
    {
        // Generate new points, and add these to the corresponding list
        timer++;
        // Timer
        if (timer%3 == 1){
            for (ArrayList<Point> point : points){
                Point p = point.get(point.size() - 1).genNextPoint();
                point.add(p);
//            for (Point p : point){
//                thePoint = point;
//                newPoint = p.genNextPoint();
//            }
            }
        }
    }

    public static void main(String[] args)
    {
        launch(Screensaver.class);
    }

}
