import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

public class BlockDrag extends Application {
    ResizableCanvas canvas;
    ArrayList<Renderable> renderables;
    Point2D mousePoint;
    int fixedX = 319;
    int fixedY = 240;
    Graphics2D g;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Lijst met rechthoeken
        renderables = new ArrayList<>();
        renderables.add(new Renderable(Color.lightGray, new Point2D.Double(0.0, 0.0), new Rectangle2D.Double(0, 0, 50, 50)));
        renderables.add(new Renderable(Color.yellow, new Point2D.Double(0.0, 0.0), new Rectangle2D.Double(-80, -70, 50, 50)));
        renderables.add(new Renderable(Color.green, new Point2D.Double(0.0, 0.0), new Rectangle2D.Double(120, 140, 50, 50)));
        renderables.add(new Renderable(Color.blue, new Point2D.Double(0.0, 0.0), new Rectangle2D.Double(10, -100, 50, 50)));
        renderables.add(new Renderable(Color.pink, new Point2D.Double(0.0, 0.0), new Rectangle2D.Double(-30, 180, 50, 50)));
        renderables.add(new Renderable(Color.orange, new Point2D.Double(0.0, 0.0), new Rectangle2D.Double(-180, 10, 50, 50)));
        renderables.add(new Renderable(Color.magenta, new Point2D.Double(0.0, 0.0), new Rectangle2D.Double(150, -150, 50, 50)));

        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("Block Dragging");
        primaryStage.show();

        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(e -> mouseReleased(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));

        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }


    public void draw(FXGraphics2D graphics)
    {
        graphics.setTransform(new AffineTransform());
        graphics.setBackground(Color.white);
        graphics.clearRect(-(int) canvas.getWidth()/2, -(int) canvas.getHeight()/2, (int) canvas.getWidth(), (int) canvas.getHeight());
        graphics.translate(canvas.getWidth()/2, canvas.getHeight()/2);
        graphics.scale(1, 1);

        // Om graphics op andere plekken kunnen gebruiken
        this.g = graphics;

        // Update rechthoeken
        for (Renderable renderable : renderables){
            graphics.setColor(renderable.getColor());
            graphics.fill(renderable.getShape());
            graphics.setColor(Color.black);
            graphics.draw(renderable.getShape());
        }
    }

    public static void main(String[] args)
    {
        launch(BlockDrag.class);
    }

    private void mousePressed(MouseEvent e)
    {
        // Linkermuisknop
        if (e.isPrimaryButtonDown()){
            mousePoint = new Point2D.Double(e.getX() - fixedX, e.getY() - fixedY);
            System.out.println("Clicked on:" + mousePoint);

            // Checken of de muis in een rechthoek is
            for (Renderable renderable : renderables){
                if (mousePoint.getX() >= renderable.getShape().getBounds2D().getMinX() && mousePoint.getX() <= renderable.getShape().getBounds2D().getMaxX()
                && mousePoint.getY() >= renderable.getShape().getBounds2D().getMinY() && mousePoint.getY() <= renderable.getShape().getBounds2D().getMaxY()){
                    System.out.println("In block!");
                    // Drag dit block
                    renderable.setDragging(true);
                    // Set posities
                    renderable.setExtraX(mousePoint.getX() - renderable.getShape().getBounds2D().getMinX());
                    renderable.setExtraY(mousePoint.getY() - renderable.getShape().getBounds2D().getMinY());
                } else {
                    renderable.setDragging(false);
                }
            }
        }
    }

    private void mouseReleased(MouseEvent e)
    {
        // Linkermuisknop
        if (e.isPrimaryButtonDown()){
            for (Renderable renderable : renderables){
                renderable.setDragging(false);
            }
        }
    }

    private void mouseDragged(MouseEvent e)
    {
        // Linkermuisknop
        if (e.isPrimaryButtonDown()){
            mousePoint = new Point2D.Double(e.getX() - fixedX, e.getY() - fixedY);
            System.out.println("Dragging: " + mousePoint);

            g.clearRect(-(int) canvas.getWidth()/2, -(int) canvas.getHeight()/2, (int) canvas.getWidth(), (int) canvas.getHeight());
            for (Renderable renderable : renderables){
                if (renderable.getDragging()){
                    renderable.setPosition(this.mousePoint);
                    renderable.generateShape();
                }
                g.setColor(renderable.getColor());
                g.fill(renderable.getShape());
                g.setColor(Color.black);
                g.draw(renderable.getShape());
            }
        }
    }
}
