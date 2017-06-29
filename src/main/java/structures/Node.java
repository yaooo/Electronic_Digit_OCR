package structures;

import org.jetbrains.annotations.Contract;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * @author Yao Shi
 */
public class Node {
    private int x1,x2,y1,y2;
    private double  slope;
    public Node next;

    public Node(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.slope = (y2-y1)/(x2-x1); // since the origin(0,0) is on the top left corner
        this.next = null;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    public double getSlope() {
        return slope;
    }

    public static Node add(int x1, int y1, int x2, int y2, Node list){
        Node added =  new Node(x1,y1,x2,y2);
        if(list == null){
            return added;
        }
        Node temp = list;

        while(true){
            if(temp.next == null){
                temp.next = added;
                break;
            }
            if(temp.slope <= added.slope && added.slope <= temp.next.slope){
                added.next = temp.next;
                temp.next = added;
                break;
            }
            temp = temp.next;
        }

        return list;
    }

    public static void traverse(Node list){
        if(list == null)
            System.out.println("List is empty.");
        Node temp = list;
        while(temp != null){
            String xyslope = "Point: p1 (" + temp.x1 + "," + temp.y1 +") ,p2 (" + temp.x2 + "," + temp.y2 +")---------> Slope:" + temp.slope;
            System.out.println(xyslope);
            System.out.println("Angle between current node and the first node: "+Line.angle(list,temp)+"\n---------------------------------------");
            temp = temp.next;
        }
    }

    // Using recursion, feel free to change it back to regular loop
    public static Node lastNode(Node list){
        if(list == null)
            return null;
        if(list.next == null)
            return list;
        else
            return lastNode(list.next);
    }

    // Just for testing purpose, assuming the length of the linked list is more than 1
    public static Node NodeBeforelastNode(Node list){
        if(list.next.next == null)
            return list;

        return NodeBeforelastNode(list.next);
    }


}
