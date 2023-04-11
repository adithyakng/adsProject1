// MinHeap class which implements the min heap data structure
public class MinHeap{


    minHeapNode minHeap[];
    int size;

    MinHeap(){
        // Maximum size of the heap can be atmost 2000 as per the question
        minHeap = new minHeapNode[2000];
        // Initially the size of the heap is 0
        size = 0;
    }

    // Method to get the parent node of the node at the given index
    public minHeapNode getParentNode(int index){
        return minHeap[getParentIndex(index)];
    }

    // Method to get the left child node of the node at the given index
    public minHeapNode getLeftChildNode(int index){
        return minHeap[getLeftChildIndex(index)];
    }

    // Method to get the right child node of the node at the given index
    public minHeapNode getRightChildNode(int index){
        return minHeap[getRightChildIndex(index)];
    }

    // Method to get the parent index of the node at the given index
    public int getParentIndex(int index){
        if(index == 0){
            return 0;
        }
        return (index % 2) == 0 ? (index - 2)/2 : (index - 1)/2;
    }

    // Method to get the left child index of the node at the given index
    public int getLeftChildIndex(int index){
        return (2*index)+1;
    }

    // Method to get the right child index of the node at the given index
    public int getRightChildIndex(int index){
        return (2*index)+2;
    }

    // Method to check if a node is a leaf node
    public boolean isNodeALeaf(int index){

        // If the node is a leaf node, then it will not have leftChild and rightChild
        if(!hasLeftChild(index) && !hasRightChild(index)){
            return true;
        }
        return false;
    }

    // Method to check if a node has a left child
    public boolean hasLeftChild(int index){
        // If the left child index is within the size of the heap, then the node has a left child
        if(getLeftChildIndex(index) < size){
            return true;
        }
        return false;
    }

    // Method to check if a node has right child
    public boolean hasRightChild(int index){

        // If the right child index is within the size of the heap, then the node has a right child
        if(getRightChildIndex(index) < size){
            return true;
        }
        return false;
    }

    // Utility function to swap two nodes, given their indices
    public void swapNodes(int node1, int node2){
        minHeapNode temp = minHeap[node1];
        minHeap[node1] = minHeap[node2];
        minHeap[node1].index = node1;
        minHeap[node2] = temp;
        minHeap[node2].index = node2;
    }

    // Method to insert a new node into the min heap
    public void insert(minHeapNode newNode){

        // Insert the new node at the end of the heap
        minHeap[size] = newNode;
        newNode.index = size;
        int currentIndex = size;

        // Increment the size of the heap by 1
        size = size + 1;
        
        // Once a new node is inserted, the heap property may be violated, so heapify the heap in upward direction
        heapifyUp(currentIndex);
    }


    public void heapifyUp(int currentIndex){
        /* 
         * Check the heap property for the current node and its parent node and swap 
         * if necessary until the heap property is satisfied or we reach the root node
        */ 
        while((minHeap[currentIndex].rideCost < getParentNode(currentIndex).rideCost) || (minHeap[currentIndex].rideCost == getParentNode(currentIndex).rideCost && minHeap[currentIndex].tripDuration < getParentNode(currentIndex).tripDuration)){
            swapNodes(currentIndex, getParentIndex(currentIndex));
            currentIndex = getParentIndex(currentIndex);
        }
    }

    public void heapifyDown(int index){
        /*
         * Check the heap property for the current node and its child nodes and swap if necessary until we reach a leaf node
         */
        int beforeSwapIndex;
        while(!isNodeALeaf(index)){
            minHeapNode left = getLeftChildNode(index);
            minHeapNode minimum = left;
            minHeapNode right;
            if(hasRightChild(index)){
                right = getRightChildNode(index);
                if(right.rideCost < left.rideCost){
                    minimum = right;
                }
                else if(right.rideCost == left.rideCost && right.tripDuration < left.tripDuration){
                    minimum = right;
                }
            }
            if(minHeap[index].rideCost > minimum.rideCost){
                beforeSwapIndex = minimum.index;
                swapNodes(index, minimum.index);
                index = beforeSwapIndex;
            }
            // If the ride cost is same, then compare the trip duration and the one with lower trip duration is given priority
            else if(minHeap[index].rideCost == minimum.rideCost && minHeap[index].tripDuration > minimum.tripDuration){
                beforeSwapIndex = minimum.index;
                swapNodes(index, minimum.index);
                index = beforeSwapIndex;
            }
            else{
                break;
            }
        }
    }

    public minHeapNode removeMin(){

        // If the heap is empty, then return null
        if(size == 0){
            return null;
        }
        /*
         * If the heap is not empty, then swap the root node with the last node in the heap and 
         * heapify the heap in downward direction
         */
        minHeapNode root = minHeap[0];
        minHeap[0] = minHeap[size-1];
        minHeap[0].index = 0;
        size = size - 1;
        heapifyDown(0);
        return root;
    }

    public void removeNode(minHeapNode node){

        // If the node is the last node in the heap then just remove it
        if(size == 1){
            size = size - 1;
            return;
        }

        // If the index of the node is 0, then remove the min node and return
        if(node.index == 0){
            removeMin();
            return;
        }

        // If the node is not the last node in the heap then swap it with the last node and heapify
        minHeapNode lastNode = minHeap[size-1];
        node.rideNumber = lastNode.rideNumber;
        node.rideCost = lastNode.rideCost;
        node.tripDuration = lastNode.tripDuration;
        node.rbtPointer = lastNode.rbtPointer;

        // Change the rbt pointer also
        lastNode.rbtPointer.heapNode = node;
        size = size - 1;

        // Now depending on the value of the node, either heapify up or heapify down
        if(node.rideCost < getParentNode(node.index).rideCost || (node.rideCost == getParentNode(node.index).rideCost && node.tripDuration < getParentNode(node.index).tripDuration)){
            heapifyUp(node.index);
        }
        else
        {
            heapifyDown(node.index);
        }
    }

}