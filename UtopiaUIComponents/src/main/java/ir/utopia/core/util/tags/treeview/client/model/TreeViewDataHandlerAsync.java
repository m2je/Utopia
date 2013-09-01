package ir.utopia.core.util.tags.treeview.client.model;

import ir.utopia.core.util.tags.datainputform.client.tree.model.TreeNode;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TreeViewDataHandlerAsync {

	void loadchildren(String usecaseName,String formClass,Long nodeId, AsyncCallback<TreeNode[]> callback);


}
