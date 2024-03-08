package Components;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Planet{
    private ArrayList<Moon> moons;
    private String planetName;
    private BufferedImage image;
    private int distanceToSun;
    private float angle;
    private boolean scatteredDiskObject;
    private double xOffset = 0;
    private double yOffset = 0;
    private float fullRotation = (float) (2*Math.PI);

    public Planet(String planetName, BufferedImage image, double scale, int distanceToSun, float startingAngle, boolean scatteredDiskObject){
        this.moons = new ArrayList<Moon>();
        this.planetName = planetName;
        this.image = scaleImage(image, scale);
        this.distanceToSun = distanceToSun;
        this.angle = startingAngle;
        this.scatteredDiskObject = scatteredDiskObject;
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

    public String getName(){
        return this.planetName;
    }

    public boolean hasMoons(){
        return !this.moons.isEmpty();
    }

    public void addMoon(Moon moon){
        this.moons.add(moon);
    }

    public ArrayList<Moon> getMoons(){
        return this.moons;
    }

    public void setDistanceToSun(int distanceToSun){
        this.distanceToSun = distanceToSun;
    }

    public int getDistanceToSun(){
        return this.distanceToSun;
    }

    public void setAngle(float angle){
        this.angle = angle;
    }

    public float getAngle(){
        return this.angle;
    }

    public boolean isAScatteredDiskObject(){
        return this.scatteredDiskObject;
    }

    public double getXOffset(){
        return this.xOffset;
    }

    public double getYOffset(){
        return this.yOffset;
    }

    public void setFullRotation(float newFullRotation){
        this.fullRotation = newFullRotation;
    }

    public float getFullRotation(){
        return this.fullRotation;
    }

}
