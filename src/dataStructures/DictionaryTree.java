/**
 * DictionaryTree.java
 * @version 5/1/17
 * @author Laurent Declercq
 */
package dataStructures;


/**
 * DictionaryTree is a Dictionary that is built usin the BinaryTree datastructure. 
 * The BinaryTree consists of DICTIONARYPAIRS
 * It takes 2 types : T1 and T2. T1 is the KEY, T2 is the corresponding VALUE
 */
public class DictionaryTree<T1 extends Comparable<T1>,T2> { // the key needs to be comparable
	
	public class DictionaryPair<K extends Comparable<K>,V> implements Comparable<DictionaryPair<K,V>>{	// the key needs to be comparable
		private K key; 
		private V value;
		
		/**
		 * CONSTRUCTOR of DICTIONARYPAIR
		 */
		public DictionaryPair (K someKey, V someValue){
			this.key = someKey;
			this.value = someValue;
		}
		
		public K getKey() {
			return key;
		}
		
		public V getValue() {
			return value;
		}
		public void setKey(K newKey) {
			key = newKey;
		}
		public void setValue(V newValue)
		{
			value = newValue;
		}
		
		public String toString(){
			String txt = "(Key: " + key + ", Value:" + value + ")";
			return txt;
		}
		
		@Override
		public int compareTo(DictionaryTree<T1,T2>.DictionaryPair<K,V> o) {
			DictionaryPair<K,V> p2 = (DictionaryPair<K,V>)o;
			return key.compareTo(p2.key);
		}
		
	}
	
	
	//------------------------------------------------------------------------------
	
	
	private BinaryTree<DictionaryPair<T1,T2>> data;
	
	/**
	 * CONSTRUCTOR of DICTIONARY
	 */
	public DictionaryTree(){
		data = new BinaryTree<DictionaryPair<T1,T2>>();
	}
	
	public String toString(){
		return data.toString();
	}
	
	public void print(){
		data.print();
	}
	
	/**
	 * @return TRUE if binary tree is empty
	 */
	public boolean isEmpty(){
		return data.isEmpty();
	}
	
	public int size(){
		return data.size();
	}
	
	public BinaryTree<DictionaryPair<T1,T2>> getData(){
		return data;
	}
	
	/**
	 * inserts a new DictionaryPair OR replaces an existing pair.value is pair.key is already there
	 * @param key
	 * @param value
	 */
	public void add(T1 key,T2 value) {
		DictionaryPair<T1,T2> d = new DictionaryPair<T1,T2>(key, value); // this still creates an object!!
		DictionaryPair<T1,T2> existing = data.find(d);

		if(existing != null)
			existing.setValue(value);
		else
			data.insert(d);
	}
	
	/**
	 * Takes a key and returns the element that has this key-value, OR null, if not found
	 * @param key
	 * @return
	 */
	public T2 findKey(T1 key) {
		DictionaryPair<T1,T2> tmp = new DictionaryPair<T1,T2>(key, null); // this still creates an object!!
		tmp = data.find(tmp);
		return tmp != null? tmp.getValue() : null;
	}
	
}
