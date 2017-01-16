/**
 * City.java
 * @version 30/12/16
 * @author Laurent Declercq
 */

package traffic;

import dataStructures.*;
import traffic.CrossRoad.Orientation;
import traffic.TrafficLight.State;

/**
 * City Class is based on interactions of CROSSROADS. It has 2 structures that work side by side : CityMatrix and CityGraph.
 * CityMatrix is used for reaching the right elements and basic iterations
 * CityGraph is needed for path-calculations.
 */
public class City {
	
	private Matrix<CrossRoad> cityMatrix;
	private Graph<CrossRoad> cityGraph;
	private String name;
	

	/*
	 * CONSTRUCTORS
	 */
	
	/**
	 * Automatically Sets up a city of identical crossroads based on the following given information
	 * @param citySize		: width or length of the cityMatrix
	 * @param lightFlow		: # of vehicles that pass per cycle at GREEN light
	 * @param lightCap		: # of vehicles the light can take
	 * @param streetCap		: # of vehicles the street can take
	 * @param streetTime	: time it takes to travel accross the street
	 */
	public City(String name, int cityRows, int cityCols, int lightFlow, int lightCap, int streetCap, int streetTime){
		this.name = name;
		cityMatrix = new Matrix<CrossRoad>(cityRows, cityCols);
		cityGraph = new Graph<CrossRoad>();
		
		setupCityMatrix(lightFlow, lightCap, streetCap, streetTime);
		System.out.println(cityMatrix);
		setupCityGraph();
	}
	
	/**
	 * This constructor calls the other constructor with default values for lightFlow, lightCap, streetCap and streetTime
	 * @param citySize is the width and height of the city
	 */
	public City(String name, int cityRows, int cityCols){		
		this(name, cityRows, cityCols, 2,10,10,3);		// calls bigger constructor using (citySize, lightFlow, lightCap, streetCap, streetTime)
	}
	
	public String toString(){
		return name + "\n" + cityMatrix.toString();
	}
	
	
	/*
	 * HELPER METHODS
	 */
	
	/**
	 * Creates instances of CrossRoads and places them inside the matrix. The name is set at CR1, CR2 ...
	 * @param lightFlow, lightCap, streetCap, streetTime
	 */
	private void setupCityMatrix(int lightFlow, int lightCap, int streetCap, int streetTime){
		int crNumber = 1;
		String crName = "CR" + crNumber;
		for(int row=0;row<cityMatrix.getNumberRows();row++){
			for(int col=0;col<cityMatrix.getNumberCols();col++){
				cityMatrix.set(row, col, new CrossRoad(crName, lightFlow, lightCap, streetCap, streetTime));
				crNumber++;
				crName = "CR" + crNumber;
			}
		}
	}
	
	/**
	 * Looks at the existing matrix and places the corresponding CrossRoads inside a graph with the correct edges
	 */
	private void setupCityGraph(){
		for(int row=0;row<cityMatrix.getNumberRows();row++){
			//System.out.println("---- current row: " + row + " ----");
			for(int col=0;col<cityMatrix.getNumberCols();col++){
				//System.out.println("- current cell: r" + row + ", c" + col +" -");
				cityGraph.addNode(cityMatrix.get(row, col));	// add all the CR as nodes to the graph
			}
		}
		//System.out.println("All nodes added.");
		
		System.out.println("Adding edges to the graph");
		for(int row=0;row<cityMatrix.getNumberRows();row++){
			System.out.println("---- current row: " + row + " ----");
			for(int col=0;col<cityMatrix.getNumberCols();col++){
				System.out.println("- current cell: r" + row + ", c" + col +" -");
				
				CrossRoad current = cityMatrix.get(row, col);	// link all the neighbours to each other using edges
				CrossRoad neighbour;
				double weight;									// the weight is recomputed at each iteration
				
				System.out.println("Current:" + current + " at index r" + row + ", c" + col );
				for(Orientation o: Orientation.values()){
					neighbour = getNeighbourCR(row,col,o); // gives null when neighbour is out of bound
					System.out.println(o + " neighbour of " + current + " = " + neighbour);
					
					if(neighbour != null){
						int trafficDuration = Integer.MAX_VALUE;
						int greenFlow = neighbour.getLight(o).getGreenFlow();
						if(greenFlow != 0)
							trafficDuration = neighbour.getTraffic(o.getOpposite()) / neighbour.getLight(o).getGreenFlow();
 
						weight = (double)(current.getStreetTime(o) + trafficDuration );
						cityGraph.addEdge(current, neighbour, weight);
						System.out.println("\t"+ neighbour.getName() +  " added as edge of " + current);
					}
	
				}
			}
		}	
	}
	
	/*
	 * Renews the values of the weights for all edges in the graph. 
	 * Based on this, another optimal path might be found
	 */
	private void recomputeWeights(){
		
		for(int row=0;row<cityMatrix.getNumberRows();row++){
			for(int col=0;col<cityMatrix.getNumberCols();col++){
				
				CrossRoad current = cityMatrix.get(row, col);
				CrossRoad neighbour;
				double weight;									
				
				for(Orientation o: Orientation.values()){
					neighbour = getNeighbourCR(row,col,o);
					if(neighbour != null){
						int trafficDuration = Integer.MAX_VALUE;
						int greenFlow = neighbour.getLight(o).getGreenFlow();
						if(greenFlow != 0)
							trafficDuration = neighbour.getTraffic(o.getOpposite()) / neighbour.getLight(o).getGreenFlow();						weight = (double)(current.getStreetTime(o) + trafficDuration );
						cityGraph.setWeight(current,neighbour,weight);	
					}					
				}
			}
		}
	}
	
	/*
	 * Since the matrix does not know Orientation, it needs a method to return the corresponding matrix cell
	 */
	private CrossRoad getNeighbourCR(int row , int col, Orientation o){
		CrossRoad neighbour = null;
		switch(o){
		case North:
			if(row-1>=0)
				neighbour =  cityMatrix.get(row-1, col);
			break;

		case East:
			if (col+1 <cityMatrix.getNumberCols())
				neighbour =  cityMatrix.get(row, col+1);
			break;
		case South:
			if (row+1 <cityMatrix.getNumberRows())
				neighbour =  cityMatrix.get(row+1, col);
			break;
		case West:
			if (col-1 >=0)
				neighbour =  cityMatrix.get(row, col-1);
			break;
		}

		return neighbour;
	}
	
	/*
	 * Since the matrix does not know Orientation, it needs a method to return Orientation based on the corresponding index differences
	 * if nextCR lies below currentCR, it returns SOUTH
	 * It takes the CR names as strings and first finds the matching indexes
	 */
	public Orientation getDirection(String currentCR, String nextCR){
		CrossRoad cr ;
		
		int rowC = 0, colC = 0, rowNext = 0, colNext = 0;
	
		for(int row=0;row<cityMatrix.getNumberRows();row++){
			for(int col=0;col<cityMatrix.getNumberCols();col++){
				cr = cityMatrix.get(row, col);
				if(cr.getName().equals(currentCR)){
					rowC = row;
					colC = col;
				}
				if(cr.getName().equals(nextCR)){
					rowNext = row;
					colNext = col;
				}
			}
		}
		if(rowNext == rowC && colNext == colC){
			System.out.println("Error: In getDirection, at least one of the names of CR was not found. Nothing returned");
			System.out.println("Given names were: currentCR " + currentCR + " , nextCR " + nextCR);
			return null;
		}	
		else if(rowNext-rowC < 0)
			return Orientation.North;
		else if(colNext-colC > 0)
			return Orientation.East;
		else if(rowNext-rowC > 0)
			return Orientation.South;
		else
			return Orientation.West;
	}
	
	/**
	 * GETTERS AND SETTERS
	 */
	
	
	/** This allows for manually changing the CR name using the matrix-position */
	public void setCRName(int row, int col, String newName){
		cityMatrix.get(row,col).setName(newName);
	}
	
	/** Retrieves the name of the CR using the matrix-position */
	public String getCRName(int row, int col){
		return cityMatrix.get(row, col).getName();
	}
	
	public Graph<CrossRoad> getCityGraph(){
		return cityGraph;
	}
	
	public Matrix<CrossRoad> getCityMatrix(){
		return cityMatrix;
	}

	
	public CrossRoad findCR(String name){
		CrossRoad tmp = new CrossRoad(name);
		tmp = cityGraph.find(tmp);
		if (tmp == null)
			System.out.println("Crossroad with name : " + name + " not found!");
		return tmp;
	}

	/** 
	 * Changes all the values of the existing crossroad-objects inside the matrix
	 * If the objects themselves would be replaced, the existing graph would loose the connection with the corresponding CR 
	 */
	public void changeAllCrossRoads(int newLightFlow, int newLightCap, int newStreetCap, int newStreetTime){
		for(int row=0;row<cityMatrix.getNumberRows();row++){
			for(int col=0;col<cityMatrix.getNumberCols();col++){
				CrossRoad current = cityMatrix.get(row, col);
				current.changeCRValues(newLightFlow, newLightCap, newStreetCap, newStreetTime);
			}
		}
	}
	
	/** Changes all values of 1 existing CR, based on new values */
	public void changeCR(int row, int col, int newLightFlow, int newLightCap, int newStreetCap, int newStreetTime){
		CrossRoad current = cityMatrix.get(row, col);
		current.changeCRValues( newLightFlow, newLightCap, newStreetCap, newStreetTime);
	}
	
	/**
	 * Changes all values of 1 existing CR, based on a given CR
	 * This method assigns the existing CR in the matrix all new values corresponding to the given CrossRoad
	 * The new CR cannot simply replace the old CR because the cityGraph has already created nodes for the existing objects
	 * If the CR would simply be replaced, the cityGraph would still point to the old object.
	 * @param row
	 * @param col
	 * @param cr
	 */
	public void changeCRreferral(int row, int col, CrossRoad cr){
		CrossRoad current = cityMatrix.get(row, col);
		current.setName(cr.getName());
		for(Orientation o : Orientation.values()){
			current.getLight(o).setCapacity(cr.getLight(o).getCapacity());
			current.getLight(o).setGreenFlow(cr.getLight(o).getGreenFlow());
			for(State s : State.values()){
				current.getLight(o).setTimeForState(s, cr.getLight(o).getTimeForState(s));
			}
			current.getStreet(o).setCapacity(cr.getStreet(o).getCapacity());
			current.getStreet(o).setTravelingTime(cr.getStreet(o).getTravelingTime());
		}
	}
	
	
	/** Adds a vehicle to the TrafficLight at a given orientation of a CR with the given name */
	public void addVehicle(String crName, Orientation o, Vehicle v){
		CrossRoad current = findCR(crName);
		if (current != null)
			try{
				current.addVehicle(o, v);
			} catch(InsufficientCapacityException e){
				System.out.println("In city: trying to add vehicle to crossRoad but FULL !");
				throw new RuntimeException(e);
			}
		else
			System.out.println("Unable to add vehicle to : " + crName);
	}
	
	/** Changes the weight of the edge between from and to. BUT ONLY if from and to are directly connected ! */
	public void changeRoadLength(String from, String to, Double newLength){
		
		CrossRoad from_tmp = new CrossRoad(from);
		CrossRoad to_tmp = new CrossRoad(to);
		
		cityGraph.setWeight(from_tmp, to_tmp, newLength); // setWeight() does nothing when from and to are not connected!
	}
	
	/**
	 * For a given CR (row and col), all streets from neighbour CR's are checked for available vehicles. 
	 * These are added to the corresponding TrafficLight of the given CR, but only if the TrafficLight is not jammed
	 * @param row (in matrix)
	 * @param col (in matrix)
	 */
	private void fromStreetsToCR(int row, int col){
		
		CrossRoad current = cityMatrix.get(row, col);
		CrossRoad neighbour;
		Vehicle v;
		
		// All connected streets can have released Cars --> add to CR if light not FULL
		for(Orientation o : Orientation.values()){
			neighbour = getNeighbourCR(row,col,o);
			
			// this will loop until ALL the cars that can be released from the street, are released
			while(true){
				if (neighbour != null && ! current.getLight(o).full()){
					v = neighbour.getStreet(o.getOpposite()).releaseVehicle();
					
					if(v==null)
						break;
					else{
						if(v.getDestination() == current.getName()){
							// we don't add the car to the destination CrossRoad 
							// since this will complicate the queue of cars that actually need to travel somewhere else
							v.addLocationToRoute(current.getName());
							System.out.println("Vehicle : " + v.toString() + "arrived!");
							System.out.print("Followed Route of " + v.getName() + " : ");
							v.getFollowedRoute().print();
						}
						else{
							try{
								current.addVehicle(o, v);
								}
							catch(InsufficientCapacityException e){
								System.out.println("Error: unable to add vehicle to CrossRoad");
								throw new RuntimeException (e);
							}
						}
					}
				}
				else
					break;
			}
		} // end for all orientations
	} // end fromStreetsToCR
	
	
	/**
	 * The given CR iterates (lights and streets iterate + cars released from lights when possible)
	 * @param row (in cityMatrix)
	 * @param col (in cityMatrix)
	 */
	private void fromCRToStreets(int row,int col){
		CrossRoad current = cityMatrix.get(row, col);
		current.iterate(this); 	// when crossroads iterate, the lights and streets iterate
								// AND the cars are released from the lights to the streets 
								// HOWEVER, the cars increment traveltime inside CarRegistry !
	}
	

	public void iterate(){
		recomputeWeights(); // only the dynamic vehicles will use these new weights !
		
		for(int row=0;row<cityMatrix.getNumberRows();row++){
			for(int col=0;col<cityMatrix.getNumberCols();col++){
				
				fromStreetsToCR(row,col);
			}
		}
		
		
		for(int row=0;row<cityMatrix.getNumberRows();row++){
			for(int col=0;col<cityMatrix.getNumberCols();col++){
				
				fromCRToStreets(row,col);
			}
		}
	}
	

	// question : In Vehicle, now you are always finding a CR based on the name and you are then afterwards not pushing the CR but the name
	// would it be better to store the crossroads in the vehicle route ?
	
}
