package dataStructures;

public class MatrixGraph {
	
	private Matrix<Double> data;
	
	public MatrixGraph(int nrNodes){
		data = new Matrix<Double>(nrNodes);
	}
	
	public void addEdge(int from, int to, Double w){
		data.set(from, to, w);
	}
	
	public Double getEdge(int from, int to){
		return data.get(from, to);
	}
	
	public void print(){
		data.print();
	}
	
	public String toString(){
		return data.toString();
	}

}
