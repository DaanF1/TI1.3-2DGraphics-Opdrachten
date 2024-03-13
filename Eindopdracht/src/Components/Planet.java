package Components;

import org.jfree.fx.FXGraphics2D;

import java.awt.*;
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
    private AffineTransform planetTransform;

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

    public int getDistanceToSun(){
        return this.distanceToSun;
    }

    public float getAngle(){
        return this.angle;
    }

    public double getYOffset(){
        return this.yOffset;
    }

    public AffineTransform getPlanetTransform(){
        return this.planetTransform;
    }

    public void draw(FXGraphics2D graphics){
        // Translate posities per planeet
        this.planetTransform = new AffineTransform();
        this.planetTransform.rotate(this.angle);
        this.planetTransform.translate(0, this.distanceToSun);
        this.planetTransform.rotate(-this.angle);

        // Pak translatie coordinaten
        double x1 = this.planetTransform.getTranslateX();
        double y1 = this.planetTransform.getTranslateY();

        // Centreer afbeelding (coordinaat - offset)
        graphics.drawImage((Image) this.image, (int) ((int) x1-(this.xOffset/2)), (int) ((int) y1-(this.yOffset/2)), null);
        //graphics.drawImage(planet.getImage(), planetTransform, null);
    }

    public void update(double planetDecreaseAngle){
        // Update planeet angle
        this.angle = (float) (this.angle - planetDecreaseAngle);

        // Als de planeet een Scattered Disk Object is, moet de baan ovaal worden (dit doe ik met een zelfgemaakte formule)
        // Dit werkt alleen niet als je de speed gaat aanpassen!
        if (this.scatteredDiskObject){

            double distanceMultiplier = 0;
            switch (this.planetName) {
                case "Pluto":
                    distanceMultiplier = 1;
                    break;
                case "Eris":
                    distanceMultiplier = 5;
                    break;
            }
            if (this.angle%this.fullRotation < -Math.PI){
                this.distanceToSun = (int) (this.distanceToSun+distanceMultiplier);
            } else if (this.angle%this.fullRotation > -Math.PI){
                this.distanceToSun = (int) (this.distanceToSun-distanceMultiplier);
            } else if (this.angle%this.fullRotation == 2*Math.PI){
                this.fullRotation = (this.fullRotation + (float) (2*Math.PI));
            }
        }
    }

}
