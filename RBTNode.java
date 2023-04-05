class RBTNode{

    final static int black = 1;
    final static int red = 0;

    int rideNumber;
    int rideCost;
    int tripDuration;

    RBTNode left;
    RBTNode right;
    RBTNode parent;
    minHeapNode heapNode;

    int colour;

    RBTNode(){
      left = null;
      right = null;
      parent = null;
      colour = RBTNode.black;
    }

    RBTNode(int rideNumber, int rideCost, int tripDuration, int colour, RBTNode externalNode){
		this.rideCost = rideCost;
		this.rideNumber = rideNumber;
		this.tripDuration = tripDuration;
		this.colour = colour;
		this.left = externalNode;
		this.right = externalNode;
		this.parent = null;
    }

    public String toString(){
        return rideNumber+" "+rideCost+" "+tripDuration;
    }
}