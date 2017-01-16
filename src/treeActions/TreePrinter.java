package treeActions;

import dataStructures.BinaryTree;
import dataStructures.BinaryTree.TreeNode;

public class TreePrinter extends TreeAction{
	
	public void run(BinaryTree.TreeNode n){
		System.out.println(n.getValue());
	}

}
