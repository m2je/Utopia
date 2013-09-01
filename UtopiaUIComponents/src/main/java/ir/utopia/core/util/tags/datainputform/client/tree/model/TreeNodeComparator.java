package ir.utopia.core.util.tags.datainputform.client.tree.model;


import java.util.Comparator;

public class TreeNodeComparator implements Comparator< TreeNode> {

	@Override
	public int compare(TreeNode o1, TreeNode o2) {
		String name1=o1.getName().toString();
		String name2=o2.getName().toString();
		if(name1==null)return -1;
		return name1.compareTo(name2);
	}

}
