/**
 * TrafficFlowSimulation.java
 * @version 30/12/16
 * @author Unknown, adaptations by Laurent Declercq
 */

import traffic.*;
import traffic.CrossRoad.Orientation;

/**
 * TrafficFlowSimulation class contains all the methods needed to configure a working city with driving cars.
 * Using start(). The simulation runs for the given amount of loops and is able to give some basic information back to the user
 */
public class TrafficFlowSimulation {
	
	private City aCity;
	private CarRegistry aCarRegistry;
	private int countIterations;

	/**
	 * Create an object for the CarRegistry. The City is created afterwards using an input-file. 
	 */
	public TrafficFlowSimulation(){
		aCarRegistry = new CarRegistry();
	}
	
	public String toString(){
		int nrRows = aCity.getCityMatrix().getNumberRows();
		int nrCols = aCity.getCityMatrix().getNumberCols();
		return " Simulation of a city with size : " + nrRows + " rows by " + nrCols + " columns. \n There are " 
				+ aCarRegistry.size() + " cars driving around.";
	}
	
	/**
	 * Main method for configuring CITY. Based on the input, other methods are called such as initiateCity(), addRoadWork() etc.
	 * @param fileName = txt-file with settings for city
	 */
	public void setupCity(String fileName) {
		SimulationReader sr = new SimulationReader(this);
		sr.readCity(fileName);
	}
	
	/**
	 * Main method for adding cars to CITY
	 * @param fileName = txt-file with settings for car.
	 */
	public void addVehicles(String fileName) {
		SimulationReader sr = new SimulationReader(this);
		sr.readVehicles(fileName);
	}
	
	public void addVehicle(Vehicle v, String start) {
		Orientation o = Orientation.values()[(int)(Math.random()*4)];
		aCity.addVehicle(start, o, v);
		aCarRegistry.addVehicle(v);
		System.out.println(v + " Start at " + start + " , " + o + "." );
	}
	
	public void initiateCity(String cityName, int numberRows, int numberCols) {
		aCity = new City(cityName, numberRows, numberCols);
		System.out.println("Initiated city named " + cityName + " of size " + numberRows + " rows by " + numberCols + " cols.");
	}
	
	public void addRoadWork(String from, String to) {
		aCity.changeRoadLength(from, to, 1000000.0);
		System.out.println("Roadwork added from " + from + " to " + to + ".");
		System.out.println("City:\n" + aCity);
	}
	
	public void setUpCrossroad(int row, int col, CrossRoad cr) {
		aCity.changeCRreferral(row,col,cr);		// replaces all existing values in the matrix-cell without replacing the object
		System.out.println("North Flow in matrix: " + aCity.getCityMatrix().get(row, col).getLight(Orientation.North).getGreenFlow());
		System.out.println("Finished set up of CR at r" + row + ", c" + col + " named " + cr.getName() + ".\n");
	}
	
	/**
	 * Start the simulation for the given amount of loops. 0 loops given means "Run till completion".
	 * For numbers larger than 0, the simulation will run maximum given times, or stops when all vehicles reached destination.
	 * @param loops
	 */
	public void start(int loops) {
		countIterations = 0;
		
		if (loops == 0) { 					// run complete simulation
			while(! aCarRegistry.allFinished()){
				countIterations++;
				System.out.println("Starting iteration number " + countIterations +"\n\n\n");
				aCarRegistry.iterate();		// increments travelTime ONLY if current != destination
				aCarRegistry.print();
				aCity.iterate();

			}
		} 
		else if(loops > 0) {				// run simulation for number of loops
			while(countIterations < loops && ! aCarRegistry.allFinished()){
				countIterations++;
				System.out.println("Starting iteration number " + countIterations +"\n\n\n");
				aCarRegistry.iterate();
				aCity.iterate();
				aCarRegistry.print();
			}
		}
		else 
			System.out.println("No Simulations Occurred, please enter a positive number of loops or zero for complete simulation");
		
		
		System.out.println("\n\n---------------------------\n ** Simulation Finished **\n---------------------------------------\n");
		System.out.println(countIterations + " iterations needed!\n\n");
		System.out.println("Total Travel Time: " + totalTravelTime());
		System.out.println("Average Travel Time: " + averageTravelTime());
		System.out.println("Number of cars finished: " + aCarRegistry.getNumberFinished() + " of total " + aCarRegistry.size());
		System.out.println("All Finished? --> "+ aCarRegistry.allFinished());
		// add requested information for specific vehicle here; during simulation each vehicle route is printed when finished;
	}
	
	/**
	 * METHODS FOR RETRIEVING INFORMATION FROM CAR_REGISTRY
	 */
	
	
	/** Return total travel time (number of iterations) for all cars combined */
	private int totalTravelTime() {
		return aCarRegistry.getTotalTravelTime();
	}
	
	/** Return average travel time (number of iterations) for all cars */
	private float averageTravelTime() {
		float avgTravel = totalTravelTime() / aCarRegistry.size();
		return avgTravel;
	}
	
	/** Print the travelTime for a given vehicle */
	private int travelTimeForVehicle(String vehicleName) {
		return aCarRegistry.getTravelTimeVehicle(vehicleName);
	}
	
	/** Print the route for a given vehicle */
	private void printRouteForVehicle(String vehicleName) {
		Vehicle v = aCarRegistry.find(vehicleName);
		System.out.println("Route of vehicle "+ v.getName() + " : " + v.getFollowedRoute().toString() );
	}

}
