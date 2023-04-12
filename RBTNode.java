class RBTNode{

    // Static variables for the colour of the node in the Red Black Tree
    // 1 - Black
    // 0 - Red
    final static int black = 1;
    final static int red = 0;

    // Variables to store the ride number, ride cost and trip duration
    int rideNumber;
    int rideCost;
    int tripDuration;

    // Pointers to the left child, right child and parent of the node
    RBTNode left;
    RBTNode right;
    RBTNode parent;

    // Pointer to the corresponding node in the min heap
    minHeapNode heapNode;

    // Variable to store the colour of the node
    int colour;

    // Constructor to initialize the external node, left, parent and right child pointers are set to null, colour is set to black
    RBTNode(){
      left = null;
      right = null;
      parent = null;
      colour = RBTNode.black;
    }

    // Constructor to initialize the internal node, left, parent and right child pointers are set to external node, colour is set to black
    RBTNode(int rideNumber, int rideCost, int tripDuration, int colour, RBTNode externalNode){
		this.rideCost = rideCost;
		this.rideNumber = rideNumber;
		this.tripDuration = tripDuration;
		this.colour = colour;
		this.left = externalNode;
		this.right = externalNode;
		this.parent = null;
    }

    // Overriding the toString method to print the node details
    public String toString(){
        return rideNumber+" "+rideCost+" "+tripDuration;
    }
}