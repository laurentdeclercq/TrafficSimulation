
/**
 * LinkedList.java
 * @version 20/10/16
 * @author Laurent Declercq
 */
package dataStructures;

/**
 * @version 20/10/16
 * @author Laurent Declercq
 *
 * @param <T1> defines which data type is in the LinkedList upon creation of an instance
 */
public class LinkedList<T1 extends Comparable<T1>> {

	/**
	 * @version 20/10/16
	 * @author Laurent Declercq
	 *
	 * @param <T2> defines which data type is in the ListElement upon creation of an instance
	 */
	private class ListElement<T2 extends Comparable<T2>> {
		
		/** el1 is the object that is of importance inside the ListElement */
		private T2 el1; 
		
		/** el2 is merely a pointer to another ListElement, or to null */
		private ListElement<T2> el2;
		
		/*
		 * CONSTRUCTORS FOR CLASS LISTELEMENT -----------------------------------------------
		 */
		
		public ListElement(T2 el, ListElement<T2> nextElement) {
			el1 = el;
			el2 = nextElement; 
		}
		
		/**
		 * Other constructor of ListElement Class.
		 * It calls the constructor above providing el and null ass nextElement.
		 * This means that it is the last element, with no pointer.
		 */
		public ListElement(T2 el) { 
			this(el,null); 											
		}
		
		/*
		 * METHODS FOR CLASS LISTELEMENT  -----------------------------------------------------
		 */
		
		/**
		 * @return the first part of the ListElement, this is the actual value (or object or ..)
		 */
		public T2 first() {
			return el1; 
		}
		
		/**
		 * @return the last part of the ListElement, this is a pointer to the next ListElement
		 */
		public ListElement<T2> rest() {
			return el2; 
		}
		
		/**
		 * Sets the value of the first part of the ListElement
		 * @param value (can be anything depending on T2)
		 */
		public void setFirst(T2 value) {
			el1 = value; 
		}
		
		/**
		 * Sets the value of the last part of the ListElement
		 * @param value (can be a ListElement or null)
		 */
		public void setRest(ListElement<T2> value) {
			el2 = value; 
		}

		/**
		 * return the ListElement as a String
		 */
		public String toString() {
			return "ListElement [el1=" + el1 + ", el2=" + el2 + "]";
		}
		
		public int compareTo(Object o){
			ListElement<T2> p2 = (ListElement<T2>)o;
			return this.first().compareTo(p2.first());		// compare the listElements based on the value of el1
	}
		
		
	} // end class ListElement
	
	/** head is the ListElement at the beginning of the LinkedList */
	private ListElement<T1> head;
	private int count;
	
	
	/*
	 * CONSTRUCTORS FOR CLASS LINKEDLIST -----------------------------------------------
	 */
	
	public LinkedList() { 	
		head = null; 
	}
	
	/*
	 * METHODS FOR CLASS LINKEDLIST -----------------------------------------------------
	 */
	
	/**
	 * @return the list as a string, first element left
	 */
	public String toString(){
		String listStr = "("; 
		ListElement<T1> d = head; 
		while(d != null){
			listStr += d.first().toString() + "\t";
			d = d.rest(); 
		}
		listStr += ")";
		return listStr;
	}
	
	/**
	 * This compareTo() method is bad but shows that it is impossible to compare 2 LinkedLists
	 * @return 0
	 */
	public int compareTo(LinkedList<T1> l2){
		return 0; 	// impossible to compare 2 lists, this implementation is not good
	}
	
	
	/**
	 * @param o is an element that is added at the beginning of the list
	 */
	public void addFirst(T1 o) { 
		head = new ListElement<T1>(o,head);
		count++;
	}
	
	
	/**
	 * @param o is an element that is added at the last spot of the list
	 */
	public void addLast(T1 o){ 	
		ListElement<T1> last = new ListElement<T1>(o);
		ListElement<T1> d = head;
		if (head == null)
			head = last;
		else{
			while (d.rest() != null)	// go to the last el and make it point to the new el
				d = d.rest();
			d.setRest(last);
		}
		count++;
	}
	
	
	/**
	 * removes the first element in the list
	 */
	public void removeFirst(){		
		if(head != null){				// only do something when list is not empty
			ListElement<T1> d = head;
			if (d.rest() == null)			// if head is the only element, replace it by NULL
				head = null;
			else						// else, make the 2nd el the new head (and drop the 1st el)
				head = d.rest();
			count--;
		}
		// else, do nothing
	}
	
	
	/**
	 * removes the last element in the list
	 */
	public void removeLast(){		
		if(head != null){				// only do something when list is not empty
			ListElement<T1> d = head;
			if (d.rest() == null)			// if head is the only element, replace it by NULL
				head = null;
			else{						// else, go to the second last el and make it point to NULL 
				while(d.rest().rest() != null)
					d = d.rest();
				d.setRest(null);
			}
			count--;
		}
		// else, do nothing
	}
	
	
	/**
	 * @return the first element in the list, or null when list empty
	 */
	public T1 getFirst(){ 		
		if (head != null)
			return head.first();
		else
			return null;
	}
	
	
	/**
	 * @return the last element in the list, or null when list empty
	 */
	public T1 getLast(){ 		
		if(head != null){
			ListElement<T1> d = head;
			while (d.rest() != null){	// go to the last element
					d = d.rest();
			}
			return d.first();
		}
		else
			return null;
		
	}
	
	
	/**
	 * @param n is the given index
	 * @return the element at the given index (starting from 0), or null when n not acceptable
	 */
	public T1 get(int n) {		
		if((n < count) && (n >= 0)){			// only do something when index is acceptable
			ListElement<T1> d = head; 
			while(n > 0){					// go to the element at index n
				d = d.rest();
				n--; 
			}
			return d.first(); 
		} // end if
		else
			return null;
	}
	
	
	/**
	 * replaces the element with index n, with the element
	 * @param n 	is the index at which the existing element needs to be replaced
	 * @param o 	is the element that replaces the existing element
	 */
	public void set(int n, T1 o){	
		if((n < count) && (n >= 0)){		// only do something if index is acceptable
			ListElement<T1> d = head;
			while(n > 0){					// go to the element at index n
				d = d.rest();
				n--;
			}
			d.setFirst(o);
		}
		// else do nothing
		
	}
	
	
	/**
	 * prints the list
	 */
	public void print() {			
		System.out.print("("); 
		ListElement<T1> d = head; 
		while(d != null){
			System.out.print(d.first().toString() + "\t");
			d = d.rest(); 
		}
		System.out.println(")"); 
	}
	
	
	/**
	 * @return 	the number of elements in the list
	 */
	public int size(){ 				
		return count;
	}
	
	
	/**
	 * @return TRUE if the list is empty
	 */
	public boolean isEmpty(){		
		return count == 0;
	}
	
	
	/**
	 * @param o is the element that the method looks for
	 * @return TRUE if the list contains o
	 */
	public boolean contains(T1 o){	
		ListElement<T1> d = head;
		while(d != null){
			if(d.first() == o)
				return true;
			d = d.rest();
		} 
		return false;
	}
	
	
	/**
	 * @return a list with every value of the original list repeated twice
	 */
	public LinkedList<T1> createDouble(){
		LinkedList<T1> list2 = new LinkedList<T1>();
		ListElement<T1> d = head;
	
		while(d != null){
			for (int i = 0;i < 2;i++){
				list2.addLast(d.first());
			}
			d = d.rest();
		}
		return list2;
	}
	

	
	/**
	 * reverses the elements of the list without creating a new copy
	 */
	public void reverse(){ 
		ListElement<T1> currentEl = head;
		ListElement<T1> previousEl = null;
		ListElement<T1> nextEl;
		if(head == null)
			; // do nothing
		else{
			nextEl = head.rest();
		
		
		while(nextEl != null){
			currentEl.setRest(previousEl);	// make it point in the other direction
			previousEl = currentEl;
			currentEl = nextEl;
			nextEl = currentEl.rest();
		}
		head = currentEl;					// still your head needs to point to the new start
		head.setRest(previousEl);		
		
		}
	}
	
	
	/**
	 * swaps every two consecutive elements of a list without creating a new one
	 */
	public void fropple(){ 

		if (count <=1) 			// for linkedlist with max 1 element do nothing
			;// do nothing
		else if (count <4){		// for linkedlist with max 4 elements:
			ListElement<T1> p1_first = head;				// use variables to point to the current 2 first elements
			ListElement<T1> p1_sec = p1_first.rest();		
			if(p1_sec.rest() != null)					// if there is a 3rd element
				p1_first.setRest(p1_sec.rest());
			else										// if not (so only 2 elements)
				p1_first.setRest(null); 				// current pair el1 needs to refer to null,
			p1_sec.setRest(p1_first); 					// current pair el2 needs to refer to current pair el1
			head = p1_sec;
		}
		else{							// for all other linkedlists
			ListElement<T1> p1_first = head;
			ListElement<T1> p1_sec = p1_first.rest();		// use variables to point to the current 4 first elements
			head = p1_sec;
			ListElement<T1> p2_first = p1_sec.rest();
			ListElement<T1> p2_sec = p2_first.rest();
			
			while(p2_sec.rest().rest() != null){		// while it is possible to do a full shift
				
				p1_first.setRest(p2_sec); 		// current pair el1 needs to refer to el2 of next pair,
				p1_sec.setRest(p1_first); 		// current pair el2 needs to refer to current pair el1
				
				p1_first = p2_first; 			// shift current pair to the next pair
				p1_sec = p2_sec; 				
				p2_first = p2_sec.rest();		// to make sure you don't loose the path, shift next pair to the right as well
				p2_sec = p2_first.rest();
			
			} // end while
			p1_first.setRest(p2_sec);// current pair el1 needs to refer to el2 of next pair,
			p1_sec.setRest(p1_first);// current pair el2 needs to refer to current pair el1
		
			if(p2_sec.rest() == null)			// if even number of elements
				p2_first.setRest(null); 		//this will be the end of the linkedlist
			else 								// this means that there is one extra element in the list
				p2_first.setRest(p2_sec.rest());
			p2_sec.setRest(p2_first);
				
		} // end else
	} // end fropple
	
	
	/**
	 * adds all the elements of a given LinkedList to the original LinkedList
	 * @param list2 is a LinkedList
	 */
	public void append(LinkedList<T1> list2){ 		
		if(list2.head != null){
			ListElement<T1> d2 = list2.head;
			
			while (d2.rest() != null){ 				// loop over all elements of list2 and add to list1
				this.addLast(d2.first());
					d2 = d2.rest();
			}
			this.addLast(d2.first()); 				// one last add needed
		}
		// else do nothing
	}
	
	/**
	 * adds a comparable to the list in the right position
	 * this is in front of the first element that is bigger then the given comparable
	 * @param o is the given comparable
	 */
	public void addSorted(T1 o) {
		// an empty list, add element in front
		if(head == null) 
			head = new ListElement<T1>(o,null); 
		
		else if(head.first().compareTo(o) > 0) {
			// we have to add the element in front
			head = new ListElement<T1>(o,head); 
		}
			
		else {
			// we have to find the first element which is bigger
			ListElement<T1> d = head; 
			while( (d.rest() != null) && (d.rest().first().compareTo(o) < 0) ) {
				d = d.rest();
			}
			ListElement<T1> next = d.rest(); 
			d.setRest(new ListElement<T1>(o,next));
		}
		count++;
	 }
	
	public void removeSorted(){
				if(head == null)
					;//do nothing
				else{
					ListElement<T1> highest = head;
					ListElement<T1> previous = head;
					ListElement<T1> beforeH = head;
					ListElement<T1> d = head;
					while(d != null){	// loop over the whole list to check for elements that are bigger than the current biggest
						if ( d.first().compareTo(highest.first()) > 0 ){
							highest = d;
							beforeH = previous;
						}
							
						if ( d != head) // this makes sure that previous = 1 behind d
							previous = previous.rest();
						d = d.rest();
					}
					 // remove highest from the list
					beforeH.setRest(highest.rest());
						
				}
			count--;
	}
	
	/**
	 * 
	 * @returns the biggest element in the list
	 */
	public T1 getSorted(){
		if(head == null)
			return null;
		else{
			ListElement<T1> highest = head;
			ListElement<T1> d = head;
			while(d != null){	// loop over the whole list to check for elements that are bigger than the current biggest
				if ( d.first().compareTo(highest.first()) > 0 ){
					highest = d;
				}
				d = d.rest();
			}
			 // return value of highest element
			return highest.first();
				
		}
	}


	
} // end class LinkedList
