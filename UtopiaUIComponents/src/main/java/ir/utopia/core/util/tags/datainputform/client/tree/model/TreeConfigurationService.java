package ir.utopia.core.util.tags.datainputform.client.tree.model;

import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface TreeConfigurationService extends RemoteService {

	List<TreeNode> getNodes(TreeNode node);
	
	
	// return Children by parent Id
	public List<TreeNode> getChildren(TreeNode parent);

	// return true if the object have not child
	public Boolean lastChild(TreeNode id);

	// return true if the object can be Edit
	public Boolean isEditable(TreeNode id);

	// return true if the object can be delete
	public Boolean isDeletable(TreeNode id);
	
	
	//Delete Object  By id
	public ProcessExecutionResult  deleteNode(TreeNode node);
	

	//update Object and 
	public ProcessExecutionResult updateNode(TreeNode node);
	
	
	//return true if the object can drag and drop
	public Boolean dragable(TreeNode id);
	
  public void setObject(TreeNode obj);
  public TreeNode getObject();
  
  public void addObject(TreeNode obj);
  
  public ArrayList<TreeNode>  loadIntialize();

}
