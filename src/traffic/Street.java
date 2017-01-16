/**
 * Street.java
 * @version 22/11/16
 * @author Laurent Declercq
 */
package traffic;

import dataStructures.*;


/**
 * class Street is an implementation of a street with a specific travelTime 
 * It has properties such as a queue of VehiclePairs with a certain capacity. It has a method to add these vehicles or to return them
 * 
 * @version 1/11/16
 * @author Laurent Declercq
 *
 */
public class Street{
	 
//-------------------------------------------------------------------------------------------------
	/**
	 * class VehiclePair combines a vehicle with its remaining time in a certain street
	 * This VehiclePair will be put in the streetTraffic, not the vehicle alone.
	 * @version 1/11/16
	 * @author laurentdeclercq
	 *
	 */
	private class VehiclePair implements Comparable<VehiclePair>{
		private Vehicle car;
		private int remainingTime;
		
		/**
		 * CONSTRUCTOR for VehiclePair
		 * @param v is the vehicle given to this pair
		 */
		private VehiclePair(Vehicle v, int travelingTime){
			car = v;
			remainingTime = travelingTime;
		}
		
		/**
		 * this compareTo will return a positive number when this vPair has a higher remaining time
		 * than the other vPair
		 * @param o
		 * @return
		 */
		public int compareTo(VehiclePair o){
			VehiclePair p2 = o;
			return remainingTime - p2.remainingTime;
		}
		
		
		public String toString(){
			return "Vehicle: " + car + " , remaining time: " + remainingTime;
		}
		
		protected Vehicle getVehicle(){
			return car;
		}
		
		/*
		protected int getRemainingTime(){
			return remainingTime;
		}
		
		protected void setRemainingTime(int t){
			if(t > 0)
				this.remainingTime = t;
		}
		*/
		
		protected void decrementTime(){
			if( remainingTime > 0)
				remainingTime--;
			else
				System.out.println("Traffic Jam");
		}
	} // end class VehiclePair
	
//---------------------------------------------------------------------------------------------------	

	
	
	/** time required to reach the next crossroad */
	private int travelingTime = 0;
	
	/** number of cars that can be on that street */
	private int capacity;
	
	/** A 'queue' of all cars currently on the street */
	private CircularVector<VehiclePair> streetTraffic;	// circular vector used instead of Queue because 
														// EACH vehicle (the remainingTime) in the vector 
														// needs to be adjusted each iteration
	
	/**
	 * CONSTRUCTOR for Street : it is given a capacity and travelingTime. Also an empty 'queue' is created
	 * @param capacity
	 * @param travelingTime
	 */
	public Street(int capacity, int travelingTime){
		this.capacity = capacity;
		this.travelingTime = travelingTime;
		streetTraffic = new CircularVector<VehiclePair>(capacity);
	}

	/**
	 * METHODS for Street
	 */
	
	public String toString(){
		return "A street with travelingTime: " + String.valueOf(travelingTime) + " and capacity: " + String.valueOf(capacity);
	}
	
	public int compareTo(Object o){
		return 0; 	// not a good compareTo method but shows that streets cannot be compared
	}
	
	public int getCapacity(){
		return capacity;
	}
	
	public boolean full(){
		return capacity == streetTraffic.size();
	}
	
	public void setCapacity(int cap){
		if(cap >= 0)
			capacity = cap;
		else
			System.out.println(this.toString() + ": Unable to set a negative capacity");
	}
	
	public int getTravelingTime(){
		return travelingTime;
	}
	
	public void setTravelingTime(int newT){
		if (newT >= 0)
			travelingTime = newT;
		else
			System.out.println(this.toString() + ": Unable to set a negative traveling time");
	}
	
	public CircularVector<VehiclePair> getVehicles(){
		return streetTraffic;
	}
	
	
	/**
	 * This methods only adds a vehicle (ADDFIRST) , 
	 * together with the travelingtime as remainingtime to the 'queue' (circularVector) of VehiclePairs
	 * If the capacity is not yet reached.
	 * @param v is the vehicle to be added
	 */
	public void addVehicle(Vehicle v) throws InsufficientCapacityException{
		if(streetTraffic.size() < capacity){
			VehiclePair vPair = new VehiclePair(v,travelingTime);
			streetTraffic.addFirst(vPair);
		}
		else{
			System.out.println("Insufficient Capacity at street!");
			throw new InsufficientCapacityException(capacity);
		}
	}
	
	
	/**
	 * This releases a vehicle from the street. 
	 * It returns the vehicle and removes the VehiclePair from the vector
	 * But only if that vehicle has a remainingTime of 0
	 * @return a Vehicle
	 */
	public Vehicle releaseVehicle(){
		if(streetTraffic.getLast() != null){
			if(streetTraffic.getLast().remainingTime == 0){
				Vehicle aCar = streetTraffic.getLast().getVehicle(); // get the vehicle from the last vehiclePair
				
				streetTraffic.removeLast();	// remove the whole vehiclePair
				return aCar;
			}
		}
		return null; 		// else don't release
	}
	
	
	/**
	 * each time, the remainingTime of each vehiclePair from last to first is decremented
	 */
	public void iterate(){
		for(int i=streetTraffic.size()-1; i>=0 ; i--){				// loop over the queue of VehiclePairs from last to first (FIFO)
			VehiclePair current = streetTraffic.get(i);
			current.decrementTime();					// decrement time at current vehicle
			// NOTE : The travelTime of cars is Incremented in CARREGISTRY !
		}
	}
	

	
	

}
