/**
 * Stack.java
 * @version 25/10/16
 * @author Laurent Declercq
 */
package dataStructures;

/**
 * @version 1/11/16
 * @author Laurent Declercq
 *
 * @param <T> defines which data type is in the Stack upon creation of an instance
 */
public class Stack<T extends Comparable<T> > {
	
	// As we push, the linked list grows from the beginning. 
	// When we pop, the list shrinks from the left
	private CircularVector<T> data; 
	
	/*
	 *  CONSTRUCTOR OF STACK
	 */
	
	public Stack(){ 
		data = new CircularVector<T>();
	}
	
	/*
	 * METHODS OF STACK
	 */
	
	
	/**
	 * method toString():
	 * This returns the stack as String, with the top of the stack on the RIGHT of the String
	 */
	public String toString(){
		return data.toString();
		
	}

	/**
	 * adds 1 element to the top of the stack (right)
	 * @param o is the element to be added
	 */
	public void push(T o) {
		data.addLast(o);	// with a LinkedList use addFirst() : time complexity = O(1)
	}
	
	/**
	 * @return  and remove the last element LIFO (top of stack = right)
	 */
	public T pop() {
		T item = data.getLast(); // with a LinkedList use getFirst() : time complexity = O(1)
		data.removeLast();
		return item;
	}
	
	/**
	 * @return the last element LIFO (top of stack = right)
	 */
	public T top() {
		return data.getLast(); // with a LinkedList use getFirst() : time complexity = O(1)
	}
	
	/**
	 * @return the number of elements in the stack
	 */
	public int size() {
		return data.size();

	}
	
	/**
	 * @return TRUE when no elements found
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}
	
	/**
	 * print the elements in the stack
	 */
	public void print(){
		data.print();
	}
	
} // end class Stack
