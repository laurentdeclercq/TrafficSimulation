/**
 * Matrix.java
 * @version 25/11/16
 * @author Laurent Declercq
 */

package dataStructures;

public class Matrix<T extends Comparable<T>> {
	
	// some appropriate private members
	CircularVector<CircularVector<T>> data;
	private int nrRows;
	private int nrCols;
	
	/**
	 * allocata an N-byN matrix where N = nrNodes
	 * all elements are initially 0
	 */
	
	// The matrix is a list of lists ( more specifically it is circularvector of circularvector
	// the outer list contains all the rows. Each row is a list of colums
	// [[row0col0, row0col1, row0col2],[row1col0, row1col1, row1col2],[row2col0, row2col1, row2col2]]
	public Matrix(int nrNodes){
		this(nrNodes,nrNodes);
	}
	
	public Matrix(int nrRows, int nrCols){
		this.nrRows = nrRows;
		this.nrCols = nrCols;
		data = new CircularVector<CircularVector<T>>(nrRows);
		for (int row = 0;row<nrRows;row++){
			CircularVector<T> cv = new CircularVector<T>(nrCols);
			for(int col = 0;col<nrCols;col++){
				cv.addLast(null);
			}
			data.addLast(cv);
		}
	}
	
	/* store the weight at the given row and column */
	public void set(int row, int col, T weight){
		if(row <nrRows && row >=0 && col >= 0 && col < nrCols)
			data.get(row).set(col, weight);
		else
			System.out.println("Did not SET anything : Row or Column out of bound");

	}
	
	public int getNumberRows(){
		return nrRows;
	}
	public int getNumberCols(){
		return nrCols;
	}
	
	/**
	 * @param row (index, from 0 to nrNodes, excluding)
	 * @param col
	 * @return the weight at the given row and column
	 */
	public T get(int row, int col){
		if(row <nrRows && row >=0 && col >= 0 && col < nrCols)
			return data.get(row).get(col);
		else
			System.out.println("Row or Column out of bound");
		return null;
		
	}
	
	public String toString(){
		String res = "";
		for (int row = 0;row<nrRows;row++){
			res += data.get(row).toString() + "\n";
		}
		return res;
	}
	
	public void print(){
		for (int row = 0;row<nrRows;row++){
			System.out.println(data.get(row).toString());
		}
	}
	
}
