package ir.utopia.core.util.tags.controlpanel.client;

import ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelService;
import ir.utopia.core.util.tags.controlpanel.client.model.ControlPanelServiceAsync;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class ControlPanelServerService {

	private static ControlPanelServerService instance;
	private ControlPanelServiceAsync proxy;
//****************************************************************************************	
	private ControlPanelServerService(){
		 proxy = (ControlPanelServiceAsync) GWT
		          .create(ControlPanelService.class);
						((ServiceDefTarget) proxy).setServiceEntryPoint(GWT.getHostPageBaseURL() + "ControlPanel-Service");
         
}
//****************************************************************************************
	public static ControlPanelServerService getServer(){
		if(instance==null){
			instance=new ControlPanelServerService();
		}
		return instance;
	}
//****************************************************************************************
	public void loadData(int type,AsyncCallback<UILookupInfo> callback){
		proxy.loadData(type,callback);
	}
//****************************************************************************************
	public void loadDetailData(Long []parentIds,int parentType,int rootType,AsyncCallback<UILookupInfo[]> callback){
		proxy.loadDetailData(parentIds,parentType,rootType, callback);
	}
//****************************************************************************************
	public void requestWindowNumber(AsyncCallback<Integer>callback){
		proxy.requestWindowNumber(callback);
	}
//****************************************************************************************
	public void getSaveResult(AsyncCallback<ExecutionResult>callback,int windowNumber){
		proxy.getExecutionResult("save", windowNumber, callback);
	}
	
}
