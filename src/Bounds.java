/**
 * Created by pedri017 on 4/27/18.
 */
import java.awt.*;

public class Bounds {

    // The extrema of our rectangle
    private Vec2 min;
    private Vec2 max;

    // Getters, no setters. Use extend instead.
    public Vec2 getMin(){ return min; }
    public Vec2 getMax(){ return max; }

    // Default min and max to null, so that they are
    // initialized on the first call to extend.
    public Bounds(){
        min = null;
        max = null;
    }

    public boolean isOutside(double x, double y){

        if (x>= min.x && y >= min.y && x<= max.x && y <= max.y) {
            return false;
        }
        return true;
    }

    public void extend(double x, double y){
        if (min==null || max==null){
            min=new Vec2(x,y);
            max = new Vec2(x,y);
        }
        else {
            if (min.x > x)
                min.x = x;
            if (min.y > y)
                min.y =y;
            if(max.x < x)
                max.x=x;
            if (max.y<y)
                max.y=y;
        }

    }

    // Returns the distance from the box surface to a point
    // Return 0 if the point is inside the box!
    public double exteriorDistance(double x, double y){
        if(!isOutside(x,y))
            return 0;
        else {
            Vec2 temp = new Vec2(x, y);
            double minDis = min.distance(temp);
            double maxDis = max.distance(temp);
            if (minDis < maxDis)
                return minDis;
            return maxDis;
        }

    }

    // Extend the size of the box to include a new bounds
    public void extend(Bounds b){
        if(min==null && max==null){
            min=new Vec2();
            max = new Vec2();
        }
        Vec2 compMin=b.getMin();
        Vec2 compMax=b.getMax();

        if (compMin.y < min.y) {
            min.y=compMin.y;
        }
        if (compMin.x < min.x) {
            min.x = compMin.x;
        }
        if (compMax.y > max.y) {
            max.y = compMax.y;
        }
        if (compMax.x > max.x) {
            max.x = compMax.x;
        }

        else return;

    }

    // Draw a black opaque rectangle
    public void paint(Graphics2D g){
        g.setColor(Color.black);
        g.drawRect((int)min.x, (int)min.y, (int)(max.x-min.x), (int)(max.y-min.y));
    }

} // end class Bounds