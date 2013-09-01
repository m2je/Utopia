package ir.utopia.core.util.tags.datainputform.client.grid;

import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.OrderBy;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.model.AbstractPageDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.LinkDataModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class AdvanceSearchGrid<R extends GridRowData> extends AbstractSearchGrid{
	protected static transient final int DEFAULT_ACTION_ICON_SIZE=20;
	public static final AdvanceSearchGridResources gwtCssDataGridResources = GWT.create(AdvanceSearchGridResources.class);
	ListDataProvider<R> dataProvider = new ListDataProvider<R>();
	List<R>gridData=new ArrayList<R>();
	Set<R>previousSelection;
	public interface DataGridResource extends DataGrid.Resources {

		  @Source({ DataGrid.Style.DEFAULT_CSS, "AdvanceSearchGrid.css" })
		  DataGrid.Style dataGridStyle();
		};
	interface Resources extends ClientBundle {
			  @Source("edit.gif")
			  ImageResource getEditImage();
			  
			  @Source("disabled_edit.gif")
			  ImageResource getEditDisableImage();
			  
			  @Source("drop.gif")
			  ImageResource getDeleteImage();
			  
			  @Source("disabled_drop.gif")
			  ImageResource getDeleteDisableImage();
			  
			  @Source("report.gif")
			  ImageResource getReportImage();
			  
			  @Source("report.gif")
			  ImageResource getReportDisableImage();
			}
	
    static {
        gwtCssDataGridResources.advancedGrid().ensureInjected();
    }
    Resources resources = GWT.create(Resources.class);
    public DataGridResource resource=GWT.create(DataGridResource.class);
	ProvidesKey<R> provideKey=new ProvidesKey<R>() {
		@Override
		public Object getKey(R item) {
			return item.getRowId();
		}
	};
	DataGrid<R> dataGrid=new DataGrid<R>(SearchPageData.DEFAULT_PAGE_SIZE,resource,provideKey);
	RowStyles<R> gridRowStye=new RowStyles<R>() {

		@Override
		public String getStyleNames(R row, int rowIndex) {
			
			if(row.getCssClass()!=null&&row.getCssClass().trim().length()>0){
				return row.getCssClass();
			}
			if(rowIndex%2==1){
				return "clsTableEvenRow"+(highlightRowOnMouseOver?"-LOV":"");
			}else{
				return "clsTableOddRow"+(highlightRowOnMouseOver?"-LOV":"");
			}
		}
	};
	AsyncHandler columnSortHandler ;
	ListHandler<R> sortHandler;
	SelectionModel<R> selectionModel ;
	Column<R, Boolean> checkColumn ;
	int currentSoringColumn=-1;
	int sortDirection=OrderBy.SORT_ORDER_ASCENDING;
	


//***************************************************************************************
	public AdvanceSearchGrid(SearchPageModel model, String width,
			String height, boolean actionEnabled, boolean multiSelectable) {
		super(model, width, height, actionEnabled, multiSelectable);
		dataGrid.setWidth(width);
		dataGrid.setHeight(height);
		this.actionEnabled=actionEnabled;
		initAdvanceGrid(model, width, height, actionEnabled);
		add(dataGrid);
	}
//***************************************************************************************
	protected void initAdvanceGrid(AbstractPageDataModel model, String width,
			String height, boolean actionEnabled) {
		   dataProvider.addDataDisplay(dataGrid);
		   gridData= dataProvider.getList();
		   sortHandler = new ListHandler<R>(gridData);
		   columnSortHandler= new AsyncHandler(dataGrid);
		dataGrid.setStylePrimaryName(highlightRowOnMouseOver?"clsLOVTable":"clsTableBody");
		dataGrid.setRowStyles(gridRowStye);
		
		if(multiSelectable){
			
			     checkColumn =
				        new Column<R, Boolean>(new CheckboxCell(true, false)) {
				          @Override
				          public Boolean getValue(R object) {
				            return object.isSelected();
				          }
				        };
				    dataGrid.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
				    dataGrid.setColumnWidth(checkColumn, 40, Unit.PX);
				  
			    
		}
		 
		    if(multiSelectable){
		    	   selectionModel = new MultiSelectionModel<R>(
		    		        );
		    	dataGrid.setSelectionModel(selectionModel,
		    	        DefaultSelectionEventManager.<R> createCheckboxManager());
		    	selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				      public void onSelectionChange(SelectionChangeEvent event) {
				    	  Set<R> newSelection=((MultiSelectionModel<R>)selectionModel).getSelectedSet();
				    	  if(newSelection!=null&&newSelection.size()>0){
				    		  for(R newSelected:newSelection){
				    			  newSelected.setSelected(true);
				    			  fireRowSelected(getRowIndex(newSelected), true);
				    		  }
				    		  
				    		  
				    	  }
				    	  if(previousSelection!=null&&previousSelection.size()>0){
				    		  if(newSelection!=null)previousSelection.removeAll(newSelection);
				    		  for(R r:previousSelection){
				    			  fireRowSelected(getRowIndex(r), false);
				    			  r.setSelected(false);
				    		  }
		    			  }
				    	  previousSelection=newSelection;
				      }
				    });
		    }else{
		    	final SingleSelectionModel<R> selectionModel = new SingleSelectionModel<R>();
			    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			      public void onSelectionChange(SelectionChangeEvent event) {
			    	  R row =(R) selectionModel.getSelectedObject();
			    	  row.setSelected(!row.isSelected());  
			    	  fireRowSelected(getRowIndex(row),row.isSelected());
			      }
			    });
		    	dataGrid.setSelectionModel(selectionModel);
		    	} 
		
		
		initTableColumns( sortHandler);
		dataGrid.addColumnSortHandler(sortHandler);
		
		dataGrid.addHandler(new ColumnSortEvent.AsyncHandler(dataGrid) {
  	    @SuppressWarnings("unchecked")
		public void onColumnSort(ColumnSortEvent event) {
      		   super.onColumnSort(event);
      		 currentSoringColumn=dataGrid.getColumnIndex((Column<R, ?>) event.getColumn());
      		sortDirection=event.getColumnSortList().get(currentSoringColumn).isAscending()?OrderBy.SORT_ORDER_ASCENDING:OrderBy.SORT_ORDER_DESCENDING;
      	  }
		}, ColumnSortEvent.getType());
		dataGrid.addColumnSortHandler(columnSortHandler);
	}
//***************************************************************************************
	public int getRowIndex(R row){
		int index=0;
		for(R r:gridData){
			if(r.equals(row)){
				return index; 
			}
			index++;
		}
		return -1;
	}
//***************************************************************************************
	 private void initTableColumns(
			 ListHandler<R> sortHandler) {
		   final InputItem[] columns =model.getSearchResultItems();
		    
		   for(int i=0;i<columns.length;i++){
			   final int colIndex=i;
			    Column<R, String> currentColumn =
		        new Column<R, String>(new TextCell()) {
		          @Override
		          public String getValue(R object) {
		            return object.getData()[colIndex];
		          }
		        };
		        currentColumn.setSortable(true);
		        
		        if(sortHandler!=null){
				        sortHandler.setComparator(currentColumn, new Comparator<R>() {
						      public int compare(R o1, R o2) {
						    	  int result=0;
						    	  try {
						    	  String s1=o1.getData()[colIndex];
						    	  String s2=o2.getData()[colIndex];
						    	  if(InputItem.DISPLAY_TYPE_NUMERIC== columns[colIndex].getDisplayType()){
						    		  NumberFormat format= NumberFormat.getDecimalFormat();
						    		  if(s2==null||s2.trim().length()==0){
						    			  if(s1==null||s1.trim().length()==0){
						    				  return 0;
						    			  }else{
						    				  return -1;
						    			  }
						    		  }else{
						    			  if(s1==null||s1.trim().length()==0){
						    				  return 1;
						    			  }else{
						    				  double n1=format.parse(EditorFactory.revertRightToLeft(s1));
						    				  double n2=format.parse(EditorFactory.revertRightToLeft(s2));
						    				  return (int)(n2-n1);
						    			  }
						    		  }
						    	  }else if(InputItem.DISPLAY_TYPE_DATE== columns[colIndex].getDisplayType()){
						    		  if(s2==null||s2.trim().length()==0){
						    			  if(s1==null||s1.trim().length()==0){
						    				  return 0;
						    			  }else{
						    				  return -1;
						    			  }
						    		  }else{
						    			  if(s1==null||s1.trim().length()==0){
						    				  return 1;
						    			  }
											DateTimeFormat format= DateTimeFormat.getFormat("yyyy/MM/dd");
										    Date d1=format.parse(EditorFactory.revertRightToLeft(s1));
											Date d2=format.parse(EditorFactory.revertRightToLeft(s2));
											return d2.compareTo(d1);
						    		  }
						    		 
						    	  }else{
						    	
						    	  result=s1==null?(s2==null?0:-1):s1.compareTo(s2);
						    	  }
						    	  } catch (Exception e) {
										GWT.log(e.toString());
									}
						        return result;
						      }
						    });
				   }
		        dataGrid.addColumn(currentColumn,columns[i].getHeader());
		        currentColumn.setCellStyleNames("clsSearchTableCell");
		        dataGrid.getColumnSortList().push(currentColumn);
		   }
		   
		  	   initActionColumns();
		  	   
		  }
//***************************************************************************************
	 protected void initActionColumns(){
		 int extendedColumnCount=initDefaultActionColumn();
		 extendedColumnCount+=addCustomLinksColumns();
		 if(extendedColumnCount>0){//fix for scroller overlap issue in dataGrid
					 Column<R, String> scrollColumn =
						        new Column<R, String>(new TextCell()) {
						          @Override
						          public String getValue(R object) {
						            return "";
						          }
						        };
				        dataGrid.addColumn(scrollColumn);
				        dataGrid.setColumnWidth(scrollColumn, "20");
			        }
		 
	 }
//***************************************************************************************
	 protected int initDefaultActionColumn(){
		 int result=0;
		 if(deleteable){
	 		 result++;
			 addImageColumn(new ImageHyperLinkCell(dataGrid.getColumnCount(),constants.delete(),null,new ImageHyperLinkCell.ImageHyperLinkCellCallBack() {
	
				@Override
				public void onClick(int rowIndex, int columnIndex, Object key) {
					if(dataProvider.getList().get(rowIndex).isDeleteable()){
						if(Window.confirm(constants.deleteRecordConfirm())){
							redirect(deleteUrl!=null?deleteUrl+"&"+primarykeyfieldName+"="+key:null);
						}
					}
					
				}
				
				
			}), "", resources.getDeleteImage(),resources.getDeleteDisableImage(),0);
			 }
			 if(editable){
			 result++;
			 addImageColumn(new ImageHyperLinkCell(dataGrid.getColumnCount(),constants.edit(),null,new ImageHyperLinkCell.ImageHyperLinkCellCallBack() {
					
					@Override
					public void onClick(int rowIndex, int columnIndex, Object key) {
						if(dataProvider.getList().get(rowIndex).isUpdateable()){
							redirect(updateUrl!=null?updateUrl+"&"+EDIT_ITEM+"="+key:null);
						}
					}
					
					
				}), "", resources.getEditImage(),resources.getEditDisableImage(),1);	
			 }
			 if(reportable){
			 	 result++; 
				 addImageColumn(new ImageHyperLinkCell(dataGrid.getColumnCount(),constants.report(),null,new ImageHyperLinkCell.ImageHyperLinkCellCallBack() {
						
						@Override
						public void onClick(int rowIndex, int columnIndex, Object key) {
								redirect(reportUrl!=null?reportUrl+"&"+EDIT_ITEM+"="+key:null);
						}
						
						
					}), "", resources.getReportImage(),resources.getReportDisableImage(),3);
			 }
				 
		return result;		 
	 }
//***************************************************************************************
	 private int addCustomLinksColumns(){
		 int result=0;
		 if(super.actionEnabled&&links!=null&&links.size()>0){
			  for(final LinkDataModel link:links){
				  if(link.isPerRecord()){
					  result++;
					  addImageColumn(new ImageHyperLinkCell(dataGrid.getColumnCount(),constants.report(),GWT.getHostPageBaseURL()+IMAGE_RESOLVER_SERVLET_PATH+"?imageName="+link.getIcon(),new ImageHyperLinkCell.ImageHyperLinkCellCallBack() {
							@Override
							public void onClick(int rowIndex, int columnIndex, Object key) {
								String confirmMessage=link.getConfirmMessage();
								if(confirmMessage!=null&&confirmMessage.trim().length()>0){
									if(Window.confirm(confirmMessage)){
										redirect(link.getUrl());
									}
								}else{
									redirect(link.getUrl()+"&"+EDIT_ITEM+"="+data.getRows().get(rowIndex).getRowId());
								}	
							}
							
						}), "", null,null,3);
					 
					  
				  }
			  }  
		  }
		 return result;
	 }
//***************************************************************************************	 
	 private <C> Column<R, C> addImageColumn(Cell<C> cell, String headerText,
			 final C resource,final C disabledResource,final int type) {
		    Column<R, C> column = new Column<R, C>(cell) {
		      @Override
		      public C getValue(R row) {
		    	  if(type==0){
		    		  return row.isDeleteable()?resource:disabledResource;
		    	  }else if(type==1){
		    		  return row.isUpdateable()?resource:disabledResource;
		    	  }		        
		    	  return  resource;
		      }
		    };
		    
		    dataGrid.addColumn(column, headerText);
		    dataGrid.setColumnWidth(column, String.valueOf(DEFAULT_ACTION_ICON_SIZE));
		    return column;
		  }
//***************************************************************************************

	@SuppressWarnings("unchecked")
	@Override
	public void setData(int from, int pageSize, SearchPageData data) {
	super.setData(from, pageSize, data);
	gridData.clear();
	if(data!=null){
		dataGrid.setRowCount(data.getTotalResultCount(), true);
		if(data.getRows()!=null)
			gridData.addAll((List<? extends R>) data.getRows());
	}
//	dataProvider.updateRowData(from, gridData);
	dataProvider.refresh();
	
	dataGrid.flush();
	}
//***************************************************************************************
	@Override
	public Long[] getSelectedIds() {
		return null;
	}
//***************************************************************************************
	@SuppressWarnings("unchecked")
	@Override
	public void unCheckRow(Long rowId) {
		R row=(R)findById(rowId);
		if(row!=null){
			selectionModel.setSelected(row, false);
			row.setSelected(false);
		if(previousSelection!=null){
			previousSelection.remove(row);
		}
		}
	}
//***************************************************************************************
	@SuppressWarnings("unchecked")
	@Override
	public void checkRow(Long rowId) {
		R row=(R)findById(rowId);
		if(row!=null){
			selectionModel.setSelected(row, true);
			row.setSelected(false);
			if(previousSelection==null){
				previousSelection=new HashSet<R>();
			}
			previousSelection.add(row);
			}
	}
//***************************************************************************************
	@Override
	protected String getText(int row, int coulmn) {
		return data.getRows().get(row).getData()[coulmn];
	}
//***************************************************************************************
	@Override
	protected void addRow(int from, String[] newRow, String[] urls,
			boolean[] actionenabled, long rowId,String cssClass) {
	}
//***************************************************************************************
	public DataGrid<R> getGrid(){
		return dataGrid;
	}
//***************************************************************************************
	public void refresh() {
		 dataGrid.flush();
	}
//************************************************************************************************************************
	public void setHighlightRowOnMouseOver(boolean highlightRowOnMouseOver) {
		super.setHighlightRowOnMouseOver(highlightRowOnMouseOver);
		dataGrid.setStylePrimaryName(highlightRowOnMouseOver?"clsLOVTable":"clsTableBody");
	}
}
