package ir.utopia.core.util.tags.datainputform.client.lov;

import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.bubble.BubbleText;
import ir.utopia.core.util.tags.datainputform.client.bubble.BubbleTextListener;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.model.UILOVConfiguration;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.SearchWidget;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.SearchWidgetListener;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.ContextItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

@SuppressWarnings("unchecked")
public class LOVSearchDialog extends Window implements SearchWidgetListener {
	SearchPageConstants constants=GWT.create(SearchPageConstants.class);
	private UILOVConfiguration model;
	 Panel container=new Panel();
	 SearchWidget searchWidget;
	 Widget searchingWidget;
	 LOVWidget holder;
	 Map<Long,String[]> selectedIds=new HashMap<Long,String[]>();
	 Panel selectedItems=new Panel();
	 Map<Long,BubbleText>bubbles=new HashMap<Long,BubbleText>();
	 Button submit,cancel,dropAll;
//*******************************************************************************************************************************	
	public LOVSearchDialog(UILOVConfiguration model ,LOVWidget holder){
		this.model=model;
		this.holder=holder;
		createDialog();
	}
//*******************************************************************************************************************************		
	protected void createDialog(){
		setTitle(model.getLovSearchTitle());  
        setClosable(true);  
        setWidth("90%");  
        setHeight(getScreenHeight()*75/100);  
        setPlain(true);  
        setLayout(new BorderLayout());  
        BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);  
        centerData.setMargins(3, 0, 3, 3);
        container.setFrame(false);
        add(container, centerData);  
        setCloseAction(HIDE);  
        searchWidget=new SearchWidget(model.getUsecaseName(), model.getSearchFormClass(), true, false,model.isMultiSelect(), model.getCondiotions(), this);
        if(model.isMultiSelect()){
        	submit=new Button(constants.ok());
        	cancel=new Button(constants.cancel());
        	dropAll=new Button(constants.dropAll());
        	dropAll.addListener(new ButtonListenerAdapter(){
        		public void onClick(Button button, EventObject e) {
        			ArrayList<Long>keys=new ArrayList<Long>(selectedIds.keySet());
        			for(Long selectedId:keys){
        				unSelectItem(selectedId, true);
        			}
        	    }
        	});
        	cancel.addListener(new ButtonListenerAdapter(){
        		public void onClick(Button button, EventObject e) {
        			selectAndHide();
        		}
        	});
        	submit.addListener(new ButtonListenerAdapter(){
        		public void onClick(Button button, EventObject e) {
        			if(selectedIds.size()==0){
        				MessageUtility.error(constants.warning(), constants.selectOneRecordAtLeast());
        				return;
        			}else{
        				Set<Long>ids=new HashSet<Long>();
        				List<String>texts=new ArrayList<String>();
        				for(Long key:bubbles.keySet()){
        					ids.add(key);
        					texts.add(bubbles.get(key).getText());
        				}
        				holder.setWidgetValue(ids, texts);
        			}
        			selectAndHide();
        		}
        	});
        	if(LocaleInfo.getCurrentLocale().isRTL()){
        		setButtons(new Button[]{dropAll,cancel,submit});
        	}else{
        		setButtons(new Button[]{submit,cancel,dropAll});
        	}
	    	
    	}
      
	}
//*******************************************************************************************************************************	
public static native int getScreenHeight() /*-{
    return $wnd.screen.height;
 	}-*/;
//*******************************************************************************************************************************
@Override
public void configurationLoaded(SearchPageModel model, Widget searchingWidget) {
	container.add(searchingWidget);
	this.searchingWidget=searchingWidget;
	searchWidget.setHighlightRowOnMouseOver(true);
	selectedItems.setBorder(false);
	container.add(selectedItems);
	container.setAutoScroll(true);
	doLayout();
	initiateAndShowLOVDialog();
}

//*******************************************************************************************************************************
public void initiateAndShowLOVDialog(){
	searchWidget.clearContextItems();
	List<ContextItem>contextItems= holder.getContextItems();
	if(contextItems!=null&&contextItems.size()>0){
		for(ContextItem item:contextItems){
			searchWidget.addContextItems(item);
		}
			searchWidget.doSearch();
	}
	if(searchingWidget==null){
		MessageUtility.progress(constants.pleaseWait(), constants.search());
	}else{
		super.show();
	}
	LOVWindowEvent e=new LOVWindowEvent();
    e.setWindowStatus(LOVWindowEvent.WINDOW_STATUS_OPENED);
	holder.fireWindowStatusChanged(e);
}
//*******************************************************************************************************************************
public void selectAndHide(){
	 hide();
	 LOVWindowEvent e=new LOVWindowEvent();
	 e.setWindowStatus(LOVWindowEvent.WINDOW_STATUS_CLOSED);
	 holder.fireWindowStatusChanged(e);
}
//*******************************************************************************************************************************
@Override
public void rowSelected(int rowIndex, Long rowId,String []columnNames, String[] rowData,boolean selected) {
	StringBuffer result=new StringBuffer();
	if(columnNames!=null&&columnNames.length>0&&model.getDisplayBoxColumns()!=null&&model.getDisplayBoxColumns().length>0){
		String []displayColumns=model.getDisplayBoxColumns();
	L1:	for(int i=0;i<displayColumns.length;i++){
			if(displayColumns[i]!=null){
				for(int j=0;j<columnNames.length;j++){
					if(displayColumns[i].equals(columnNames[j])){
						if(result.length()>0){
							result.append(model.getDisplaItemSeperator());
						}
						result.append(rowData[j]);
						continue L1;
					}
				}
			}
			
		}
		
	}else{
		GWT.log("invalid lov configuration fail to load selected LOV  columns ");
	}
	if(model.isMultiSelect()){
		if(selected){
			if(!selectedIds.containsKey(rowId)){
				BubbleText bubble=new BubbleText(rowId,result.toString());
				selectedItems.add(bubble);
				bubbles.put(rowId, bubble);
				bubble.addListener(new BubbleTextListener() {
					@Override
					public void bubbleDroped(Long id, String text) {
						unSelectItem(id,true);
					}
				});
				
				selectedIds.put(rowId, rowData);
				this.doLayout();
			}
		}else{
			unSelectItem(rowId,false);
		}
	}else{
		
		holder.setWidgetValue(rowId,result.toString());
		Timer t=new Timer() {//on FF we had a problem that when the dialog was closed the entire page went blank
							 //which looks to be an issue in GWT DataGrid causing this problem, this way we could avoid that issue
			@Override
			public void run() {
				selectAndHide();
			}
		};
		t.schedule(5);
		
	}
}
//*******************************************************************************************************************************
@Override
public void buttonClicked(int buttonType) {
	
	
}
//*******************************************************************************************************************************
protected void unSelectItem(Long id,boolean updateSearchGrid){
	 selectedIds.remove(id);
	 BubbleText bubble=bubbles.remove(id);
	 if(bubble!=null)
	 selectedItems.remove(bubble);
	 if(updateSearchGrid)
		 searchWidget.unCheckRow(id);
}
//*******************************************************************************************************************************
}
