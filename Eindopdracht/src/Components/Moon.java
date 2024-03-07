package Components;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Moon{
    private String moonName;
    private BufferedImage image;
    private int distanceToPlanet;
    private float angle;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean rotateCounterClockWise;

    public Moon(String moonName, BufferedImage image, double scale, int distanceToPlanet, float startingAngle, boolean rotateCounterClockWise){
        this.moonName = moonName;
        this.image = scaleImage(image, scale);
        this.distanceToPlanet = distanceToPlanet;
        this.angle = startingAngle;
        this.rotateCounterClockWise = rotateCounterClockWise;
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

    public BufferedImage getImage(){
        return this.image;
    }

    public int getDistanceToPlanet(){
        return this.distanceToPlanet;
    }

    public void setAngle(float angle){
        this.angle = angle;
    }

    public float getAngle(){
        return this.angle;
    }

    public double getXOffset(){
        return this.xOffset;
    }

    public double getYOffset(){
        return this.yOffset;
    }

    public boolean isRotatingCounterClockWise(){
        return !this.rotateCounterClockWise;
    }

}
