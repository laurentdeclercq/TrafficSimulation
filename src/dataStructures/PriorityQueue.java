/**
 * PriorityQueueSLL.java
 * @version 28/11/16
 * @author Laurent Declercq
 */
package dataStructures;

// This priority queue implementation is based on a SORTED LINKED LIST!
public class PriorityQueue<T1 extends Comparable<T1> > {
	
	private class PriorityPair<K extends Comparable<K>> implements Comparable<PriorityPair<K>>{
		
		private K element;
		private int priority;
		/*
		 * CONSTRUCTOR OF PRIORITY_PAIR
		 */
		public PriorityPair(K element, int priority){
			this.element=  element;
			this.priority = priority;
		}
		/*
		 * METHODS OF PRIORITY_PAIR
		 */
		
		public String toString(){
			String txt = "(" + element + ", P" + priority + ")";
			return txt;
		}
		/**
		 * This version of compareTo() on a PriorityPair calls 
		 * the compareTo() method for the priority value
		 * @param o is a PriorityPair
		 * @return 0 when equal priority AND element is equal, >0 when this.priority > o.priority , <0 when vice versa
		 * Remember: LOWER priority value is more important and thus popped FIRST
		 */
		@Override
		public int compareTo(PriorityQueue<T1>.PriorityPair<K> o) {
			PriorityPair<K> p2 = (PriorityPair<K>)o;
			if(priority-p2.priority == 0)
				if(element.compareTo(p2.element) != 0)
					return 1; // the existing element always has priority on a new element
				else
					return 0; // both elements inside the pairs are equal
			else
				return priority - p2.priority;
		}
		
		
	} // END CLASS PRIORITY_PAIR
	
	private BinaryTree< PriorityPair<T1> > data;
	
	/*
	 * CONSTRUCTOR OF PRIORITY_QUEUE
	 */
	public PriorityQueue(){
		
		data = new BinaryTree<PriorityPair<T1>>();	// create a binary tree of priority pairs
	
	}
	/*
	 * METHODS OF PRIORITY_QUEUE
	 */
	
	public String toString(){
		return data.toString();
	}
	
	public boolean isEmpty(){
		return data.isEmpty();
	}
	
	public int size(){
		return data.size();
	}
	
	// lowest priority-value will be situated at the head
	public void push(T1 o, int priority){
		PriorityPair<T1> d = new PriorityPair<T1>(o, priority);
		data.insert(d);
		
		
		// make a pair of o and priority
		// add this pair to the binary tree
	}
	
	public T1 pop(){
		if(! data.isEmpty()){
			PriorityPair<T1> pair = (PriorityPair<T1>)data.getSmallest();
			data.removeSmallest();
			return pair.element;
		}
		else 
			return null;

	}
	
	public T1 top(){
		PriorityPair<T1> pair = (PriorityPair<T1>)data.getSmallest();
		return pair.element;
	}
	
}