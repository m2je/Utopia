package ir.utopia.core.util.tags.datainputform.client.searchwidget.model;


import java.util.List;

import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchPageServiceAsync{

	public void getSearchItems(String usecase,String formClass, AsyncCallback<SearchPageModel> callback);
	
	public void getSearchResult(String usecase,String formClass,String [][]conditions,String defaultCondition,String []orderby,int from,int fetchSize,List<ContextItem>contextItems, AsyncCallback<SearchPageData> callback);

	void prepareReport(String usecaseName, String formClass, Long[] reordIds,
			String orderByCol, AsyncCallback<Long> callback);

	void getImportTypes(AsyncCallback<UILookupInfo> callback);

	void getImportConfigurations( String usecaseId,
			AsyncCallback<UILookupInfo> callback);

	
	
}
