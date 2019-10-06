package files;

public class Punkt2D {
    public Punkt2D(double xValue, double yValue){
        this.x=xValue;
        this.y=yValue;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public void setX(double xValue){
        this.x=xValue;
    }
    public void setY(double yValue){
        this.y=yValue;
    }

    public double distance(Punkt2D pkt){
        return Math.sqrt( (double) Math.pow( Math.abs(this.x-pkt.x), 2 ) + (double) Math.pow( Math.abs(this.y-pkt.y), 2 ));
    }


   protected double x;
   protected double y;
}
