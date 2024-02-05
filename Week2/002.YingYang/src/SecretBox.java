import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class SecretBox extends Application{
    private ResizableCanvas canvas;
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Secret Box");
        primaryStage.show();
    }

    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);
        graphics.scale(1, -1);

        for (int j = 0; j < 13; j++){
            Color color = new Color(j, j*2, j*3);
            for (int i = 0; i < 13; i++){
                Rectangle2D rectangle = new Rectangle2D.Double();
                rectangle.setRect(i, i + 50, 50, 50);
                graphics.setColor(color);
                graphics.fill(rectangle);
            }
        }
    }

    public static void main(String[] args)
    {
        launch(SecretBox.class);
    }
}
