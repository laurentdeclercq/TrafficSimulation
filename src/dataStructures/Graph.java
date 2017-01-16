// OPTIMIZATION : change the nodes list to a tree of nodes
// and use traversals

//-----------------------------


/**
 * Graph.java
 * @version 12/12/16
 * @author Laurent Declercq
 */
package dataStructures;


// generic so that the nodes can have an infoLabel of strings but also of integer etc.
// the weights however, are specificly set to DOUBLE
public class Graph<K extends Comparable<K>> {
	
	private class Node<T1 extends Comparable<T1>> implements Comparable<Node<T1>>{
		private T1 info;
		private CircularVector<Edge<T1>> edges;
		private Boolean visited;
		
		public Node(T1 label){
			info = label;
			edges = new CircularVector<Edge<T1>>();
			visited = false;
		}
		
		public void addEdge(Edge<T1> e){
			edges.addLast(e);
		}
		
		/**
		 * 2 nodes are equal if they have the same label
		 */
		public int compareTo(Node<T1> n2){
			return n2.info.compareTo(info); // actually, shouldn't this be info.compareTo(n2.info) ??
		}
		
		public T1 getLabel(){
			return info;
		}
		
		public String toString(){
			String txt = "Node: ";
			if (info != null)
				txt += info.toString() + " : Edges [" + edges.toString() + "]"; 
			else
				txt += "null";
			
			return txt;
		}
		
		public Boolean getVisited(){
			return visited;
		}
		
		public void setVisited(Boolean v){
			visited = v;
			
		}
		
		public CircularVector<Edge<T1>> getEdges(){
			return edges;
		}
		
		/*
		public void setWeight(int i, Double weight){
			if(i>=0 && i<edges.size())
				edges.get(i).setWeight(weight);
			else
				System.out.println("unable to set weight, index out of bound of edges list");
		}
		*/
	}
	
	public class Edge<T2 extends Comparable<T2>> implements Comparable<Edge<T2>>{
		private Node<T2> toNode;
		Double weight;
		
		public Edge(Node<T2> to, Double weight){
			toNode = to;
			this.weight = weight;
		}
		
		/**
		 * 2 edges are equal if they point to the same node.
		 * This assumes that the edges are starting from the same node !!!
		 */
		public int compareTo(Edge<T2> e2){
			return e2.toNode.compareTo(toNode);
		}
		
		public String toString(){
			return toNode.getLabel().toString() + "(w:" + weight + ")";
		}
		
		
		public void setWeight(Double weight){
			this.weight = weight;
		}

	}
	
	private CircularVector<Node<K>> nodes;
	
	/**
	 * CONSTRUCTOR of GRAPH
	 */
	public Graph(){
		nodes = new CircularVector<Node<K>>();
	}
	
	public String toString(){
		String res = "";
		for(int i = 0;i<nodes.size();i++){
			res += nodes.get(i).toString() + "\n";
		}
		return res;
	}
	
	public void print(){

		for(int i = 0;i<nodes.size();i++){
			System.out.println(nodes.get(i).toString());
		}
	}
	
	/**
	 * Adds a node to the nodes list of the graph
	 * @param label
	 */
	public void addNode(K label){
		nodes.addLast(new Node<K>(label));
	}
	
	private void resetAllVisited(){
		for (int i= 0;i<nodes.size();i++){
			nodes.get(i).setVisited(false);
		}
	}
	
	public CircularVector<Node<K>> getNodes(){
		return nodes;
	}
	
	/**
	 * @param nodeLabel
	 * @return NULL if not found in the nodes list, else return the node
	 */
	private Node<K> findNode(K nodeLabel){
		Node<K> tmp = new Node<K>(nodeLabel);
		return nodes.find(tmp);
	}
	
	/**
	 * @param nodeLabel
	 * @return the element that is contained in the node, if found based on the nodeLabel
	 */
	public K find(K nodeLabel){
		Node<K> found = findNode(nodeLabel);
		if (found != null)
			return found.info;
		else
			return null;
	}
	
	public void DFS(){
		
		resetAllVisited();
		
		for(int i=0;i<nodes.size();i++){
			Node<K> current = nodes.get(i);
			if(!current.visited)
				DFS(current);
		}
	}

	private void DFS(Node<K> current){
		current.setVisited(true);
		for(int i=0;i<current.edges.size();i++){
			Edge<K> e = current.edges.get(i);
			Node<K> next = e.toNode;
			if(!next.visited)
				DFS(next);
		}
		System.out.println(current.info);

	}
	/*
	public int getNumberEdges(int index){
		Node<K> aNode = nodes.get(index);
		return aNode.getEdges().size();
	}
	
	public K getNodeLabel(int index){
		if(index >=0 && index < nodes.size())
			return nodes.get(index).info;
		else{
			System.out.println("Unable to getNodeLabel, index out of bound of nodeslist in graph");
			return null;
		}
	}
	
	public K getEdgeLabel(int i, int j){
		if(i >=0 && i < nodes.size() && j >=0 && j < nodes.get(i).edges.size())
			return nodes.get(i).edges.get(j).toNode.info;
		else{
			System.out.println("Unable to getEdgeLabel, index out of bound");
			return null;
		}
	}
	*/
	
	/**
	 * finds 2 nodes and adds the second node to the edges list of the first. Only if it found both
	 * @param nodeLabel1
	 * @param nodeLabel2
	 */
	public void addEdge(K nodeLabel1, K nodeLabel2, Double weight){
		Node<K> n1 = findNode(nodeLabel1);
		Node<K> n2 = findNode(nodeLabel2);
		if(n1 != null && n2 != null)
			n1.addEdge(new Edge<K>(n2, weight));
		else
			System.out.println("addEdge " + nodeLabel2 + " to " + nodeLabel1 + " not executed: one of the nodes is not found");
	}
	

	public void setWeight(K nodeLabel1, K nodeLabel2, Double weight){
		Node<K> n1 = findNode(nodeLabel1);
		if(n1 == null){
			System.out.println("Unable to set weight! NodeLabel1 not found in graph!");
		}
		else{
			Node<K> tmpNode = new Node<K>(nodeLabel2);
			Edge<K> tmp = new Edge<K>(tmpNode, 0.0);
			tmp = n1.edges.find(tmp);
			if(tmp == null){
				System.out.println("Unable to set weight! NodeLabel2 not an edge of nodeLabel1.");
			}
			else
				tmp.setWeight(weight);
		}
		
	}

	/*
	 * Cycle checking, DFS and TopologicalSort are equivalent problems
	 * If graph has cycle, TopologicalSort fails. If topologicalSort fails, cycle exists
	 * DFS is used to do topologicalsort. That means, you can check cycle by doing DFS
	 * Time Complexity of DFS is O(|V| + |E|)
	 */
	
	/**
	 * @param nodeLabel1
	 * @param nodeLabel2
	 * @return TRUE if a path exists between the nodes that where given
	 * This is Depth First Graph Traversal (because STACK)
	 */
	public boolean checkPathExists(K nodeLabel1, K nodeLabel2){
		
		Node<K> startState = findNode(nodeLabel1);
		Node<K> endState = findNode(nodeLabel2);
		
		resetAllVisited();
		
		if(startState != null && endState != null){
			
			Stack<Node<K>> toDoList = new Stack<Node<K>>();
			toDoList.push(startState);
			
			while(!toDoList.isEmpty()){
				Node<K> current = toDoList.pop();
				current.setVisited(true);
			
				if(current == endState)
					// addfirst of the endState until you insert the startstate
					return true;
				else{
					for(int i=0;i<current.edges.size();i++){
						Edge<K> e = current.edges.get(i);
						if(! e.toNode.visited)
							toDoList.push(e.toNode);
					}
				}
			}
		}
		return false;
	}

	/**
	 * Class PathInfo is a combination of information needed during Dijkstra.
	 * When Dijkstra(start) is called, each node in the graph gets its own PathInfo that shows,
	 * based on 'start' what the current estimated PREVIOUS node is, together with the current estimated cost: SHORTESTFROMSOURCE.
	 * The node itself is found inside TARGET.
	 * @author laurentdeclercq
	 *
	 */
	private class PathInfo implements Comparable<PathInfo>{
		
		private final Double MAX_WEIGHT = 2000000.0; 
		private Node<K> target;
		
		// Estimations for shortest distance and previous node that leads back to Start-node
		private Double shortestFromSource;
		private Node<K> previous;

		public PathInfo(Node<K> target){
			shortestFromSource = MAX_WEIGHT;
			previous = new Node<K>(null);
			this.target = target;
		}
		
		public void setShortest(Double newDistance){
			this.shortestFromSource = newDistance;
		}
		
		public Double getShortest(){
			return shortestFromSource;
		}
		
		public void setPrevious(Node<K> prev){
			previous = prev;
		}
		
		public Node<K> getPrevious(){
			return previous;
		}
		public int compareTo(PathInfo p){
			return (int)(shortestFromSource - p.getShortest());
		}
		
		public String toString(){
			String txt = "[";
			txt += "Target : " + target.toString() + "\n";
			if(shortestFromSource != null)
				txt += "ShortestFromSource : " + shortestFromSource + "\n";
			else
				txt += "ShortestFromSource : null \n";
			if(previous != null)
				txt += "Previous :" + previous.toString() + "]\n";
			else
				txt += "Previous : null ]\n";
			return txt;
		}
	}
	
	/** 
	 * Helper Method for dijkstraShortestPaths(Node<K>)
	 * Returns the PathInfo of the node that is still unvisited and has the shortest estimated distance to START node
	 */
	private PathInfo findSmallestUnvisited(CircularVector<PathInfo> aList){
		PathInfo smallest = null;
						
		// get the PathInfo of the first unvisited node to have a value for smallest.
		for(int i = 0;i<aList.size();i++){
			if (! aList.get(i).target.visited){
				smallest = aList.get(i);
				break;
			}
		}
		
		// Loop again over the list to compare with other distances to replace current SMALLEST when applicable
		for(int i = 0;i<aList.size();i++){
			if(aList.get(i).getShortest() != null && smallest != null){
				if (! aList.get(i).target.visited && aList.get(i).compareTo(smallest)<0) // compareTo compares the distance
					smallest = aList.get(i);
			}
		}
		return smallest;
	}
		
	
	/*
	 * Algorithm:
	 * ---------------
	 * 
	 * Let distance of start vertex from start vertex = 0
	 * Let distance of all other vertices from start = infinity (null)
	 * 
	 * Repeat:
	 * 		Visit the unvisited vertex with the smallest known distance from the start vertex
	 * 		For the current vertex, examine its unvisited neighbours
	 * 		For the current vertex, calculate distance of each neighbour from the start vertex
	 * 		If the calculated distance of a vertex is less than the known distance, update the shortest distance
	 * 		Update the previous vertex for each of the updated distances
	 * 		Add the current vertex to the list of visited vertices
	 * Until all vertices visited
	 * ----------------------------------------------------------------------
	 */
	private CircularVector<PathInfo> dijkstraShortestPaths(Node<K> startState){

		CircularVector<PathInfo> results = new CircularVector<PathInfo>();		// PATHINFO : this structure holds a the node, 
																		// together with the estimated distance to start
																		// together with the estimated previous node
			resetAllVisited();

			for (int i=0;i<nodes.size();i++){
				results.addLast(new PathInfo(nodes.get(i)));
				if(nodes.get(i) == startState)
					results.getLast().setShortest(0.0);		// set distance from source to source = 0
															// distances to all other nodes from source are unknown, therefore null
			}// end for
				
			//System.out.println("Entering findSmallestUnvisited(results)");		
				// visit  the unvisited vertex with the smallest known distance from the start vertex
				// the first time, this will be the source itself, since this is the only non-null value
				// the assumption here is that by taking the smallest, we will get to the end more quickly
				PathInfo current = findSmallestUnvisited(results);
				double newDistance;
				
				while(current != null){
					// for the current vertex, calculate the distance of each neighbour from the start vertex
					for(int i=0;i<current.target.edges.size();i++){		// loop over all EDGES
						for(int j=0;j<results.size();j++){				// loop over resultsList to match with EDGES
							
							// examine each unvisited neighbour
							if(current.target.edges.get(i).toNode == results.get(j).target && ! results.get(j).target.getVisited()){
								newDistance = current.shortestFromSource + current.target.edges.get(i).weight; 
								// newDistance = current distance to source + direct weight
								// if the calculated distance of a vertex is less than the known distance, update the shortest distance
								// and update the pointer to the previous
								if(newDistance < results.get(j).getShortest() || results.get(j).getShortest() == null){
									results.get(j).setShortest(newDistance);
									results.get(j).setPrevious(current.target);
									break;

								}
							}
						}// end inner FOR
								
					}// end outer FOR
					current.target.setVisited(true);
					current = findSmallestUnvisited(results);
				}// end while	
		return results;
	} // end dijkStraShortestPaths
	
	
	// returns a stack with the top being the first next step to take on the shortest path to the destination
	// last element is the destination
	public Stack<K> shortestPath(K startLabel, K endLabel){
		Node<K> startState = findNode(startLabel);
		Node<K> endState = findNode(endLabel);
		
		resetAllVisited();
		
		Stack<K> path = new Stack<K>();
		CircularVector<PathInfo> allShortest = new CircularVector<PathInfo>();
		
		if(startState != null && endState != null){
			
			//System.out.println("Entering dijkStraShortestPaths(startstate)");
			//System.out.println("Startstate: "+ startState.toString());
			//System.out.println("EndState: "+ endState.toString());

			allShortest = dijkstraShortestPaths(startState);

			Node<K> current = endState;
			PathInfo currentRes = null;
			path.push(endState.info);

			while(current!= startState){

				for(int i=0;i<allShortest.size();i++){
					currentRes = allShortest.get(i);
					if(currentRes.target == current){
						path.push(currentRes.getPrevious().info);
						//System.out.println("Current path" + path);
						current = currentRes.getPrevious();
						break;
					}
				}

			}
			path.pop();	// The CURRENT position is not needed in the path. It is therefore POPPED
		}

		return path;
	}


	
	
	
	
}

