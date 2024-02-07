import com.sun.javafx.geom.Point2D;

import java.awt.*;

public class Renderable{
    private Color color;
    private Point2D position;
    private Shape shape;

    public Renderable(Color color, Point2D position, Shape shape){
        this.color = color;
        this.position = position;
        this.shape = shape;
    }
}
