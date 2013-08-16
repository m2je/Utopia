package ir.utopia.core.util.tags.datainputform.client;

import ir.utopia.core.util.tags.datainputform.client.calendar.PersianCalendar;
import ir.utopia.core.util.tags.datainputform.client.grid.IncludedGrid;
import ir.utopia.core.util.tags.datainputform.client.grid.model.ColorLogic;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridMetaData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVWidget;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.ContextItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.form.ComboBox;

public abstract class AbstractUtopiaGWTEditor implements EditorDataChangeListener,IncludedGridDataHandler,WidgetContainer {
	private DataInputFormConstants constants=(DataInputFormConstants)GWT.create(DataInputFormConstants.class);
	private int loadingComboLeft=0;
	private InputItem []items;
	private Long usecaseActionId;
	protected HashMap<String,Widget>widgetsMap;
	protected HashMap<String,Label>widgetsLabelMap;
	protected String formClass, usecaseName, actionName;
	protected boolean dirty;
	protected HashMap<String,String>context;
//**********************************************************************
	protected AbstractUtopiaGWTEditor(){
		if(getNativeFunction("getGWTComponentId")==null)//to prevent overriding getGWTComponentId method ( for example in LOV )
			registerNativeFunctions();
	}
//**********************************************************************
	public void setItems(InputItem []items){
		this.items=items;
		}
//**********************************************************************
	public void setWidgetsMap(HashMap<String,Widget>widgetsMap){
		this.widgetsMap=widgetsMap;
	}
//**********************************************************************
	public void setWidgetsLabelMap(HashMap<String,Label>widgetsLabelMap){
		this.widgetsLabelMap=widgetsLabelMap;
	}
//**********************************************************************
	public void setUsecaseActionId(Long usecaseActionId){
		this.usecaseActionId=usecaseActionId;
	}
//**********************************************************************
	public Long getUsecaseActionId(){
		return this.usecaseActionId;
	}
//**********************************************************************
	public static EditorDataChangeListener createProcessChangeHandler(InputItem []items,
	Long usecaseActionId,
	HashMap<String,Widget>widgetsMap){
		AbstractUtopiaGWTEditor adaptee=new AbstractUtopiaGWTEditor(){};
		adaptee.setItems(items);
		adaptee.setUsecaseActionId(usecaseActionId);
		adaptee.setWidgetsMap(widgetsMap);
		return adaptee;
	}
//***********************************************************************
	public boolean isModified(){
		return dirty;
	}
//***********************************************************************
	protected void setModified(boolean modified){
		this.dirty=modified;
	}
//**********************************************************************	
	protected String getEditorFormClass() {
		return formClass;
	}
//**********************************************************************
	protected void setEditorFormClass(String formClass) {
		this.formClass = formClass;
	}
//**********************************************************************
	protected String getEditorUsecaseName() {
		return usecaseName;
	}
//**********************************************************************	
	protected void setEditorUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}
//**********************************************************************	
	protected String getEditorActionName() {
		return actionName;
	}
//**********************************************************************	
	protected void setEditorActionName(String actionName) {
		this.actionName = actionName;
	}
//**********************************************************************
	public void initData(HashMap<String, String>values){
		if(values!=null){
			for(String key: values.keySet()){
				Widget widget= widgetsMap.get(key);
				if(widget!=null){
					Object value= values.get(key);
					setWidgetValue(widget, value);
				}
			}
		}else{
			for(Widget widget: widgetsMap.values()){
				setWidgetValue(widget, null);
			}
			
		}
		for(InputItem item: items){
			evaluateDisplayLogic(item);
			evaluateReadonlyLogic(item);
			if(item.getDisplayType()==InputItem.DISPLAY_TYPE_GRID){
				GridMetaData gridMeta= item.getGridMetaData();
				if(gridMeta!=null&&gridMeta.getColumns()!=null){
				for(InputItem gitem:item.getGridMetaData().getColumns(false)){
					evaluateGridItemDisplayLogic(item, gitem);
				}
					}
				evaluateGridColorLogic(item);
			}
		}
	}
//***********************************************************************************
	protected void evaluateGridColorLogic(InputItem item ){
		GridMetaData gridMeta= item.getGridMetaData();
		List<ColorLogic>colorLogics= gridMeta.getColorLogic();
		IncludedGrid grid=(IncludedGrid) widgetsMap.get( item.getColumnName());
		if(colorLogics!=null&&colorLogics.size()>0){
			for(int i=0;i<grid.getRowCount();i++){
				for(ColorLogic logic:colorLogics){
					String logicStr =logic.getLogic();
					if(logicStr!=null&&logicStr.trim().length()>0){
						HashMap<String,Object> extendedValuesMap=grid.getGridRowContext( i);
						boolean eval=evaluateLogic(logicStr, extendedValuesMap);
						if(eval){
							grid.setGridRowCSSClass(i, logic.getCssStyleName());
							break;
						}else{
							grid.removeRowCSSClass(i);
						}
					}
				}
			}
			grid.getGrid().getView().refresh();
		}
	}
//***********************************************************************************
	protected void evaluateGridItemDisplayLogic(InputItem item ,InputItem gitem){
		String displayLogic=gitem.getDisplayLogic();
		if(displayLogic!=null&&displayLogic.trim().length()>0){
			boolean evaluation = evaluateLogic(displayLogic,null);
			IncludedGrid grid=(IncludedGrid) widgetsMap.get( item.getColumnName());
			if(grid!=null){
				grid.setColumnHidden(gitem.getColumnName(), !evaluation);
			}
		}
	}
//***********************************************************************************
	protected void setWidgetValue(Widget widget,Object value){
		EditorFactory.setWidgetValue(widget, value);
	}
//**********************************************************************	
	@Override
	public void dataChanges(Widget widget, Object newValue,
			int[] depenedetTypes, String[][] dependentfields) {
		dirty=true;
		if(items!=null&&depenedetTypes!=null){
			
			
			for(int i=0;i<depenedetTypes.length;i++){
				for(int j=0;j<dependentfields[i].length;j++){
					if(depenedetTypes[i]==InputItem.DEPENDENCY_TYPE_DATA_FILTER){
						String f=dependentfields[i][j];
						for(InputItem item:items){
							if(item.getColumnName().equals(f)&&item.getDisplayType()!=InputItem.DISPLAY_TYPE_LOV){
								reloadComboboxes(item);
							}
						}
					}else if(depenedetTypes[i]==InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY){
						String f=dependentfields[i][j];
						for(InputItem item:items){
							if(item.getColumnName().equals(f)){
								evaluateReadonlyLogic(item);
							}
						}
					}else if(depenedetTypes[i]==InputItem.DEPENDENCY_TYPE_DATA_DISPLAY){
						String f=dependentfields[i][j];
						for(InputItem item:items){
							if(item.getColumnName().equals(f)){
								evaluateDisplayLogic(item);
							}
						}
					}else if(depenedetTypes[i]==InputItem.DEPENDENCY_TYPE_COLOR_LOGIC){
						String f=dependentfields[i][j];
						for(InputItem item:items){
							if(item.getColumnName().equals(f)){
								evaluateGridColorLogic(item);
								break;
							}
						}
					}
				}
				
			}
		}
		}
//**********************************************************************
	protected void evaluateDisplayLogic(InputItem item){
		String displayLogic=item.getDisplayLogic();
		if(displayLogic!=null&&displayLogic.trim().length()>0){
			try {
				boolean evaluation = evaluateLogic(displayLogic,null);
				widgetsMap.get(item.getColumnName()).setVisible(evaluation);
				if(widgetsLabelMap.get(item.getColumnName())!=null){
					widgetsLabelMap.get(item.getColumnName()).setVisible(evaluation);	
				}
			} catch (Exception e) {
				GWT.log(e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
//**********************************************************************
	private boolean evaluateLogic(String logic,HashMap<String,Object>extendedValuesMap) {
		try {
			
			if(extendedValuesMap!=null){
				for(String key:extendedValuesMap.keySet()){
					Object value=extendedValuesMap.get(key);
					logic=logic.replaceAll("@"+key+"@",value==null?"null":(value.toString().trim().length()==0?"\""+value.toString()+"\"": value.toString() ));
				}
			}
			int index=0;
			while(logic.indexOf("@")>=0&&index<items.length){
				Object value= EditorFactory.getWidgetValue(widgetsMap.get(items[index].getColumnName()));
				logic=logic.replaceAll("@"+items[index].getColumnName()+"@",value==null?"null":(value.toString().trim().length()==0?"\""+value.toString()+"\"": value.toString() ));
				index++;
			}
			if(context!=null){
				for(String key:context.keySet()){
					Object value=context.get(key);
					logic=logic.replaceAll("@"+key+"@",value==null?"null":(value.toString().trim().length()==0?"\""+value.toString()+"\"": value.toString() ) );
				}
			}
			logic=revertUTFChars( logic.replaceAll("=", "==").replaceAll("!", "!=").replace("&", "&&").replace("|", "||").replaceAll("<==", "<=").replaceAll(">==", ">="));
			boolean evaluation=eval(logic);
			return evaluation;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
//**********************************************************************
	protected String revertUTFChars(String input){
		if(input!=null){
			input=input.replaceAll("\u06f0", "0").replaceAll("\u06f1", "1").replaceAll("\u06f2", "2").
			replaceAll("\u06f3", "3").replaceAll("\u06f4", "4").replaceAll("\u06f5", "5").replaceAll("\u06f6", "6").
			replaceAll("\u06f7", "7").replaceAll("\u06f8", "8").replaceAll("\u06f9", "9");		
		}
		
		
		return input;
	}
//**********************************************************************	
		protected  boolean  eval(String opration){
			try {
				return evaljs(opration);
			} catch (Exception e) {
				GWT.log("fail to evaluate logic :"+opration);
				return false;
			}
		}
//**********************************************************************
	protected native boolean  evaljs(String opration)/*-{
    	return  eval(opration);
	}-*/;
//**********************************************************************
	protected void evaluateReadonlyLogic(InputItem item){
		evaluateReadonlyLogic(item,null,null);
	}
//**********************************************************************
	protected void evaluateReadonlyLogic(InputItem item,Widget w,HashMap<String,Object>extendedValuesMap){
		String readOnlyLogic=item.getReadOnlyLogic();
		if(readOnlyLogic!=null&&readOnlyLogic.trim().length()>0){
			try {
				boolean evaluation = evaluateLogic(readOnlyLogic,extendedValuesMap);
				if(w==null){
					w= widgetsMap.get(item.getColumnName());
				}
				EditorFactory.setWidgetEnabled(w,! evaluation);
			} catch (Exception e) {
				GWT.log(e.getMessage());
			}
		}
		
	}
//**********************************************************************
	@Override
	public void dataChanges(String widget, Object newValue,
			int[] depenedetTypes, String[][] dependentfields) {
		Widget dWidget= widgetsMap.get(widget);
		dirty=true;
		if(dWidget!=null){
			dataChanges(dWidget, newValue, depenedetTypes, dependentfields);
		}
	}
//**********************************************************************
	
	protected void reloadComboboxes(InputItem item){
		if(widgetsMap==null)return;
		MessageUtility.progress(constants.pleaseWait(),constants.loadingData());
		loadingComboLeft++;
		String []deps= item.getFilterDepenedents();
		String []depValues=new String[deps.length];
		
		for(int i=0;i<depValues.length;i++){
			Object value=EditorFactory.getWidgetValue(widgetsMap.get(deps[i]));
			depValues[i]=value==null?"":value.toString();
		}
		Widget box=(Widget)widgetsMap.get(item.getColumnName());
		
		refreshlookup(item.getColumnName(),deps,depValues,box);
	
}
//***********************************************************************************
		@Override
		public void notifyCellEditing(int row, int column, Widget editingwidget,
				Object value, GridRowData rowData, IncludedGrid grid,InputItem item) {
			 HashMap<String,Object> extendedValues=grid.getGridRowContext(row);
			 evaluateReadonlyLogic(item,editingwidget,extendedValues);
			 if(EditorFactory.isWidgetEnabled(editingwidget)){
					if((editingwidget instanceof ComboBox )||(editingwidget instanceof ListBox )){
						handleIncludedLookups(column, editingwidget, rowData, grid);
					}
			}
			String gridColumnName=EditorFactory.getWidgetName(grid);
			for(InputItem cItem:items){
				if(gridColumnName.equals(cItem.getColumnName())){
					evaluateGridColorLogic(cItem);
				}
			}
				    	
		}
//**********************************************************************		
@Override
	public void notifyRowAdded(IncludedGrid grid, int row) {
	String gridColumnName=EditorFactory.getWidgetName(grid);
	for(InputItem cItem:items){
		if(gridColumnName.equals(cItem.getColumnName())){
			evaluateGridColorLogic(cItem);
		}
	}
		
		
	}
//**********************************************************************
		private void handleIncludedLookups(int column, Widget editingwidget,
				GridRowData rowData, IncludedGrid grid) {
			GridMetaData gridMeta= grid.getMetaData();
			InputItem[] colMeta=gridMeta.getColumns(true);
			InputItem []displayCols= gridMeta.getDisplayColumns();
			ArrayList<String>depValues=new ArrayList<String>();
			String []rowDataStr=rowData.getData();

			String gridName="";
			for(String widgetName:widgetsMap.keySet()){
				if(widgetsMap.get(widgetName).equals(grid)){
					gridName=widgetName;
					break;
				}
			}
			String []filedNames=displayCols[column].getFilterDepenedents();
			if(filedNames!=null){
				int columnCounter=0;
			L1:	for(String field:filedNames){
					for(InputItem meta: colMeta){//lookup data in grid 
						if(field.equals(meta.getColumnName())){
								depValues.add(rowDataStr[columnCounter]);
								columnCounter++;
								continue L1;
						}
					}
					if(widgetsMap.containsKey(field)){	//lookup data in master
						Object parentValue=EditorFactory.getWidgetValue(widgetsMap.get(field));
						depValues.add(parentValue==null?null:parentValue.toString());
						columnCounter++;
						continue L1;
						}
					
				}
				MessageUtility.progress(constants.pleaseWait(),constants.loadingData());
				refreshlookup(gridName+"."+displayCols[column].getColumnName(),filedNames, depValues.toArray(new String[depValues.size()]),editingwidget);
			}
		}
//**********************************************************************
	protected void refreshlookup(String lookupParameterName,String[] deps,String[] depValues,Widget box){
		DataInputServerService.getServer().reloadLooKup(formClass, usecaseName, actionName,lookupParameterName , deps, depValues, new LookupHandler(box));
	} 

//***********************************************************************
	public void registerNativeFunctions(){
		addWidgetlookupFunction(this);
	}
//***********************************************************************
	public native void addWidgetlookupFunction(AbstractUtopiaGWTEditor x)/*-{

	$wnd.getGWTComponentId = function ( t) {

	return x.@ir.utopia.core.util.tags.datainputform.client.AbstractUtopiaGWTEditor::getWidgetId(Ljava/lang/String;)(t);

	};

	}-*/;

//***********************************************************************
	private static  JavaScriptObject getNativeFunction(String functionName){
		try {
			return getNativeFunctionN(functionName);
		} catch (Exception e) {
			return null;
		}
	}
//***********************************************************************
	private static native JavaScriptObject getNativeFunctionN(String functionName)/*-{
    return  $wnd[functionName];
	}-*/;
//***********************************************************************
	public String getWidgetId(String widgetName){
		GWT.log("lookingUp for widget -->"+widgetName);
		Widget widget= widgetsMap.get(widgetName);
		if(widget==null)return "";
		if(widget instanceof IncludedGrid){
			return ((IncludedGrid)widget).getGrid().getId();
		}
		else if(widget instanceof Component){
			return ((Component)widget).getId();
		}else if(widget instanceof LOVWidget){
			return ((LOVWidget)widget).getId();
		}else if(widget instanceof PersianCalendar){
			return ((PersianCalendar)widget).getId();
		}
		return widget.getElement().getId();
	}
//**********************************************************************
	public class LookupHandler implements AsyncCallback<UILookupInfo>{
		Widget box;
		public LookupHandler(Widget box){
			this.box=box;
		}
	public void onFailure(Throwable e) {
		loadingComboLeft--;
		GWT.log("", e);
		if(loadingComboLeft<=0){
			MessageUtility.stopProgress();
		}
		EditorFactory.setLookupValue(box, null);
	}

	public void onSuccess(UILookupInfo result) {
		loadingComboLeft--;
		if(loadingComboLeft<=0){
			MessageUtility.stopProgress();
		}
		EditorFactory.setLookupValue(box, result);
	}
		
	} 
//***********************************************************************
	@Override
	public ContextItem getItem(String itemName) {
		Widget w= widgetsMap.get(itemName);
		if(w!=null){
			Object value= EditorFactory.getWidgetValue(w);
			ContextItem item=new ContextItem();
			item.setValue(value==null?null:value.toString());
			item.setFieldName(EditorFactory.getWidgetName(w));
			item.setFormClass(getEditorFormClass());
			return item;
		}
		return null;
	}
//***********************************************************************
	public void setContext(HashMap<String, String>context){
		this.context=context;
	}
//***********************************************************************
}
