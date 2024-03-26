import Components.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
    // Linkermuisknop: Zoom in (Canvas)
    // Linkermuisknop + Shift: Versnel tijd (Kan problemen veroorzaken)
    // Linkermuisknop + Control: Versloom tijd (Kan problemen veroorzaken)
    // Rechtermuisknop: Zoom uit (Canvas)
    // Scrollknop indrukken: Sleep rond
    // Scrollen: Zoom in / uit (Camera)
    private ResizableCanvas canvas;
    private SolarSystem solarSystem;
    private boolean linesSelected = false;
    private double speed = 1;
    private double zoom = 0.2;
    private int fontSize = 80;
    private Point2D mousePoint;
    private Camera camera;

    @Override
    public void start(Stage stage) throws Exception{
        // Zon aanmaken met zonnenstelsel
        BufferedImage imageSun = ImageIO.read(getClass().getResource("/planets/zon.jfif"));
        this.solarSystem = new SolarSystem("Solar System", new Sun("Sun", imageSun, 0, 0, 0.9));

        // Planeten met hun manen toevoegen aan zonnenstelsel
        this.solarSystem.addPlanet(new Planet("Mercury", ImageIO.read(getClass().getResource("/planets/mercurius.jpg")), 0.05, 150, (float) -1, false));

        this.solarSystem.addPlanet(new Planet("Venus", ImageIO.read(getClass().getResource("/planets/venus.jpg")), 0.03, 250, (float) -2.5, false));

        Planet earth = new Planet("Earth", ImageIO.read(getClass().getResource("/planets/aarde.jpg")), 0.015, 400, (float) 3, false);
        earth.addMoon(new Moon("Luna", ImageIO.read(getClass().getResource("/planets/manen/aarde_maan.jpeg")), earth, 0.005, 50, (float) -2, false));
        this.solarSystem.addPlanet(earth);

        this.solarSystem.addPlanet(new Planet("Mars", ImageIO.read(getClass().getResource("/planets/mars.jpg")), 0.015, 550, (float) -2, false));

        Planet jupiter = new Planet("Jupiter", ImageIO.read(getClass().getResource("/planets/jupiter.jpg")), 0.25, 1050, (float) -4, false);
        jupiter.addMoon(new Moon("Io", ImageIO.read(getClass().getResource("/planets/manen/jupiter_Io.jpg")), jupiter, 0.04, 65, (float) -2, false));
        jupiter.addMoon(new Moon("Europa", ImageIO.read(getClass().getResource("/planets/manen/jupiter_europa.jpg")), jupiter, 0.03, 120, (float) -3, false));
        jupiter.addMoon(new Moon("Ganymedes", ImageIO.read(getClass().getResource("/planets/manen/jupiter_ganymedes.jpg")), jupiter, 0.05, 185, (float) -3, false));
        jupiter.addMoon(new Moon("Callisto", ImageIO.read(getClass().getResource("/planets/manen/jupiter_callisto.jpg")), jupiter, 0.03, 250, (float) -3, false));
        this.solarSystem.addPlanet(jupiter);

        Planet saturn = new Planet("Saturn", ImageIO.read(getClass().getResource("/planets/saturnus.jpg")), 0.3, 1600, (float) -4.5, false);
        saturn.addMoon(new Moon("Rhea", ImageIO.read(getClass().getResource("/planets/manen/saturnus_rhea.jfif")), saturn, 0.05, 115, (float) -2.5, false));
        saturn.addMoon(new Moon("Titan", ImageIO.read(getClass().getResource("/planets/manen/saturnus_titan.jpg")), saturn, 0.1, 200, (float) -0.5, false));
        this.solarSystem.addPlanet(saturn);

        Planet uranus = new Planet("Uranus", ImageIO.read(getClass().getResource("/planets/uranus.jfif")), 0.2, 2000, (float) -1, false);
        uranus.addMoon(new Moon("Titania", ImageIO.read(getClass().getResource("/planets/manen/uranus_titania.jpg")), uranus, 0.05, 75, (float) -2, false));
        uranus.addMoon(new Moon("Oberon", ImageIO.read(getClass().getResource("/planets/manen/uranus_oberon.jpg")), uranus, 0.05, 135, (float) -2, false));
        this.solarSystem.addPlanet(uranus);

        Planet neptune = new Planet("Neptune", ImageIO.read(getClass().getResource("/planets/neptunus.jfif")),0.07, 2350, (float) -2, false);
        neptune.addMoon(new Moon("Triton", ImageIO.read(getClass().getResource("/planets/manen/neptunus_triton.jpg")), neptune, 0.07, 50, (float) -2, true));
        this.solarSystem.addPlanet(neptune);

        this.solarSystem.addPlanet(new Planet("Pluto", ImageIO.read(getClass().getResource("/planets/pluto.jpg")), 0.02, 3000, (float) -0, true));

        this.solarSystem.addPlanet(new Planet("Eris", ImageIO.read(getClass().getResource("/planets/eris.jpg")), 0.03, 6500, (float) -0, true));

        // Algemene settings
        BorderPane mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: black");
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.setHeight(1000);
        canvas.setWidth(1910);

        camera = new Camera(canvas);

        // Toon informatie checkbox
        javafx.scene.control.CheckBox showInfo = new CheckBox("Show information");
        showInfo.setStyle("-fx-background-color: white");
        showInfo.setOnAction(e -> {
            linesSelected = showInfo.isSelected();
        });
        mainPane.setTop(showInfo);
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
        // Events
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        // Extra camera zoom
        canvas.setOnScroll(e -> camera.handleScroll(e));
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

        // Teken de lijnen van de planeten en manen + namen (achtergrond)
        graphics.scale(1,-1);
        if (linesSelected){
            for (Planet planet : solarSystem.getPlanets()){
                graphics.draw(new Ellipse2D.Double(-planet.getDistanceToSun(), -planet.getDistanceToSun(), planet.getDistanceToSun()*2, planet.getDistanceToSun()*2));
                graphics.setFont(new Font("Arial", Font.BOLD, this.fontSize));
                graphics.drawString(planet.getName(), (int) (planet.getDistanceToSun()+planet.getYOffset()), 0);
                graphics.drawString("Angle: ", (int) (planet.getDistanceToSun()+planet.getYOffset()), 75);
                graphics.drawString(String.valueOf(planet.getAngle()), (int) (planet.getDistanceToSun()+planet.getYOffset()), 150);
            }
        }
        graphics.scale(1,-1);

        // Teken planeten
        for (Planet planet : solarSystem.getPlanets()){
            planet.draw(graphics);
            if (planet.hasMoons()){
                for (Moon moon : planet.getMoons()){
                    moon.draw(graphics);
                }
            }
        }

        // Teken algemene tekst (voorgrond)
        graphics.scale(1,-1);
        graphics.setFont(new Font("Arial", Font.BOLD, this.fontSize*3));
        graphics.drawString(solarSystem.getName(), -750, -2300);
        if (linesSelected){
            graphics.setFont(new Font("Arial", Font.BOLD, this.fontSize*2));
            graphics.drawString("Kuiper Belt", 3000, -500);
        }
        graphics.scale(1,-1);
    }

    private void update(double deltaTime) {
        // Angle om planeten mee te roteren
        double planetDecreaseAngle = speed*(deltaTime/4);

        // Update posities van alle planeten & manen binnen het zonnenstelsel
        for (Planet planet : solarSystem.getPlanets()){
            // Update planeet angle
            planet.update(planetDecreaseAngle);
            planetDecreaseAngle -= planetDecreaseAngle/solarSystem.getPlanets().size();

            if (planet.hasMoons()){
                double moonDecreaseAngle = planetDecreaseAngle * 5;
                for (Moon moon : planet.getMoons()){
                    // Update maan angle
                    moon.update(moonDecreaseAngle);
                    moonDecreaseAngle -= 0.01/planet.getMoons().size();
                }
            }
        }
    }

    private void mousePressed(MouseEvent e){
        // Middel muisknop: Pan
        if (e.getButton() == MouseButton.MIDDLE){
            this.mousePoint = new Point2D.Double(e.getX(), e.getY());
        }
        // Linker muisknop: Zoom in
        else if (e.getButton() == MouseButton.PRIMARY){
            if (!e.isShiftDown() && !e.isControlDown()){
                if (zoom < 3.20){
                    zoom+=0.20;
                    if (zoom < 1.20){
                        fontSize-=15;
                    }
                }
            }
            // Shift: Versnel tijd
            if (e.isShiftDown()){
                speed = speed * 1.1;
            }
            // Control: Versloom tijd
            if (e.isControlDown()){
                speed = speed * 0.9;
            }
        }
        // Rechtermuisknop: Zoom uit
        else if (e.getButton() == MouseButton.SECONDARY){
            if (zoom > 0.40){
                zoom -= 0.20;
                if (zoom < 1){
                    fontSize+=15;
                }
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