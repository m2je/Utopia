package ir.utopia.core.util.tags.datainputform.client;

import java.util.List;
import java.util.Map;

import ir.utopia.core.util.tags.datainputform.client.model.DataInputDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputFormModel;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputService;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputServiceAsync;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class DataInputServerService {
	private static DataInputServerService instance;
	private DataInputServiceAsync proxy;
	
	private DataInputServerService(){
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
						index++;
					}
				}
			}
		}
		
		  proxy = (DataInputServiceAsync) GWT
          .create(DataInputService.class);
				((ServiceDefTarget) proxy).setServiceEntryPoint(GWT.getHostPageBaseURL() +"DataInput-service.json"+ (bufferParameters.length()>0?"?"+bufferParameters:"" ));
}

  
	
	public static DataInputServerService getServer(){
		if(instance==null){
			instance=new DataInputServerService();
		}
		return instance;
	}
	
	public void getDataInputForm(String formClass,String usecaseName,String actionName,AsyncCallback<DataInputFormModel> callbackHandler){
		 proxy.getDataInputForm(formClass,usecaseName,actionName, callbackHandler);
	}
	
	public void getDataInputFormData(String formClass,String usecaseName,String actionName,String recordId,boolean reload ,String token,AsyncCallback<DataInputDataModel> callbackHandler){
		 proxy.getFormData(formClass, usecaseName, actionName, recordId,reload,token, callbackHandler);
	}

	public void reloadLooKup(Long usecaseActionId,String lookupParmeter,String []dependentFields,String [] dependesValue,AsyncCallback<UILookupInfo>handler){
		proxy.reloadLookup(usecaseActionId, lookupParmeter, dependentFields, dependesValue, handler);
	}
	public void reloadLooKup(String formClass,String usecaseName,String actionName,String lookupParmeter,String []dependentFields,String [] dependesValue,AsyncCallback<UILookupInfo>handler){
		proxy.reloadLookup(formClass, usecaseName, actionName, lookupParmeter, dependentFields, dependesValue, handler);
	}
	
}
