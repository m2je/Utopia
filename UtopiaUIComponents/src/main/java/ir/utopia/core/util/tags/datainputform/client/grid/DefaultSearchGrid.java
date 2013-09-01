package ir.utopia.core.util.tags.datainputform.client.grid;

import ir.utopia.core.util.tags.datainputform.client.ImageHyperlink;
import ir.utopia.core.util.tags.datainputform.client.UtopiaGWTUtil;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.LinkDataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class DefaultSearchGrid extends AbstractSearchGrid {

	
	public static final transient String EDIT_ITEM="editItem";
	
	private FlexTable grid;
	 Grid headerGrid;
	CheckBox selectAll;
	 Label rowNumLabel;


//************************************************************************************************************************
 @Override
protected String getText(int row, int column) {
		return grid.getText(row, column);
 }
//************************************************************************************************************************	 
	public DefaultSearchGrid(SearchPageModel model,String width,String height,boolean actionEnabled,boolean multiSelectable){
		super(model,width,height,actionEnabled,multiSelectable);
		initDefaultGrid(model, width, height);
	} 
//************************************************************************************************************************	
	protected void initDefaultGrid(SearchPageModel model,String width,String height){
			this.hasRowNumberColumn=true;
		    grid= new FlexTable();
		    grid.setStylePrimaryName(highlightRowOnMouseOver?"clsLOVTable":"clsTableBody");
		    grid.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Cell cell = grid.getCellForEvent(event);
					if(cell!=null){
						 int receiverRowIndex = cell.getRowIndex(); // <- here!
						 boolean selected=false;
							if(isMultiSelectable()){
								CheckBox box=(CheckBox) grid.getWidget(receiverRowIndex, 1);
								if(cell.getCellIndex()!=1){
									box.setValue(!box.getValue());
								}
									selected=box.getValue();
							}
						fireRowSelected(receiverRowIndex, selected);	
					}
					
				}
			});
		  
		    ScrollPanel scroller=new ScrollPanel(grid);
		    scroller.ensureDebugId("cwTree-staticTree-Wrapper");
		    scroller.setSize(width,height);
		    
		    headerGrid=getHeaderGrid(columnWidth,model.getSearchResultItems(),actionColumnCount);
		    
		    add(headerGrid);
		    add(scroller);
	}
//************************************************************************************************************************
	public void setHighlightRowOnMouseOver(boolean highlightRowOnMouseOver) {
		super.setHighlightRowOnMouseOver(highlightRowOnMouseOver);
		grid.setStylePrimaryName(highlightRowOnMouseOver?"clsLOVTable":"clsTableBody");
	}
//************************************************************************************************************************
	  private Grid getHeaderGrid(int []columnWidth,InputItem []searchResultColumn, int actionColCount){
		  int gridColCount=columnWidth.length+(isMultiSelectable()? 2:1);
		  Grid header = new Grid(1, gridColCount);
		   rowNumLabel=new Label();
		  rowNumLabel.setStylePrimaryName("clsLabel");
		  header.setWidget(0, 0,rowNumLabel);
		  CellFormatter formater= header.getCellFormatter();
		  formater.setWidth(0,0, String.valueOf(ROW_NUM_COLUMN_WIDTH));
		  if(isMultiSelectable()){
			  selectAll=new CheckBox();
			  selectAll.setStylePrimaryName("clsSelect");
			  header.setWidget(0, 1,selectAll);
			  selectAll.addValueChangeHandler(new ValueChangeHandler<Boolean>(){

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					for(int i=0;i<grid.getRowCount();i++){
						Widget w= grid.getWidget(i, 1);
						if(w instanceof CheckBox){
							((CheckBox)w).setValue(event.getValue());
							fireRowSelected(i, event.getValue());
						}
							}
				}});
			  formater.setWidth(0,1, String.valueOf(ROW_CHECK_COLUMN_WIDTH));
		  }
		  		  
		 
		
		  int leftColCount=isMultiSelectable()?2:1;;
		  header.setStylePrimaryName("clsTableBody");
		  int numColumns = columnWidth.length;
		      for (int col = 0; col < numColumns; col++) {
		    	  if(col<numColumns-actionColCount){
		    		  Grid headerCell=new Grid(1,3);
			    	  headerCell.setStylePrimaryName("clsHeaderCell");
			    	  Label colLabel=new Label(searchResultColumn[col].getHeader());
			    	  colLabel.setStylePrimaryName("clsLabel");
			    	  headerCell.setWidget(0, 0,colLabel );
			    	  ImageHyperlink  asecSort=new ImageHyperlink(images.btn_SortAsc().createImage());
			    	  asecSort. addClickListener(new TableColumnSorter(col,false));
			    	  asecSort.setTitle(constants.ascendingSrot());
			    	  asecSort.setStylePrimaryName("clsAscSortButton");
			    	  headerCell.setWidget(0, 1,asecSort );
			    	  ImageHyperlink descSort=new ImageHyperlink(images.btn_SortDesc().createImage());
			    	  descSort.addClickListener(new TableColumnSorter(col,true));
			    	  descSort.setStylePrimaryName("clsDescSortButton");
			    	  descSort.setTitle(constants.descendingSort());
			    	  headerCell.setWidget(0, 2,descSort);
			    	
			    	  header.setWidget(0, col+leftColCount,headerCell);
			    	  formater.setWidth(0,col+leftColCount, String.valueOf(columnWidth[col]));
					  header.getCellFormatter().setHorizontalAlignment(0, col+leftColCount, HasAlignment.ALIGN_CENTER);  
					  header.getCellFormatter().setVerticalAlignment(0, col+leftColCount, HasVerticalAlignment.ALIGN_MIDDLE);  
		    	  }else{
		    		  formater.setWidth(0,col+leftColCount, String.valueOf(DEFAULT_ACTION_ICON_SIZE));	  
		    	  }
		    	  
		      }
		      
		      header.getRowFormatter().setStylePrimaryName(0, "clsTableHeader");
		    
		    // Return the panel
		    header.ensureDebugId("cwGrid");
		    header.setWidth("100%");
		    return header;
	  }

//**************************************************************************************************************************
	protected void addRow(int from ,String [] newRow,final String []urls,final boolean []actionenabled ,long rowId,String cssClass) {
		    int numRows = grid.getRowCount();
		    CellFormatter cellformater= grid.getCellFormatter();
		    Label rowNumLabel=new Label(UtopiaGWTUtil.getLocaleNumber(String.valueOf(from+numRows+1),
		    		LocaleInfo.getCurrentLocale().getLocaleName()));
		    rowNumLabel.setStylePrimaryName("clsWrapedText");
		    rowNumLabel.setWidth(String.valueOf(ROW_NUM_COLUMN_WIDTH));
		    grid.setWidget(numRows, 0,rowNumLabel );
		    cellformater.setStylePrimaryName(numRows,0, "clsSearchTableCell");
		   int leftColCount=1;
		    if(isMultiSelectable()){
			   CheckBox c= new CheckBox();
			   c.setStylePrimaryName("clsSelect");
			   grid.setWidget(numRows, 1, c);
			   leftColCount++;
		   }
		    
		    for(int j=0;j<newRow.length;j++){
		    	Label l= new Label(newRow[j]);
		    	l.setStylePrimaryName("clsWrapedText");
		    	l.setWidth(String.valueOf(columnWidth[j]));
		    	grid.setWidget(numRows, j+leftColCount, l);
			    cellformater.setStylePrimaryName(numRows,j+leftColCount, "clsSearchTableCell");
			    cellformater.setAlignment(numRows,j+leftColCount, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
		    }
		   
		    int totalLinkCount=0;
		    if(actionEnabled){
		      totalLinkCount=assignPredefinedActionsToSearchGrid(newRow, urls, actionenabled,
					numRows);
		     totalLinkCount= assignCustomActionsToSearchGrid(numRows, actionenabled,totalLinkCount,numRows,newRow.length);
		     }
			 for(int j=newRow.length;j<totalLinkCount+newRow.length;j++){
				 cellformater.setStylePrimaryName(numRows,j+leftColCount, "clsSearchTableCell"); 
				 cellformater.setAlignment(numRows,j+leftColCount,HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
			 }
			 if(cssClass!=null&&cssClass.trim().length()>0){
				 grid.getRowFormatter().setStylePrimaryName(numRows, cssClass+(highlightRowOnMouseOver?"-LOV":""));
			 }else{
			    if((numRows+1)%2==0){
			    	grid.getRowFormatter().setStylePrimaryName(numRows, "clsTableEvenRow"+(highlightRowOnMouseOver?"-LOV":""));
			    }else{
			    	grid.getRowFormatter().setStylePrimaryName(numRows, "clsTableOddRow"+(highlightRowOnMouseOver?"-LOV":""));
			    }
		    }
		  }
//************************************************************************************************************************	 
	  @SuppressWarnings("deprecation")
private int  assignCustomActionsToSearchGrid(final int rowIndex,final boolean []actionenabled ,int totalLinkCount ,int rowNumber,int colCount){
		  if(links!=null&&links.size()>0){
			  for(final LinkDataModel link:links){
				  if(link.isPerRecord()){
					 
					  ImageHyperlink imgHp=new ImageHyperlink(new Image(GWT.getHostPageBaseURL()+IMAGE_RESOLVER_SERVLET_PATH+"?imageName="+link.getIcon()));
					  imgHp.addClickListener(new ClickListener() {
						@Override
						public void onClick(Widget sender) {
							String confirmMessage=link.getConfirmMessage();
							if(confirmMessage!=null&&confirmMessage.trim().length()>0){
								if(Window.confirm(confirmMessage)){
									redirect(link.getUrl());
								}
							}else{
								redirect(link.getUrl()+"&"+EDIT_ITEM+"="+data.getRows().get(rowIndex).getRowId());
							}	
						}
					});
					  imgHp.setTitle(link.getTitle());
					  imgHp.setWidth(String.valueOf(DEFAULT_ACTION_ICON_SIZE));
					  grid.setWidget(rowNumber,totalLinkCount+colCount+1 ,imgHp );
					  totalLinkCount++;	
				  }
			  }  
		  }
		  return totalLinkCount;
	  }
//************************************************************************************************************************
	  @SuppressWarnings("deprecation")
private int assignPredefinedActionsToSearchGrid(String[] newRow,
		final String[] urls, final boolean[] actionenabled, int numRows) {
	int totalLinkCount=0;
	if(deleteable&& urls[0]!=null&&urls[0].trim().length()>0) {
		 
		  ImageHyperlink image=new ImageHyperlink(actionenabled[0]?images.drop().createImage():images.disabled_drop().createImage());
		  if(actionenabled[0]){
			  
	    	  image.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					if(Window.confirm(constants.deleteRecordConfirm())){
						redirect(urls[0]);
					}
				}});
		  }
		  image.setTitle(constants.delete());
		  image.setWidth(String.valueOf(DEFAULT_ACTION_ICON_SIZE));
		  grid.setWidget(numRows, newRow.length+1+(isMultiSelectable()?1:0),image);
		  totalLinkCount++;
	  }
	  if(editable&&urls[1]!=null&&urls[1].trim().length()>0) {
		  ImageHyperlink image=new ImageHyperlink(actionenabled[1]?images.edit().createImage():images.disabled_edit().createImage());
		  if(actionenabled[1]){
			  image.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					redirect(urls[1]);
					
				}});
		  }
		  image.setTitle(constants.edit());
		  image.setWidth(String.valueOf(DEFAULT_ACTION_ICON_SIZE));
		  grid.setWidget(numRows, newRow.length+totalLinkCount+1+(isMultiSelectable()?1:0),image);
		  totalLinkCount++;
	  }
	  if(reportable&&urls[2]!=null&&urls[2].trim().length()>0) { 
		  ImageHyperlink image=new ImageHyperlink(images.report().createImage());
		  image.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
					redirect(urls[2]);
			}});
		  image.setTitle(constants.report());
		  image.setWidth(String.valueOf(DEFAULT_ACTION_ICON_SIZE));
		  grid.setWidget(numRows, newRow.length+totalLinkCount+1+(isMultiSelectable()?1:0),image );
		  totalLinkCount++;
	  }
	  return totalLinkCount;
}
//************************************************************************************************************************
	  public void setData(int from ,int pageSize,SearchPageData data){
			 super.setData(from, pageSize, data);
			 removeAllRows();
			 List<? extends GridRowData> rows=data.getRows(); 
			 int leftColCount=1;
			 if(isMultiSelectable()){
				 selectAll.setValue(false, false);
				 leftColCount=2;
				 }
			 int colCount= headerGrid.getColumnCount();
			 int i=0;
			 for(GridRowData row :rows){
				 addRow(from,row.getDisplayData(),new String[]{deleteUrl!=null?deleteUrl+"&"+primarykeyfieldName+"="+row.getRowId():null,
						 updateUrl!=null?updateUrl+"&"+EDIT_ITEM+"="+row.getRowId():null,
						 reportUrl!=null?reportUrl+"&"+EDIT_ITEM+"="+row.getRowId():null},
						 new boolean[]{row.isDeleteable(),row.isUpdateable(),true},row.getRowId(),row.getCssClass());
				 CellFormatter formater=grid.getCellFormatter();
				 
				 formater.setWidth(i,0,String.valueOf(DefaultSearchGrid.ROW_NUM_COLUMN_WIDTH ));
				 if(isMultiSelectable()){
					 formater.setWidth(i,1,String.valueOf(DefaultSearchGrid.ROW_CHECK_COLUMN_WIDTH));	 
				 }
				 for(int j=leftColCount;j<colCount;j++){
					 formater.setWidth(i,j, String.valueOf(columnWidth[j-leftColCount]));
				 }
				 i++;
			 }
			 
			
			 headerGrid.getCellFormatter().setWidth(0,0,String.valueOf(ROW_NUM_COLUMN_WIDTH) );
			 
	  }
//************************************************************************************************************************
	  private void removeAllRows(){
		  int rowcount=grid.getRowCount();
		  for(int i=rowcount-1;i>=0;i--){
			  grid.removeRow(i);
		  }
	  }
//************************************************************************************************************************
	  /**
	   * Remove a row from the flex table.
	   */
	  protected void removeRow(int rowIndex) {
	    int numRows = grid.getRowCount();
	    if (numRows > 1&&rowIndex<numRows) {
	      grid.removeRow(rowIndex);
	      grid.getFlexCellFormatter().setRowSpan(0, 1, rowIndex);
	    }
	  }
//************************************************************************************************************************
	  private class TableColumnSorter implements  ClickListener{
		  public TableColumnSorter(int column , boolean asc){
			  this.column=column;
			  this.asc=asc;
		  }
		  int column ;
		  boolean asc;
		  public void onClick(Widget sender) {
				 int numRows = grid.getRowCount();
				 if(numRows==0)return ;
				 int columnCount=grid.getCellCount(0);
				 Object [][] data=new Object[numRows][columnCount];
				 for(int i=0;i<numRows;i++){
					 for(int j=0;j<columnCount;j++){
						 Widget w= grid.getWidget(i,j);
						 if(w instanceof ImageHyperlink){
							 data[i][j]= w;
						 }else{
							 data[i][j]= grid.getText(i, j);
						 }
					 }
				 	}
				 Arrays.sort((Object[])data,new Comparator<Object>(){

					public int compare(Object o1, Object o2) {
						Object oo1=((Object[])o1)[column];
						Object oo2=((Object[])o2)[column];
						String s1,s2;
						if(oo1 instanceof String||oo1==null){
							s1=(String)oo1;
						} else{
							return 0;
						}
						if(oo2 instanceof String||oo2==null){
							s2=(String)oo2;
						}else{
							return 0;
						}
						if(asc){
							if(s1==null)return -1;
							if(s2==null)return 1;
							return s2.trim().toLowerCase().compareTo(s1.trim().toLowerCase());
						}else{
							if(s1==null)return 1;
							if(s2==null)return -1;
							return s1.trim().toLowerCase().compareTo(s2.trim().toLowerCase());
							}
					}}
				);
				 for(int i=0;i<numRows;i++){
					 for(int j=0;j<columnCount;j++){
						 if(data[i][j] instanceof Widget){
							 grid.setWidget(i, j, (Widget)data[i][j]);
						 }else{
							 grid.setText(i, j, (String)data[i][j]);	 
						 }
						 
					 }
				 	}
				 }
	  }
//************************************************************************************************************************
		public Long[] getSelectedIds(){
			ArrayList<Long>selectedIds=new ArrayList<Long>();
			int count=grid.getRowCount();
			for(int i=0;i<count;i++){
				Widget w= grid.getWidget(i,1);
				if((w instanceof CheckBox)&&((CheckBox) w).getValue())
					selectedIds.add(data.getRows().get(i).getRowId());
			}
			return selectedIds.toArray(new Long[selectedIds.size()]);
		}
//************************************************************************************************************************
		public void unCheckRow(Long rowId){
			setRowChecked(rowId, false);
		}
//************************************************************************************************************************
		public void checkRow(Long rowId){
			setRowChecked(rowId, true);
		}
//************************************************************************************************************************
		public void setRowChecked(Long rowId,boolean selected){
			if(rowId!=null&&isMultiSelectable()){
				int index=0;
				for(GridRowData row:data.getRows()){
					if(rowId.equals(row.getRowId())){
						CheckBox box= (CheckBox)grid.getWidget(index,1);
						box.setValue(selected);
					}
					index++;
				}
			}
		}
//************************************************************************************************************************
		
}
