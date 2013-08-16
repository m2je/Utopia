package ir.utopia.core.util.tags.treeview.client.model;

import ir.utopia.core.util.tags.datainputform.client.tree.model.TreeNode;

import com.google.gwt.user.client.rpc.RemoteService;

public interface TreeViewDataHandler extends RemoteService{
	/**
	 * 
	 * @param nodeId
	 * @return
	 */
	public TreeNode[] loadchildren(String usecaseName,String formClass,Long nodeId);
	
	
	
}
