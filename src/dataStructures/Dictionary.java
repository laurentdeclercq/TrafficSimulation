package dataStructures;


public class Dictionary<T1 extends Comparable<T1>,T2> { // the key needs to be comparable
	
	private class DictionaryPair<K extends Comparable<K>,V> implements Comparable<DictionaryPair<K,V>>{	// the key needs to be comparable
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
		public int compareTo(Dictionary<T1,T2>.DictionaryPair<K,V> o) {
			DictionaryPair<K,V> p2 = (DictionaryPair<K,V>)o;
			return key.compareTo(p2.key);
		}
		
	}
	
	private CircularVector<DictionaryPair<T1,T2>> data;
	
	/**
	 * CONSTRUCTOR of DICTIONARY
	 */
	public Dictionary(){
		data = new CircularVector<DictionaryPair<T1,T2>>();
	}
	
	public String toString(){
		return data.toString();
	}
	
	public int size(){
		return data.size();
	}
	
	public boolean isEmpty(){
		return data.isEmpty();
	}
	
	public void add(T1 key,T2 value) {
		int index = findPosition(key);
		if(index == -1){
			DictionaryPair<T1,T2> d = new DictionaryPair<T1,T2>(key, value);
			data.addLast(d);
		}
		else
			data.get(index).setValue(value);

	}
	private int findPosition(T1 key) {
		int index = 0;
		while(index < data.size()){
			if(data.get(index).key.compareTo(key) == 0)
					return index;
			else
				index++;
		}
		return -1;
	}
	
	public T2 findKey(T1 key) {
		int index = findPosition(key);
		if (index > -1)
			return data.get(index).value;
		else
			return null;
	}
	
	private T2 get(int i){
		return data.get(i).value;
	}
}
