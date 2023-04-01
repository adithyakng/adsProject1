import java.util.ArrayList;

public class gatorTaxi {
    
    public static void main(String args[]){
        // Create a minHeap and RBT Tree
        MinHeap mHeap = new MinHeap();
        RBT rbt = new RBT();

        insert(1, 10, 10, mHeap, rbt);
        insert(2, 9, 1, mHeap, rbt);
        insert(3, 25, 2, mHeap, rbt);
        insert(4, 7, 3, mHeap, rbt);
        insert(5, 6, 4, mHeap, rbt);
        insert(0, 8, 1, mHeap, rbt);
        insert(9, 14, 1, mHeap, rbt);
        // RBT.prettyPrint(rbt.root);
        mHeap.print();
        cancelRide(4, mHeap, rbt);
        mHeap.print();
        // RBT.prettyPrint(rbt.root);
        // mHeap.print();
        // System.out.println(printRideNumberWithInRange(2, 5, new ArrayList<RBTNode>(), rbt.root));
        // System.out.println(getNextRide(mHeap, rbt));
        // RBT.prettyPrint(rbt.root);
        // System.out.println(getNextRide(mHeap, rbt));
    }

    public static boolean insert(int rideNumber, int rideCost, int tripDuration, MinHeap mHeap, RBT rbt){

        // First check if rideNumber is present in the RBT tree
        if(rbt.checkRideNumber(rideNumber)){
            // As the rideNumber is already present we cannot insert into the Data Structure return false and quit
            return false;
        }
        
        // Create a minHeapNode
        minHeapNode minHeapNode = new minHeapNode(rideNumber, rideCost, tripDuration);
        // Create a RBTNode
        RBTNode newRbtNode = new RBTNode(rideNumber, rideCost, tripDuration, RBTNode.red,RBT.externalNode);
        // Establish the the link between minHeapNode and RBTNode
        minHeapNode.rbtPointer = newRbtNode;
        newRbtNode.heapNode = minHeapNode;

        // Insert the minHeapNode and RBTNode into the minHeap and RBT tree respectively
        mHeap.insert(minHeapNode);
        rbt.insert(newRbtNode);
        return true; 
    }

    public static String printRideNumber(int rideNumber, RBT rbt){
        
        // get the node from the RBT tree with the desired rideNumber
        RBTNode rbtNode = rbt.getNodeFromRideNumber(rideNumber);
        if(rbtNode == null){
            // If the node with the ride number is not present in the RBT tree return (0,0,0)
            return "(0,0,0)";
        }

        // return the node details with the ride number
        return "("+rbtNode.rideNumber+","+rbtNode.rideCost+","+rbtNode.tripDuration+")";
    }

    public static ArrayList<RBTNode> printRideNumberWithInRange(int min, int max, ArrayList<RBTNode> al,RBTNode rbt){

        // Check and print the ride number if it is in the range
        if(rbt.rideNumber >= min && rbt.rideNumber <= max){
            al.add(rbt);
        }

        // Recursively check the left and right subtree of the RBT tree
        if(rbt.rideNumber >= min && rbt.left != RBT.externalNode){
            printRideNumberWithInRange(min,max,al,rbt.left);
        }
        if(rbt.rideNumber <= max && rbt.right != RBT.externalNode){
            printRideNumberWithInRange(min,max,al,rbt.right);
        }
        return al;
    }

    public static String getNextRide(MinHeap mHeap, RBT rbt){

        // If there are not active ride requests return "No active ride requests"
        if(mHeap.size == 0){
            return "No active ride requests";
        }

        // Remove min and remove the curresponding node from the RBT tree and return the node details
        minHeapNode minNode = mHeap.removeMin();
        rbt.delete(minNode.rideNumber);
        return "("+minNode.rideNumber+","+minNode.rideCost+","+minNode.tripDuration+")";
    }

    public static void cancelRide(int rideNumber, MinHeap mHeap, RBT rbt){

        // Check if the rideNumber is present in the RBT tree
        RBTNode cancelNode = rbt.getNodeFromRideNumber(rideNumber);
        if(cancelNode == null){
            // The rideNumber is not present in the RBT tree return
            return;
        }
        minHeapNode cancelHeapNode = cancelNode.heapNode;
        // Remove the node from the minHeap and RBT tree
        rbt.delete(rideNumber);
        mHeap.removeNode(cancelHeapNode);
    }

    public static void updateTripDuration(int rideNumber, int modifiedTripDuration, RBT rbt, MinHeap mHeap){

        // First check and get the rideNumber if it is present in the RBT tree

        RBTNode updateNode = rbt.getNodeFromRideNumber(rideNumber);
        if(updateNode == null || updateNode.tripDuration >= modifiedTripDuration){
            /* The rideNumber is not present in the RBT tree  or if the the tripDuration is 
                already greater than the new modifiedTripDuration do nothing
            */
            return;
        }
        else if((updateNode.tripDuration < modifiedTripDuration) && modifiedTripDuration <= 2*(updateNode.tripDuration)){
            /* IF the existing trip duration is less than the new modifiedTripDuration and the 
                modifiedTripDuration is less than or equal to 2 times the existing trip duration
                then update the trip duration and heapify the minHeap
             *  Then cancel the ride and create a new ride with cost penalty of 10
             */
            int modifiedRideCost = updateNode.rideCost + 10;
            cancelRide(rideNumber, mHeap, rbt);
            insert(rideNumber,modifiedRideCost,modifiedTripDuration,mHeap,rbt);

        }
        else if(modifiedTripDuration > 2*(updateNode.tripDuration)){
            // Just cancel the ride
            cancelRide(rideNumber, mHeap, rbt);
        }

        
    }


}
