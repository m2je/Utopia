package ir.utopia.core.util.tags.datainputform.client.searchwidget.model;

import java.util.List;

import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class SearchPageServerService {

	private static SearchPageServerService instance;
	private SearchPageServiceAsync proxy;
	
	private SearchPageServerService(){
		  proxy = (SearchPageServiceAsync) GWT.create(SearchPageService.class);
				((ServiceDefTarget) proxy).setServiceEntryPoint(GWT.getHostPageBaseURL() + "DataInput-service.json");
		  
}
	  public static SearchPageServerService getServer(){
			if(instance==null){
				instance=new SearchPageServerService();
			}
			return instance;
		}		  

	  public void getSearchPageModel(String usecaseName,String formClass,AsyncCallback<SearchPageModel>callback){
		  proxy.getSearchItems(usecaseName, formClass, callback);
	  }
	  
	  public void getSearchResult(String usecaseName,String formClass,String [][]conditions,String defaultCondition,String []orderby,int from,int fetchSize,List<ContextItem>contextItems,AsyncCallback<SearchPageData> callback){
		  proxy.getSearchResult(usecaseName, formClass,conditions,defaultCondition,orderby, from, fetchSize,contextItems, callback);
	  }
	  
	  public void prepareReport(String usecaseName,String formClass,Long []reordIds,String orderByCol,AsyncCallback<Long>callback){
		  proxy.prepareReport(usecaseName, formClass, reordIds, orderByCol, callback);
	  }
	  
	  public void loadFileFormats(AsyncCallback<UILookupInfo> callback){
			proxy.getImportTypes(callback);
		}
		
		public void getImportConfigurations(String usecaseId, AsyncCallback<UILookupInfo> callback){
			proxy.getImportConfigurations(usecaseId, callback);
		}
}
