package dataStructures;

import treeActions.TreeAction;
import treeActions.TreeSizeAction;

public class BinaryTree<T1 extends Comparable<T1>> {
	
	/**
	 * The class for implementing a node in the tree.
	 * Contains a value, a pointer to the left child and a pointer to the right child
	 * @author laurentdeclercq
	 *
	 * @param <T2>
	 */
	public class TreeNode<T2 extends Comparable<T2>> implements Comparable<TreeNode<T2>>{
		
		private T2 value;
		private TreeNode<T2> leftNode;
		private TreeNode<T2> rightNode;
		private TreeNode<T2> parentNode;
		
		/**
		 * TreeNode CONSTRUCTOR 1
		 * @param v
		 */
		public TreeNode(T2 v){
			value = v;
			leftNode = null;
			rightNode = null;
			parentNode = null;
		}
		
		/**
		 * TreeNode CONSTRUCTOR 2
		 * @param v
		 * @param left
		 * @param right
		 */
		public TreeNode(T2 v, TreeNode<T2> left, TreeNode<T2> right){
			value = v;
			leftNode = left;
			rightNode = right;
			parentNode = null;
		}
		
		public TreeNode<T2> getLeftTree(){
			return leftNode;
		}
		
		public TreeNode<T2> getRightTree(){
			return rightNode;
		}
		
		public T2 getValue(){
			return value;
		}
		
		public int compareTo(TreeNode<T2> o){
			return value.compareTo(o.value);
		}
		
		public String toString(){
			return value.toString();
		}
	}// end class TreeNode

	
	private TreeNode<T1> root;
	
	/**
	 * TREE CONSTRUCTOR
	 */
	public BinaryTree(){
		root = null;

	}
	
	public String toString(){
		Stack<TreeNode<T1>> t = new Stack<TreeNode<T1>>(); 
		String str = "[";
		TreeNode<T1> current = root; 
		
		while(!t.isEmpty() || current != null){ 
			
			if(current != null) { 
				t.push(current); 
				current = current.getLeftTree(); 
			} 
			else { 
				TreeNode<T1> n = t.pop(); 
				str += n.toString() + " , ";
				current = n.getRightTree(); 
			} 
		}
		str += "]";
		return str;
	}
	
	public boolean isEmpty(){
		return root == null;
	}
	
	public int size(){
		TreeSizeAction sizeAction = new TreeSizeAction();
		traverseDFS(sizeAction);
		return sizeAction.getCount();
	}
	
	/* inserts an element in the binary search tree */
	public void insert(T1 element){
		insertAtNode(element, root, null);

	}
	
	/**
	 * We traverse the tree
	 * @param element
	 * @param current : holds the pointer to the TreeNode
	 * @param parent : holds the pointer to the parent of the current TreeNode
	 */
	private void insertAtNode(T1 element, TreeNode<T1> current, TreeNode<T1> parent){
		// if the node we check, is empty
		if(current == null){
			TreeNode<T1> newNode = new TreeNode<T1>(element);
			
			// if the current node is empty, but we have a parent
			// make the parent point, either left or right, to the new element
			if(parent != null){
				if (element.compareTo(parent.value) < 0)
					parent.leftNode = newNode;
				else
					parent.rightNode = newNode;
			newNode.parentNode = parent;
			}
			
			// current is null, and has no parent --> empty tree
			else root = newNode;
		
		}
		else if(element.compareTo(current.value) == 0){
			// if the element is already in the tree, what do we do?
			// at this point, we don't care
		}
		
		// if the element is smaller than current, go left
		else if(element.compareTo(current.value) < 0)
			insertAtNode(element, current.getLeftTree(), current);
		// if the element is bigger than current, go right
		else
			insertAtNode(element, current.getRightTree(), current);
	}
	
	/* checks whether an element is present in the tree , returns the element-value if it is found */
	public T1 find(T1 element){
		return findNode(element, root);
	}
	
	private T1 findNode(T1 element, TreeNode<T1> current){
		if(current == null) return null;
		else if (element.compareTo(current.value) == 0)
			return current.value;
		else if (element.compareTo(current.value) < 0)
			return findNode(element, current.getLeftTree());
		else
			return findNode(element, current.getRightTree());
	}
	
	public void print(){
		traverseDFS(new TreeAction() {
			public void run(TreeNode n){
				System.out.println(n.getValue());
			}
		});
	}
	
	public void printSorted(){
		traverseSorted(new TreeAction() {
			public void run(TreeNode n){
				System.out.println(n.getValue());
			}
		});
	}
	
	
	// Traverses the tree using BREADTH-FIRST-SEARCH
	private void traverseBFS(TreeAction action){
		Queue<TreeNode<T1>> t = new Queue<TreeNode<T1>>();
		t.push(root);
		while (!t.isEmpty()){
			TreeNode<T1> n = t.pop();
			action.run(n);				// here the TreeAction class is called
			if(n.getLeftTree() != null)
				t.push(n.getLeftTree());
			if(n.getRightTree() != null)
				t.push(n.getRightTree());
		}
	}
	
	// Traverses the tree using DEPTH-FIRST-SEARCH
	public void traverseDFS(TreeAction action){
		Stack<TreeNode<T1>> t = new Stack<TreeNode<T1>>();
		t.push(root);
		while (!t.isEmpty()){
			TreeNode<T1> n = t.pop();
			action.run(n);				// here the TreeAction class is called
			if(n != null){
				if(n.getRightTree() != null)
					t.push(n.getRightTree());
				if(n.getLeftTree() != null)
					t.push(n.getLeftTree());
			}

		}
	}

	
	public void traverseSorted(TreeAction action){

		Stack<TreeNode<T1>> t = new Stack<TreeNode<T1>>(); 
		TreeNode<T1> current = root; 
		
		while(!t.isEmpty() || current != null){ 
			
			if(current != null) { 
				t.push(current); 
				current = current.getLeftTree(); 
			} 
			else { 
				TreeNode<T1> n = t.pop(); 
				action.run(n); 
				current = n.getRightTree(); 
			} 
		}
	}
		
	public void swapTree(){
		traverseBFS(new TreeAction(){		// does not matter if you use BFS or DFS
			public void run(TreeNode n){
				TreeNode<T1> ptr = n.leftNode;
				n.leftNode = n.rightNode;
				n.rightNode = ptr;
			}
		});
	}
	
	public int treeDepth(){
		return currentTreeDepth(root);
	}
	
	private static <T extends Comparable<T>> T findMax(T...values){
		if(values.length <= 0)
			throw new IllegalArgumentException();
		
		T m = values[0];
		for (int i = 1; i<values.length; ++i){
			if (values[i].compareTo(m) > 0)
				m = values[i];
		}
		return m;
	}
	
	private int currentTreeDepth(TreeNode<T1> current){
		int DepthCount = 0;
		if(current == null) ;
		else {
			DepthCount += 1 + findMax(currentTreeDepth(current.getLeftTree()), currentTreeDepth(current.getRightTree())) ;
		}
		
		return DepthCount;

	}
	
	public T1 getBiggest(){
		TreeNode<T1> current = root;
		while(current.getRightTree() != null){
			current = current.getRightTree();
			
		}
		return current.getValue();
	}
	
	public T1 getSmallest(){
		return minNode(root).getValue();
	}
	
	private TreeNode<T1> minNode(TreeNode<T1> current){
		if(current == null)
			return null;
		else if(current.getLeftTree() == null)
			return current;
		else
			return minNode(current.getLeftTree());
	}
	
	public void remove(T1 element){
			removeNode(element, root);
		}
	
	private void removeNode(T1 element, TreeNode<T1> current){
		if(current == null)
			return;
		
		else if (element.compareTo(current.value) == 0){ 
			if(current.leftNode == null)
				transplant(current, current.rightNode);			// transplant = put at the current position, the stuff that is at ...
			else if (current.rightNode == null)
				transplant(current, current.leftNode);
			else{
				TreeNode<T1> y = minNode(current.rightNode);	// minNode() = find the smallest element
				if(y.parentNode != current){
					transplant(y,y.rightNode);			// move the right childNode of the minNode up 1 level (replace minNode)
					y.rightNode = current.rightNode;	// make minNode point to the rightNode of current
					y.rightNode.parentNode = y;			// make the rightNode of current point to minNode as parent instead of the current
				}
				transplant(current,y);
				y.leftNode = current.leftNode;
				y.leftNode.parentNode = y;
			}
		}
		else if(element.compareTo(current.value) < 0)
			removeNode(element, current.getLeftTree());
		else
			removeNode(element, current.getRightTree());
	}
	
	/**
	 * This replaces a subtree with another subtree
	 * @param oldNode
	 * @param newNode
	 */
	private void transplant(TreeNode<T1> oldNode, TreeNode<T1> newNode){
		if(oldNode.parentNode == null)
			root = newNode; 	// if the oldNode IS the root, then the newNode should now become the root
		else if(oldNode.parentNode.getLeftTree() == oldNode) {	// if the oldNode was the left child of its parent
			oldNode.parentNode.leftNode = newNode; 	// make the parent point left to the newNode
		}
		else
			oldNode.parentNode.rightNode = newNode;	// else, make the parent point right to the newNode
		if(newNode != null)
			newNode.parentNode = oldNode.parentNode;	// newNode should point to the parent of the oldNode
	}
	
	public void removeSmallest(){
		remove(getSmallest());
	}
	
	// calculates a to the power b, only works for positive b
	long pow(int a, int b){
		if(b == 0)
			return 1;
		if(b == 1)
			return a;
		if(b%2 == 0)
			return pow(a*a, b/2);
		else
			return a * pow(a*a,b/2);
		
	}
	
	public T1 median(){
		
			/*
			traverseSorted(new TreeAction() {
				int visited = 0;
				int finished;
				T1 median;
				if(count%2 == 0)
					finished = count/2 + 1;
				else
					finished = count/2;
				public void run(TreeNode n){

						if(visited < finished){
							visited++;
						}
						if(visited = finished){
							median = n.value;
							break;
						}
				}

					System.out.println(n.getValue());
			});
			retur
			*/
			return null;
		
	} // end median()
	
} // end CLASS
