package structures;

/**
 * @author Yao Shi
 */
public class Location {

    /**
     * Return an array containing three groups of angle ranges:0-25,25-65,65-90
     */
    private static Node[] Grouping(Node node){
        Node[] arr = new Node[3];

        while(node != null){
            Node added = node;
            node = node.next;
            added.next = null;

            if(Math.abs(added.getSlope()) < Math.tan(Math.toRadians(25))){
                Node temp = arr[0];
                arr[0] = Node.add(added,temp);
                //System.out.print("added");
            }else if(Math.abs(added.getSlope()) > Math.tan(Math.toRadians(65))){
                arr[2] = Node.add(added,arr[2]);
            }else{
                arr[1] = Node.add(added,arr[1]);
            }
        }
        return arr;
    }

    public static Node[] printGrouping(Node node){
        Node [] arr = Grouping(node);

        System.out.println("0-25  ");
        Node.traverse(arr[0]);
        System.out.println("25-65");
        Node.traverse(arr[1]);
        System.out.println("65-90");
        Node.traverse(arr[2]);

        return arr;
    }

    private static Node combineLines(Node list){
        Node temp = list;

        while(temp.next != null){

            // allow 10% error
            if(Math.abs(temp.getSlope()- temp.next.getSlope()) <= temp.getSlope()*0.1
                    //&&



//todo
            )


            temp = temp.next;
        }


        return list;
    }


    private static boolean ifTwoPointsAreClose(Node one, Node two){
        boolean temp = false;

        return temp;
    }


}
