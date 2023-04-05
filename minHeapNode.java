// Class to store the node details of the min heap
class minHeapNode{

    int rideNumber;
    int rideCost;
    int tripDuration;

    // Index of the node in the min heap
    int index;

    // Pointer to the corresponding node in the RBT tree
    RBTNode rbtPointer;


    minHeapNode(int rideNumber, int rideCost, int tripDuration){
        this.rideNumber = rideNumber;
        this.rideCost = rideCost;
        this.tripDuration = tripDuration;

        // Initially the rbtPointer is null, as a link is not established with the RBT tree
        rbtPointer = null;
    }

    // Overriding the toString method to print the node details
    public String toString(){
        return index+" "+rideNumber+" "+rideCost+" "+tripDuration;
    }

}