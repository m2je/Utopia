package ir.utopia.core.util.tags.datainputform.client.tree.model;

import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TreeConfigurationServiceAsync {

	void addObject(TreeNode obj, AsyncCallback<Void> callback);

	void deleteNode(TreeNode node, AsyncCallback<ProcessExecutionResult> callback);

	void dragable(TreeNode id, AsyncCallback<Boolean> callback);

	void getChildren(TreeNode parent, AsyncCallback<List<TreeNode>> callback);

	void getNodes(TreeNode node, AsyncCallback<List<TreeNode>> callback);

	void getObject(AsyncCallback<TreeNode> callback);

	void isEditable(TreeNode id, AsyncCallback<Boolean> callback);

	void isDeletable(TreeNode id, AsyncCallback<Boolean> callback);

	void lastChild(TreeNode id, AsyncCallback<Boolean> callback);

	void setObject(TreeNode obj, AsyncCallback<Void> callback);

	void updateNode(TreeNode node,
			AsyncCallback<ProcessExecutionResult> callback);

	void loadIntialize(AsyncCallback<ArrayList<TreeNode>> asyncCallback);
	


}
