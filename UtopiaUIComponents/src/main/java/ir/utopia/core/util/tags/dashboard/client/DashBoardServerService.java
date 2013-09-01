package ir.utopia.core.util.tags.dashboard.client;

import ir.utopia.core.util.tags.dashboard.client.model.TransitionalUsecaseInfo;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseSearchCriteria;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class DashBoardServerService {
	private static DashBoardServerService instance;
	private DashboardClientServiceAsync proxy;
	
	private DashBoardServerService(){
		Map<String,List<String>>paramsMap= com.google.gwt.user.client.Window.Location.getParameterMap();
		StringBuffer bufferParameters=new StringBuffer();
		int index=0;
		if(paramsMap!=null){
			for(String param:paramsMap.keySet()){
				List<String>values= paramsMap.get(param);
				if(values!=null&&values.size()>0){
					for(String value:values){
						if(index>0){
							bufferParameters.append("&");
						}
						bufferParameters.append(param).append("=").append(value);
					}
				}
			}
		}
		
		  proxy = (DashboardClientServiceAsync) GWT
          .create(DashboardClientService.class);
				((ServiceDefTarget) proxy).setServiceEntryPoint(GWT.getHostPageBaseURL() +"getTransitional_Co_Wf_DashBoard.htm"+ (bufferParameters.length()>0?"?"+bufferParameters:"" ));
}

  
	public void getTransitionalUsecases(AsyncCallback<List<TransitionalUsecaseInfo>> callback){
		proxy.getTransitionalUsecases(callback);
	}
	
	public void getDashBoardRecords(UsecaseSearchCriteria criteria,
			AsyncCallback<SearchPageData> callback){
		proxy.getDashBoardRecords(criteria, callback);
	}
	
	public static DashBoardServerService getServer(){
		if(instance==null){
			instance=new DashBoardServerService();
		}
		return instance;
	}
	
	public void markAs(Long usecaseId,Long recordId,boolean read,AsyncCallback<ExecutionResult>callback){
		proxy.markAs(usecaseId, recordId, read, callback);
	}
}
