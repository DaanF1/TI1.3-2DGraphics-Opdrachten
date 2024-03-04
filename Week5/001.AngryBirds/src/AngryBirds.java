
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.*;
import org.dyn4j.geometry.Rectangle;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;

public class AngryBirds extends Application {

    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private boolean debugSelected = false;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private BufferedImage backgroundImage;

    @Override
    public void start(Stage stage) throws Exception {
        this.backgroundImage = ImageIO.read(getClass().getResource("/background.png"));

        BorderPane mainPane = new BorderPane();

        // Add debug button
        javafx.scene.control.CheckBox showDebug = new CheckBox("Show debug");
        showDebug.setOnAction(e -> {
            debugSelected = showDebug.isSelected();
        });
        mainPane.setTop(showDebug);

        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

        camera = new Camera(canvas, g -> draw(g), g2d);
        mousePicker = new MousePicker(canvas);

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane, 1920, 1080));
        stage.setTitle("Angry Birds");
        stage.show();
        draw(g2d);
    }

    public void init() {
        // World
        world = new World();
        world.setGravity(new Vector2(0, -9.8));

        // Body's
        Body ground = new Body();
        BodyFixture groundFixture = new BodyFixture(Geometry.createRectangle(27, 2));
        //groundFixture.setFriction(100.0);
        ground.addFixture(groundFixture);
        Vector2 groundVector = new Vector2(0, -3);
        ground.getTransform().setTranslation(groundVector);
        ground.setMass(MassType.INFINITE);
        this.world.addBody(ground);
        GameObject groundObject = new GameObject("/ground.png", ground, groundVector.add(0, -75), 0.3);
        this.gameObjects.add(groundObject);

        Body red = new Body();
        BodyFixture redFixture = new BodyFixture(Geometry.createCircle(0.2));
        redFixture.setRestitution(0.5);
        red.addFixture(redFixture);
        Vector2 redVector = new Vector2(0, 0);
        red.getTransform().setTranslation(redVector);
        red.setMass(MassType.NORMAL);
        this.world.addBody(red);
        GameObject redObject = new GameObject("/birds/red.png", red, redVector.add(-70, -65), 0.05);
        this.gameObjects.add(redObject);

    }

    public void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.drawImage(this.backgroundImage, 0, 0, null);


        AffineTransform originalTransform = graphics.getTransform();

        graphics.setTransform(camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()));
        graphics.scale(1, -1);

        for (GameObject go : gameObjects) {
            go.draw(graphics);
        }

        if (debugSelected) {
            graphics.setColor(Color.blue);
            DebugDraw.draw(graphics, world, 100);
        }

        graphics.setTransform(originalTransform);
    }

    public void update(double deltaTime) {
        mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        world.update(deltaTime);
    }

    public static void main(String[] args) {
        launch(AngryBirds.class);
    }

}
