/**
 * InsufficientCapacityException.java
 * @version 25/12/16
 * @author Laurent Declercq
 */

package traffic;

/**
 * This class is needed to prevent the program from looping forever when the input-files of the simulation add too many vehicles to the same light
 * When the capacity of the streets or lights are reached, and addVehicle is called. The program will stop running.
 * Inside the City and the CrossRoad classes, addVehicle() is put inside a try, catch statement
 */
public class InsufficientCapacityException extends Exception {
	
	private static final long serialVersionUID = 1L; // this prevents a warning in Eclipse
	
	private int capacity;
	
	public InsufficientCapacityException(int capacity){
		this.capacity = capacity;
	}
	
	public int getCapacity(){
		return capacity;
	}

}
