class RBTNode{

    final static int black = 1;
    final static int red = 0;

    int rideNumber;
    int rideCost;
    int tripDuration;

    RBTNode left;
    RBTNode right;
    RBTNode parent;

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
        this.parent = externalNode;
    }

    public String toString(){
        return rideNumber+" "+rideCost+" "+tripDuration;
    }
}

class RBT1 {

    RBTNode root = null;
    static RBTNode externalNode = null;

    RBT1(){
      if(externalNode == null){
        externalNode = new RBTNode();
        root = externalNode;
      }
    }
    public void insert(int rideNumber, int rideCost, int tripDuration){

        RBTNode currentNode = root;
        RBTNode currentParent = null;

        while(currentNode != externalNode){
            currentParent = currentNode;
            if(currentNode.rideNumber > rideNumber){
                currentNode = currentNode.left;
            }
            else if(currentNode.rideNumber < rideNumber){
                currentNode = currentNode.right;
            }
        }

        RBTNode newNode = new RBTNode(rideNumber, rideCost, tripDuration, RBTNode.red,externalNode);
        
        if(currentParent == null){
            // This is the first node that is being inserted so point it to the root
            root = newNode;
            // NewNode parent change it to null
            root.colour = RBTNode.black;
            return;
        }
        else if(currentParent.rideNumber > rideNumber){
            currentParent.left = newNode;
        }
        else{
            currentParent.right = newNode;
        }

        newNode.parent = currentParent;
        if(newNode.parent.parent == null){
            return;
        }
        fixRBTreeInsert(newNode);

    }

    public void fixRBTreeInsert(RBTNode node){

        RBTNode parent = node.parent;
        RBTNode grandfather = parent.parent;
        RBTNode uncle;

        while(node.parent.colour == RBTNode.red){
            parent = node.parent;
            grandfather = (parent.parent != null) ? parent.parent : null;
            uncle = getUncleNode(parent);

            if(grandfather.right == parent){
              if(uncle!=null && uncle.colour == RBTNode.red){
                uncle.colour = RBTNode.black;
                grandfather.colour = RBTNode.red;
                parent.colour = RBTNode.black;
                node = grandfather;
              }
              else{
                if(node == parent.left){
                  node = parent;
                  rotateRight(node);
                }
                node.parent.colour = RBTNode.black;
                node.parent.parent.colour = RBTNode.red;
                rotateLeft(node.parent.parent);
              }
            }
            else{
              if(uncle!=null && uncle.colour ==  RBTNode.red){
                uncle.colour = RBTNode.black;
                grandfather.colour = RBTNode.red;
                parent.colour = RBTNode.black;
                node = grandfather;
              }
              else{
                  if(node == parent.right){
                    node = parent;
                    rotateLeft(node);
                  }
                  node.parent.colour = RBTNode.black;
                  node.parent.parent.colour = RBTNode.red;
                  rotateRight(node.parent.parent);
              }
            }

            if(node == root){
              root.colour = RBTNode.black;
              break;
            }
        }
    }

    public void rotateRight(RBTNode node){
        RBTNode leftChildNode = node.left;
        RBTNode parentNode = node.parent;

        node.left = leftChildNode.right;
        if(leftChildNode.right != RBT1.externalNode){
          leftChildNode.right.parent = node;
        }

        leftChildNode.right = node;
        leftChildNode.parent = parentNode;
        node.parent = leftChildNode;

        if(parentNode == RBT1.externalNode){
          root = leftChildNode;
        }
        else if(parentNode.left == node){
          parentNode.left = leftChildNode;
        }
        else{
          parentNode.right = leftChildNode;
        }
        
        
    }

    public void rotateLeft(RBTNode node){
        RBTNode rightChildNode = node.right;
        RBTNode parentNode = node.parent;

        node.right = rightChildNode.left;
        if(rightChildNode.left != RBT1.externalNode){
          rightChildNode.left.parent = node;
        }

        rightChildNode.left = node;
        rightChildNode.parent = parentNode;
        node.parent = rightChildNode;
        
        if(parentNode == RBT1.externalNode){
          root = rightChildNode;
        }
        else if(parentNode.left == node){
          parentNode.left = rightChildNode;
        }
        else{
          parentNode.right = rightChildNode;
        }
        
    }


    public RBTNode getUncleNode(RBTNode parent){
        
        if(parent.parent.left == parent){
            return parent.parent.right;
        }
        else if(parent.parent.right == parent){
            return parent.parent.left;
        }
        return null;
    }

    public static void printHelper(RBTNode root, String indent, boolean last) {
      // print the tree structure on the screen
         if (root != null) {
         System.out.print(indent);
         if (last) {
            System.out.print("R----");
            indent += "     ";
         } else {
            System.out.print("L----");
            indent += "|    ";
         }
              
             String sColor = root.colour == 0?"RED":"BLACK";
         System.out.println(root.rideNumber + "(" + sColor + ")");
         printHelper(root.left, indent, false);
         printHelper(root.right, indent, true);
      }
    }

    public RBTNode inOrderSuccessor(RBTNode node){
      
        while(node.left != RBT1.externalNode){
          node = node.left;
        }
        return node;
    }

    public static void prettyPrint(RBTNode root) {
      printHelper(root, "", true);
    }

    public void delete(int rideNumber){
       
        RBTNode delNode = null;
        RBTNode currentNode = root;

        while(currentNode != RBT1.externalNode){
          if(currentNode.rideNumber == rideNumber){
            delNode = currentNode;
            break;
          }
          else if(currentNode.rideNumber > rideNumber){
            currentNode = currentNode.left;
          }
          else{
            currentNode = currentNode.right;
          }
        }

        if(delNode == null){
          // Node not present in the RBTree, ignore the delete operation
          return;
        }
        int delNodeColour = delNode.colour;
        RBTNode successorRightChild = null;
        if(delNode.left == RBT1.externalNode){
          // No left child condition
          if(delNode.parent == null){
            root = delNode.right;
          }
          else if(delNode == delNode.parent.left){
            delNode.parent.left = delNode.right;
          }
          else{
            delNode.parent.right = delNode.right;
          }
          delNode.right.parent = delNode.parent;
        }
        else if(delNode.right == RBT1.externalNode){
          // No right child condition
          if(delNode.parent == null){
            root = delNode.left;
          }
          else if(delNode == delNode.parent.left){
            delNode.parent.left = delNode.left;
          }
          else {
            delNode.parent.right = delNode.left;
          }
          delNode.left.parent = delNode.parent;
        }
        else{
          // Both left and right child present
          RBTNode successor = inOrderSuccessor(delNode.right);

          // As we are replacing the delete Node with inorder successor, change the colour of the delete node to the colour of the successor
          delNodeColour = successor.colour;
          successorRightChild = successor.right;
          if(successor.parent == delNode){
            successorRightChild.parent = successor;
          }
          else{
            if(successor.parent == null){
              root = successorRightChild;
            }
            else if(successor == successor.parent.left){
              successor.parent.left = successorRightChild;
            }
            else{
              successor.parent.right = successorRightChild;
            }
            successorRightChild.parent = successor.parent;
            successor.right = delNode.right;
            successor.right.parent = successor;
          }

          // u is delNode
          // v is successor
          if(delNode.parent == null){
            root = successor;
          }
          else if(delNode == delNode.parent.left){
            delNode.parent.left = successor;
          }
          else{
            delNode.parent.right = successor;
          }
          successor.parent = delNode.parent;
          successor.left = delNode.left;
          successor.left.parent = successor;
          successor.colour = delNode.colour;
        }

        if(delNodeColour == RBTNode.black){
          //fixDeleteCases(successorRightChild);
        }
        

    }

    public static void main(String args[]){
        RBT1 r = new RBT1();

        r.insert(10, 1, 2);
        r.insert(20,2,3);
        r.insert(15,5,3);
        r.insert(2,1,3);
        r.insert(25,14,13);
        r.insert(13,5,12);

        r.delete(13);
        prettyPrint(r.root);
   }
    
}
