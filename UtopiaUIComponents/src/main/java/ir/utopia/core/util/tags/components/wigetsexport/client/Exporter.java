package ir.utopia.core.util.tags.components.wigetsexport.client;

import ir.utopia.core.util.tags.datainputform.client.AbstractUtopiaGWTEditor;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.UILOVConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Exporter extends AbstractUtopiaGWTEditor implements EntryPoint{
	public static final String WIDGET_ANCHOR_KEY="__UtopiaWidget__";
	public static final String WIDGET_DISPLAY_TYPE_KEY="__widgetType__";
	public static final String WIDGET_NAME_KEY="__widgetName__";
	public static final String WIDGET_USECASE_NAME_KEY="__usecaseName__";
	public static final String WIDGET_FORM_CLASS_NAME_KEY="__formClassName__";
	public static final String WIDGET_SEARCH_TITLE_KEY="__searchTitle__";
	public static final String WIDGET_SEPARATOR_CHAR_KEY="__separator__";
	public static final String WIDGET_DISPLAY_ITEMS_KEY="__displayItems__";
	public static final String WIDGET_MULTI_SELECT_KEY="__multiSelect__";
	public static final int MAXIMUM_WIDGET_SUPPORT=100;
	protected RootPanel []panels;
	protected Integer []widgetsDisplayTypes;
	protected InputItem[] inputItems;
	@Override
	public void onModuleLoad() {
		 initWidgets();
		 assignWidgets();
	}
//***************************************************************************************************************
	protected void initWidgets(){
		ArrayList<RootPanel>panels=new ArrayList<RootPanel>();
		ArrayList<Integer>displayTypes=new ArrayList<Integer>();
		for(int i=0;i<MAXIMUM_WIDGET_SUPPORT;i++){
			RootPanel panel= RootPanel.get(WIDGET_ANCHOR_KEY+i);
			if(panel!=null){
				panels.add(panel);
				displayTypes.add(getDisplayType(i));
			}else{
				break;
			}
		}
		this.panels=panels.toArray(new RootPanel[panels.size()]);
		this.widgetsDisplayTypes=displayTypes.toArray(new Integer[displayTypes.size()]);
	}
//***************************************************************************************************************
	public  Integer getDisplayType(int index){
		return Integer.parseInt(getNativeValue(WIDGET_DISPLAY_TYPE_KEY+index)) ;
	}
//***************************************************************************************************************
	public native String getNativeValue(String key)/*-{
		return $doc.getElementById(key).value;
		}-*/;
//***************************************************************************************************************
	public void assignWidgets(){
		HashMap<String,Widget>widgetsMap=new HashMap<String, Widget>();
		String direction=LocaleInfo.getCurrentLocale().isRTL()?"RTL":"LTR";
		inputItems=new InputItem[widgetsDisplayTypes.length];
		for(int i=0;i<widgetsDisplayTypes.length;i++){
			if(widgetsDisplayTypes[i]==InputItem.DISPLAY_TYPE_LOV){
				inputItems[i]=getLOVMetaData(i);
			}
			Widget widget=EditorFactory.createEditor(inputItems[i],direction);
			widgetsMap.put(inputItems[i].getColumnName(), widget);
			panels[i].add(widget);
		}
		super.setWidgetsMap(widgetsMap);
	}
//***************************************************************************************************************
	protected InputItem getLOVMetaData(int index){
		String columnName=getNativeValue(WIDGET_NAME_KEY+index);
		InputItem item=new InputItem();
		item.setColumnName(columnName);
		item.setDisplayType(InputItem.DISPLAY_TYPE_LOV);
		UILOVConfiguration lovConfig=new UILOVConfiguration();
		lovConfig.setUsecaseName(getNativeValue(WIDGET_USECASE_NAME_KEY+index));
		lovConfig.setSearchFormClass(getNativeValue(WIDGET_FORM_CLASS_NAME_KEY+index));
		lovConfig.setLovSearchTitle(getNativeValue(WIDGET_SEARCH_TITLE_KEY+index));
		lovConfig.setDisplaItemSeperator(getNativeValue(WIDGET_SEPARATOR_CHAR_KEY+index));
		String displayColumn=getNativeValue(WIDGET_DISPLAY_ITEMS_KEY+index);
		boolean isMultiSelect=false;
		try {
			isMultiSelect=Boolean.parseBoolean(getNativeValue(WIDGET_MULTI_SELECT_KEY+index));
		} catch (Exception e) {
			e.printStackTrace();
			GWT.log(e.toString());
		}
		lovConfig.setMultiSelect(isMultiSelect);
		if(displayColumn.indexOf(",")>0){
			ArrayList<String>tokens=new ArrayList<String>();
			int fromIndex=0;
			while(fromIndex>=0&&fromIndex<displayColumn.length()){
				int nextIndex=displayColumn.indexOf(",",fromIndex);
				if(nextIndex==-1){
					nextIndex=displayColumn.length();
				}
				tokens.add(displayColumn.substring(fromIndex,nextIndex));
				fromIndex=nextIndex+1;
			}
			lovConfig.setDisplayBoxColumns(tokens.toArray(new String[tokens.size()]));	
		}else{
			lovConfig.setDisplayBoxColumns(displayColumn);	
		}
		
		item.setLOVConfiguration(lovConfig);
		return item;
	}
//***************************************************************************************************************
}
