/**
 * TrafficLight.java
 * @version 22/11/16
 * @author Laurent Declercq
 */
package traffic;

import dataStructures.*; // in this class all specific data structures reside

/**
 * class TrafficLight is an implementation of a TrafficLight at a specific street at en intersection, 
 * with properties such as currentState and flow and capacity and methods such as addVehicle and releaseVehicle
 * 
 * @version 22/11/16
 * @author Laurent Declercq
 *
 */
public class TrafficLight {

	/** The light has 3 states */
	public enum State {
		RED(3), YELLOW(1), GREEN(2); // calls a different constructor depending on the value used (3, 2 or 1 in this case)
		private int time;
		// you can even have multiple values like RED(3,1) -- this can be useful for another ENUM
		private State(int t){
			this.time = t;
		}
		
		// you don't need a method to get the time because you get this with currentState.time
		
		protected void setTime(int t){
			this.time = t;
		}
	}

	
	/** Queue of vehicles waiting in front of TrafficLight. PriorityVehicles can pass all others */
	private PriorityQueue<Vehicle> vehicles;
	
	/** number of cars that can pass per minute, normally this equals greenFlow but 
	 * on Yellow ,flow is reduced to (greenFlow / 2)  */
	private int flow;
	
	/** number of cars that can pass per minute on green */
	private int greenFlow;
	/** max amount of vehicles that can wait for the traffic light */
	private int capacity;
	
	/** the current state of this traffic light: RED, YELLOW or GREEN */
	private State currentState;
		
	/** the time a specific light has been ON */
	private int waited = 0;
	
	
	/**
	 * CONSTRUCTORS OF TRAFFICLIGHT
	 */
	public TrafficLight(){
		vehicles = new PriorityQueue<Vehicle>();
		flow = 0;
		greenFlow = 0;
		capacity = 0;
		this.currentState = State.RED;
	}
	
	public TrafficLight(int flow, int capacity, State s){
		vehicles = new PriorityQueue<Vehicle>();
		this.flow = flow;
		this.greenFlow = flow;
		this.capacity = capacity;
		if(s == State.GREEN || s == State.RED)
			this.currentState = s;
		else
			this.currentState = State.RED;
	}
	
	/**
	 * METHODS OF TRAFFICLIGHT
	 */
	
	public String toString(){
		String txt = "A " + currentState + " light with capacity: " + capacity + " cars, and flow: " + flow + " cars per minute. \n" ;
		txt += "\t Vehicles at light: " + vehicles.toString();
		return txt;
	}
	
	public int compareTo(TrafficLight o){
		return 0;	// not a good compareTo(), but 0 is used to show that trafficLights cannot be compared to one another
	}
	
	public boolean full(){
		return capacity == vehicles.size();
	}
	
	/**
	 * This will take a state and time value and assign that new time value to that state, only if time is acceptable (> 0)
	 * @param s is the given state RED, YELLOW or GREEN for which you want to change the time
	 * @param t is the new time you want to give that state
	 */
	public void setTimeForState(State s,int t){
		if(t>0){
			s.setTime(t);
		}
		// else do nothing	
	}
	
	public int getTimeForState(State s){
		return s.time;
	}
	
	/**
	 * This sets GREENFLOW (flow will be adjusted inside iterate() for YELLOW)
	 */
	public void setGreenFlow(int number){
		this.greenFlow = number;
	}
	
	public void setCapacity(int number){
		this.capacity = number;
	}
	
	public void setState(State s){
		this.currentState = s;
	}
	
	public State getState(){
		return currentState;
	}
	
	/**
	 * @return the current flow: during green, flow equals greenFlow, during orange flow = greenFlow / 2
	 */
	public int getFlow(){
		return flow;
	}
	
	public int getGreenFlow(){
		return greenFlow;
	}
	
	public int getCapacity(){
		return capacity;
	}
	
	public PriorityQueue<Vehicle> getVehicles(){
		return vehicles;
	}
	
	public int getTrafficSize(){
		return vehicles.size();
	}
	
	
	/**
	 * This method is called every minute to either wait or switch lights
	 * After switching the light, the time waited is reset to 0;
	 * Also, for GREEN and YELLOW, the flow is adjusted
	 */
	public void iterate(){
		if(waited >= currentState.time){		// if the light has been ON for its set time
			switch(currentState){
			case RED:
				currentState = State.GREEN;		// going to Green, flow needs to be reset to GreenFLow
				flow = greenFlow;
				break;
			case YELLOW:
				currentState = State.RED;
				flow = 0;
				break;
			case GREEN:
				currentState = State.YELLOW;	// going to Yellow, flow needs to be reduced
				flow = greenFlow/2;
			}
			waited = 0;							// reset timer
		}
		else
			waited++;							// if light has not been ON long enough, increment time
	}
	
	/**
	 * This method will add a vehicle to the PriorityQueueSLL only if the capacity allows this
	 * @param v is the vehicle to be added. Its priority is used to determine its place in the queue
	 */
	public void addVehicle(Vehicle v) throws InsufficientCapacityException{
		if(vehicles.size() < capacity){
			int priority = 2;		// this is the LOWEST priority
			if(v.isPriorityVehicle())
				priority = 1;		// a lower number means a HIGHER priority !!
			vehicles.push(v, priority);
		}
		else{
			System.out.println("ERROR : CAR LOST ! CAPACITY DOES NOT ALLOW ADDING A VEHICLE");
			throw new InsufficientCapacityException(capacity);
		}
			
		
		// else do nothing
	}
	
	/**
	 * @returns the first vehicle in the PriorityQueueSLL. 
	 * Also this vehicle is now dropped from the queue
	 */
	public Vehicle releaseVehicle(){
		return vehicles.pop();
	}
	
	/**
	 * @returns the first vehicle in the PriorityQueueSLL
	 */
	public Vehicle getFirstVehicle(){
		return vehicles.top();
	}


}
