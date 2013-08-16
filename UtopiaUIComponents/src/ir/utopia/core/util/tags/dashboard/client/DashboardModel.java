package ir.utopia.core.util.tags.dashboard.client;

import ir.utopia.core.util.tags.dashboard.client.model.DashboardRecord;
import ir.utopia.core.util.tags.dashboard.client.model.TransitionalUsecaseInfo;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseSearchCriteria;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DashboardModel {
	List<DashboardModelListener> listenersList=new ArrayList<DashboardModelListener>();
	final DashBoardConstants constants=(DashBoardConstants)GWT.create(DashBoardConstants.class);
	List<TransitionalUsecaseInfo> transitions;
	
	TransitionalUsecaseInfo current;
	UsecaseSearchCriteria currentCriteria;
	SearchPageData searchResult;
	
//**********************************************************************************************************************	
	public void loadAccessibleTranistions(){
		
	    DashBoardServerService.getServer().getTransitionalUsecases(new AsyncCallback<List<TransitionalUsecaseInfo>>() {
			
			@Override
			public void onSuccess(List<TransitionalUsecaseInfo> result) {
				transitions=result;
				fireTransitionInfoLoaded(true);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				fireTransitionInfoLoaded(false);
			}
		});
	
	}
//**********************************************************************************************************************
	public List<TransitionalUsecaseInfo> getUsecaseTransitionInfos(){
		return transitions;
	}
//**********************************************************************************************************************
	public void addListener(DashboardModelListener listener){
		listenersList.add(listener);
	}
//**********************************************************************************************************************
	protected void fireTransitionInfoLoaded(boolean success){
		for(DashboardModelListener listener:listenersList){
			listener.modelLoaded(success);
		}
	}
//**********************************************************************************************************************
	protected void fireDashboardCriteriaChanged(UsecaseSearchCriteria criteria){
		for(DashboardModelListener listener:listenersList){
			listener.dashSearchCriterialChanged(criteria);
		}
	}
//**********************************************************************************************************************	
	public TransitionalUsecaseInfo getCurrent(){
		return current;
	}
//**********************************************************************************************************************
	public void setCurrent(TransitionalUsecaseInfo current){
		this.current=current;
	}
//************************************************************************************************************************
	public UsecaseSearchCriteria getCurrentCriteria() {
		return currentCriteria;
	}
//************************************************************************************************************************
	public void setCurrentCriteria(UsecaseSearchCriteria currentCriteria,boolean refresh) {
		this.currentCriteria = currentCriteria;
		if(refresh){
			fireDashboardCriteriaChanged(currentCriteria);
		}
	}
//************************************************************************************************************************
	public void updateRecord(DashboardRecord record){
		fireRecordUpdated(record);
	}
//************************************************************************************************************************
	private void fireRecordUpdated(DashboardRecord record) {
		for(DashboardModelListener listener:listenersList){
			listener.recordUpdated(record);
		}
}
//************************************************************************************************************************
	public void loadCriteriaData(AsyncCallback<SearchPageData> callback){
		loadCriteriaData(currentCriteria, callback);
	}
//************************************************************************************************************************
	public void loadCriteriaData(UsecaseSearchCriteria criteria, AsyncCallback<SearchPageData> callback){
		DashBoardServerService.getServer().getDashBoardRecords(criteria, callback);
	}
//************************************************************************************************************************
	public void markAsRead(DashboardRecord record,boolean read){
		record.setRead(read);
		fireRecordUpdated(record);
	}
//************************************************************************************************************************
}
