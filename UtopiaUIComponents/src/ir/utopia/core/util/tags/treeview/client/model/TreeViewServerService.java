package ir.utopia.core.util.tags.treeview.client.model;

import ir.utopia.core.util.tags.datainputform.client.tree.model.TreeNode;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class TreeViewServerService {
	private static TreeViewServerService instance;
	private TreeViewDataHandlerAsync proxy;
	
	private TreeViewServerService(){
		  proxy = (TreeViewDataHandlerAsync) GWT
          .create(TreeViewDataHandler.class);
				((ServiceDefTarget) proxy).setServiceEntryPoint(GWT.getHostPageBaseURL() + "DataInput-service.json");
}

  
	
	public static TreeViewServerService getServer(){
		if(instance==null){
			instance=new TreeViewServerService();
		}
		return instance;
	}

	
	public void loadChildren(String usecaseName,String formClass,Long nodeId, AsyncCallback<TreeNode[]> callback){
		proxy.loadchildren(usecaseName,formClass, nodeId,  callback);
	}
	
}
