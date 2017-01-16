/**
 * TreeVehicleAction.java
 * @version 5/1/17
 * @author Laurent Declercq
 */
package treeActions;

import dataStructures.BinaryTree;
import dataStructures.DictionaryTree;
import dataStructures.BinaryTree.TreeNode;
import dataStructures.DictionaryTree.DictionaryPair;
import traffic.Vehicle;

/**
 * TreeVehicleAction is created when either the number of busy vehicles is needed or the totalTravelTime is needed
 */
public class TreeVehicleAction extends TreeAction{
	int countBusy;
	int totalTravelTime;
	
	public TreeVehicleAction(){
		countBusy = 0;
		totalTravelTime = 0;
	}
	
	public int getBusy(){
		return countBusy;
	}
	
	public int getTotalTravelTime(){
		return totalTravelTime;
	}
	
	
	public void run(BinaryTree.TreeNode n){
		if(n != null){
				DictionaryPair p = (DictionaryPair)(n.getValue());
				Vehicle currentV = (Vehicle)(p.getValue());
				
			totalTravelTime += currentV.getTravelTime();
				
			if(currentV.getCurrentPosition().compareTo(currentV.getDestination()) != 0)
				countBusy++;
		}	
	}

}
