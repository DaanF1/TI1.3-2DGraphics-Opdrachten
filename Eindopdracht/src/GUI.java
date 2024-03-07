import Components.Moon;
import Components.Planet;
import Components.SolarSystem;
import Components.Sun;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class GUI extends Application{
    private ResizableCanvas canvas;
    private SolarSystem solarSystem;
    private double zoom = 1;
    // Extra: Pan Event
    private Camera camera;
    private Point2D mousePoint;

    @Override
    public void start(Stage stage) throws Exception{
        // Zon aanmaken met zonnenstelsel
        BufferedImage imageSun = ImageIO.read(getClass().getResource("/planets/zon.jfif"));
        this.solarSystem = new SolarSystem("Solar System", new Sun("Components.Sun", imageSun, 0, 0, 0.9));

        // Planeten met hun manen toevoegen aan zonnenstelsel
        this.solarSystem.addPlanet(new Planet("Mercury", ImageIO.read(getClass().getResource("/planets/mercurius.jpg")), 0.05, 150, (float) 1));

        this.solarSystem.addPlanet(new Planet("Venus", ImageIO.read(getClass().getResource("/planets/venus.jpg")), 0.03, 300, (float) 2.5));

        Planet earth = new Planet("Earth", ImageIO.read(getClass().getResource("/planets/aarde.jpg")), 0.015, 600, (float) 3);
        earth.addMoon(new Moon("Luna", ImageIO.read(getClass().getResource("/planets/maanen/aarde_maan.jpeg")), 0.005, 50, (float) 2, false));
        this.solarSystem.addPlanet(earth);

        this.solarSystem.addPlanet(new Planet("Mars", ImageIO.read(getClass().getResource("/planets/mars.jpg")), 0.015, 900, (float) 2));

        Planet jupiter = new Planet("Jupiter", ImageIO.read(getClass().getResource("/planets/jupiter.jpg")), 0.25, 1200, (float) 4);
        jupiter.addMoon(new Moon("Io", ImageIO.read(getClass().getResource("/planets/maanen/jupiter_Io.jpg")), 0.05, 65, (float) 2, true));
        jupiter.addMoon(new Moon("Europa", ImageIO.read(getClass().getResource("/planets/maanen/jupiter_europa.jpg")), 0.05, 120, (float) 3, true));
        this.solarSystem.addPlanet(jupiter);

        Planet saturn = new Planet("Saturn", ImageIO.read(getClass().getResource("/planets/saturnus.jpg")), 0.3, 1600, (float) 4.5);
        saturn.addMoon(new Moon("Titan", ImageIO.read(getClass().getResource("/planets/maanen/saturnus_titan.jpg")), 0.1, 125, (float) 0.5, true));
        this.solarSystem.addPlanet(saturn);

        this.solarSystem.addPlanet(new Planet("Uranus", ImageIO.read(getClass().getResource("/planets/uranus.jfif")), 0.2, 2000, (float) 1));

        this.solarSystem.addPlanet(new Planet("Neptune", ImageIO.read(getClass().getResource("/planets/neptunus.jfif")), 0.07, 2400, (float) 2));


        // Algemene settings
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.setHeight(1000);
        canvas.setWidth(1910);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
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
        // Extra: Pan event
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));
        //canvas.setOnMouseReleased(e -> mouseReleased(e));
        stage.setScene(new Scene(mainPane));
        stage.setTitle(this.solarSystem.getName());
        stage.show();
        draw(g2d);
    }

    private void draw(FXGraphics2D graphics) {
        // Algemene settings
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.black);
        graphics.translate(this.canvas.getWidth()/2, this.canvas.getHeight()/2);
        graphics.clearRect((int) (-this.canvas.getWidth()/2), (int) (-this.canvas.getHeight()/2), (int) this.canvas.getWidth(), (int) this.canvas.getHeight());
        graphics.scale(zoom,-zoom);

        // Teken zon
        graphics.drawImage(solarSystem.getSun().getImage(), solarSystem.getSun().getX()-(solarSystem.getSun().getXOffset()/2), solarSystem.getSun().getY()-(solarSystem.getSun().getYOffset()/2), null);

        // Teken planeten
        for (Planet planet : solarSystem.getPlanets()){
            // Translate posities per planeet
            AffineTransform planetTransform = new AffineTransform();
            planetTransform.rotate(planet.getAngle());
            planetTransform.translate(0, planet.getDistanceToSun());
            planetTransform.rotate(-planet.getAngle());
            // Pak translatie coordinaten
            double x1 = planetTransform.getTranslateX();
            double y1 = planetTransform.getTranslateY();
            // Centreer afbeelding (coordinaat - offset)
            graphics.drawImage((Image) planet.getImage(), (int) ((int) x1-(planet.getXOffset()/2)), (int) ((int) y1-(planet.getYOffset()/2)), null);
            //graphics.drawImage(planet.getImage(), planetTransform, null);

            if (planet.hasMoons()){
                for (Moon moon : planet.getMoons()){
                    // Translate posities per maan
                    AffineTransform moonTransform = new AffineTransform(planetTransform);
                    moonTransform.rotate(moon.getAngle());
                    moonTransform.translate(0, moon.getDistanceToPlanet());
                    moonTransform.rotate(-moon.getAngle());
                    // Pak translatie coordinaten
                    double x2 = moonTransform.getTranslateX();
                    double y2 = moonTransform.getTranslateY();
                    // Centreer afbeelding (coordinaat - offset)
                    graphics.drawImage((Image) moon.getImage(), (int) ((int) x2-(moon.getXOffset()/2)), (int) ((int) y2-(moon.getYOffset()/2)), null);
                    //graphics.drawImage(moon.getImage(), moonTransform, null);
                }
            }
        }
    }

    private void update(double deltaTime) {
        // Laat tijd lopen, en pas translaties aan
        double planetDecreaseAngle = 0.01;
        for (Planet planet : solarSystem.getPlanets()){
            planet.setAngle((float) (planet.getAngle() + planetDecreaseAngle));
            planetDecreaseAngle -= planetDecreaseAngle/solarSystem.getPlanets().size();
            if (planet.hasMoons()){
                double moonDecreaseAngle = 0.1;
                for (Moon moon : planet.getMoons()){
                    moonDecreaseAngle -= moonDecreaseAngle/planet.getMoons().size();
                    if (moon.getRotationClockWise()){
                        moonDecreaseAngle = -moonDecreaseAngle;
                    }
                    moon.setAngle((float) (moon.getAngle() + moonDecreaseAngle));
                }
            }
        }
    }

    private void mousePressed(MouseEvent e){
        // Linker muisknop: Zoom in
        if (e.isPrimaryButtonDown()){
            if (zoom < 2)
                zoom+=0.20;
        }
        // Rechtermuisknop: Zoom uit
        else if (e.isSecondaryButtonDown()){
            if (zoom > 0.40)
                zoom-=0.20;
        }
        if (e.isMiddleButtonDown()){
            this.mousePoint = new Point2D.Double(e.getX(), e.getY());
        }
    }

    private void mouseDragged(MouseEvent e){
        Point2D newMousePoint = new Point2D.Double(e.getX(), e.getY());

        AffineTransform at = new AffineTransform();
        at.translate(newMousePoint.getY() - mousePoint.getY(), newMousePoint.getX() - mousePoint.getX());
        at.scale(zoom, zoom);
    }

//    private void mouseReleased(MouseEvent e){
//        Point2D newMousePoint = new Point2D.Double(e.getX(), e.getY());
//
//        AffineTransform at = new AffineTransform();
//            at.translate(newMousePoint.getX() - mousePoint.getX(), newMousePoint.getY() - mousePoint.getY());
//            at.scale(zoom, zoom);
//    }

    public static void main(String[] args){
        launch(GUI.class);
    }

}