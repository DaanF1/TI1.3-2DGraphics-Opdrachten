import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

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

public class FadingImage extends Application {
    private ResizableCanvas canvas;
    private BufferedImage currentImage;
    private BufferedImage newImage;
    private float opacityChange = 0;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.currentImage = ImageIO.read(getClass().getResource("/images/cat1.jpg"));
        this.newImage = ImageIO.read(getClass().getResource("/images/cat2.png"));

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.setWidth(300);
        canvas.setHeight(300);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
		        if(last == -1)
                    last = now;
		        update((now - last) / 1000000000.0);
		        last = now;
		        draw(g2d);
            }
        }.start();
        
        stage.setScene(new Scene(mainPane));
        stage.setTitle("Fading cats");
        stage.show();
        draw(g2d);
    }
    
    
    public void draw(FXGraphics2D graphics) {
        AffineTransform tx = new AffineTransform();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 0.5f));
        graphics.drawImage(this.currentImage, 0, 0, null);

        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.opacityChange));
        graphics.drawImage(this.newImage, tx, null);
    }
    

    public void update(double deltaTime) {
        if (this.opacityChange < 0.99f){
            this.opacityChange+= 0.003f;
        } else {
            BufferedImage changingImage = this.currentImage;
            this.currentImage = this.newImage;
            this.newImage = changingImage;
            this.opacityChange = 0.000f;
        }
    }

    public static void main(String[] args) {
        launch(FadingImage.class);
    }

}
