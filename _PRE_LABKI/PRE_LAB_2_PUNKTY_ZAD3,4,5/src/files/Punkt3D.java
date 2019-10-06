package files;

public class Punkt3D extends Punkt2D {
    public Punkt3D(double xValue, double yValue, double zValue){
        super(xValue, yValue);
        this.z=zValue;
    }
    public double getZ(){
        return z;
    }
    public void setZ(double zValue){
        this.z=zValue;
    }

    public double distance(Punkt3D pkt){
        return Math.sqrt( (double) Math.pow( Math.abs(this.x-pkt.x), 2 ) + (double) Math.pow( Math.abs(this.y-pkt.y), 2 ) + (double) Math.pow( Math.abs(this.z-pkt.z), 2 ));
    }



    double z;
}
