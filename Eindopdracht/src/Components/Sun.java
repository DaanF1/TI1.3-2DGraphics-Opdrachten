package Components;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Sun{
    private String sunName;
    private BufferedImage image;
    private int x;
    private int y;
    private int xOffset = 0;
    private int yOffset = 0;

    public Sun(String sunName, BufferedImage image, int x, int y, double scale){
        this.sunName = sunName;
        this.image = scaleImage(image, scale);
        this.x = x;
        this.y = y;
    }

    // Scale image to its desired size
    private BufferedImage scaleImage(BufferedImage image, double scale){
        BufferedImage before = image;
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale, scale);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        scaled = scaleOp.filter(before, scaled);
        setXOffset((int) (before.getWidth() * scale));
        setYOffset((int) (before.getHeight() * scale));
        return scaled;
    }

    public BufferedImage getImage(){
        return this.image;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setXOffset(int offset){
        this.xOffset = offset;
    }

    public int getXOffset(){
        return  this.xOffset;
    }

    public void setYOffset(int offset){
        this.yOffset = offset;
    }

    public int getYOffset(){
        return this.yOffset;
    }

}
