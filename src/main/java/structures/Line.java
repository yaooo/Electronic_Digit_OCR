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

    public static double AngleFromHrizontalAxes(int x1,int y1, int x2, int y2){
        Node one = new Node (x1,y1,x2,y2);
        Node two = new Node(0,0,1,0);
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

   /* public static boolean IfCombine(Node one, Node two){
        int x = (two.getX1()+two.getX2())/2;
        int y = (two.getY1()+two.getY2())/2;

        double m1 = one.getSlope();
        double m2 = two.getSlope();

        double d = pDistance(x,y,one);

        return (Math.abs(m1 - m2) < 0.5 && d < 20);
    }

    public static void combineLine(Node[] nodelist) {
        for (int i = 0; i < 3; i++) {
            Node node = nodelist[i];
            System.out.println("list " + i + "..............");
            while (node != null) {
                Node temp = node.next;
                while (temp != null) {

                    if (IfCombine(node, temp))
                        System.out.println("Combine +1");
                    temp = temp.next;
                }
                node = node.next;
            }
        }
    }


    private static double pDistance(int x, int y, Node line){
        int x1 = line.getX1();
        int y1 = line.getY1();
        int x2 = line.getX2();
        int y2 = line.getY2();

        int A = x - x1;
        int B = y - y1;
        int C = x2 - x1;
        int D = y2 - y1;

        int dot = A * C + B * D;
        int len_sq = C * C + D * D;
        int param = -1;
        if (len_sq != 0) //in case of 0 length line
            param = dot / len_sq;

        int xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        }
        else if (param > 1) {
            xx = x2;
            yy = y2;
        }
        else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        int dx = x - xx;
        int dy = y - yy;
        return Math.sqrt((double)(dx * dx + dy * dy));
    }*/
}
