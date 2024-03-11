package Components;

import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Moon{
    private String moonName;
    private BufferedImage image;
    private Planet motherPlanet;
    private int distanceToPlanet;
    private float angle;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean rotateClockWise;

    public Moon(String moonName, BufferedImage image, Planet motherPlanet, double scale, int distanceToPlanet, float startingAngle, boolean rotateClockWise){
        this.moonName = moonName;
        this.image = scaleImage(image, scale);
        this.motherPlanet = motherPlanet;
        this.distanceToPlanet = distanceToPlanet;
        this.angle = startingAngle;
        this.rotateClockWise = rotateClockWise;
    }

    // Scale image to its desired size
    public BufferedImage scaleImage(BufferedImage image, double scale){
        BufferedImage before = image;
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        scaled = scaleOp.filter(before, scaled);
        this.xOffset = w*scale;
        this.yOffset = h*scale;
        return scaled;
    }

    public void draw(FXGraphics2D graphics){
        // Translate posities per maan
        AffineTransform moonTransform = new AffineTransform(motherPlanet.getPlanetTransform());
        moonTransform.rotate(this.angle);
        moonTransform.translate(0, this.distanceToPlanet);
        moonTransform.rotate(-this.angle);

        // Pak translatie coordinaten
        double x2 = moonTransform.getTranslateX();
        double y2 = moonTransform.getTranslateY();

        // Centreer afbeelding (coordinaat - offset)
        graphics.drawImage((Image) this.image, (int) ((int) x2-(this.xOffset/2)), (int) ((int) y2-(this.yOffset/2)), null);
        //graphics.drawImage(moon.getImage(), moonTransform, null);
    }

    public void update(double moonDecreaseAngle){
        // Sommige manen draaien de andere kant op!
        if (this.rotateClockWise){
            moonDecreaseAngle = -moonDecreaseAngle;
        }
        // Update maan angle
        this.angle = ((float) (this.angle + moonDecreaseAngle));
    }

}
