import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class DistanceConstraint implements Constraint {

    private double distance;
    private Particle a;
    private Particle b;
    private Color color;

    public DistanceConstraint(Particle a, Particle b) {
        this(a, b, a.getPosition().distance(b.getPosition()));
    }

    public DistanceConstraint(Particle a, Particle b, double distance) {
        this.a = a;
        this.b = b;
        this.distance = distance;
    }

    @Override
    public void satisfy() {

        double currentDistance = a.getPosition().distance(b.getPosition());
        double adjustmentDistance = (currentDistance - distance) / 2;

        Point2D BA = new Point2D.Double(b.getPosition().getX() - a.getPosition().getX(), b.getPosition().getY() - a.getPosition().getY());
        double length = BA.distance(0, 0);
        if (length > 0.0001) // We kunnen alleen corrigeren als we een richting hebben
        {
            BA = new Point2D.Double(BA.getX() / length, BA.getY() / length);
        } else {
            BA = new Point2D.Double(1, 0);
        }
        // Kleine error ruimte van 5 pixels, om rekening te houden met de zwaartekracht (anders is de constraint altijd rood)
        if (currentDistance-5 <= this.distance){
            this.color = Color.green;
        } else if (currentDistance-5 > this.distance){
            this.color = Color.red;
        }

        a.setPosition(new Point2D.Double(a.getPosition().getX() + BA.getX() * adjustmentDistance,
                a.getPosition().getY() + BA.getY() * adjustmentDistance));
        b.setPosition(new Point2D.Double(b.getPosition().getX() - BA.getX() * adjustmentDistance,
                b.getPosition().getY() - BA.getY() * adjustmentDistance));
    }

    @Override
    public void draw(FXGraphics2D g2d) {
        g2d.setColor(this.color);
        g2d.draw(new Line2D.Double(a.getPosition(), b.getPosition()));
    }
}
