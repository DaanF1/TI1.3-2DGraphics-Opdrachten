
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.*;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;

public class AngryBirds extends Application {
    // Controls:
    // Scroll: Zoom in / uit
    // Middel muisknop: Reset game
    // Rechter muisknop: Spawn nieuwe volgel
    private ResizableCanvas canvas;
    private World world;
    private MousePicker mousePicker;
    private Camera camera;
    private Body birdBody;
    private boolean debugSelected = false;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private BufferedImage backgroundImage;
    private Point2D mousePoint;
    private Point2D oldMousePoint;
    private boolean shootBird = true;

    @Override
    public void start(Stage stage) throws Exception {
        this.backgroundImage = ImageIO.read(getClass().getResource("/map/background.png"));

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

        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));

        stage.setScene(new Scene(mainPane, 1920, 1080));
        stage.setTitle("Angry Birds");
        stage.show();
        draw(g2d);
    }

    public void reset(){
        gameObjects.clear();
        init();
    }

    public void newBird(){
        Body red = new Body();
        BodyFixture redFixture = new BodyFixture(Geometry.createCircle(0.2));
        redFixture.setRestitution(0.5);
        redFixture.setDensity(5);
        red.addFixture(redFixture);
        Vector2 redVector = new Vector2(-7, -1);
        red.getTransform().setTranslation(redVector);
        red.setMass(MassType.NORMAL);
        this.world.addBody(red);
        GameObject redObject = new GameObject("/birds/red.png", red, redVector.add(-70, -65), 0.05);
        this.gameObjects.add(redObject);
        this.birdBody = red;

        // Teken catapult opnieuw
        Body catapult = new Body();
        Vector2 catapultVector = new Vector2(-6.9, -1.5);
        catapult.getTransform().setTranslation(catapultVector);
        catapult.setMass(MassType.INFINITE);
        this.world.addBody(catapult);
        GameObject catapultObject = new GameObject("/map/catapult.png", catapult, catapultVector, 0.4);
        this.gameObjects.remove(catapultObject);
        this.gameObjects.add(catapultObject);

        shootBird = true;
    }

    public void init() {
        // World
        world = new World();
        world.setGravity(new Vector2(0, -20)); // 0, -9.8

        // Body's
        Body ground = new Body();
        BodyFixture groundFixture = new BodyFixture(Geometry.createRectangle(27, 2));
        groundFixture.setFriction(100);
        ground.addFixture(groundFixture);
        Vector2 groundVector = new Vector2(0, -3);
        ground.getTransform().setTranslation(groundVector);
        ground.setMass(MassType.INFINITE);
        this.world.addBody(ground);
        GameObject groundObject = new GameObject("/map/ground.png", ground, groundVector.add(0, -75), 0.3);
        this.gameObjects.add(groundObject);

        Body red = new Body();
        BodyFixture redFixture = new BodyFixture(Geometry.createCircle(0.2));
        redFixture.setRestitution(0.5);
        redFixture.setDensity(5);
        red.addFixture(redFixture);
        Vector2 redVector = new Vector2(-7, -1);
        red.getTransform().setTranslation(redVector);
        red.setMass(MassType.NORMAL);
        this.world.addBody(red);
        GameObject redObject = new GameObject("/birds/red.png", red, redVector.add(-70, -65), 0.05);
        this.gameObjects.add(redObject);
        this.birdBody = red;

        Body leftWall = new Body();
        BodyFixture leftWallFixture = new BodyFixture(Geometry.createRectangle(2.5, 5.5));
        leftWall.addFixture(leftWallFixture);
        Vector2 leftWallVector = new Vector2(-12, 0.5);
        leftWall.getTransform().setTranslation(leftWallVector);
        leftWall.setMass(MassType.INFINITE);
        this.world.addBody(leftWall);
        GameObject leftWallObject = new GameObject("/plank.png", leftWall, leftWallVector, 0.6);
        this.gameObjects.add(leftWallObject);

        Body rightWall = new Body();
        BodyFixture rightWallFixture = new BodyFixture(Geometry.createRectangle(2.5, 5.5));
        rightWall.addFixture(rightWallFixture);
        Vector2 rightWallVector = new Vector2(12, 0.5);
        rightWall.getTransform().setTranslation(rightWallVector);
        rightWall.setMass(MassType.INFINITE);
        this.world.addBody(rightWall);
        GameObject rightWallObject = new GameObject("/plank.png", rightWall, rightWallVector, 0.6);
        this.gameObjects.add(rightWallObject);

        Body catapult = new Body();
        Vector2 catapultVector = new Vector2(-6.9, -1.5);
        catapult.getTransform().setTranslation(catapultVector);
        catapult.setMass(MassType.INFINITE);
        this.world.addBody(catapult);
        GameObject catapultObject = new GameObject("/map/catapult.png", catapult, catapultVector, 0.4);
        this.gameObjects.add(catapultObject);

        Body catapultBock = new Body();
        BodyFixture catapultBlockFixture = new BodyFixture(Geometry.createRectangle(0.2, 0.1));
        catapultBlockFixture.setRestitution(0.5);
        catapultBock.addFixture(catapultBlockFixture);
        catapultBock.getTransform().setTranslation(catapultVector.add(-0.10, 0.3));
        catapultBock.setMass(MassType.INFINITE);
        this.world.addBody(catapultBock);

        // Genereer blokken
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 5; j++){
                Body block = new Body();
                BodyFixture blockFixture = new BodyFixture(Geometry.createRectangle(0.5, 0.5));
                blockFixture.setRestitution(0.25);
                blockFixture.setDensity(2.5);
                block.addFixture(blockFixture);
                Vector2 blockVector = new Vector2(5 + (0.5 * i), -1.75 + (0.5 * j));
                block.getTransform().setTranslation(blockVector);
                block.setMass(MassType.NORMAL);
                this.world.addBody(block);
                GameObject blockObject = new GameObject("/block.png", block, blockVector.add(-5, 0), 1.2);
                this.gameObjects.add(blockObject);
            }
        }

        shootBird = true;
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
        if (!shootBird){
            mousePicker.update(world, camera.getTransform((int) canvas.getWidth(), (int) canvas.getHeight()), 100);
        }
        world.update(deltaTime);
    }

    private void mousePressed(MouseEvent e){
        this.oldMousePoint = new Point2D.Double(e.getX(), e.getY());
        if (e.getButton() == MouseButton.SECONDARY){
            // Nieuwe bird
            this.oldMousePoint = null;
            newBird();
        }
        else if (e.getButton() == MouseButton.MIDDLE){
            // Reset
            this.oldMousePoint = null;
            reset();
        }
    }

    private void mouseDragged(MouseEvent e){
        this.mousePoint = new Point2D.Double(e.getX(), e.getY());
    }

    private void mouseReleased(MouseEvent e){
        if (shootBird){
            int speedFactor = 4;
            if (this.oldMousePoint != null && this.mousePoint != null){
                this.birdBody.applyForce(new Vector2((oldMousePoint.getX() - mousePoint.getX()) * speedFactor, -((oldMousePoint.getY() - mousePoint.getY()) * speedFactor)));
                shootBird = false;
            }
            mousePicker = new MousePicker(canvas);
        }
    }

    public static void main(String[] args) {
        launch(AngryBirds.class);
    }

}
