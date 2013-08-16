package ir.utopia.core.util.tags.datainputform.client.searchwidget.model;


import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SearchPageService extends RemoteService{

	public SearchPageModel getSearchItems(String usecase,String formClass);
	
	public SearchPageData getSearchResult(String usecase,String formClass,String [][]conditions,String defaultCondition,String []orderby,int from,int fetchSize,List<ContextItem>contextItems);
	
	public Long prepareReport(String usecaseName,String formClass,Long []recordIds,String orderByCol);
	
	public UILookupInfo getImportTypes();
	
	public UILookupInfo getImportConfigurations(String usecase);
	
	
}
