class minHeapNode{

    int rideNumber;
    int rideCost;
    int tripDuration;

    minHeapNode(int rideNumber, int rideCost, int tripDuration){
        this.rideNumber = rideNumber;
        this.rideCost = rideCost;
        this.tripDuration = tripDuration;
    }

    public String toString(){
        return rideNumber+" "+rideCost+" "+tripDuration;
    }

}


public class minHeap{


    minHeapNode minHeap[];
    int size;

    minHeap(){
        // Maximum size of the heap can be atmost 2000 as per the question
        minHeap = new minHeapNode[2001];
        size = 0;
    }

    public minHeapNode getParentNode(int index){
        return minHeap[getParentIndex(index)];
    }

    public minHeapNode getLeftChildNode(int index){
        return minHeap[getLeftChildIndex(index)];
    }

    public minHeapNode getRightChildNode(int index){
        return minHeap[getRightChildIndex(index)];
    }

    public int getParentIndex(int index){
        return (index/2);
    }

    public int getLeftChildIndex(int index){
        return (2*index)+1;
    }

    public int getRightChildIndex(int index){
        return (2*index)+2;
    }

    public boolean isNodeALeaf(int index){

        if((((2*index)+1) >= size) || (((2*index)+2) >= size)){
            return true;
        }
        return false;
    }

    public boolean hasLeftChild(int index){
        if(getLeftChildIndex(index) < size){
            return true;
        }
        return false;
    }

    public boolean hasRightChild(int index){
        if(getRightChildIndex(index) < size){
            return true;
        }
        return false;
    }

    public void swapNodes(int node1, int node2){
        minHeapNode temp = minHeap[node1];
        minHeap[node1] = minHeap[node2];
        minHeap[node2] = temp;
    }

    public void insert(int rideNumber, int rideCost, int tripDuration){

        minHeapNode newNode = new minHeapNode(rideNumber, rideCost, tripDuration);
        minHeap[size] = newNode;
        int currentIndex = size;
        while((minHeap[currentIndex].rideCost < getParentNode(currentIndex).rideCost) || (minHeap[currentIndex].rideCost == getParentNode(currentIndex).rideCost && minHeap[currentIndex].tripDuration < getParentNode(currentIndex).tripDuration) ){
            swapNodes(currentIndex, getParentIndex(currentIndex));
            currentIndex = getParentIndex(currentIndex);
        }
        size = size + 1;
    }

    public void heapify(int index){
        
        if(!isNodeALeaf(index)){
            int swapIndex = index;
        
            if(hasRightChild(index)){
                swapIndex = getLeftChildNode(index).rideCost < getRightChildNode(index).rideCost ? getLeftChildIndex(index) : (getLeftChildNode(index).rideCost == getRightChildNode(index).rideCost && getLeftChildNode(index).tripDuration < getRightChildNode(index).tripDuration ? getLeftChildIndex(index) : getRightChildIndex(index));
            }
            else{
                swapIndex = getLeftChildIndex(index);
            }

            if((minHeap[index].rideCost > getLeftChildNode(index).rideCost) || (minHeap[index].rideCost > getRightChildNode(index).rideCost)){
                swapNodes(index, swapIndex);
                heapify(swapIndex);
            }
            else if(hasRightChild(index)){
                minHeapNode left = getLeftChildNode(index);
                minHeapNode right = getRightChildNode(index);

                if((minHeap[index].rideCost == left.rideCost && minHeap[index].tripDuration > left.tripDuration) || (minHeap[index].rideCost == right.rideCost && minHeap[index].tripDuration > right.tripDuration)){
                    swapIndex = (minHeap[index].rideCost == left.rideCost && minHeap[index].tripDuration > left.tripDuration) ? getLeftChildIndex(index) : getRightChildIndex(index);
                    swapNodes(index, swapIndex);
                    heapify(swapIndex);
                }
            }
            else if(hasLeftChild(index)){
                minHeapNode left = getLeftChildNode(index);
                if(minHeap[index].rideCost == left.rideCost && minHeap[index].tripDuration > left.tripDuration){
                    swapIndex = getLeftChildIndex(index);
                    heapify(swapIndex);
                }
            }
        }
    }

    // Remove this function adithya

    public void print(){
        for(int i=0;i<size;i++){
            System.out.println(minHeap[i]);
        }
    }

    public minHeapNode removeMin(){
        minHeapNode root = minHeap[0];
        minHeap[0] = minHeap[size-1];
        size = size - 1;
        heapify(0);
        return root;
    }

    // Remove this function adithya

    // public static void main(String[] args) {
    //         minHeap m = new minHeap();
    //         m.insert(5, 4, 2);
    //         m.insert(6, 4, 3);
    //         m.insert(7, 4, 1);
    //         m.insert(8, 1, 3);
    //         m.insert(10, 1, 10);
    //         m.print();
    //         System.out.println();
    //         while(m.size !=0)
    //         {
    //             System.out.println(m.removeMin());
    //         }
    // }

}