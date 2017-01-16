/**
 * Fleet.java
 * @version 25/11/16
 * @author Laurent Declercq
 */
package traffic;

import dataStructures.*;

/**
 * class Fleet is a simple subclass of Queue<Vehicle> that has a separate method to
 * accept vehicles and release them. Which is push() and pop()
 * It is used in Main2.java in the background so that cars that finish traversing a street,
 * end up there, and they get placed at random in front of a trafficlight again.
 * @version 25/11/16
 * @author Laurent Declercq
 */
public class Fleet extends Queue<Vehicle>{

	private Queue<Vehicle> vehicleList;	// a queue is used, because only push and pop are needed, and order is FIFO
	
	public Fleet(){
		vehicleList = new Queue<Vehicle>(20);
	}
	
	public void addVehicle(Vehicle v){
		vehicleList.push(v);
	}
	
	public Vehicle releaseVehicle(){
		return vehicleList.pop();
	}
	
	public boolean isEmpty(){
		return vehicleList.isEmpty();
	}
	
	public String toString(){
		return "Fleet: " + vehicleList.toString();
	}

}
