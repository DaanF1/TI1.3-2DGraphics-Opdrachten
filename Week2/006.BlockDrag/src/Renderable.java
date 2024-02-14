import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Renderable{
    private Color color;
    private Point2D position;
    private Shape shape;
    private boolean dragging;
    private double extraX;
    private double extraY;
    public Renderable(Color color, java.awt.geom.Point2D position, Shape shape){
        this.color = color;
        this.position = position;
        this.shape = shape;
        this.dragging = false;
        this.extraX = 0;
        this.extraY = 0;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public void setPosition(Point2D position){
        this.position.setLocation(position.getX(), position.getY());
    }

    public Point2D getPosition(){
        return position;
    }

    public void setShape(Shape shape){
        this.shape = shape;
    }

    public Shape getShape(){
        return shape;
    }

    public void setDragging(boolean dragging){
        this.dragging = dragging;
    }

    public boolean getDragging(){
        return this.dragging;
    }

    public void setExtraX(double extraX){
        this.extraX = extraX;
    }

    public void setExtraY(double extraY){
        this.extraY = extraY;
    }

    public void generateShape(){
        this.shape = new Rectangle2D.Double(this.position.getX()-extraX,
                this.position.getY()-extraY,
                50,
                50);
    }
}
