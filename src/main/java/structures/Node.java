package structures;

import java.util.ArrayList;

/**
 * @author Yao Shi
 */
public class Node {
    public int x1,x2,y1,y2;
    public double  slope;
    public Node next;

    public Node(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.slope = (y2-y1)/(x2-x1);
        this.next = null;
    }

    public static Node add(int x1, int y1, int x2, int y2, Node list){
        Node added =  new Node(x1,y1,x2,y2);
        if(list == null){
            return added;
        }
        added.next = list;
        return added;
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


}
