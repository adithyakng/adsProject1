class RBT {

    RBTNode root = null;

	// External node is a node that is not a part of the tree
    static RBTNode externalNode = null;

	// constructor which creates an external node and also points the root to the external node
    RBT(){
      if(externalNode == null){
        externalNode = new RBTNode();
        root = externalNode;
      }
    }

	/*
	 * Method to insert newNode into RBT tree
	*/
    public void insert(RBTNode newNode){

		//Start from the root node and traverse the tree
        RBTNode currentNode = root;
        RBTNode currentParent = null;

		// Traverse the tree until you reach the external node
        while(currentNode != externalNode){
            currentParent = currentNode;
			// If the newNode has a rideNumber less than the currentNode, then traverse the left subtree
            if(currentNode.rideNumber > newNode.rideNumber){
                currentNode = currentNode.left;
            }
			// If the newNode has a rideNumber greater than the currentNode, then traverse the right subtree
            else if(currentNode.rideNumber < newNode.rideNumber){
                currentNode = currentNode.right;
            }
        }
		// If the currentParent is null, it means that the tree is empty and the newNode is the first node to be inserted
        if(currentParent == null){
			// Make the newNode as the root node
            root = newNode;
			// Make the colour of the root node as black
            root.colour = RBTNode.black;
            return;
        }
		// If the newNode has a rideNumber less than the currentParent, then make the newNode as the left child of the currentParent
        else if(currentParent.rideNumber > newNode.rideNumber){
            currentParent.left = newNode;
        }
		// If the newNode has a rideNumber greater than the currentParent, then make the newNode as the right child of the currentParent
        else{
            currentParent.right = newNode;
        }
		// Change the parent of the newNode to the currentParent
        newNode.parent = currentParent;
		// If the currentParent is the root node, then no changes are required to the tree
        if(newNode.parent.parent == null){
            return;
        }

        restoreRbtOnInsert(newNode);

    }

	/*
	 * Method to restore the RBT properties after insertion.
	 * Get the parent, grandparent and uncle of the node and check if the uncle is red or black
	 * X = relationship between parent and grandparent, Y = relationship between node and parent, Z = uncle
	 * if XYr, then change the colour of Y and Z to black and colour of X to red and move up two levels
	 * XYZ = LLb, then rotate right at Y and change the colour of Y to black and X & Z to red.(RRb is symmetric)
	 * XYZ = LRb, then rotate left at X and then rotate right at Y and change the colour of Y to black and X & Z to red.(RLb is symmetric)
	 */
    public void restoreRbtOnInsert(RBTNode node){

        RBTNode parent = node.parent;
        RBTNode grandfather = parent.parent;
        RBTNode uncle;

		// If the parent of newly inserted node is black, then no changes are required to the tree else we need to restore the RBT properties
        while(node.parent.colour == RBTNode.red){
            parent = node.parent;
            grandfather = (parent.parent != null) ? parent.parent : null;

			// Get the uncle of the node
            uncle = getUncleNode(parent);

            if(grandfather.right == parent){
				// RYr case. Change the colour of parent and uncle to black, colour grandparent to red and move up two levels
				if(uncle!=null && uncle.colour == RBTNode.red){
					uncle.colour = RBTNode.black;
					grandfather.colour = RBTNode.red;
					parent.colour = RBTNode.black;
					node = grandfather;
				}
				// RLb and RRb cases
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
				// LYr case. Change the colour of parent and uncle to black, colour grandparent to red and move up two levels
				if(uncle!=null && uncle.colour ==  RBTNode.red){
					uncle.colour = RBTNode.black;
					grandfather.colour = RBTNode.red;
					parent.colour = RBTNode.black;
					node = grandfather;
				}
				else{
					// LLb and LRb cases
					if(node == parent.right){
						node = parent;
						rotateLeft(node);
					}
					node.parent.colour = RBTNode.black;
					node.parent.parent.colour = RBTNode.red;
					rotateRight(node.parent.parent);
				}
            }

			// If the node is the root node, then change the colour of the root node to black and break the loop
            if(node == root){
              root.colour = RBTNode.black;
              break;
            }
        }
    }

	/*
	 * Method to rotate the node of the tree to the right
	 */
    public void rotateRight(RBTNode node) {
		// Get the left child of the node and make it as the right child of the parent node
     	RBTNode leftChildNode = node.left;
		node.left = leftChildNode.right;

		// If the leftChoiceNode has a right child, then change the parent of the right child to the node
		if (leftChildNode.right != RBT.externalNode) {
			leftChildNode.right.parent = node;
		}
		// Change the parent of the leftChildNode to the parent of the node
		leftChildNode.parent = node.parent;

		// If the parent of the node is null, then make the leftChildNode as the root node
		if (node.parent == null) {
			root = leftChildNode;
		} 
		// If the node is the right child of the parent, then make the leftChildNode as the right child of the parent
		else if (node == node.parent.right) {
			node.parent.right = leftChildNode;
		} 
		// If the node is the left child of the parent, then make the leftChildNode as the left child of the parent
		else {
			node.parent.left = leftChildNode;
		}
		// Make the node as the right child of the leftChildNode
		leftChildNode.right = node;
		// Change the parent of the node to the leftChildNode
		node.parent = leftChildNode;
    }

	/*
	 * Method to rotate the node of the tree to the left
	*/
    public void rotateLeft(RBTNode node) {
		// Get the right child of the node and make it as the left child of the parent node
		RBTNode rightChildNode = node.right;
		node.right = rightChildNode.left;

		// If the rightChildNode has a left child, then change the parent of the left child to the node
		if (rightChildNode.left != RBT.externalNode) {
			rightChildNode.left.parent = node;
		}
		// Change the parent of the rightChildNode to the parent of the node
		rightChildNode.parent = node.parent;

		// If the parent of the node is null, then make the rightChildNode as the root node
		if (node.parent == null) {
			root = rightChildNode;
		} 
		// If the node is the left child of the parent, then make the rightChildNode as the left child of the parent
		else if (node == node.parent.left) {
			node.parent.left = rightChildNode;
		} 
		// If the node is the right child of the parent, then make the rightChildNode as the right child of the parent
		else {
			node.parent.right = rightChildNode;
		}
		// Make the node as the left child of the rightChildNode
		rightChildNode.left = node;
		// Change the parent of the node to the rightChildNode
		node.parent = rightChildNode;
    }


	/*
	 * Method to get the uncle node of a given node
	 */
    public RBTNode getUncleNode(RBTNode parent){
        
		// If the given parent node is left child of it's parent, then return the right child of the given parent node
        if(parent.parent.left == parent){
            return parent.parent.right;
        }
		// If the given parent node is right child of it's parent, then return the left child of the given parent node
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

	/*
	 * Method to get the in-order successor of a given node
	 * In-order successor is the lowest element in the right subtree of the given node
	 */
    public RBTNode inOrderSuccessor(RBTNode node){
		// First go to the right child of the given node
		node = node.right;
		// Then go to the left child until the left child is an external node
        while(node.left != RBT.externalNode){
          node = node.left;
        }
        return node;
    }

    public static void prettyPrint(RBTNode root) {
      printHelper(root, "", true);
    }

	/*
	 * Method to delete a node from the RBTree
	 * The node to be deleted is first determined by the rideNumber
	 * If the node h
	 */
    public void deleteRBTNode(int rideNumber){
       
		RBTNode deleteNode = RBT.externalNode;

		// Start from the root node
		RBTNode currentNode = root;

		// Find the node to be deleted
		while (currentNode != RBT.externalNode){
			// If the rideNumber of the current node is equal to the rideNumber to be deleted, 
			// then mark the current node as the node to be deleted
			if (currentNode.rideNumber == rideNumber) {
				deleteNode = currentNode;
			}
			// If the rideNumber of the current node is less than the rideNumber to be deleted,
			// then go to the right child of the current node
			if (currentNode.rideNumber < rideNumber) {
				currentNode = currentNode.right;
			} 
			// If the rideNumber of the current node is greater than the rideNumber to be deleted,
			// then go to the left child of the current node
			else {
				currentNode = currentNode.left;
			}
		}

		// Node not present in the RBTree, ignore the delete operation
		if (deleteNode == RBT.externalNode) {
			return;
		} 
		
		// Get the colour of the node to be deleted
		int deleteNodeColour = deleteNode.colour;
      	RBTNode replacementNode;
		// If there is no left child then replace the node with the right child
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
		// If there is no right child then replace the node with the left child
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
		/* If there are both left and right child, then replace the node with the in-order successor and 
		* delete the in-order successor
		*/
		else {
			RBTNode inOrderSuccessor = inOrderSuccessor(deleteNode);
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
	// If the node to be deleted is black, then RBT properties might be violated, so restore the properties
      if (deleteNodeColour == RBTNode.black){
        restoreRbtOnDelete(replacementNode);
      }
    }

	/*
	 * Method to restore the RBT properties after a node is deleted, 
	 * the node which violates the RBT properties is passed as a parameter
	 */
    public void restoreRbtOnDelete(RBTNode fNode){
		
		// Get the sibling of the node which violates the RBT properties
		RBTNode siblingNode;

		/*
		While the node which violates the RBT properties is not the root and is black we continue to restore the properties 
		*/ 
		while (fNode.colour != RBTNode.red && root != fNode) {
			// If the node which violates the RBT properties is the right child of its parent
			if(fNode == fNode.parent.right) {
				// siblingNode will be the left child of the parent
				siblingNode = fNode.parent.left;

				// If the sibling is red, then change the colour of the sibling and parent and rotate 
				// the parent to the right
				if (siblingNode.colour != RBTNode.black) {
					siblingNode.colour = RBTNode.black;
					fNode.parent.colour = RBTNode.red;
					rotateRight(fNode.parent);
					siblingNode = fNode.parent.left;
				}

				// If both the children of the sibling are black, then change the colour of the sibling to red
				if (siblingNode.right.colour != RBTNode.red && siblingNode.left.colour != RBTNode.red) {
					siblingNode.colour = RBTNode.red;
					fNode = fNode.parent;
				} 
				else {
					// If the left child of the sibling is black, then change the colour of the right child of the sibling to black
					// and the colour of the sibling to red and rotate the sibling to the left
					if (siblingNode.left.colour == RBTNode.black) {
						siblingNode.right.colour = RBTNode.black;
						siblingNode.colour = RBTNode.red;
						rotateLeft(siblingNode);
						siblingNode = fNode.parent.left;
					} 

					// Change the colour of the sibling to the colour of the parent, change the colour of the parent to black
					// and the colour of the left child of the sibling to black and rotate the parent to the right
					siblingNode.colour = fNode.parent.colour;
					fNode.parent.colour = RBTNode.black;
					siblingNode.left.colour = RBTNode.black;
					rotateRight(fNode.parent);
					fNode = root;
				}
			}

			// If the node which violates the RBT properties is the left child of its parent
			// These are mirror image cases of the above cases
			else if (fNode == fNode.parent.left) {
				// siblingNode will be the right child of the parent
				siblingNode = fNode.parent.right;

				// If the sibling is red, then change the colour of the sibling and parent and rotate the parent to the left
				if (siblingNode.colour != RBTNode.black) {
					siblingNode.colour = RBTNode.black;
					fNode.parent.colour = RBTNode.red;
					rotateLeft(fNode.parent);
					siblingNode = fNode.parent.right;
				}

				// If both the children of the sibling are black, then change the colour of the sibling to red
				if (siblingNode.left.colour != RBTNode.red && siblingNode.right.colour != RBTNode.red) {
					siblingNode.colour = RBTNode.red;
					fNode = fNode.parent;
				} else {
					// If the right child of the sibling is black, then change the colour of the left child of the sibling to black
					// and the colour of the sibling to red and rotate the sibling to the right
					if (siblingNode.right.colour == RBTNode.black) {
						siblingNode.left.colour = RBTNode.black;
						siblingNode.colour = RBTNode.red;
						rotateRight(siblingNode);
						siblingNode = fNode.parent.right;
					}

					// Change the colour of the sibling to the colour of the parent, change the colour of the parent to black
					// and the colour of the right child of the sibling to black and rotate the parent to the left
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

	/*
	 * Method to check if a node with the given ride number exists in the RBT
	 * Returns true if the node exists, false otherwise
	 */
    public boolean checkRideNumber(int rideNumber){
	
	  // Start from the root node
      RBTNode currentNode = root;
	  // While the current node is not the external node
      while(currentNode != RBT.externalNode){

		// If the ride number of the current node is equal to the required ride number, return true
        if(rideNumber == currentNode.rideNumber){
          return true;
        }
		// If the ride number of the current node is greater than the required ride number, move to the left child
        else if(rideNumber < currentNode.rideNumber){
          currentNode = currentNode.left;
        }
		// If the ride number of the current node is less than the required ride number, move to the right child
        else{
          currentNode = currentNode.right;
        }
      }
	  // If the ride number is not found, return false
      return false;
    }



	/*
	 * Method to get the node with the given ride number
	 * Returns the node if it exists, null otherwise
	 */
    public RBTNode getNodeFromRideNumber(int rideNumber){
	// Start from the root node
      RBTNode currentNode = root;
	  // While the current node is not the external node
      while(currentNode != RBT.externalNode){
		// If the ride number of the current node is equal to the required ride number, return the current node
        if(rideNumber == currentNode.rideNumber){
          return currentNode;
        }
		// If the ride number of the current node is greater than the required ride number, move to the left child
        else if(rideNumber < currentNode.rideNumber){
          currentNode = currentNode.left;
        }
		// If the ride number of the current node is less than the required ride number, move to the right child
        else{
          currentNode = currentNode.right;
        }
      }
	  // If the ride number is not found, return null
      return null;
    }

    
}
