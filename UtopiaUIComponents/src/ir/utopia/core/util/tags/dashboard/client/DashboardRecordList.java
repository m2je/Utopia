/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ir.utopia.core.util.tags.dashboard.client;

import ir.utopia.core.util.tags.dashboard.client.model.DashboardRecord;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseSearchCriteria;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.grid.AdvanceSearchGrid;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchCriteria;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.SearchPager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * A composite that displays a list of dashboard that can be selected.
 */
public class DashboardRecordList extends ResizeComposite implements DashboardModelListener {

	final DashBoardConstants constants=(DashBoardConstants)GWT.create(DashBoardConstants.class);
  /**
   * Callback when mail items are selected. 
   */
  public interface Listener {
    void onItemSelected(DashboardRecord item);
    void onDataSetChanged();
  }

  interface Binder extends UiBinder<Widget, DashboardRecordList> { }
  interface SelectionStyle extends CssResource {
    String selectedRow();
    String unread();
  }
  UsecaseSearchCriteria criteria;
  private static final Binder binder = GWT.create(Binder.class);
  static final int VISIBLE_EMAIL_COUNT = UsecaseSearchCriteria.SEARCH_PAGE_SIZE;
  DashboardModel model;
  @UiField DataGrid<DashboardRecord> table;
  @UiField SelectionStyle selectionStyle;
  private AdvanceSearchGrid<DashboardRecord> advancedSearchGrid;
  @UiFactory DataGrid<DashboardRecord> RecordDetailsFactory(){
	  SearchPageModel model=new SearchPageModel();
	  InputItem []gridSearchResult=new InputItem[4];
	  gridSearchResult[0]=new InputItem("index",constants.index(), InputItem.DISPLAY_TYPE_NUMERIC,0,Integer.MAX_VALUE);
	  gridSearchResult[1]=new InputItem("date",constants.date(), InputItem.DISPLAY_TYPE_DATE,1,Integer.MAX_VALUE);
	  gridSearchResult[2]=new InputItem("subject",constants.subject(), InputItem.DISPLAY_TYPE_STRING,2,Integer.MAX_VALUE);
	  gridSearchResult[3]=new InputItem("description",constants.description(), InputItem.DISPLAY_TYPE_LARGE_STRING,3,Integer.MAX_VALUE);
	  model.setSearchResultItems(gridSearchResult);
	  advancedSearchGrid=new AdvanceSearchGrid<DashboardRecord>(model,"100%","100%",false,false);
	  return advancedSearchGrid.getGrid();
  }
  @UiField
  SearchPager pager;
  @UiFactory SearchPager createPager(){
	  SearchPager pager=new SearchPager();
	  pager.addListener(new SearchPager.SeachPageListener() {
		
		@Override
		public void pagerPositionChanged(int from,int resultPerpage) {
			MessageUtility.progress(constants.pleaseWait(), constants.loadingData());
			criteria.setFrom(from);
			criteria.setTo(from+resultPerpage);
			model.loadCriteriaData(callback);
		}
	});
	  return pager;
  }
  
  private Listener listener;

  public DashboardRecordList() {
    initWidget(binder.createAndBindUi(this));
    table.setEmptyTableWidget(new Label(constants.noRecords()));
  }
//******************************************************************************************************************************************
  public void setModel(DashboardModel model){
	this.model=model;
	model.addListener(this);
	initTable();
  }
//******************************************************************************************************************************************
  /**
   * Sets the listener that will be notified when an item is selected.
   */
  public void setListener(Listener listener) {
    this.listener = listener;
  }
//******************************************************************************************************************************************
  /**
   * Initializes the table so that it contains enough rows for a full page of
   * emails. Also creates the images that will be used as 'read' flags.
   */
  private void initTable() {
	   final SingleSelectionModel<GridRowData> selectionModel = new SingleSelectionModel<GridRowData>();
	    table.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	    	  DashboardRecord row =(DashboardRecord) selectionModel.getSelectedObject();
	        if (row != null) {
	            if (listener != null) {
	              listener.onItemSelected(row);
	            }
	        }
	      }
	    });
	    table.setColumnWidth(table.getColumn(0), "10%");
	    table.setColumnWidth(table.getColumn(1), "20%");
	    table.setColumnWidth(table.getColumn(2), "40%");
	    table.setColumnWidth(table.getColumn(3), "30%");
  }

//******************************************************************************************************************************************
@Override
public void modelLoaded(boolean success) {
}
//******************************************************************************************************************************************
@Override
public void dashSearchCriterialChanged(UsecaseSearchCriteria searchCriteria) {
	this.criteria=searchCriteria;
	loadData(searchCriteria, callback);
	
}
//******************************************************************************************************************************************
//@Override
public void loadData(SearchCriteria criteria, AsyncCallback<SearchPageData> callback) {
	if(model!=null)
		model.loadCriteriaData((UsecaseSearchCriteria) criteria,callback);
}
//******************************************************************************************************************************************
@Override
public void recordUpdated(DashboardRecord record) {
	if(record.isRead()){
		record.setCssClass(null);
	}else{
		int index=advancedSearchGrid.getRowIndex(record);
		record.setCssClass(index%2==0?DashboardRecord.EVEN_UNREAD_ROW_CSS:DashboardRecord.ODD_UNREAD_ROW_CSS);
	}
	table.flush();
}
//***************************************************************************************
 AsyncCallback<SearchPageData> callback=new AsyncCallback<SearchPageData>() {

		@Override
		public void onFailure(Throwable e) {
			MessageUtility.stopProgress();
			GWT.log(e.toString());
			MessageUtility.error(constants.error(), constants.failToAccessServer());
			advancedSearchGrid.setData(criteria==null?0:criteria.getFrom(), criteria==null?SearchPageData.DEFAULT_PAGE_SIZE: criteria.getTo(), null);
			pager.updateNavigation(0,  0);
			listener.onDataSetChanged();
		}

		@Override
		public void onSuccess(SearchPageData result) {
			MessageUtility.stopProgress();
			advancedSearchGrid.setData(criteria==null?0:criteria.getFrom(), criteria==null?SearchPageData.DEFAULT_PAGE_SIZE: criteria.getTo(), result);
			pager.updateNavigation(criteria==null?0:criteria.getFrom(),result==null?0:result.getTotalResultCount());
			listener.onDataSetChanged();
		}
	};
}
