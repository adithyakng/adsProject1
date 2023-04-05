class RBT {

    RBTNode root = null;
    static RBTNode externalNode = null;

    RBT(){
      if(externalNode == null){
        externalNode = new RBTNode();
        root = externalNode;
      }
    }
    public void insert(RBTNode newNode){

        RBTNode currentNode = root;
        RBTNode currentParent = null;

        while(currentNode != externalNode){
            currentParent = currentNode;
            if(currentNode.rideNumber > newNode.rideNumber){
                currentNode = currentNode.left;
            }
            else if(currentNode.rideNumber < newNode.rideNumber){
                currentNode = currentNode.right;
            }
        }
        if(currentParent == null){
            // This is the first node that is being inserted so point it to the root
            root = newNode;
            // NewNode parent change it to null
            root.colour = RBTNode.black;
            return;
        }
        else if(currentParent.rideNumber > newNode.rideNumber){
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

    public void rotateRight(RBTNode node) {
     	RBTNode leftChildNode = node.left;
		node.left = leftChildNode.right;
		if (leftChildNode.right != RBT.externalNode) {
			leftChildNode.right.parent = node;
		}
		leftChildNode.parent = node.parent;
		if (node.parent == null) {
			root = leftChildNode;
		} else if (node == node.parent.right) {
			node.parent.right = leftChildNode;
		} else {
			node.parent.left = leftChildNode;
		}
		leftChildNode.right = node;
		node.parent = leftChildNode;
    }

    public void rotateLeft(RBTNode node) {
		RBTNode rightChildNode = node.right;
		node.right = rightChildNode.left;
		if (rightChildNode.left != RBT.externalNode) {
			rightChildNode.left.parent = node;
		}
		rightChildNode.parent = node.parent;
		if (node.parent == null) {
			root = rightChildNode;
		} else if (node == node.parent.left) {
			node.parent.left = rightChildNode;
		} else {
			node.parent.right = rightChildNode;
		}
		rightChildNode.left = node;
		node.parent = rightChildNode;
    }


    public RBTNode getUncleNode(RBTNode node){
        
        if(node.parent.left == node){
            return node.parent.right;
        }
        else if(node.parent.right == node){
            return node.parent.left;
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
      
        while(node.left != RBT.externalNode){
          node = node.left;
        }
        return node;
    }

    public static void prettyPrint(RBTNode root) {
      printHelper(root, "", true);
    }

    public void deleteRBTNode(int rideNumber){
       
		RBTNode deleteNode = RBT.externalNode;
		RBTNode currentNode = root;
		while (currentNode != RBT.externalNode){
			if (currentNode.rideNumber == rideNumber) {
				deleteNode = currentNode;
			}

			if (currentNode.rideNumber < rideNumber) {
				currentNode = currentNode.right;
			} 
			else {
				currentNode = currentNode.left;
			}
		}

		// Node not present in the RBTree, ignore the delete operation
		if (deleteNode == RBT.externalNode) {
			return;
		} 
		
		int deleteNodeColour = deleteNode.colour;
      	RBTNode replacementNode;
		// if no left child
		if (deleteNode.left == RBT.externalNode) {
			replacementNode = deleteNode.right;
			if (deleteNode.parent == null) {
				root = deleteNode.right;
			} 
			else if (deleteNode == deleteNode.parent.left){
				deleteNode.parent.left = deleteNode.right;
			} 
			else {
				deleteNode.parent.right = deleteNode.right;
			}
			deleteNode.right.parent = deleteNode.parent;
		} 
		// If no right child
		else if (deleteNode.right == RBT.externalNode) {
			replacementNode = deleteNode.left;
			if (deleteNode.parent == null) {
				root = deleteNode.left;
			} 
			else if (deleteNode == deleteNode.parent.left){
				deleteNode.parent.left = deleteNode.left;
			} 
			else {
				deleteNode.parent.right = deleteNode.left;
			}
			deleteNode.left.parent = deleteNode.parent;
		} 
		// If deleteNode is an internal Node
		else {
			RBTNode inOrderSuccessor = inOrderSuccessor(deleteNode.right);
			deleteNodeColour = inOrderSuccessor.colour;
			replacementNode = inOrderSuccessor.right;
			if (inOrderSuccessor.parent == deleteNode) {
				replacementNode.parent = inOrderSuccessor;
			} 
			else {
				if (inOrderSuccessor.parent == null) {
					root = inOrderSuccessor.right;
				} 
				else if (inOrderSuccessor == inOrderSuccessor.parent.left){
					inOrderSuccessor.parent.left = inOrderSuccessor.right;
				} 
				else {
					inOrderSuccessor.parent.right = inOrderSuccessor.right;
				}
				inOrderSuccessor.right.parent = inOrderSuccessor.parent;
				inOrderSuccessor.right = deleteNode.right;
				inOrderSuccessor.right.parent = inOrderSuccessor;
			}
			if (deleteNode.parent == null) {
				root = inOrderSuccessor;
			} else if (deleteNode == deleteNode.parent.left){
				deleteNode.parent.left = inOrderSuccessor;
			} 
			else {
				deleteNode.parent.right = inOrderSuccessor;
			}
			inOrderSuccessor.parent = deleteNode.parent;
			inOrderSuccessor.left = deleteNode.left;
			inOrderSuccessor.left.parent = inOrderSuccessor;
			inOrderSuccessor.colour = deleteNode.colour;
		}
      if (deleteNodeColour == RBTNode.black){
        fixRBTreeDelete(replacementNode);
      }
    }

    public void fixRBTreeDelete(RBTNode fNode){
		RBTNode siblingNode;
		while (fNode.colour != RBTNode.red && fNode != root) {
			if(fNode == fNode.parent.right) {
				siblingNode = fNode.parent.left;
				if (siblingNode.colour != RBTNode.black) {
					// case 3.RBTNode.red
					siblingNode.colour = RBTNode.black;
					fNode.parent.colour = RBTNode.red;
					rotateRight(fNode.parent);
					siblingNode = fNode.parent.left;
				}

				if (siblingNode.right.colour != RBTNode.red && siblingNode.left.colour != RBTNode.red) {
					// case 3.2
					siblingNode.colour = RBTNode.red;
					fNode = fNode.parent;
				} else {
					if (siblingNode.left.colour == RBTNode.black) {
						// case 3.3
						siblingNode.right.colour = RBTNode.black;
						siblingNode.colour = RBTNode.red;
						rotateLeft(siblingNode);
						siblingNode = fNode.parent.left;
					} 

					// case 3.4
					siblingNode.colour = fNode.parent.colour;
					fNode.parent.colour = RBTNode.black;
					siblingNode.left.colour = RBTNode.black;
					rotateRight(fNode.parent);
					fNode = root;
				}
			}

			// Mirror cases of above code
			else if (fNode == fNode.parent.left) {
				siblingNode = fNode.parent.right;
				if (siblingNode.colour != RBTNode.black) {
					// case 3.RBTNode.red
					siblingNode.colour = RBTNode.black;
					fNode.parent.colour = RBTNode.red;
					rotateLeft(fNode.parent);
					siblingNode = fNode.parent.right;
				}

				if (siblingNode.left.colour != RBTNode.red && siblingNode.right.colour != RBTNode.red) {
					// case 3.2
					siblingNode.colour = RBTNode.red;
					fNode = fNode.parent;
				} else {
					if (siblingNode.right.colour == RBTNode.black) {
						// case 3.3
						siblingNode.left.colour = RBTNode.black;
						siblingNode.colour = RBTNode.red;
						rotateRight(siblingNode);
						siblingNode = fNode.parent.right;
					} 

					// case 3.4
					siblingNode.colour = fNode.parent.colour;
					fNode.parent.colour = RBTNode.black;
					siblingNode.right.colour = RBTNode.black;
					rotateLeft(fNode.parent);
					fNode = root;
				}
			} 
		}
		fNode.colour = RBTNode.black;
    }

    public boolean checkRideNumber(int rideNumber){
      RBTNode currentNode = root;
      while(currentNode != RBT.externalNode){
        if(rideNumber == currentNode.rideNumber){
          return true;
        }
        else if(rideNumber < currentNode.rideNumber){
          currentNode = currentNode.left;
        }
        else{
          currentNode = currentNode.right;
        }
      }
      return false;
    }

    public RBTNode getNodeFromRideNumber(int rideNumber){
      RBTNode currentNode = root;
      while(currentNode != RBT.externalNode){
        if(rideNumber == currentNode.rideNumber){
          return currentNode;
        }
        else if(rideNumber < currentNode.rideNumber){
          currentNode = currentNode.left;
        }
        else{
          currentNode = currentNode.right;
        }
      }
      return null;
    }

    public static void main(String args[]){
        RBT r = new RBT();

        // r.insert(10, 1, 2);
        // r.insert(20,2,3);
        // r.insert(15,5,3);
        // r.insert(2,1,3);
        // r.insert(25,14,13);
        // r.insert(13,5,12);

        r.deleteRBTNode(20);
        prettyPrint(r.root);
   }
    
}
