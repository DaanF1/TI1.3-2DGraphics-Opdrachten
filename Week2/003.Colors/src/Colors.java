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

public class Colors extends Application {
    private ResizableCanvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Colors");
        primaryStage.show();
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);
        graphics.scale(1, -1);

        // 13x loopen
        for (int i = 0; i < 13; i++){
            Rectangle2D rectangle = new Rectangle2D.Double();
            rectangle.setRect((i*50)-325, -25, 50, 50);

            // Juiste kleur vinden
            switch (i){
                case 0:
                    graphics.setColor(Color.black);
                    graphics.fill(rectangle);
                    break;
                case 1:
                    graphics.setColor(Color.blue);
                    graphics.fill(rectangle);
                    break;
                case 2:
                    graphics.setColor(Color.cyan);
                    graphics.fill(rectangle);
                    break;
                case 3:
                    graphics.setColor(Color.darkGray);
                    graphics.fill(rectangle);
                    break;
                case 4:
                    graphics.setColor(Color.gray);
                    graphics.fill(rectangle);
                    break;
                case 5:
                    graphics.setColor(Color.green);
                    graphics.fill(rectangle);
                    break;
                case 6:
                    graphics.setColor(Color.lightGray);
                    graphics.fill(rectangle);
                    break;
                case 7:
                    graphics.setColor(Color.magenta);
                    graphics.fill(rectangle);
                    break;
                case 8:
                    graphics.setColor(Color.orange);
                    graphics.fill(rectangle);
                    break;
                case 9:
                    graphics.setColor(Color.pink);
                    graphics.fill(rectangle);
                    break;
                case 10:
                    graphics.setColor(Color.red);
                    graphics.fill(rectangle);
                    break;
                case 11:
                    graphics.setColor(Color.white);
                    graphics.fill(rectangle);
                    break;
                case 12:
                    graphics.setColor(Color.yellow);
                    graphics.fill(rectangle);
                    break;
            }

            // Rand tekenen
            graphics.setColor(Color.black);
            graphics.draw(rectangle);
        }
    }


    public static void main(String[] args)
    {
        launch(Colors.class);
    }

}
