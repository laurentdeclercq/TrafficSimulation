
/**
 * Vehicle.java
 * @version 1/11/16
 * @author Laurent Declercq
 */
package traffic;

import dataStructures.*; // in this class all specific data structures reside
import traffic.CrossRoad.Orientation;
/**
 * class Vehicle is an implementation of a vehicle in traffic, 
 * with properties such as destination and route and methods such as incrementTravelTime
 * 
 * @version 1/11/16
 * @author Laurent Declercq
 *
 */
public class Vehicle implements Comparable<Vehicle>{

	/**
	 * 	Boolean variable that indicates whether the vehicle is a priority vehicle.
	 * Implicates that it does not have to wait for traffic lights
	 * TRUE = priority
	 * In trafficlight, when adding a vehicle, this boolean is converted 
	 * into 2 for FALSE, and 1 for TRUE
	 */
	private boolean priority;
	
	/** states whether or not the optimal path is recomputed at each iteration */
	private boolean dynamicPath;
									
	/* Vehicle’s destination. Corresponds with the name of a crossing */
	private String destination;	
	
	/* Vehicle’s name. */
	private String name;	
	
	
	/**
	 * the route followed from initial starting point till current state
	 * or final destination. Contains String repr of crossings
	 */
	private Stack<String> followedRoute;
	
	/** 
	 * The route from current position to destination, with the next step, the top of the stack 
	 * This is ONLY USED if the vehicle is NOT DYNAMIC
	 */
	private Stack<CrossRoad> futureRoute;
	
	
	/** the total travel time in minutes since starting at its initial position. */
	private int travelTime; 
		
	/**
	 * CONSTRUCTORS FOR CLASS VEHICLE
	 */
	
	public Vehicle(String name, String destination){
		this.destination = destination;
		this.name = name;
		priority = false;
		dynamicPath = true;
		travelTime = 0;
		followedRoute = new Stack<String>();	// a stack because we only need to push crossings to it
										// we never need a specific crossing in the route
		futureRoute = new Stack<CrossRoad>();
	}
	
	public Vehicle(String name, String destination, boolean priority, boolean dynamicPath){
		this(name,destination);
		this.priority = priority;
		this.dynamicPath = dynamicPath;
	}
	
	
	/**
	 * METHODS FOR CLASS VEHICLE
	 */
	
	/**
	 * @return a readable String to represent the instance of Vehicle
	 */
	public String toString(){
		
		String answer = name;
		
		if (priority == true)
			answer += " priority ";
		answer += " with dest. " + destination;			
		return answer;
		
	}
	
	@Override
	public int compareTo(Vehicle o) {
		return name.compareTo(o.name); // this assumes, that vehicles with the same name are equal
	}

	/**
	 * @return the destination as String
	 */
	public String getDestination(){ 
		return destination;
	}
	
	/**
	 * @param destination is used to set the destination of a vehicle
	 */
	public void setDestination(String destination){ 
		this.destination = destination;
	}
	
	public void setPriority(boolean priority){
		this.priority = priority;
	}
	/**
	 * @return TRUE when vehicle is a priority vehicle
	 */
	public boolean isPriorityVehicle(){
		return priority;
	}
	
	/**
	 * @param minutes are added to the travel time of a Vehicle instance
	 */
	public void incrementTravelTime(int minutes){
		travelTime += minutes;
	}
	
	/**
	 * resets travel time to zero
	 */
	public void resetTravelTime(){ 
		travelTime = 0;
	}
	
	public int getTravelTime(){
		return travelTime;
	}
	
	public String getName(){
		return name;
	}
	
	public Stack<String> getFollowedRoute(){
		return followedRoute;
	}
	
	/**
	 * adds 1 location at the end of the current route
	 * @param location
	 */
	public void addLocationToRoute(String location){ 
		followedRoute.push(location);
	}

	/**
	 * prints the route
	 */
	public void printRoute(){
		System.out.print(followedRoute);
	}
		
	private void setOptimalRoute(City c){
		if(futureRoute.isEmpty() || dynamicPath){
			CrossRoad current = c.findCR(getCurrentPosition());
			CrossRoad destination = c.findCR(this.destination);
			//System.out.println("In setOptimalRoute(): Current: " + current.getName() + ", destination " + destination);
			//System.out.println(c.getCityGraph());
			futureRoute = c.getCityGraph().shortestPath(current, destination);
			//System.out.println("In setOptimalRoute(): futureRoute: " + futureRoute);
		}
	}
	
	// return NULL if destination reached, or return the orientation of the next optimal step.
	public Orientation makeTurn(City c){
		System.out.println("current position: " + getCurrentPosition() + ", destination : " + destination);
		
		if(getCurrentPosition().compareTo(destination) == 0)
			return null; // do nothing;
		
		else{
			String nextStep;
			//System.out.println("Before setOptimalRoute(c) : futureRoute = " + futureRoute);
			setOptimalRoute(c); 	// for Non-dynamic vehicles, this is done ONCE, for dynamic vehicles each iteration
			System.out.println("Optimal" + futureRoute);
			nextStep = futureRoute.top().getName();
			
			// this if-statement is needed for NON-dynamic vehicles because it might happen that due to traffic, 
			// the vehicle cannot move. In this case, the futureRoute should remain the same.
			if(getCurrentPosition().compareTo(nextStep) == 0){
				futureRoute.pop();
				nextStep = futureRoute.top().getName();
			}
			return c.getDirection(getCurrentPosition(), nextStep);
		}
	}


	public String getNextStep(){
		return futureRoute.top().getName();
	}
	
	public String getCurrentPosition(){
		return followedRoute.top();
	}

			

}
