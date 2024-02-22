import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class MovingCharacter extends Application {
    private ResizableCanvas canvas;
    private ArrayList<BufferedImage> showingImages = new ArrayList<>();
    private ArrayList<BufferedImage> movingImages = new ArrayList<>();
    private ArrayList<BufferedImage> jumpingImages = new ArrayList<>();
    private int movedX = 0;
    private int movedY = 0;
    private double nextImage = 0.0;

    @Override
    public void start(Stage stage) throws Exception
    {
        // Put images in ArrayLists
        // Images: 64x64, 7x7 grid
        try {
            for (int i = 0; i < 8; i++){
                if (i == 5){
                    for (int j = 0; j < 8; j++){
                        BufferedImage image = ImageIO.read(getClass().getResource("/images/sprite.png")).getSubimage(j*64, i*64, 64, 64);
                        this.jumpingImages.add(image);
                    }
                    i++;
                }
                for (int j = 0; j < 8; j++){
                    BufferedImage image = ImageIO.read(getClass().getResource("/images/sprite.png")).getSubimage(j*64, i*64, 64, 64);
                    this.movingImages.add(image);
                }
            }
            BufferedImage idleImage = ImageIO.read(getClass().getResource("/images/sprite.png")).getSubimage(0*64, 8*64, 64, 64);
            this.jumpingImages.add(idleImage);
            this.showingImages = movingImages;
        } catch (IOException e) {
            e.printStackTrace();
        }

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
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
        stage.setTitle("Moving Character");
        stage.show();
        draw(g2d);
    }

    public void draw(FXGraphics2D graphics)
    {
        graphics.clearRect(0, 0, (int) this.canvas.getWidth(), (int) this.canvas.getHeight());
        graphics.setBackground(Color.darkGray);
        AffineTransform tx = new AffineTransform();
        if (this.movedX > 650){
            this.movedX = 0;
            this.movedY+=20;
            if (this.movedY > 500){
                this.movedY=0;
            }
        }
        tx.translate(0+this.movedX,50+this.movedY);
        tx.scale(1,1);

        // Draw image
        graphics.drawImage(this.showingImages.get((int) (this.nextImage % (this.showingImages.size()-1))), tx, null);
    }

    private void mousePressed(MouseEvent e)
    {
        // If pressed, show jumping animation
        if (e.isPrimaryButtonDown()){
            this.showingImages = this.jumpingImages;
        }
    }

    private void mouseReleased(MouseEvent e){
        // If released, show moving animation
        if (!e.isPrimaryButtonDown()){
            this.showingImages = this.movingImages;
        }
    }

    public void update(double deltaTime)
    {
        this.movedX+=1;
        if (this.showingImages.containsAll(this.jumpingImages)){
            this.movedX-=1;
        }
        this.nextImage+=0.15;
    }

    public static void main(String[] args)
    {
        launch(MovingCharacter.class);
    }

}
