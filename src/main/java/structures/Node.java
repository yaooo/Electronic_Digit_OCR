package structures;

import java.util.ArrayList;

/**
 * @author Yao Shi
 */
public class Node {
    private int x1,x2,y1,y2;
    public double  slope;
    public Node next;

    public Node(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.slope = -(y2-y1)/(x2-x1); // since the origin(0,0) is on the top left corner
        this.next = null;
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
        while(list != null){
            String xyslope = "Point: p1 (" + list.x1 + "," + list.y1 +") ,p2 (" + list.x2 + "," + list.y2 +")---------> Slope:" + list.slope;
            System.out.println(xyslope);
            list = list.next;
        }
    }

    // find angle using dot product, pass in nodes
    public static double findAngle(Node node1, Node node2){
        int flag = 1;
        double slope1 = node1.slope;
        double slope2 = node2.slope;

        double avg_y1 = (double)(node1.y1+node1.y2)/2;
        double avg_y2 = (double)(node2.y1+node2.y2)/2;
        double avg_x1 = (double)(node1.x1+node1.x2)/2;
        double avg_x2 = (double)(node2.x1+node2.x2)/2;
        if(slope1 > slope2){
            if(avg_x1 > avg_x2) flag = (avg_y1 < avg_y2)? 1:-1;
            else flag = (avg_y1 < avg_y2)? -1:1;
        }else{//slope1 <slope2
            if(avg_x1 < avg_x2) flag = (avg_y1 < avg_y2)? -1:1;
            else flag = (avg_y1 < avg_y2)? 1:-1;
        }


        return  findAngleUsingSlope(slope1,slope2,flag);
    }

    // find angle directly using slopes, pass in slopes
    // flag can be set to -1 or 1 depend on the vector direction
    public static double findAngleUsingSlope(double slope1, double slope2, int flag){
        double AdotB = flag + slope1*slope2;
        double ABProduct = Math.sqrt((1+slope1*slope1)*(1+slope2*slope2));
        return  Math.toDegrees(Math.acos(AdotB/ABProduct));
    }

    // Using recursion, feel free to change it back to regular loop
    //
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
