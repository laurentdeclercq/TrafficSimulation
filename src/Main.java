/**
 * Main.java
 * @version 30/12/16
 * @author Laurent Declercq
 */


/**
 * This is the main class with the main method that creates a simulation based on 2 input-files to set up the city, add cars to it 
 * The simulation is then started to make vehicles drive 
 * from their starting position until all cars finished with a maximum number of loops that is given.
 */
public class Main {

	public static void main(String[] args) {
	
		
		TrafficFlowSimulation  simulation = new TrafficFlowSimulation ();
		
		System.out.println("\n\n Start SIMULATION PHASE 1 : *** SETTING UP CITY *** \n\n");
		simulation.setupCity("Config_Files/city2.txt");
		
		System.out.println("\n\n Start SIMULATION PHASE 2 : *** ADDING CARS *** \n\n");
		simulation.addVehicles("Config_Files/vehicles2.txt");
		
		System.out.println("\n\n Start SIMULATION PHASE 3 : *** RUNNING THE CITY *** \n\n");
		simulation.start(250);
	
	}

}
