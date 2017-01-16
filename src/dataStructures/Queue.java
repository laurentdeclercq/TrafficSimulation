/*
 * Queue.java
 * Laurent Declercq
 * 25/10/16
 */

package dataStructures;

public class Queue<T extends Comparable<T>> {
	private CircularVector<T> data;
	
	/*
	 * CONSTRUCTORS
	 */
	public Queue(){
		data = new CircularVector<T>(0);
	}
	
	public Queue(int capacity){
		data = new CircularVector<T>(capacity);
	}
	/*
	 * METHODS
	 */
	
	/**
	 * This compareTo() method is bad but shows that it is impossible to compare 2 Queues
	 * @return 0
	 */
	public int compareTo(CircularVector<T> cv2){
		return 0; 	// impossible to compare 2 queues, this implementation is not good
	}
	
	/*
	 * Watch out, the direction you choose for push & pop determines the direction of the list
	 * In case of addFirst and removeLast, better reverse the list
	 * to keep the logical order (last element added presented on the RIGHT)
	 */
	public String toString(){ 
		return data.toString();
	}
	
	public void push(T o){
		data.addLast(o);
	}
	
	public T pop(){
		T item = data.getFirst();
		data.removeFirst();
		return item;
	}
	
	public T top(){
		return data.getFirst();
	}
	
	public int size(){
		return data.size();
	}
	
	public boolean isEmpty(){
		return data.isEmpty();
	}	
	
	public void print(){
		data.print();
	}

} // end class Queue
