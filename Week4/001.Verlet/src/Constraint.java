import org.jfree.fx.FXGraphics2D;

import java.util.ArrayList;

public interface Constraint {
    void satisfy();
    void draw(FXGraphics2D g2d);
    ArrayList<String> getConstraintInfo();
}