import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;
import org.omg.PortableServer.POA;

import javax.imageio.ImageIO;

public class VerletEngine extends Application {
    private ResizableCanvas canvas;
    private ArrayList<Particle> particles = new ArrayList<>();
    private ArrayList<Constraint> constraints = new ArrayList<>();
    private PositionConstraint mouseConstraint = new PositionConstraint(null);
    private javafx.scene.control.CheckBox saveMap = new CheckBox("Save the map");
    private String dir = System.getProperty("user.dir");
    private File theFile = new File(dir + "/Week4/001.Verlet/resources/savedPoints.txt");

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();
        // Add save button
        saveMap.setStyle("-fx-background-color: white");
        saveMap.setOnAction(e -> saveMap(e));
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        canvas.setWidth(1900);
        canvas.setHeight(1000);
        mainPane.setTop(saveMap);
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

        // Mouse Events
        canvas.setOnMouseClicked(e -> mouseClicked(e));
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        stage.setScene(new Scene(mainPane));
        stage.setTitle("Verlet Engine");
        stage.show();
        draw(g2d);

        loadMap();
        setMouseConstraint();
    }

    public void loadMap(){
        readMap();
    }

    public void setMouseConstraint(){
        if (!theFile.exists()){
            reset();
        } else {
            constraints.add(mouseConstraint);
        }
    }

    public void reset() {
        for (int i = 0; i < 20; i++) {
            particles.add(new Particle(new Point2D.Double(100 + 50 * i, 100)));
        }

        for (int i = 0; i < 10; i++) {
            constraints.add(new DistanceConstraint(particles.get(i), particles.get(i + 1)));
        }

        constraints.add(new PositionConstraint(particles.get(10)));
        constraints.add(mouseConstraint);
    }

    private void draw(FXGraphics2D graphics) {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        for (Constraint c : constraints) {
            c.draw(graphics);
        }

        for (Particle p : particles) {
            p.draw(graphics);
        }
    }

    private void update(double deltaTime) {
        for (Particle p : particles) {
            p.update((int) canvas.getWidth(), (int) canvas.getHeight());
        }

        for (Constraint c : constraints) {
            c.satisfy();
        }
    }

    private void mouseClicked(MouseEvent e) {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle nearest = getNearest(mousePosition);
        Particle newParticle = new Particle(mousePosition);
        particles.add(newParticle);

        if (e.getButton() == MouseButton.PRIMARY){
            // 1 Constraint
            if (!e.isControlDown() && !e.isShiftDown()){
                // Free spece (for dragging objects)
                particles.remove(newParticle);
            } else if (e.isControlDown() && !e.isShiftDown()){
                constraints.add(new DistanceConstraint(newParticle, nearest));
            } else if (e.isShiftDown() && !e.isControlDown()){
                // Static constraint
                constraints.add(new PositionConstraint(newParticle));
            } else if (e.isShiftDown() && e.isControlDown()){
                // Rope constraint
                constraints.add(new RopeConstraint(newParticle, nearest));
            }
        } else if (e.getButton() == MouseButton.SECONDARY) {
            // 2 Constraints
            ArrayList<Particle> sorted = new ArrayList<>();
            sorted.addAll(particles);

            //sorteer alle elementen op afstand tot de muiscursor. De toegevoegde particle staat op 0, de nearest op 1, en de derde op 2
            Collections.sort(sorted, new Comparator<Particle>() {
                @Override
                public int compare(Particle o1, Particle o2) {
                    return (int) (o1.getPosition().distance(mousePosition) - o2.getPosition().distance(mousePosition));
                }
            });

            if (!e.isControlDown() && !e.isShiftDown()){
                // Eigen afstanden
                constraints.add(new DistanceConstraint(newParticle, nearest));
                constraints.add(new DistanceConstraint(newParticle, sorted.get(2)));
            }else if (e.isControlDown() && !e.isShiftDown()){
                // Vaste afstanden (100)
                constraints.add(new DistanceConstraint(newParticle, nearest, 100));
                constraints.add(new DistanceConstraint(newParticle, sorted.get(2), 100));
            } else if (e.isShiftDown() && !e.isControlDown()){
                // 2 Dichtsbijzijnste punten
                particles.remove(newParticle);
                constraints.add(new DistanceConstraint(nearest, sorted.get(2)));
            } else if (e.isControlDown() && e.isShiftDown()){
                particles.remove(newParticle);
                int particleCount = particles.size();
                // Make cloth
                int clothLength = 4;
                int pointDistance = 80;
                for (int i = 0; i < clothLength; i++){
                    for (int j = 0; j < clothLength; j++){
                        Particle clothParticle = new Particle(new Point2D.Double(mousePosition.getX()+(i*pointDistance), mousePosition.getY()+(j*pointDistance)));
                        particles.add(clothParticle);

                        if (j == 0){
                            constraints.add(new PositionConstraint(clothParticle));
                        }
                        else {
                            Particle nextParticle = particles.get(particleCount+(i*clothLength)+(j-1));
                            constraints.add(new RopeConstraint(clothParticle, nextParticle));
                        }
                    }
                }
                for (int i = 0; i < clothLength*clothLength-clothLength; i++){
                    constraints.add(new RopeConstraint(particles.get(particleCount+i), particles.get(particleCount+i+clothLength)));
                }
            }
        } else if (e.getButton() == MouseButton.MIDDLE){
            // Reset
            particles.clear();
            constraints.clear();
            reset();
        }
    }

    private Particle getNearest(Point2D point) {
        Particle nearest = particles.get(0);
        for (Particle p : particles) {
            if (p.getPosition().distance(point) < nearest.getPosition().distance(point)) {
                nearest = p;
            }
        }
        return nearest;
    }

    private void mousePressed(MouseEvent e) {
        Point2D mousePosition = new Point2D.Double(e.getX(), e.getY());
        Particle nearest = getNearest(mousePosition);
        if (nearest.getPosition().distance(mousePosition) < 10) {
            mouseConstraint.setParticle(nearest);
        }
    }

    private void mouseReleased(MouseEvent e) {
        mouseConstraint.setParticle(null);
    }

    private void mouseDragged(MouseEvent e) {
        mouseConstraint.setFixedPosition(new Point2D.Double(e.getX(), e.getY()));
    }

    private void saveMap(ActionEvent e){
        // Save scene
        boolean saveScene = saveMap.isSelected();
        if (saveScene){
            try{
                String path = theFile.getAbsolutePath();
                // (Re)Make the file
                if (!theFile.exists()){
                    theFile.createNewFile();
                    System.out.println("File created at: " + path);
                } else {
                    theFile.delete();
                    theFile.createNewFile();
                }
                if (theFile.canRead()){
                    // Write data
                    PrintWriter pw = new PrintWriter(theFile);
                    for (Constraint constraint : constraints){
                        pw.println(constraint.getConstraintInfo());
                    }

                    pw.close();
                }

                System.out.println("File saved at: " + path);
                saveMap.setSelected(false);
            }
            catch (IOException exc) {
                System.out.println("IOException!");
                exc.printStackTrace();
            }
            catch (Exception ex){
                System.out.println("Big Exception!");
                ex.printStackTrace();
            }
        }
    }

    private void readMap(){
        // Read scene
        try{
            if (theFile.exists() && theFile.canRead()){
                // Read data
                Scanner s = new Scanner(theFile);
                Particle lastBParticle = null;
                while (s.hasNext()){
                    String line = s.nextLine();
                    String[] items = line.split(",");
                    ArrayList<String> theItems = new ArrayList<>();
                    // Get item values in string form
                    for (int i = 0; i < items.length; i++){
                        String item = "";
                        if (!items[i].equalsIgnoreCase("null")){
                            // Item
                            item = items[i];
                            if (item.contains("[")){
                                item = item.substring(1);
                            } else if (item.contains("]")){
                                item = item.substring(0, item.length()-1);
                            }
                            theItems.add(item);
                        } else {
                            break;
                        }
                    }
                    // Convert item values to other forms
                    for (int i = 0; i < theItems.size(); i++){
                        // Make new constraint
                        if (theItems.get(i).equalsIgnoreCase("DistanceConstraint")){
                            // Make new particle
                            Particle aParticle = new Particle(new Point2D.Double(Double.parseDouble(theItems.get(2)), Double.parseDouble(theItems.get(3))));
                            if (lastBParticle != null){
                                if (lastBParticle.getPosition().getX() == aParticle.getPosition().getX() && lastBParticle.getPosition().getY() == aParticle.getPosition().getY()){
                                    Particle bParticle = new Particle(new Point2D.Double(Double.parseDouble(theItems.get(4)), Double.parseDouble(theItems.get(5))));
                                    particles.add(bParticle);

                                    DistanceConstraint dConstraint = new DistanceConstraint(lastBParticle, bParticle, Double.parseDouble(theItems.get(1)));
                                    constraints.add(dConstraint);
                                    lastBParticle = bParticle;
                                } else {
                                    particles.add(aParticle);

                                    Particle bParticle = new Particle(new Point2D.Double(Double.parseDouble(theItems.get(4)), Double.parseDouble(theItems.get(5))));
                                    particles.add(bParticle);

                                    DistanceConstraint dConstraint = new DistanceConstraint(aParticle, bParticle, Double.parseDouble(theItems.get(1)));
                                    constraints.add(dConstraint);
                                    lastBParticle = bParticle;
                                }
                            } else {
                                Particle bParticle = new Particle(new Point2D.Double(Double.parseDouble(theItems.get(4)), Double.parseDouble(theItems.get(5))));
                                lastBParticle = bParticle;
                            }

                        } else if (theItems.get(i).equalsIgnoreCase("RopeConstraint")){
                            // Make new particle
                            Particle aParticle = new Particle(new Point2D.Double(Double.parseDouble(theItems.get(2)), Double.parseDouble(theItems.get(3))));
                            if (lastBParticle != null){
                                if (lastBParticle.getPosition().getX() == aParticle.getPosition().getX() && lastBParticle.getPosition().getY() == aParticle.getPosition().getY()){
                                    Particle bParticle = new Particle(new Point2D.Double(Double.parseDouble(theItems.get(4)), Double.parseDouble(theItems.get(5))));
                                    particles.add(bParticle);

                                    RopeConstraint rConstraint = new RopeConstraint(lastBParticle, bParticle);
                                    constraints.add(rConstraint);
                                    lastBParticle = bParticle;
                                } else {
                                    particles.add(aParticle);

                                    Particle bParticle = new Particle(new Point2D.Double(Double.parseDouble(theItems.get(4)), Double.parseDouble(theItems.get(5))));
                                    particles.add(bParticle);

                                    RopeConstraint rConstraint = new RopeConstraint(aParticle, bParticle);
                                    constraints.add(rConstraint);
                                    lastBParticle = bParticle;
                                }
                            }

                        } else if (theItems.get(i).equalsIgnoreCase("PositionConstraint")){
                            // Make new particle
                            Particle aParticle = new Particle(new Point2D.Double(Double.parseDouble(theItems.get(1)), Double.parseDouble(theItems.get(2).substring(0, theItems.get(2).length()-1))));
                            particles.add(aParticle);
                            PositionConstraint pConstraint = new PositionConstraint(aParticle);
                            constraints.add(pConstraint);

                            DistanceConstraint dConstraint = new DistanceConstraint(aParticle, lastBParticle);
                            constraints.add(dConstraint);

                            //lastBParticle = aParticle;
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException!");
            ex.printStackTrace();
        } catch (Exception e){
            System.out.println("Big Exception!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(VerletEngine.class);
    }

}
