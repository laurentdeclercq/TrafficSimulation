/**
 * TreeSizeAction.java
 * @version 3/12/16
 * @author Laurent Declercq
 */

package treeActions;

import dataStructures.BinaryTree;
import dataStructures.BinaryTree.TreeNode;

public class TreeSizeAction extends TreeAction{
	int count;
	
	public TreeSizeAction(){
		count = 0;
	}
	
	// after traversing the whole tree, this returns the size of the tree
	public int getCount(){
		return count;
	}
	
	
	public void run(BinaryTree.TreeNode n){
		if(n != null)
			count++;
	}

}
