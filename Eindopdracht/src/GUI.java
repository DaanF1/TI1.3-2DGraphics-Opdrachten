import Components.Moon;
import Components.Planet;
import Components.SolarSystem;
import Components.Sun;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class GUI extends Application{
    // Controls:
    // Linkermuisknop: Zoom in
    // Rechtermuisknop: Zoom uit
    // Scrollknop: Sleep rond
    private ResizableCanvas canvas;
    private SolarSystem solarSystem;
    private double zoom = 1;
    private Point2D mousePoint;
    private boolean linesSelected = false;
    @Override
    public void start(Stage stage) throws Exception{
        // Zon aanmaken met zonnenstelsel
        BufferedImage imageSun = ImageIO.read(getClass().getResource("/planets/zon.jfif"));
        this.solarSystem = new SolarSystem("Solar System", new Sun("Components.Sun", imageSun, 0, 0, 0.9));

        // Planeten met hun manen toevoegen aan zonnenstelsel
        this.solarSystem.addPlanet(new Planet("Mercury", ImageIO.read(getClass().getResource("/planets/mercurius.jpg")), 0.05, 150, (float) 1));

        this.solarSystem.addPlanet(new Planet("Venus", ImageIO.read(getClass().getResource("/planets/venus.jpg")), 0.03, 250, (float) 2.5));

        Planet earth = new Planet("Earth", ImageIO.read(getClass().getResource("/planets/aarde.jpg")), 0.015, 400, (float) 3);
        earth.addMoon(new Moon("Luna", ImageIO.read(getClass().getResource("/planets/manen/aarde_maan.jpeg")), 0.005, 50, (float) 2, true));
        this.solarSystem.addPlanet(earth);

        this.solarSystem.addPlanet(new Planet("Mars", ImageIO.read(getClass().getResource("/planets/mars.jpg")), 0.015, 550, (float) 2));

        Planet jupiter = new Planet("Jupiter", ImageIO.read(getClass().getResource("/planets/jupiter.jpg")), 0.25, 1050, (float) 4);
        jupiter.addMoon(new Moon("Io", ImageIO.read(getClass().getResource("/planets/manen/jupiter_Io.jpg")), 0.04, 65, (float) 2, true));
        jupiter.addMoon(new Moon("Europa", ImageIO.read(getClass().getResource("/planets/manen/jupiter_europa.jpg")), 0.03, 120, (float) 3, true));
        jupiter.addMoon(new Moon("Ganymedes", ImageIO.read(getClass().getResource("/planets/manen/jupiter_ganymedes.jpg")), 0.05, 185, (float) 3, true));
        jupiter.addMoon(new Moon("Callisto", ImageIO.read(getClass().getResource("/planets/manen/jupiter_callisto.jpg")), 0.03, 250, (float) 3, true));
        this.solarSystem.addPlanet(jupiter);

        Planet saturn = new Planet("Saturn", ImageIO.read(getClass().getResource("/planets/saturnus.jpg")), 0.3, 1600, (float) 4.5);
        saturn.addMoon(new Moon("Rhea", ImageIO.read(getClass().getResource("/planets/manen/saturnus_rhea.jfif")), 0.05, 115, (float) 2.5, true));
        saturn.addMoon(new Moon("Titan", ImageIO.read(getClass().getResource("/planets/manen/saturnus_titan.jpg")), 0.1, 200, (float) 0.5, true));
        this.solarSystem.addPlanet(saturn);

        Planet uranus = new Planet("Uranus", ImageIO.read(getClass().getResource("/planets/uranus.jfif")), 0.2, 2000, (float) 1);
        uranus.addMoon(new Moon("Titania", ImageIO.read(getClass().getResource("/planets/manen/uranus_titania.jpg")), 0.05, 75, (float) 2, true));
        uranus.addMoon(new Moon("Oberon", ImageIO.read(getClass().getResource("/planets/manen/uranus_oberon.jpg")), 0.05, 135, (float) 2, true));
        this.solarSystem.addPlanet(uranus);

        Planet neptune = new Planet("Neptune", ImageIO.read(getClass().getResource("/planets/neptunus.jfif")),0.07, 2350, (float) 2);
        // Triton draait ten opzichte van andere manen, tegen de klok in!
        neptune.addMoon(new Moon("Triton", ImageIO.read(getClass().getResource("/planets/manen/neptunus_triton.jpg")), 0.07, 50, (float) 2, false));
        this.solarSystem.addPlanet(neptune);

        this.solarSystem.addPlanet(new Planet("Pluto", ImageIO.read(getClass().getResource("/planets/pluto.jpg")), 0.02, 3200, (float) 1));

        this.solarSystem.addPlanet(new Planet("Eris", ImageIO.read(getClass().getResource("/planets/eris.jpg")), 0.03, 4200, (float) 2));

        // Algemene settings
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: black");
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.setHeight(1000);
        canvas.setWidth(1910);

        // Add planet lines button
        javafx.scene.control.CheckBox showLines = new CheckBox("Show planet lines");
        showLines.setStyle("-fx-background-color: white");
        showLines.setOnAction(e -> {
            linesSelected = showLines.isSelected();
        });
        mainPane.setTop(showLines);
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
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        stage.setScene(new Scene(mainPane));
        stage.setTitle(this.solarSystem.getName());
        stage.show();
        draw(g2d);
    }

    private void draw(FXGraphics2D graphics) {
        // Algemene settings
        graphics.setTransform(new AffineTransform());
        graphics.translate(this.canvas.getWidth()/2, this.canvas.getHeight()/2);
        graphics.clearRect((int) (-this.canvas.getWidth()/2), (int) (-this.canvas.getHeight()/2), (int) this.canvas.getWidth(), (int) this.canvas.getHeight());
        graphics.setColor(Color.white);
        graphics.scale(zoom,-zoom);

        // Teken zon
        graphics.drawImage(solarSystem.getSun().getImage(), solarSystem.getSun().getX()-(solarSystem.getSun().getXOffset()/2), solarSystem.getSun().getY()-(solarSystem.getSun().getYOffset()/2), null);

        // Teken de buitenrand van het zonnenstelsel
        graphics.scale(1,-1);
        graphics.setFont(new Font("Arial", Font.BOLD, 130));
        graphics.drawString(solarSystem.getName(), -450, -2300);
        if (linesSelected){
            graphics.setFont(new Font("Arial", Font.BOLD, 60));
            graphics.drawString("< Beyond here: Scattered Disk Objects (SDO)", -2500, -1000);
            for (Planet planet : solarSystem.getPlanets()){
                graphics.draw(new Ellipse2D.Double(-planet.getDistanceToSun(), -planet.getDistanceToSun(), planet.getDistanceToSun()*2, planet.getDistanceToSun()*2));
            }
        }
        graphics.scale(1,-1);

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
        // Terwijl de tijd loopt, draai de planeten en manen
        double planetDecreaseAngle = 0.01;
        for (Planet planet : solarSystem.getPlanets()){
            planet.setAngle((float) (planet.getAngle() + planetDecreaseAngle));
            planetDecreaseAngle -= planetDecreaseAngle/solarSystem.getPlanets().size();
            if (planet.hasMoons()){
                double moonDecreaseAngle = 0.05;
                for (Moon moon : planet.getMoons()){
                    moonDecreaseAngle -= 0.01/planet.getMoons().size();
                    if (!moon.isRotatingClockWise()){
                        moonDecreaseAngle = -moonDecreaseAngle;
                        moon.setAngle((float) (moon.getAngle() + moonDecreaseAngle));
                        moonDecreaseAngle = -moonDecreaseAngle;
                    } else {
                        moon.setAngle((float) (moon.getAngle() + moonDecreaseAngle));
                    }
                }
            }
        }
    }

    private void mousePressed(MouseEvent e){
        this.mousePoint = new Point2D.Double(e.getX(), e.getY());
        // Linker muisknop: Zoom in
        if (e.getButton() == MouseButton.PRIMARY){
            if (zoom < 2){
                zoom+=0.20;
            }
        }
        // Rechtermuisknop: Zoom uit
        else if (e.getButton() == MouseButton.SECONDARY){
            if (zoom > 0.40){
                zoom -= 0.20;
            }
        }
    }

    private void mouseDragged(MouseEvent e){
        // Pan Event
        if (e.getButton() == MouseButton.MIDDLE){
            Point2D newMousePoint = new Point2D.Double(e.getX(), e.getY());
            double translateX = canvas.getTranslateX();
            double translateY = canvas.getTranslateY();
            canvas.setTranslateX(translateX + newMousePoint.getX() - mousePoint.getX());
            canvas.setTranslateY(translateY + newMousePoint.getY() - mousePoint.getY());
        }
    }

    private void mouseReleased(MouseEvent e){
        // Stop Pan Event
        if (e.getButton() == MouseButton.MIDDLE){
            this.mousePoint = null;
        }
    }

    public static void main(String[] args){
        // Start Applicatie
        launch(GUI.class);
    }

}