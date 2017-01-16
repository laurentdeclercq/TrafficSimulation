/**
 * Crossroad.java
 * @version 25/11/16
 * @author Laurent Declercq
 */
package traffic;

import traffic.TrafficLight.State;

/**
 * class CrossRoad combines 4 instances of class Street and 4 of TrafficLight
 * It sets the states of the trafficlights to sync them.
 * It can add vehicles to the crossroad and iterate over time
 * @version 25/11/16
 * @author Laurent Declercq
 */
public class CrossRoad implements Comparable<CrossRoad> {
// FEEDBACK : use an ENUM to refer to orientation, and instead of doing streetlist[0] you 
	// can then write streetList[orientation.NORTH] if you make sure that the enum is also given a value
	// also make a method inside the enum to getOppositeSide ---> so you dont need to use %2
	
	// but anyway, in the last part it is the CAR that decides the direction, so later on 
	// this functionality can be dropped from crossroad and moved to CAR
	
	private String name;
	
	// 4 TrafficLights (North, East, South and West)
	private TrafficLight[] lightList;
	
	// 4 Streets (North, East, South and West)
	private Street[] streetList;
	
	/** The crossroad has 4 orientations */
	public enum Orientation {
		North(0), East(1), South(2), West(3); // calls a different constructor depending on the value used (3, 2 or 1 in this case)
		
		private int sideNumber;
		
		private Orientation(int t){
			this.sideNumber = t;
		}
		
		// you don't need a method to get the time because you get this with currentState.time
		
		protected void setSideNumber(int t){
			this.sideNumber = t;
		}
		
		public Orientation getOpposite(){
			switch(this){
			case North:
				return South;
			case East:
				return West;
			case South:
				return North;
			case West:
				return East;
			}
			return null;
		}
	}
	
	
	// a constructor that takes TrafficLights and Streets
	public CrossRoad(	String name,
						TrafficLight lightN, TrafficLight lightE, TrafficLight lightS,  TrafficLight lightW, 
						Street streetN, Street streetE, Street streetS, Street streetW){
		this.name = name;
		this.lightList = new TrafficLight[] {lightN, lightE, lightS, lightW};
		this.streetList = new Street[] {streetN, streetE, streetS, streetW};
		// what is best, creating new objects that you receive from the parameters ?, or simply pointing to it?

		syncLights();


	}
	// a constructor that MAKES the lights and streets
	public CrossRoad( String name, int lightFlow, int lightCap, int streetCap, int streetTime){
		this.name = name;
		
		// make the lights inside the lightList
		this.lightList = new TrafficLight[4];
		
		// make the streets inside the streetList
		this.streetList = new Street[4];
		
		for(Orientation o : Orientation.values()){
			
			streetList[o.ordinal()] = new Street(streetCap, streetTime);
			// System.out.println("Current orientation value: " + o.ordinal());
			if(o == Orientation.North || o == Orientation.South)
				lightList[o.ordinal()] = new TrafficLight(lightFlow,lightCap,State.RED);	// set North and South to RED
			else
				lightList[o.ordinal()] = new TrafficLight(lightFlow,lightCap,State.GREEN);	// set East and West to GREEN
		}
		// here, no SyncLights() needed because it is already done above

		//System.out.println("All lights and streets at "+ this.name + " created");
	}
	
	public CrossRoad(String name){
		this(name, 0, 0, 0, 0);
	}
	
	private void syncLights(){
		// sync the lights		
		for (Orientation o : Orientation.values()){
			if(o == Orientation.North || o == Orientation.South)
				lightList[o.ordinal()].setState(State.RED);	// set North and South to RED
			else
				lightList[o.ordinal()].setState(State.GREEN);	// set East and West to GREEN
		}
	}
	
	public int compareTo(CrossRoad c2){
		return name.compareTo(c2.name);
	}
	
	public String toString(){
		/*
		String txt = "Crossroad " + name + " : \n" ;

		for(Orientation o : Orientation.values()){
			txt += "\t" + o + " : " + lightList[o.ordinal()].toString() + "\n";
		}
		return txt;
		*/
		return name;
	}
	
	
	/**
	 * GETTERS AND SETTERS
	 */
	
	
	public Street[] getStreetList(){
		return streetList;
	}
	
	
	public Street getStreet(Orientation o){
		return streetList[o.ordinal()];
	}
	
	
	public TrafficLight[] getLightList(){
		return lightList;
	}
	
	
	public TrafficLight getLight(Orientation o){
		return lightList[o.ordinal()];
	}
	
	
	public int getTraffic(Orientation o){
		return lightList[o.ordinal()].getTrafficSize();
	}
	
	
	public int getStreetTime(Orientation o){
		return streetList[o.ordinal()].getTravelingTime();
	}
	
	
	public String getName(){
		return name;
	}

	
	public void setName(String newName){
		this.name = newName;
	}
	
	
	/**
	 * OTHER METHODS
	 */
	
	
	/** Changes the configuration of the CR, with the same new values in all orientations */
	public void changeCRValues(int newLightFlow, int newLightCap, int newStreetCap, int newStreetTime){
		for(Orientation o: Orientation.values()){
			changeCRValuesInOrientation(o, newLightFlow, newLightCap, newStreetCap, newStreetTime);
		}
	}
	
	
	/** Changes the configuration of the CR for a given orientation only*/
	public void changeCRValuesInOrientation(Orientation o, int lightFlow, int lightCap, int streetTime, int streetCap){
		lightList[o.ordinal()].setGreenFlow(lightFlow);
		lightList[o.ordinal()].setCapacity(lightCap);
		streetList[o.ordinal()].setCapacity(streetCap);
		streetList[o.ordinal()].setTravelingTime(streetTime);
	}
	
	
	/** Adds a vehicle to the TrafficLight of the CR in the given orientation, but only if the capacity allows this*/
	public void addVehicle(Orientation o , Vehicle v) throws InsufficientCapacityException{
		try{
			lightList[o.ordinal()].addVehicle(v);
			v.addLocationToRoute(this.name);
		}
		catch(InsufficientCapacityException e){
			System.out.println("The light of " + name + "," + o + " has insufficient capacity");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * A CR iterates in the following steps:
	 * 1. iterate its lights and streets.
	 * 2. pass all priority vehicles from the light to the correct street (determined by the vehicle)
	 * 3. pass all normal vehicles.
	 * @param c = a city, needed to pass the vehicles in the right direction. A CR on its own does not have this knowledge
	 */
	public void iterate(City c){

		for(Orientation o : Orientation.values()){
			lightList[o.ordinal()].iterate();	// switch lights
			streetList[o.ordinal()].iterate();	// decrement remaining time in street for each car
			System.out.println(name + "," + o + " - LIGHT : " + lightList[o.ordinal()].getVehicles().toString());
			System.out.println(name + "," + o + " - STREET : " + streetList[o.ordinal()].getVehicles().toString());
		}
		System.out.println("-------------------------------------------------------------------------");
		System.out.println();
			// NOTE : TravelTime of cars is incremented in CARREGISTRY !
			// Reason : only 1 loop necessary and best way to keep track of all travellingTimes
		
		checkAndPassPrio(c);		// 1. All prio vehicles are passed
		checkAndPassNormal(c);		// 2. Normal vehicles are passed acoording to flow
		System.out.println("-------------------------------------------------------------------------");
		System.out.println();
		
	}// end iterate
	
	
	/*
	 * REMARK : reason for double code to pass prio and to pass normal:
	 * In this implementation, ALL priorityVehicles have priority over normal vehicles
	 * Normal vehicles could make PriorityVehicles wait IF the next street is FULL.
	 * When only 1 loop over the ORIENTATION, the following could happen: normal vehicles of NORTH still get passed faster 
	 * than priorityVehicles of EAST. That normal car can lead the next street to be full, blocking the priority Vehicle
	 */
	
	private void checkAndPassPrio(City c){
		// first priority vehicles can pass
		for(Orientation o : Orientation.values()){ // Loop over all lights to let vehicles pass when applicable
			TrafficLight currentLight = lightList[o.ordinal()];
			System.out.println(name + ", " + o + ": state = " + currentLight.getState());

			//System.out.println(name + ", " + o + " : Checking for priority vehicles");
			//System.out.println("Green Flow: " + currentLight.getGreenFlow() + " and currently: "+currentLight.getState());
			while(true){
				// if there are no more vehicles in front of the current light, stop the loop
				if(currentLight.getVehicles().isEmpty()){	// this is to avoid an error when trying to check priority on null
					//System.out.println(" ------> No more priority vehicles");
					break;
				}
				else{
					Vehicle firstVehicle = currentLight.getFirstVehicle();
					if(firstVehicle.isPriorityVehicle()){
						System.out.println(firstVehicle.getName() + " is priority vehicle!");
						Boolean passed = letVehiclePass(o,firstVehicle, c); // here NO garantee that it is passed
						if (! passed)
							break;	// This prevents endless loops
						else{
							System.out.println(" ------> " + firstVehicle.getName() + " passed!");
						}

					}
					else{
						//System.out.println(" ------> No more priority vehicles");
						break;
					}
				}
			}
		}
	}
	
	private void checkAndPassNormal(City c){
		// NOW, normal vehicles can pass corresponding to the flow
		for(Orientation o : Orientation.values()){
			
			int normalPassed = 0;
			//System.out.println(name + ", " + o + " : Checking for normal vehicles");
			TrafficLight currentLight = lightList[o.ordinal()];
			System.out.println(name + ", " + o + ": state = " + currentLight.getState());
			while(true){

				// if there are no more vehicles in front of the current light, stop the loop
				if(currentLight.getVehicles().isEmpty()){	// this is to avoid an error when trying to check priority on null
					//System.out.println(" ------> No more normal vehicles");
					break;
				}
				else if (normalPassed == currentLight.getFlow() || currentLight.getState() == State.RED){	// Passes maximum FLOW vehicles (at RED, flow is 0)
					System.out.println(	" ------> Max amount of cars passed (Light = " + currentLight.getState());
					break;
				}
				else{
					Vehicle firstVehicle = currentLight.getFirstVehicle();

					Boolean passed = letVehiclePass(o,firstVehicle,c);
					System.out.println("Passed: " + passed);
					if(!passed){
						System.out.println(" ------> First vehicle not passed : destination street FULL");
						break;
					}

					else{
						System.out.println(" ------> " + firstVehicle.getName() + " passed!");
						normalPassed++;
					}
				}	
			}
		}		
	}
		
	private Boolean letVehiclePass(Orientation o, Vehicle v, City c){
		Boolean passed = false;
		System.out.println("Trying to get vehicle " + v + " pass.");
		// makeTurn(c) returns NULL when current == destination
		Orientation oNext = v.makeTurn(c);
		if(oNext != null){
			if(streetList[oNext.ordinal()].full()){
				System.out.println(	name + ", orientation " + o + " : Traffic Jam at " + oNext + " street, "
									+ "vehicle cannot be released from traffic light");
			}
			else{
				try{
					v = lightList[o.ordinal()].releaseVehicle();
					streetList[oNext.ordinal()].addVehicle(v);
					passed = true;
					System.out.println(name + ", orientation" + o + " : Vehicle released to " + oNext + " street.");
				}catch(InsufficientCapacityException e){
					System.out.println("Insuffient Capacity at " + name + "," + o + " street!");
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		else{
			// else : the car is at its destination and is simply dropped. It can still be found in the carRegistry however
			// also, in the future the travelledTime will remain the same
			// This scenario will only occur when START = DESTINATION
			v = lightList[o.ordinal()].releaseVehicle();
			passed = true;
			System.out.println(v.getName() + " reached destination!");
		}	
		return passed;
	} // end letVehiclePass()

} // end CLASS CrossRoad

