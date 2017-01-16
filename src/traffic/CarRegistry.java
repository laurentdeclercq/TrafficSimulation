/**
 * CarRegistry.java
 * @version 30/12/16
 * @author Laurent Declercq
 */

package traffic;

import dataStructures.*;
import dataStructures.BinaryTree.TreeNode;
import dataStructures.DictionaryTree.DictionaryPair;
import treeActions.*;



/**
 * CarRegistry class is used in the simulation as the place where all information about cars can be retrieved.
 * Also, for time complexity reasons, it is the CarRegistry that increments travelled time for all cars that did not yet finish
 * travelling from start to destination in the City.
 */
public class CarRegistry {
	
	/** A dictionary of cars, that can be found by their name */
	private DictionaryTree<String, Vehicle> data;
	

	public CarRegistry(){
		data = new DictionaryTree<String, Vehicle>();
	}
	
	public String toString(){
		return data.toString();
	}
	
	public void addVehicle(Vehicle v){
		data.add(v.getName(), v);
	}
	
	public int size(){
		return data.size();
	}
	
	/** Increments the travelTime of each car that did not yet finish. */	
	public void iterate(){
		data.getData().traverseDFS(new TreeAction(){
			public void run(BinaryTree.TreeNode n){
			DictionaryPair p = (DictionaryPair)(n.getValue());
			Vehicle currentV = (Vehicle)(p.getValue());
			Boolean finished = (currentV.getCurrentPosition().compareTo(currentV.getDestination()) == 0);
			if(! finished)
				currentV.incrementTravelTime(1);
			}
		});
	}
	
	public Vehicle find(String vName){
		Vehicle v = data.findKey(vName);
		if(v == null)
			System.out.println("NOT FOUND: Vehicle with name : "+ vName + ", returning null.");
		return v;
	}
	
	/** Returns true if all cars are at their destination */
	public boolean allFinished(){
		return ( getNumberBusy() == 0 );
	}
	
	
	/** Returns number of cars that are NOT yet at their destination */
	public int getNumberBusy(){
		TreeVehicleAction busyAction = new TreeVehicleAction();
		data.getData().traverseDFS(busyAction);
		return busyAction.getBusy();
	}
	
	
	/** Returns number of cars that have arrived */
	public int getNumberFinished(){
		int numberFinished = data.size() - getNumberBusy();
		return numberFinished;
	}
	
	
	public void print(){
		data.getData().traverseSorted(new TreeAction(){
			public void run(TreeNode n){
				DictionaryPair p = (DictionaryPair)(n.getValue());
				Vehicle currentV = (Vehicle)(p.getValue());
				System.out.println(currentV.getName() + " followed route :" + currentV.getFollowedRoute());
				System.out.println("Current Position: " + currentV.getCurrentPosition() + " and destination: " + currentV.getDestination()
				+ " are equal ? " + (currentV.getCurrentPosition().compareTo(currentV.getDestination()) == 0));
			}
		});
	}
	
	/** Returns the travelTime of a Vehicle with a given name, if that name is found in the dictionary */
	public int getTravelTimeVehicle(String vName){
		Vehicle v = data.findKey(vName);
		if(v != null)
			return v.getTravelTime();
		else{
			System.out.println("NOT FOUND: Vehicle with name : "+ vName + ", returning -1.");
			return -1;
		}
	}
	
	/** Returns number of cars that are NOT yet at their destination */
	public int getTotalTravelTime(){
		TreeVehicleAction travelAction = new TreeVehicleAction();
		data.getData().traverseDFS(travelAction);
		return travelAction.getTotalTravelTime();
	}

} // end Class CARREGISTRY
