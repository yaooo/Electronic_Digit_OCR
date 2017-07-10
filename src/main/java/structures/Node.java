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


    /**
     * (x1,y1) and (x2,y2) are input points
     * IMPORTANT: (x1,y1) should always be closed to the origin(0,0)
     */

    public Node(int x1, int y1, int x2, int y2) {
        if(Math.hypot(x1,y1) > Math.hypot(x2,y2)) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }else{
            this.x1 = x2;
            this.y1 = y2;
            this.x2 = x1;
            this.y2 = y1;
        }

        if(x1==x2){
            this.slope = Double.POSITIVE_INFINITY;
        } else{
            this.slope =(double)((int)(((double)(y2-y1)/(double)(x2-x1))*1000))/1000;
        } // Keep three decimal place for slope
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

        //first node
        if(added.getSlope() <= list.getSlope()) {
            added.next = list;
            return added;
        }

        Node temp = list;
        while(true){
            if(temp.next == null){
                temp.next = added;
                break;
            }
            if(temp.getSlope() <= added.getSlope() && added.getSlope() <= temp.next.getSlope()){
                added.next = temp.next;
                temp.next = added;
                break;
            }

            temp = temp.next;
        }

        return list;
    }

    public static Node add(Node added, Node list){
        if(list == null){
            return added;
        }
        Node temp = list;

        if(added.getSlope() <= list.getSlope()) {
            added.next = list;
            return added;
        }
        while(true){
            if(temp.next == null){
                temp.next = added;
                break;
            }
            if(temp.getSlope() <= added.getSlope() && added.getSlope() <= temp.next.getSlope()){
                added.next = temp.next;
                temp.next = added;
                break;
            }
            temp = temp.next;
        }
        return list;
    }

    public static Node copyNode(Node node){
        int x1 = node.getX1();
        int y1 = node.getY1();
        int x2 = node.getX2();
        int y2 = node.getY2();
        return new Node(x1,y1,x2,y2);
    }

    public static int numberOfNode(Node list){
        if(list == null) return 0;
        int numNode= 0;
        Node temp = list;
        while(temp != null){
            numNode++;
            temp = temp.next;
        }
        return numNode;
    }

    public static int traverse(Node list){
        int numNode= 0;
        if(list == null)
            System.out.println("This list is empty.");
        Node temp = list;
        while(temp != null){
            String xyslope = "Point: p1 (" + temp.getX1() + "," + temp.getY1() +") ,p2 (" + temp.getX2() + "," + temp.getY2() +")---------> Slope:" + temp.getSlope();
            System.out.println(xyslope);
            numNode++;
            temp = temp.next;
        }
        return numNode;
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
