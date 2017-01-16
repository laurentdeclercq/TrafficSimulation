package treeActions;

import dataStructures.Queue;

public class TreeCoordinates<T1 extends Comparable<T1>> {
	
	/**
	 * The class for implementing a node in the tree.
	 * Contains a value, a pointer to the left child and a pointer to the right child
	 * @author laurentdeclercq
	 *
	 * @param <T2>
	 */
	protected class TreeNode<T2 extends Comparable<T2>> implements Comparable<TreeNode<T2>>{
		
		private T2 valueX;
		private T2 valueY;
		private TreeNode<T2> child1;
		private TreeNode<T2> child2;
		private TreeNode<T2> child3;
		private TreeNode<T2> child4;
		
		/**
		 * TreeNode CONSTRUCTOR 1
		 * @param v
		 */
		public TreeNode(T2 x, T2 y){
			valueX = x;
			valueY = y;
			child1 = null;
			child2 = null;
			child3 = null;
			child4 = null;
		}
		
		/**
		 * TreeNode CONSTRUCTOR 2
		 * @param v
		 * @param left
		 * @param right
		 */
		public TreeNode(T2 x, T2 y, TreeNode<T2> c1, TreeNode<T2> c2, TreeNode<T2> c3, TreeNode<T2> c4){
			valueX = x;
			valueY = y;
			child1 = c1;
			child2 = c2;
			child3 = c3;
			child4 = c4;
		}
		
		public String toString(){
				return "(" + valueX.toString() + " , " + valueY.toString() + ")";
			
		}
		
		public TreeNode<T2> getSmaller(){		// case 1
			return child1;
		}
		
		public TreeNode<T2> getBiggerY(){		// case 2
			return child2;
		}
		
		public TreeNode<T2> getBiggerX(){		// case 3
			return child3;
		}
		
		public TreeNode<T2> getBigger(){		// case 4
			return child4;
		}
		
		public T2 getX(){
			return valueX;
		}
		
		public T2 getY(){
			return valueY;
		}
		
		public int compareNodes(TreeNode<T2> o){
			if(valueX.compareTo(o.valueX) == 0 && valueY.compareTo(o.valueY) == 0)
				return 0;
			else if (valueX.compareTo(o.valueX) <= 0){
				if(valueY.compareTo(o.valueY) <= 0)
					return 1;
				else
					return 2;
			}
			else if(valueY.compareTo(o.valueY) <= 0)
				return 3;
			else
				return 4;
		}
		
		public int compareTo(TreeNode<T2> o){
			return 0;
		}
	}// end class TreeNode

	
	private TreeNode<T1> root;
	
	/**
	 * TREE CONSTRUCTOR
	 */
	public TreeCoordinates(){
		root = null;
	}
	
	/* inserts an element in the binary search tree */
	public void insert(T1 valueX, T1 valueY){
		TreeNode<T1> newNode = new TreeNode<T1>(valueX, valueY);
		insertAtNode(newNode, root, null);
		
		
		System.out.println("---> Inserted following: " + newNode.toString());
		System.out.println("---> Root is :" + root.toString());
	}
	
	/**
	 * We traverse the tree
	 * @param newNode
	 * @param current : holds the pointer to the TreeNode
	 * @param parent : holds the pointer to the parent of the current TreeNode
	 */
	private void insertAtNode(TreeNode<T1> newNode, TreeNode<T1> current, TreeNode<T1> parent){
		// if the node we check, is empty
		if(current == null){
			System.out.println("Trying to insert " + newNode.toString() +" --> Current is null");
			// if the current node is empty, but we have a parent
			// make the parent point, either to c1, c2, c3 or c4, to the new element
			if(parent != null){
				System.out.println("Current has parent: " + parent.toString() );
				switch(newNode.compareNodes(parent)){
				case 0:
					System.out.println("Case 0 : NewNode " + newNode.toString() + " ends up in child1." );
					parent.child1 = newNode;
				case 1:
					System.out.println("Case 1 : NewNode " + newNode.toString() + " ends up in child1." );
					parent.child1 = newNode;
				case 2:
					System.out.println("Case 2 : NewNode " + newNode.toString() + " ends up in child2." );
					parent.child2 = newNode;
				case 3:
					System.out.println("Case 3 : NewNode " + newNode.toString() + " ends up in child3." );
					parent.child3 = newNode;
				case 4:
					System.out.println("Case 4 : NewNode " + newNode.toString() + " ends up in child4." );
					parent.child4 = newNode;
				}
		
			}
			
			// current is null, and has no parent --> empty tree
			else root = newNode;
		}
		
		else{
			System.out.println("Trying to insert " + newNode.toString() +" --> Current is NOT null");
			switch(newNode.compareNodes(current)){
			case 0:
				System.out.println("Case 0 with current: " + current.toString());
				insertAtNode(newNode, current.getSmaller(), current);
			case 1:
				System.out.println("Case 1 with current: " + current.toString());
				insertAtNode(newNode, current.getSmaller(), current);
			case 2:
				System.out.println("Case 2 with current: " + current.toString());
				insertAtNode(newNode, current.getBiggerY(), current);
			case 3:
				System.out.println("Case 3 with current: " + current.toString());
				insertAtNode(newNode, current.getBiggerX(), current);
			case 4:
				System.out.println("Case 4 with current: " + current.toString());
				insertAtNode(newNode, current.getBigger(), current);
			}
		}
	}
	
	/* checks whether an element is present in the tree , True or False */
	public boolean find(T1 valueX, T1 valueY){
		TreeNode<T1> testNode = new TreeNode<T1>(valueX,valueY);
		return findNode(testNode, root);
	}
	
	private boolean findNode(TreeNode<T1> testNode, TreeNode<T1> current){
		if(current == null) return false;
		else{
			switch(testNode.compareNodes(current)){
			case 0:
				return true;
			case 1:
				return findNode(testNode, current.getSmaller());
			case 2:
				return findNode(testNode, current.getBiggerY());
			case 3:
				return findNode(testNode, current.getBiggerX());
			case 4:
				return findNode(testNode, current.getBigger());
			default:
				return false;
			}
		}
	}
	
	public void print(){
		traverse(new TreeActionWCoordinates() {
			public void run(TreeNode n){
				System.out.println(n);
			}
		});
	}
	/**
	 * I GET NULLPOINTER_EXCEPTION : after debugging i see that the root is still not containing anything
	 * how is this possible?!
	 * 
	 */
	
	// BREADTH FIRST SEARCH
	public void traverse(TreeActionWCoordinates action){
		Queue<TreeNode<T1>> t = new Queue<TreeNode<T1>>();
		t.push(root);
		while (!t.isEmpty()){
			TreeNode<T1> n = t.pop();
			action.run(n);				// here the TreeAction class is called
			if(n.getSmaller() != null)
				t.push(n.getSmaller());
			if(n.getBiggerY() != null)
				t.push(n.getBiggerY());
			if(n.getBiggerX() != null)
				t.push(n.getBiggerX());
			if(n.getBigger() != null)
				t.push(n.getBigger());
		}
	}
	
	private static <T extends Comparable<T>> T findMin(T...values){
		if(values.length <= 0)
			throw new IllegalArgumentException();
		
		T m = values[0];
		for (int i = 1; i<values.length; ++i){
			if (values[i].compareTo(m) < 0)
				m = values[i];
		}
		return m;
	}
	
	
	public TreeNode<T1> findSmallestX(){
		return findCurrentSmallestX(root);
	}
	
	public TreeNode<T1> findCurrentSmallestX(TreeNode<T1> current){
		if(current == null)
			return null;
		else if(current.child1 == null && current.child2 == null)
			return current;
		else if(current.child1 != null && current.child2 != null)
			return findMin(current, findCurrentSmallestX(current.child1), findCurrentSmallestX(current.child2));
		else if(current.child1 != null)
			return findMin(current, findCurrentSmallestX(current.child1));
		else
			return findMin(current, findCurrentSmallestX(current.child2));
	}

	
}
