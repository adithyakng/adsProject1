import java.io.*;

public class gatorTaxi {
    
    static int printCount = 0;
    public static void main(String args[]) throws IOException{
        // Create a minHeap and RBT Tree
        MinHeap mHeap = new MinHeap();
        RBT rbt = new RBT();
        File inputFile = new File("b.txt"); // change the file name here adithya
        BufferedReader inputReader = new BufferedReader(new InputStreamReader( new FileInputStream(inputFile)));
        //String input[] = "Print(a,b,c)".split("\\(");
        String inputLine;
        while(true){
            inputLine = inputReader.readLine();
            if(inputLine == null){
                break;
            }
            String input[] = inputLine.split("\\(");
            String subInput[];
            String nextRideReturn;
            boolean insertSuccess;
            int returnCount;
            switch(input[0]){
                case "Print":
                    subInput = input[1].split("\\,");
                    if(subInput.length == 1){
                        // Print function for single ride number
                        int rideNumber = Integer.valueOf(subInput[0].split("\\)")[0]);
                        System.out.println(printRideNumber(rideNumber,rbt));

                    }
                    else{
                        // Print function for range of rideNumbers
                        gatorTaxi.printCount = 0;
                        returnCount =  printRideNumberWithInRange(Integer.valueOf(subInput[0]),Integer.valueOf(subInput[1].split("\\)")[0]),rbt.root);
                        if(returnCount == 0){
                            System.out.println("(0,0,0)");
                        }
                        else{
                            System.out.println();
                        }
                        gatorTaxi.printCount = 0;

                    }
                break;
                case "Insert":
                    subInput = input[1].split("\\,");
                    insertSuccess = insert(Integer.parseInt(subInput[0]),Integer.parseInt(subInput[1]), Integer.parseInt(subInput[2].split("\\)")[0]), mHeap, rbt);
                    if(!insertSuccess){
                        System.out.println("Duplicate RideNumber");
                        return;
                    }
                break;
                case "UpdateTrip":
                    subInput = input[1].split("\\,");
                    updateTripDuration(Integer.parseInt(subInput[0]), Integer.parseInt(subInput[1].split("\\)")[0]), rbt, mHeap);
                break;
                case "CancelRide":
                    cancelRide(Integer.parseInt(input[1].split("\\)")[0]), mHeap, rbt);
                break;
                case "GetNextRide":
                    nextRideReturn = getNextRide(mHeap, rbt);
                    System.out.println(nextRideReturn);
                break;
                default:
                    break;
            }
        }
        inputReader.close();
        
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

    public static int printRideNumberWithInRange(int min, int max,RBTNode rbt){

        if(rbt == RBT.externalNode){
            return 0;
        }
        int count = 0;
        if(rbt.rideNumber >= min){
            count = count + printRideNumberWithInRange(min,max,rbt.left);
        }
        if(min <= rbt.rideNumber && rbt.rideNumber <= max && rbt != RBT.externalNode){
            if(gatorTaxi.printCount > 0){
                System.out.print(",");
            }
            System.out.print("("+rbt.rideNumber+","+rbt.rideCost+","+rbt.tripDuration+")");
            gatorTaxi.printCount++;
            count++;
        }
        if(rbt.rideNumber <= max){
            count = count + printRideNumberWithInRange(min,max,rbt.right);
        }
        return count;
    }

    public static String getNextRide(MinHeap mHeap, RBT rbt){

        // If there are not active ride requests return "No active ride requests"
        if(mHeap.size == 0){
            return "No active ride requests";
        }

        // Remove min and remove the curresponding node from the RBT tree and return the node details
        minHeapNode minNode = mHeap.removeMin();
        rbt.deleteRBTNode(minNode.rideNumber);
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
        rbt.deleteRBTNode(rideNumber);
        mHeap.removeNode(cancelHeapNode);
    }

    public static void updateTripDuration(int rideNumber, int modifiedTripDuration, RBT rbt, MinHeap mHeap){

        // First check and get the rideNumber if it is present in the RBT tree

        RBTNode updateNode = rbt.getNodeFromRideNumber(rideNumber);
        if(updateNode == null){
            // The rideNumber is not present in the RBT tree return
            return;
        }
        if(updateNode.tripDuration >= modifiedTripDuration){
            /* if the the tripDuration is 
                already greater than the new modifiedTripDuration do nothing
            */
            updateNode.tripDuration = modifiedTripDuration;
            updateNode.heapNode.tripDuration = modifiedTripDuration;

            // Now check if the minHeap property is violated and heapify the minHeap either in the upward or downward direction

            // If the updateNode ride cost is greater than the parent node ride cost or if the ride cost is equal and the trip duration is less than the parent node trip duration then heapify up
            if(updateNode.rideCost < mHeap.getParentNode(updateNode.heapNode.index).rideCost || ((updateNode.rideCost == mHeap.getParentNode(updateNode.heapNode.index).rideCost && updateNode.tripDuration < mHeap.getParentNode(updateNode.heapNode.index).tripDuration))){
                mHeap.heapifyUp(updateNode.heapNode.index);
            }
            // Or else heapify down if necessary
            else{
                mHeap.heapifyDown(updateNode.heapNode.index);
            }
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
