package ir.utopia.core.util.tags.datainputform.client.grid;

import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.LinkDataModel;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class  AbstractSearchGrid  implements SearchGrid{

	protected boolean reportable;
	protected boolean editable;
	protected boolean deleteable;
	protected String deleteUrl;
	protected String updateUrl;
	protected String reportUrl;
	protected String primarykeyfieldName;
	protected boolean actionEnabled;
	protected boolean multiSelectable;
	protected boolean hasRowNumberColumn=false;
	protected boolean highlightRowOnMouseOver;
	protected SearchPageModel model;
	protected UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	protected SearchPageConstants constants =(SearchPageConstants)GWT.create(SearchPageConstants.class);
	protected List<LinkDataModel> links;
	List<SearchGridListener> listeners=new ArrayList<SearchGridListener>();
	public static final int ROW_NUM_COLUMN_WIDTH=25;
	public static final int ROW_CHECK_COLUMN_WIDTH=1;
	public static final transient String EDIT_ITEM="editItem";
	public static final String IMAGE_RESOLVER_SERVLET_PATH="Image-Resolver";
	protected  int []columnWidth;
	protected SearchPageData data;
	protected int actionColumnCount;
	protected Widget widget;
	protected static transient final int DEFAULT_ACTION_ICON_SIZE=16;
	public class AbstractSearchGridContainer extends VerticalPanel{
		SearchGrid adaptee;
		public AbstractSearchGridContainer(SearchGrid adaptee){
			this.adaptee=adaptee;
		} 
		public SearchGrid getGrid(){
			return adaptee;
		}
	}
	private	AbstractSearchGridContainer root=new AbstractSearchGridContainer(this);
	//************************************************************************************************************************
	protected abstract String getText(int row,int coumn);
	protected abstract void addRow(int from ,String [] newRow,final String []urls,final boolean []actionenabled ,long rowId,String cssClass) ;
	//************************************************************************************************************************
	public AbstractSearchGrid(SearchPageModel model,String width,String height,boolean actionEnabled,boolean multiSelectable){
		this.model=model;
		this.multiSelectable=multiSelectable;
		this.actionEnabled=actionEnabled;
		initSearchGrid(model,width,height,actionEnabled);
	}
//************************************************************************************************************************
	protected void initSearchGrid(SearchPageModel model,String width,String height,boolean actionEnabled){
		links=model.getLinks();
		deleteUrl=model.getDeleteUrl();
		updateUrl=model.getEditUrl();
		reportUrl=model.getReportUrl();
		primarykeyfieldName=model.getPrimaryKeyFieldName();
		InputItem []columns=model.getSearchResultItems();
		  deleteable=model.isDeleteEnable()&&actionEnabled;
		  editable=model.isEditEnable()&&actionEnabled;
		  reportable=model.isReportEnable()&&actionEnabled;
		   int totalsize=1000-ROW_NUM_COLUMN_WIDTH-(isMultiSelectable()? ROW_CHECK_COLUMN_WIDTH:0);
		   int actionColCount=0;
		   if(actionEnabled){
			   if(deleteable){
				   totalsize-=DEFAULT_ACTION_ICON_SIZE;
				   actionColCount++;
			   }
			   if(editable){
				   totalsize-=DEFAULT_ACTION_ICON_SIZE;
				   actionColCount++;
			   }
			   if(reportable){
				   totalsize-=DEFAULT_ACTION_ICON_SIZE;
				   actionColCount++;
			   }
			   if(model.getLinks()!=null){
				   for(LinkDataModel link:model.getLinks()){
					   if(link.isPerRecord()){
						   totalsize-=DEFAULT_ACTION_ICON_SIZE;
						   actionColCount++;
					   }
				   }
			   }
		   }
		   columnWidth=new int[columns.length+actionColCount];
		   for(int i=0;i<columnWidth.length;i++){
			   if(i>=columnWidth.length-actionColCount){
				   columnWidth[i]=DEFAULT_ACTION_ICON_SIZE;
			   }else{
				   columnWidth[i]=totalsize/columnWidth.length;
			   }
		   }
		   this.actionColumnCount=actionColCount;
		   
	   }
//************************************************************************************************************************	 
	 public boolean isMultiSelectable() {
		return multiSelectable;
	}
//**************************************************************************************************************************	  
	public void setMultiSelectable(boolean multiSelectable) {
		this.multiSelectable = multiSelectable;
	}
//************************************************************************************************************************	
	public boolean isHighlightRowOnMouseOver() {
		return highlightRowOnMouseOver;
	}
//************************************************************************************************************************	
	public void setHighlightRowOnMouseOver(boolean highlightRowOnMouseOver) {
		this.highlightRowOnMouseOver = highlightRowOnMouseOver;
	}
//************************************************************************************************************************
	public void addGridListener(SearchGridListener listener){
		listeners.add(listener);
		
	}
//************************************************************************************************************************
	public void add(Widget w){
		root.add(w);
	}
//************************************************************************************************************************
	public Widget getWidget(){
		return root;
	}
//************************************************************************************************************************
	@Override
	public void setData(int from, int pageSize, SearchPageData data) {
		this.data=data;
	}
//************************************************************************************************************************
	protected GridRowData findById(Long rowId){
		if(data!=null){
			for(GridRowData row:data.getRows()){
				if(rowId.equals(row.getRowId())){
					return row;
				}
			}
			}
		return null;
	}
//************************************************************************************************************************
		protected void fireRowSelected(int receiverRowIndex,boolean selected){
			if(listeners.size()>0){
				final InputItem []modelColumnNames=model.getSearchResultItems();
				for(SearchGridListener listener:listeners){
					int columnCount=modelColumnNames.length;
					 String []rowData=new String[(hasRowNumberColumn?columnCount-1:columnCount)];
					 String []columnNames=new String[rowData.length];
					 for(int i=0;i<rowData.length;i++){
						 rowData[i]=getText(receiverRowIndex, i+(hasRowNumberColumn?1:0));
						 columnNames[i]=modelColumnNames[i].getColumnName();
					 }
					 listener.rowSelected(receiverRowIndex, data.getRows().get(receiverRowIndex).getRowId(),columnNames, rowData,selected)	;
				}
			}
		}
//************************************************************************************************************************
		public static void redirect(String url){
			Window.Location.replace(GWT.getHostPageBaseURL()+url);
		}

}
