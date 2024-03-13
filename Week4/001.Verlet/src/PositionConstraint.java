import org.jfree.fx.FXGraphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PositionConstraint implements Constraint {

    private Particle particle;
    private Point2D fixedPosition;

    public PositionConstraint(Particle particle) {
        this.particle = particle;
        if (particle != null) {
            fixedPosition = particle.getPosition();
        }
    }

    @Override
    public void satisfy() {
        if (particle != null) {
            particle.setPosition(fixedPosition);
        }
    }

    @Override
    public void draw(FXGraphics2D g2d) {

    }

    public void setParticle(Particle particle) {
        this.particle = particle;
        if (particle != null) {
            fixedPosition = particle.getPosition();
        }
    }

    public void setFixedPosition(Point2D fixedPosition) {
        this.fixedPosition = fixedPosition;
    }

    @Override
    public ArrayList<String> getConstraintInfo(){
        ArrayList<String> info = new ArrayList<>();
        info.add("PositionConstraint");
        if (this.particle != null){
            info.add(this.particle.getPosition().getX() + "," + this.particle.getPosition().getY());
            return info;
        }
        return null;
    }

}
