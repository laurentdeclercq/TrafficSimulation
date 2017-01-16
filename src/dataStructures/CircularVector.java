package dataStructures;

public class CircularVector<T extends Comparable<T>> implements Comparable<CircularVector<T>> {
	
	private T[] data;
	private int first;
	private int count;
	private int capacity;
	
	@SuppressWarnings("unchecked")
	public CircularVector(int capacity){
		count = 0;
		first = 0;
		data = (T[]) new Comparable[capacity];
		this.capacity = capacity;
	}
	
	@SuppressWarnings("unchecked")
	public CircularVector(){
		count = 0;
		first = 0;
		capacity = 0;
		data = (T[]) new Comparable[capacity];
	}
	
	public String toString(){
		String txt = "(";
		for (int i=0; i<count; i++){
			int index = (first + i) % capacity;
			if (data[index] != null)
				txt += data[index] + "\t";
			else
				txt += null;
		}
		
		txt += ")";
		
		return txt;
	}
	
	public int compareTo(CircularVector<T> v2){
		return 0; 	// impossible to compare 2 circular vectors, this implementation is not good
	}
	
	public int size(){
		return count;
	}
	
	public boolean isEmpty(){
		return (count == 0);
	}
	
	public void addFirst(T element){
		// If no empty spaces, make a bigger array
		if(count == capacity)
			extendCapacity();
		// Change FIRST to point to the empty space before FIRST
		if(count == 0) ;
			// do nothing;
		else
			first = (first + capacity - 1) % capacity ;
		// fill that space and increment count
		data[first] = element;
		count++;
	}
	
	public void addLast(T element){
		// If no empty spaces, make a bigger array
		if(count == capacity)
			extendCapacity();
		// fill first empty space and increment count
		data[(first + count) % capacity] = element;
		count++;
	}
	
	public T getFirst(){
		if (count > 0)
			return data[first];
		else
			return null;
	}
	
	public T getLast(){
		if(count > 0)
			return data[(first + count - 1) % capacity];
		else
			return null;
	}
	
	public T get(int index){ // Here, with index we mean starting from FIRST (0)
		if(index < count && index >=0)	
			return data[(first + index) % capacity];
		else
			return null;
	}
	
	public void set(int index, T item){ // Here, with index we mean starting from FIRST (0)
		if(index < count && index >=0)	
			data[(first + index) % capacity] = item;
	}
	
	public void removeFirst(){
		if (count > 0){
			// make FIRST point to 1 to the right of the current FIRST
			first = (first + 1) % capacity;
			count--;
		}
	}
	
	public void removeLast(){
		if(count > 0)
			count--;	
	}
	
	public boolean contains(T item){
		int checked = 0;
		while(checked < count){
			if(data[(first + checked) % capacity].compareTo(item) == 0)
				return true;
			checked++;
		}
		return false ;
	}
	
	public T find(T item){
		int checked = 0;
		T result = null;
		while(result == null && checked < count){
			if(data[(first + checked) % capacity].compareTo(item) == 0)
				result = data[(first + checked) % capacity];
			checked++;
		}

		return result ;
	}
	
	public T findSmallest(){
		T smallest = getFirst();
		for (int i=1;i<count;i++){
			if (get(i).compareTo(smallest) < 0)
				smallest = get(i);
		}
		return smallest;
	}
	
	public void reverse(){ // reverses the vector elements without creating a new copy
		T temp;
		int swapped = 0;
		while(swapped < count/2){
			temp = data[(first + swapped) % capacity];
			data[(first + swapped) % capacity]=data[(first+count-1-swapped) % capacity];
			data[(first+count-1-swapped) % capacity] = temp;
			swapped += 1;
		}
	}

	public void print(){
		System.out.print("(");
		for (int i=0; i<count; i++){
			int index = (first + i) % capacity;
			System.out.print(data[index] + "\t");
		}
		System.out.println(")");
	}
	
	public void extendCapacity(){
		if (capacity >1)
			capacity = capacity*2;
		else
			capacity = 2;
		
		T[] data2 = (T[]) new Comparable[capacity];
		
		for( int i = 0; i<count; i++){
			int index = (first + i) % count;
			data2[i] = data[index];
		}
				
		data = data2;
		first = 0;
	}

}
