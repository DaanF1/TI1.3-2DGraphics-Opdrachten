public class Point{
    private double x;
    private double y;
    private boolean hitLeft;
    private boolean hitTop;
    public Point(double x, double y, boolean hitLeft, boolean hitTop){
        this.x = x;
        this.y = y;
        this.hitLeft = hitLeft;
        this.hitTop = hitTop;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public Point genNextPoint(){
        if (this.hitLeft){
            this.x+=5;
            if (this.x > 1920){
                this.hitLeft = false;
            }
        } else {
            this.x-=5;
            if (this.x < 5){
                this.hitLeft = true;
            }
        }
        if (this.hitTop){
            this.y+=5;
            if (this.y > 1000){
                this.hitTop = false;
            }
        } else {
            this.y-=5;
            if (this.y < 5){
                this.hitTop = true;
            }
        }
        return new Point(this.x, this.y, this.hitLeft, this.hitTop);
    }
}
