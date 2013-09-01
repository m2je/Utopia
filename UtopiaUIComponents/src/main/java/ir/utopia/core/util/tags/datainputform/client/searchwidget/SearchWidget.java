package ir.utopia.core.util.tags.datainputform.client.searchwidget;

import ir.utopia.core.util.tags.datainputform.client.AbstractUtopiaGWTEditor;
import ir.utopia.core.util.tags.datainputform.client.EditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.GlobalEditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.ImageHyperlink;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;
import ir.utopia.core.util.tags.datainputform.client.grid.AdvanceSearchGrid;
import ir.utopia.core.util.tags.datainputform.client.grid.DefaultSearchGrid;
import ir.utopia.core.util.tags.datainputform.client.grid.SearchGrid;
import ir.utopia.core.util.tags.datainputform.client.grid.SearchGridListener;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVWidget;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.ContextItem;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.SearchPageServerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;

public class SearchWidget extends AbstractUtopiaGWTEditor implements SearchGridListener,SearchPager.SeachPageListener{
	SearchPageConstants constants =(SearchPageConstants)GWT.create(SearchPageConstants.class);
	private boolean useAdvanedGrid=true;
	private SearchGrid searchGrid;
	int loadsize=SearchPageData.DEFAULT_PAGE_SIZE;
	private SearchPager pager=new SearchPager();
	HashMap<String,Widget>searchWidgetMap=new HashMap<String, Widget>();
	HashMap<String, Label> searchWidgetsLabelMap=new HashMap<String, Label>();
	HashMap<String,Label>searchWidgetsLabels=new HashMap<String, Label>();
	UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	SearchPageModel searchModel;
	Widget []searchwidgets;
	HorizontalPanel actionPanel;
	VerticalPanel rootPane;
	 int conditionCount=0;
	 ImageHyperlink clearSearch;
	 ListBox orderby;
	 List<ContextItem>contextItems;
	 boolean isactionEnables,isSeacrhEnabled;
	 String defaultConditions;
	 private SearchWidgetListener listener;
	 private boolean multiselect=false;
	 
	
	 public SearchWidget(String usecaseName,String formClassName,boolean isSeacrhEnabled,boolean isactionEnables,boolean multiselect,String defaultConditions,SearchWidgetListener listener){
		 super.setEditorUsecaseName(usecaseName);
		 super.setEditorFormClass(formClassName);
		 this.isactionEnables=isactionEnables;
		 this.isSeacrhEnabled=isSeacrhEnabled;
		 this.defaultConditions=defaultConditions;
		 this.listener=listener;
		 this.multiselect=multiselect;
		 pager.addListener(this);
		 loadConfiguration();
	 }
//************************************************************************************************************************
	  private void loadConfiguration(){
		SearchPageServerService.getServer().getSearchPageModel(usecaseName, formClass, new AsyncCallback<SearchPageModel>(){

			public void onFailure(Throwable e) {
				GWT.log("", e);
				MessageUtility.error(constants.error(), constants.failToAccessServer());
			}

			public void onSuccess(SearchPageModel model) {
				if(model==null){
					MessageUtility.error(constants.error(), constants.failToAccessServer());
				}else{
					searchModel=model;
					setUsecaseActionId(model.getUsecaseActionId());
					setEditorFormClass(formClass);
					setEditorActionName("search");
					setEditorUsecaseName(usecaseName);
					createSearchPage(model);
					String [][]conditions=getSearchConditions();
					final String orderbyCol=orderby.getValue(orderby.getSelectedIndex());
					loadData(conditions,orderbyCol ,0);
					listener.configurationLoaded(model,rootPane);
				}
				
			}
			
		})  ;
	  }
//************************************************************************************************************************
	  private void loadData(String [][]searchCondtions ,String orderby,final int from){
		  MessageBox.progress(constants.pleaseWait(), constants.loadingData());
		  SearchPageServerService.getServer().getSearchResult(usecaseName, formClass,
				  searchCondtions,defaultConditions,"-1".equals(orderby)?new String[0]:new String[]{orderby},from, loadsize,contextItems, new AsyncCallback<SearchPageData>(){

					public void onFailure(Throwable e) {
						GWT.log("", e);
						MessageUtility.stopProgress();
						MessageUtility.error(constants.error(), constants.failToAccessServer());
					}

					public void onSuccess(SearchPageData result) {
						MessageUtility.stopProgress();
						if(result==null){
							MessageUtility.error(constants.error(), constants.failToAccessServer());
						}else{
							
							setData(from, loadsize,result);
						}
					}
			  
		  });
		     
	  }
//************************************************************************************************************************	  
	  private void setData(int from ,int pageSize,SearchPageData data){
			 pager.updateNavigation(from, data.getTotalResultCount());
			 searchGrid.setData(from, pageSize, data);
			 }
//************************************************************************************************************************
	  private String [][] getSearchConditions(){
		  ArrayList<String[]>result=new ArrayList<String[]>();
		  int length=searchwidgets==null?0:searchwidgets.length;
		  for(int i=0;i<length;i++){
			  if(((searchwidgets[i] instanceof CheckBox)&&(!((CheckBox)searchwidgets[i]).isChecked()))
				||
				((searchwidgets[i] instanceof Checkbox)&&(!((Checkbox)searchwidgets[i]).getValue()))
				||(!EditorFactory.isVisible( searchwidgets[i]))
				){
				  continue;
			  }
			  String []cur=new String[2];
			  cur[0]=EditorFactory.getWidgetName(searchwidgets[i]);
              Object value=EditorFactory.getWidgetValue(searchwidgets[i]);  
			  cur[1]=(value==null||
					  ((searchwidgets[i] instanceof ListBox)&&(new Long(-1l).equals(value)
							  ||((ListBox)searchwidgets[i] ).getSelectedIndex()==0))
					  )?null:value.toString().trim();
			  
			 if(cur[1]==null||cur[1].toString().trim().length()==0){
				 continue;
			 } 
			result.add(cur);  
		  }
		  
		  return (String[][])(result.toArray(new String[result.size()][2]));
	  }
//************************************************************************************************************************
	  private void createSearchPage(SearchPageModel model){
		    rootPane=new VerticalPanel();
		    if(useAdvanedGrid){
		    	this.searchGrid=new AdvanceSearchGrid<GridRowData>(model,  "100%", "500px", isactionEnables, multiselect);
		    }else{
		    	this.searchGrid=new DefaultSearchGrid(model, "100%", "500px", isactionEnables,multiselect);
		    }
		    
		    InputItem []items=model.getSearchItems();
		    
		    rootPane.add(getSearchPanel(items,model.getOrderbyItems()));
		    actionPanel=new HorizontalPanel();
		    HorizontalPanel p= getActionButtonPanel();
		    if(p.getWidgetCount()>0){
			    p.setWidth("100%");
			    actionPanel.add(p);
			    rootPane.add(actionPanel);
		    }
		    
		    searchGrid.getWidget().setWidth("100%");
		    searchGrid.addGridListener(this);
		    searchGrid.setMultiSelectable(multiselect);
		    rootPane.add((Widget)searchGrid.getWidget());
		    pager.updateNavigation(0,0);
		    rootPane.add(pager);
		    rootPane.setWidth("100%");
		    if(LocaleInfo.getCurrentLocale().isRTL()){
		    	rootPane.getElement().setDir("rtl");
		    }
		    super.initData(model.getValueModel());
	  }
//************************************************************************************************************************
	  private HorizontalPanel getActionButtonPanel(){
		  HorizontalPanel result=new HorizontalPanel();
		  result.setSpacing(10);
		  if(isactionEnables){
			  if(searchModel.isSaveEnable()){
				  Button saveButton=new Button();
				  saveButton.setStylePrimaryName("clsGoSaveButton");
				  saveButton.setText(constants.newRecord());
				  saveButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						redirect(searchModel.getSaveUrl());
					}
				});
				  result.add(saveButton);
			  }
			  if(searchModel.isImportEnable()){
				  Button importButton=new Button();
				  importButton.setStylePrimaryName("clsImportButton");
				  importButton.setText(constants.importRecords());
				  importButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						listener.buttonClicked(SearchWidgetListener.import_button_clicked);
					}
				});
				  result.add(importButton);
			  }

		  }
		  
		  return result;
	  }
	  @SuppressWarnings("deprecation")
	private Widget getSearchPanel(InputItem []searchItems,InputItem []orderBys){
		  super.setItems(searchItems);
		 
		  GlobalEditorDataChangeListener listener=new GlobalEditorDataChangeListener(this,searchItems);
		  searchwidgets =new Widget[searchItems==null?0:searchItems.length];
		  HorizontalPanel rootPanel=new HorizontalPanel();
		  rootPanel.setStylePrimaryName("clsTableBody");
		  clearSearch=new ImageHyperlink(images.drop().createImage());
		  clearSearch.setVisible(false);
		 
		  if(searchItems!=null&&searchItems.length>0){
		    Grid searchItemGrid = getSearchItemGrid(searchItems, listener);
		    rootPanel.add(searchItemGrid);
		    searchItemGrid.getElement().getStyle().setOverflow(Overflow.AUTO);
			Grid searchGrid=new Grid(1,6);
			searchGrid.getColumnFormatter().setWidth(0, "10%");
			searchGrid.getColumnFormatter().setWidth(1, "15%");
			searchGrid.getColumnFormatter().setWidth(2, "15%");
			searchGrid.getColumnFormatter().setWidth(3, "20%");
			searchGrid.getColumnFormatter().setWidth(4, "20%");
			searchGrid.getColumnFormatter().setWidth(5, "15%");
			
		    Button button = new Button(constants.search());
		    button.setStylePrimaryName("clsSearchButton");
		    orderby=new ListBox();
            orderby.addItem("----","-1");
            if(orderBys!=null&&orderBys.length>0){
            	for(InputItem item:orderBys)
            		orderby.addItem(item.getHeader(), item.getColumnName());
            }
		    button.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					doSearch();
				}});
		    Label l=new Label(constants.orderBy()+":");
		    orderby.setStylePrimaryName("clsSelect");
		    l.setStylePrimaryName("clsLabel");
		    
		    
		    clearSearch.setTitle(constants.resetSearchFields());
		    clearSearch.setVisible(false);
		    clearSearch.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					for(Widget widget: searchWidgetMap.values()){
						EditorFactory.setWidgetValue(widget, null);
					}
					orderby.setSelectedIndex(0);
					clearSearch.setVisible(false);
				}
			});
		    searchGrid.setWidget(0, 1, l);
		    searchGrid.setWidget(0, 2,orderby);
		    searchGrid.setWidget(0, 4,clearSearch);
		    searchGrid.setWidget(0, 5,button);
		    rootPanel.add(searchGrid);
		 }
		  	
		    return rootPanel;
	  }
//************************************************************************************************************************
	  public void doSearch(){
		  	String [][]cond=getSearchConditions();
			String orderbyCol=orderby.getValue(orderby.getSelectedIndex());
			loadData(cond,orderbyCol, 0); 
	  }
//************************************************************************************************************************
private Grid getSearchItemGrid(InputItem[] searchItems,
		GlobalEditorDataChangeListener listener) {
	  Grid searchItemGrid = new Grid((searchwidgets.length/2)+1,4);
	  searchItemGrid.setStylePrimaryName("clsTableBody");
	  int index=0;
	  searchItemGrid.setWidth("100%");
	  searchItemGrid.getColumnFormatter().setWidth(0, "20%");
	  searchItemGrid.getColumnFormatter().setWidth(1, "25%");
	  searchItemGrid.getColumnFormatter().setWidth(2, "20%");
	  searchItemGrid.getColumnFormatter().setWidth(3, "25%");
	  
	  if(isSeacrhEnabled){
		  GlobalEditorDataChangeListener searchItemChangeListener=new GlobalEditorDataChangeListener(new EditorDataChangeListener(){

				@Override
				public void dataChanges(Widget widget, Object newValue,
						int[] depenedetTypes, String[][] dependentfields) {
					clearSearch.setVisible(true);
					
				}

				@Override
				public void dataChanges(String widgetName, Object newValue,
						int[] depenedetTypes, String[][] dependentfields) {
					clearSearch.setVisible(true);
				}
				  
			  }, searchItems);  
		L1: for(int i=0;i<searchItems.length;i++){
			 for(int j=0;j<2;j++){
				 if(index>=searchItems.length)break L1;
				 
				 Widget w[]= createCondition(searchItems[index],listener,index);
				 if(w[0]!=null){
					    searchItemGrid.setWidget(i, j==0?0:2, w[0]);
					    }
			   if(w[1]!=null){
				 EditorFactory.addListener(w[1], searchItemChangeListener);  
			    searchItemGrid.setWidget(i, j==0?1:3, w[1]);
			    searchWidgetMap.put(searchItems[index].getColumnName(), w[1]);
			    searchWidgetsLabelMap.put(searchItems[index].getColumnName(), (Label)w[0]);
			    }
			   index++;
			 }
		 }
	   }
	  super.setWidgetsMap(searchWidgetMap);
	  super.setWidgetsLabelMap(searchWidgetsLabelMap);
	  
	return searchItemGrid;
}
//************************************************************************************************************************
	  private Widget[] createCondition(InputItem item,GlobalEditorDataChangeListener listener,int editorIndex){
		  conditionCount++;
		   Widget widget= EditorFactory.createEditor(item, RootPanel.getBodyElement().getDir(),true);
		  EditorFactory.addListener(widget, listener);
		   searchwidgets[editorIndex]=widget;
		   if(widget instanceof TextField){
			   ((TextField)widget).addListener(new FieldListenerAdapter(){
				   public void onSpecialKey(Field field, EventObject e) {
						  if(e.getKey()==EventObject.ENTER){
							  loadData(getSearchConditions(),orderby.getValue(orderby.getSelectedIndex()), 0);
						  }
					    }
			   }   
			   );
		   }else  if(widget instanceof TextBox){
			   ((TextBox)widget).addKeyUpHandler(new KeyUpHandler() {
				
				@Override
				public void onKeyUp(KeyUpEvent e) {
					 if(e.getNativeKeyCode()==EventObject.ENTER){
						  loadData(getSearchConditions(),orderby.getValue(orderby.getSelectedIndex()), 0);
					  }
				}
			});
		   }else if(widget instanceof LOVWidget){
			   ((LOVWidget)widget).setContainer(this);
		   }
		   Label label=EditorFactory.createLable(item, LocaleInfo.getCurrentLocale().isRTL()?"rtl":"ltr",false);
		   return new Widget[]{label,widget};
	  }
//************************************************************************************************************************
		public static void redirect(String url){
			Window.Location.replace(GWT.getHostPageBaseURL()+url);
			
		}
//************************************************************************************************************************
	public Long[] getSelectedIds(){
		return searchGrid.getSelectedIds();
	}	
//************************************************************************************************************************
	public String getOrderby(){
		return orderby.getSelectedIndex()==0?null:orderby.getValue(orderby.getSelectedIndex());
	}
//************************************************************************************************************************	
	public void setHighlightRowOnMouseOver(boolean highlightRowOnMouseOver) {
		searchGrid.setHighlightRowOnMouseOver(highlightRowOnMouseOver);
	}
//************************************************************************************************************************	
	@Override
	public void rowSelected(int rowIndex, Long rowId,String []columnNames, String[] rowData,boolean selected) {
		listener.rowSelected(rowIndex, rowId,columnNames, rowData,selected);
	}
//************************************************************************************************************************
		public void unCheckRow(Long rowId){
			searchGrid.unCheckRow(rowId);
		}
//************************************************************************************************************************
		public void checkRow(Long rowId){
			searchGrid.checkRow(rowId);
		}
//************************************************************************************************************************
		public List<ContextItem> getContextItems() {
			return contextItems;
		}
//************************************************************************************************************************
		public void addContextItems(ContextItem contextItem) {
			if(contextItems==null){
				this.contextItems=new ArrayList<ContextItem>();
			}
			contextItems.add(contextItem);
		}
//************************************************************************************************************************
		public void removeContextItem(ContextItem contextItem){
			if(contextItems!=null){
				contextItems.remove(contextItem);
			}
		}
//************************************************************************************************************************
		public void clearContextItems(){
			if(contextItems!=null){
				contextItems.clear();
			}
		}
//************************************************************************************************************************
@Override
public void pagerPositionChanged(int from,int resultPerpage) {
	loadsize=resultPerpage;
	loadData(getSearchConditions(), orderby.getValue(orderby.getSelectedIndex()), from);
	
}
}
