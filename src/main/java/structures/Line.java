package structures;

import java.awt.*;

/**
 * @author Yao Shi
 */
public class Line {

    private double m;
    private double b;

    private Line(Node node) {
        int x1;
        x1 = node.getX1();
        this.m = node.getSlope();
        this.b = (0-x1)*(double)m+node.getY1();
    }

    private double getM() {
        return m;
    }

    private double getB() {
        return b;
    }

    private static Point intersect(Node one, Node two) {
        Line line = new Line(one);
        Line line1 = new Line(two);

        double x = (line.getB() - line1.getB()) / (line1.getM() - line.getM());
        double y = line1.getM() * x + line1.getB();
        System.out.println("equation fist node:"+ line.getM() + "x + "+ line.getB());
        System.out.println("equation temp:"+ line1.getM() + "x + "+ line1.getB());
        return new Point((int) x, (int) y);
    }

    private static double[] vector(Point intersection, Node node){
        double int_x = intersection.getX();
        double int_y = intersection.getY();

        int x1 = node.getX1();
        int x2 = node.getX2();
        int y1 = node.getY1();
        int y2 = node.getY2();

        double distance = Math.hypot(int_x - x1, int_y - y1);
        double distance1 = Math.hypot(int_x - x2, int_y - y2);

        if(distance > distance1){
            return new double[]{x1-int_x, y1-int_y};
        }else return new double[]{x2-int_x, y2-int_y};
    }


    public static double angle(Node one, Node two){
        if(one.getSlope() == two.getSlope()) return 0;

        Point intersection = intersect(one, two);

        System.out.println("intersection:"+intersection.getX()+","+intersection.getY());
        double[] v1 = vector(intersection,one);
        double[] v2 = vector(intersection,two);

        double dot = v1[0]*v2[0]+v1[1]*v2[1];
        double mag_v1 = Math.sqrt ( v1[0]*v1[0] + v1[1]*v1[1] );
        double mag_v2 = Math.sqrt ( v2[0]*v2[0] + v2[1]*v2[1]  );
        double angle = Math.toDegrees(Math.acos((double) (dot/(mag_v1*mag_v2))));

        return (double)((int)(angle*1000))/1000; // keep three decimal place for the angle
    }


}
