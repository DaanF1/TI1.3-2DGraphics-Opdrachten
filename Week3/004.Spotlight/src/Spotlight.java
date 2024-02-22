import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class Spotlight extends Application {
    private ResizableCanvas canvas;
    private Shape shape;
    private Image image;
    private Point2D middlePoint;

    @Override
    public void start(Stage stage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.setOnMouseDragged(e -> mouseDragged(e));
        canvas.setWidth(700);
        canvas.setHeight(500);
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
        stage.setTitle("Spotlight cat");
        stage.show();
        draw(g2d);
    }

    public void draw(FXGraphics2D graphics)
    {
        graphics.setClip(null);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.setBackground(Color.lightGray);
        graphics.scale(1,1);
        // Comment this line to see the picture:
        graphics.setClip(this.shape);
        graphics.drawImage(this.image, 0, 0, null);
    }

    private void mouseDragged(MouseEvent e){
        // Linkermuisknop
        if (e.isPrimaryButtonDown()){
            this.middlePoint = new Point2D.Double(e.getX() - 150, e.getY() - 150);
        }
    }

    public void init() throws Exception
    {
        this.image = ImageIO.read(getClass().getResource("/images/Png.png"));
        this.middlePoint = new Point2D.Double(150, 150);
    }

    public void update(double deltaTime)
    {
        GeneralPath path = new GeneralPath();
        path.moveTo(275+this.middlePoint.getX(),150+this.middlePoint.getY());
        path.lineTo(205+this.middlePoint.getX(),125+this.middlePoint.getY());
        path.lineTo(215+this.middlePoint.getX(),50+this.middlePoint.getY());
        path.lineTo(150+this.middlePoint.getX(), 100+this.middlePoint.getY());
        path.lineTo(75+this.middlePoint.getX(),50+this.middlePoint.getY());
        path.lineTo(85+this.middlePoint.getX(),125+this.middlePoint.getY());
        path.lineTo(15+this.middlePoint.getX(),150+this.middlePoint.getY());
        path.lineTo(85+this.middlePoint.getX(), 175+this.middlePoint.getY());
        path.lineTo(75+this.middlePoint.getX(), 250+this.middlePoint.getY());
        path.lineTo(150+this.middlePoint.getX(), 200+this.middlePoint.getY());
        path.lineTo(215+this.middlePoint.getX(), 250+this.middlePoint.getY());
        path.lineTo(205+this.middlePoint.getX(), 175+this.middlePoint.getY());
        path.closePath();
        this.shape = path;
    }

    public static void main(String[] args)
    {
        launch(Spotlight.class);
    }

}
