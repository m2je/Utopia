package ir.utopia.core.util.tags.importpage.client;

import ir.utopia.core.util.tags.datainputform.client.DataInputServerService;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.GWTConstants;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants;
import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputFormModel;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.NumberField;

public class ImExDefinitionPage implements EntryPoint {
	ImportFormConstants constants=(ImportFormConstants)GWT.create(ImportFormConstants.class);
	UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	public static final String ACTION_NAME=UIComponentsConstants.USE_ACTION_NAME_PARAMETER_NAME;
	public static final String USECASE_PARAMETER=UIComponentsConstants.USE_CASE_NAME_PARAMETER_NAME;
	public static final String FORM_CLASS_PARAMETER=UIComponentsConstants.USE_CASE_FORM_CLASS_PARAMETER_NAME;
	public static final String PREVIOUS_PAGE_PARAMETER_NAME="previousPage";
	public static final String SETTING_ID="__settingId";
	public static final String AFTER_SAVE_URL="aftersaveURL";
	public static final String IMPORT_TYPE="importType";
	public static final String SETTING_INSTANCE_ID="settingInstanceId";
	DataInputFormModel model;
	FormPanel form=new FormPanel();
	FlexTable widgetsTable=new FlexTable();
	TextArea description;
	String direction=LocaleInfo.getCurrentLocale().isRTL()?"RTL":"LTR";
	Map<InputItem,Integer>itemRowMapping=new HashMap<InputItem, Integer>();
	Label configNameLabel;
	TextBox configurationName;
	ScrollPanel messagesScroller;
	VerticalPanel messagesPanel;
	FormPanel importRedirectForm=new FormPanel(new NamedFrame("menuFrame"));
	@Override
	public void onModuleLoad() {
		MessageUtility.progress(constants.pleaseWait(), constants.loading());
		DataInputServerService.getServer().getDataInputForm(getFormClass(),getUsecaseName(),
				getActionName(), new AsyncCallback<DataInputFormModel>() {
					
					@Override
					public void onSuccess(DataInputFormModel result) {
						MessageUtility.stopProgress();
						if(result!=null){
							createPage(result);
						}else{
							MessageUtility.error(constants.error(), constants.failToAccessServer());
						}
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						MessageUtility.stopProgress();
						GWT.log(caught.toString());
						MessageUtility.error(constants.error(), constants.failToAccessServer());
					}
				});
		
	}
//**********************************************************************************************************************
	protected void createPage(DataInputFormModel model){
		VerticalPanel rootPane=new VerticalPanel();
		rootPane.setSpacing(10);
		rootPane.setWidth("100%");
		rootPane.add(createMessagesListBox());
		rootPane.add(createHeaderItems());
		rootPane.add(createItemPanel(model));
		rootPane.add(createButtonsPanel());
		Hidden formClass=new Hidden(UIComponentsConstants.USE_CASE_FORM_CLASS_PARAMETER_NAME,getFormClass());
		rootPane.add(formClass);
		Hidden usecase=new Hidden(UIComponentsConstants.USE_CASE_NAME_PARAMETER_NAME,getUsecaseName());
		rootPane.add(usecase);
		form.setWidget(rootPane);
		String recordId=getRecordId();
		if(recordId!=null&&recordId.trim().length()>0){
			form.setAction("update_Co_Ut_ImportExportConfiguration"+GWTConstants.STRUTS_EXTENSION);
			Hidden pk=new Hidden("__settingId",recordId);
			rootPane.add(pk);
			loadData(recordId);
		}else{
			form.setAction("save_Co_Ut_ImportExportConfiguration"+GWTConstants.STRUTS_EXTENSION);	
		}
		
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				messagesScroller.setVisible(false);
				messagesPanel.clear();
				String result=event.getResults();
				if(result!=null&&result.trim().length()>0){
					try {
						result= result.replaceAll("<pre>","").replaceAll("</pre>", "");
						JSONValue jsonValue = JSONParser.parseStrict(result);
						JSONObject jsonResult= jsonValue.isObject();
						JSONObject executionresult=jsonResult.get("result").isObject();
						JSONBoolean success= executionresult.get("success").isBoolean();
						JSONArray message=executionresult.get("messages").isArray();
						showMessages(message);
						if(!success.booleanValue()){
							MessageUtility.error(constants.error(), constants.failToSave());
						}else{
							importRedirectForm.clear();
							VerticalPanel p=new VerticalPanel();
							String recordId=jsonResult.get("__settingId").isNumber().toString();
							String url= getAfterSaveURL();
							p.add(new Hidden("settingId", recordId));
							p.add(new Hidden("usecase", getUsecaseName()));
							p.add(new Hidden("formClass", getFormClass()));
							p.add(new Hidden(SETTING_INSTANCE_ID,getSettingInstanceId()));
							importRedirectForm.setWidget(p);
							importRedirectForm.setAction(url);
							importRedirectForm.submit();
						}
			        }
					catch(Exception e){
						GWT.log(e.toString());
						}
					}
				}
		});
		if(LocaleInfo.getCurrentLocale().isRTL()){
			rootPane.getElement().setDir("rtl");
		}
		VerticalPanel vp=new VerticalPanel();
		vp.setWidth("100%");
		vp.add(form);
		vp.add(importRedirectForm);
		RootPanel.get().add(vp);
	}
//***********************************************************************************
	protected void loadData(String recordId){
		if(recordId!=null&&recordId.trim().length()>0){
			ImExServiceAsync proxy=(ImExServiceAsync) GWT.create(ImExService.class);
			((ServiceDefTarget) proxy).setServiceEntryPoint(GWT.getHostPageBaseURL() + "load_Co_Ut_ImportExportConfiguration"+GWTConstants.STRUTS_EXTENSION);
			proxy.getSettingData(Long.parseLong(recordId), new AsyncCallback<ImExPageDataModel>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.toString()); 
					MessageUtility.error(constants.error(), constants.failToAccessServer());
				}

				@Override
				public void onSuccess(ImExPageDataModel result) {
					if(result==null){
							MessageUtility.error(constants.error(), constants.failToAccessServer());
						}else{
							MessageUtility.stopProgress();
							 description.setText( result.getDescription());
							 configurationName.setText(result.getName());
							 if(result.getSetting()!=null&&result.getSetting().trim().length()>0){
								 JSONValue jsonValue = JSONParser.parseStrict(result.getSetting());
								 JSONArray setupItems= jsonValue.isArray();
								 for(int i=0;i<setupItems.size();i++){
									 JSONObject item= setupItems.get(i).isObject();
									 int type= Integer.parseInt(item.get("type").isString().stringValue());
									 JSONString defaultValue= item.get("defaultValue").isString();
									 Double index= item.get("index").isNumber().doubleValue();
									 String fieldName= item.get("fieldName").isString().stringValue();
									 for(InputItem cItem:itemRowMapping.keySet()){
										 if(cItem.getColumnName().equals(fieldName)){
											 try {
												int rowIndex=itemRowMapping.get(cItem);
												 ListBox mappingType=(ListBox) widgetsTable.getWidget(rowIndex, 1);
												 EditorFactory.setWidgetValue(mappingType, type);
												 Widget indexField= (Widget)widgetsTable.getWidget(rowIndex, 2);
												 EditorFactory.setWidgetValue(indexField, Math.abs(index.intValue()));
												 Widget defaultValueWidget= widgetsTable.getWidget(rowIndex, 3);
												 EditorFactory.setWidgetValue(defaultValueWidget, defaultValue==null?null:defaultValue.stringValue());
											} catch (Exception e) {
												GWT.log(e.toString());
												e.printStackTrace();
											}
										 }
									 }
								 }
							 }
						}
					
				}
			});

		}
			}
//***********************************************************************************
	public void showMessages(JSONArray messages){
		if(messages==null){
			messagesScroller.setVisible(true);
			return;
		}
		ArrayList<String>errors=new ArrayList<String>();
		ArrayList<String>warnings=new ArrayList<String>();
		ArrayList<String>infos=new ArrayList<String>();
		if(messages.size()>0){
			messagesScroller.setVisible(true);
		}
		for(int i=0;i<messages.size();i++){
			JSONObject value=(JSONObject) messages.get(i);
			JSONString type= value.get("type").isString();
			if("error".equals(type.stringValue())){
				errors.add(value.get("message").toString());
			}else if("warning".equals(type.stringValue())){
				warnings.add(value.get("message").toString());
			}else{
				infos.add(value.get("message").toString());
			}
		}
		
		for(String error:errors){
			Widget message=createMessage(error, images.error().createImage());
			messagesPanel.add(message);
		}
		for(String warning:warnings){
			Widget message=createMessage(warning, images.warning().createImage());
			messagesPanel.add(message);		
			}
		for(String info:infos){
			Widget message=createMessage(info, images.info().createImage());
			messagesPanel.add(message);
		}
	}
//***********************************************************************************
	protected Widget createMessage(String message,Widget icon){
		HorizontalPanel h=new HorizontalPanel();
		h.setWidth("100%");
		h.add(icon);
		Label l=new Label(message);
		l.setStylePrimaryName("clsLabel");
		h.add(l);
		h.setCellWidth(icon, "20px");
		if(LocaleInfo.getCurrentLocale().isRTL()){
			h.getElement().setDir("RTL");
		}
		return h;
	}
//***********************************************************************************
		private Widget createMessagesListBox(){
		    messagesPanel=new VerticalPanel();
			messagesScroller = new ScrollPanel(messagesPanel);
			messagesScroller.setHeight("50px");
			messagesScroller.setWidth("100%");
			
			messagesScroller.setVisible(false);
			if(LocaleInfo.getCurrentLocale().isRTL()){
				messagesScroller.getElement().setDir("RTL");
			}
			return messagesPanel;
		}
//**********************************************************************************************************************
	protected Widget createHeaderItems(){
		FlexTable table=new FlexTable();
		table.setWidth("100%");
		configNameLabel=new Label(constants.configurationName()+"*:");
		configNameLabel.setStylePrimaryName("clsLabel");
		table.setWidget(0, 0, configNameLabel);
		configurationName=new TextBox();
		configurationName.setName("__configurationName");
		configurationName.setStylePrimaryName("clsText");
		table.setWidget(0, 1, configurationName);
		Label l2=new Label(constants.description()+":");
		l2.setStylePrimaryName("clsLabel");
		table.setWidget(1, 0, l2);
		 description=new TextArea();
		description.setName("__ConfigurationDesc");
		description.setStylePrimaryName("clsTextArea");
		table.setWidget(1, 1, description);
		CellFormatter cellFormater= table.getCellFormatter();
		cellFormater.setWidth(0, 0, "10%");
		cellFormater.setWidth(0, 1, "50%");
		cellFormater.setWidth(0, 3, "40%");
		cellFormater.setWidth(1, 0, "10%");
		cellFormater.setWidth(1, 1, "50%");
		cellFormater.setWidth(1, 3, "40%");
		return table;
	}
//**********************************************************************************************************************
	protected Widget createButtonsPanel(){
		FlexTable table=new FlexTable();
		table.setWidth("100%");
		CellFormatter formatter= table.getCellFormatter();
		Button save=new Button(constants.save());
		save.setStylePrimaryName("clsSaveButton");
		Button cancel=new Button(constants.cancel());
		cancel.setStylePrimaryName("clsCancelButton");
		Button reload=new Button(constants.reload());
		reload.setStylePrimaryName("clsReloadButton");
		table.setWidget(0, 1, save);
		table.setWidget(0, 2, reload);
		table.setWidget(0, 3, cancel);
		formatter.setWidth(0, 0, "40%");
		formatter.setWidth(0, 1, "6%");
		formatter.setWidth(0, 2, "6%");
		formatter.setWidth(0, 3, "6%");
		formatter.setWidth(0, 4, "40%");
		save.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(validate())
					submit();
			}
		});
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				redirect(getPreviousPage());
			}
		});
		reload.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				form.reset();
			}
		});
		if(LocaleInfo.getCurrentLocale().isRTL()){
			table.getElement().setDir("RTL");
		}
		return table;
	}
//**********************************************************************************************************************
	protected void submit(){
		for(int i=0;i<widgetsTable.getRowCount();i++){
			Widget w= widgetsTable.getWidget(i, 3);
			if(w instanceof ComboBox){
				 Object value=((ComboBox)w).getValue();
				 value=value==null?"-1":value.toString();
				 Hidden h= (Hidden)widgetsTable.getWidget(i, 4);
				 h.setValue((String)value);
			}

		}
		form.submit();
	}
//**********************************************************************************************************************
	protected boolean validate(){
		resetPreviousValidations();
		InputItem []items= model.getItems();
		HashMap<String,InputItem>indexMap=new HashMap<String,InputItem>();
		if(configurationName.getText()==null||configurationName.getText().trim().length()==0){
			MessageUtility.error(constants.warning(), constants.pleaseEnterConfigurationName());
			configNameLabel.setStylePrimaryName("clsErrorLabel");
			return false;
		}
		for(InputItem item:items){
			Integer rowIndex=itemRowMapping.get(item);
			if(rowIndex!=null){
				Object value=EditorFactory.getWidgetValue(widgetsTable.getWidget(rowIndex.intValue(), 2));
				value=value==null?null:value.toString().trim();
				if(value!=null&&((String)value).length()>0&&indexMap.containsKey(value)){
					Label l1=(Label) widgetsTable.getWidget(rowIndex.intValue(), 0);
					InputItem previousItem= indexMap.get(value);
					int eindex= itemRowMapping.get(previousItem).intValue();
					Label l2=(Label) widgetsTable.getWidget(eindex, 0);
					String message=constants.duplicateIndexMessage();
					message= message.replace("@line1@", String.valueOf(eindex));
					message= message.replace("@line2@", String.valueOf(rowIndex));
					MessageUtility.error(constants.warning(),message );
					l1.setStylePrimaryName("clsErrorLabel");
					l2.setStylePrimaryName("clsErrorLabel");
					return false;
				}
				indexMap.put((String)value,item);
					
			}
		}
		return true;
	}
//**********************************************************************************************************************
	protected void resetPreviousValidations(){
		configNameLabel.setStyleName("clsLabel");
		for(int i=1;i<widgetsTable.getRowCount();i++){
			widgetsTable.getWidget(i, 0).setStylePrimaryName("clsLabel");
		}
	}
//**********************************************************************************************************************
	private Widget createItemPanel(DataInputFormModel model) {
		this.model=model;
		InputItem []items= model.getItems();
		
		widgetsTable.setStylePrimaryName("clstableBody");
		widgetsTable.setWidth("100%");
		widgetsTable.setWidget(0, 0, new Label(constants.fieldName()));
		widgetsTable.setWidget(0, 1, new Label(constants.mapType()));
		widgetsTable.setWidget(0, 2, new Label(constants.index()));
		widgetsTable.setWidget(0, 3, new Label(constants.defaultValue()));
		CellFormatter formatter= widgetsTable.getCellFormatter();
		formatter.setStylePrimaryName(0, 0, "clsTableHeader");
		formatter.setStylePrimaryName(0, 1, "clsTableHeader");
		formatter.setStylePrimaryName(0, 2, "clsTableHeader");
		formatter.setStylePrimaryName(0, 3, "clsTableHeader");
		int index=0;
		formatter.setWidth(index, 0, "20%");
		formatter.setWidth(index, 1, "20%");
		formatter.setWidth(index, 2, "20%");
		formatter.setWidth(index, 3, "40%");
		for(InputItem item:items){
			if(!item.isHidden()){
				addImportRow(widgetsTable, item);
				index++;
				itemRowMapping.put(item, index);
				formatter.setWidth(index, 0, "20%");
				formatter.setWidth(index, 1, "20%");
				formatter.setWidth(index, 2, "20%");
				formatter.setWidth(index, 3, "40%");
			}
		}
		return widgetsTable;
	}
//**********************************************************************************************************************
	protected void addImportRow(FlexTable table,InputItem item){
		Label l=EditorFactory.createLable(item, direction, true);
		int row=table.getRowCount();
		table.setWidget(row, 0,l );
		ListBox mapTypeBox=createMapTypeBox(item);
		mapTypeBox.setStylePrimaryName("clsSelect");
		table.setWidget(row, 1, mapTypeBox);
		NumberField itemIndex=new NumberField("",item.getColumnName()+"_index");
		itemIndex.setCls("clsText");
		itemIndex.setDecimalPrecision(0);
		itemIndex.setMinValue(1);
		table.setWidget(row, 2, itemIndex);
		Widget defaultValue=EditorFactory.createEditor(item, direction);
		table.setWidget(row, 3, defaultValue);
		if(defaultValue instanceof ComboBox){
			Hidden h=new Hidden(item.getColumnName());
			table.setWidget(row, 4, h);
		}
	}
//**********************************************************************************************************************
	protected ListBox createMapTypeBox(InputItem item){
		ListBox result=new ListBox();
		if(item.getDisplayType()==InputItem.DISPLAY_TYPE_LARGE_STRING||
				item.getDisplayType()==InputItem.DISPLAY_TYPE_STRING
				){
			result.addItem(constants.equal(),"2");
		}else if(item.getDisplayType()==InputItem.DISPLAY_TYPE_NUMERIC||
				item.getDisplayType()==InputItem.DISPLAY_TYPE_CURRENCY){
			result.addItem(constants.equal(),"3");
		}else if(item.getDisplayType()==InputItem.DISPLAY_TYPE_DATE){
			if(LocaleInfo.getCurrentLocale().getLocaleName().indexOf("fa")>0){
				result.addItem(constants.parseAsPersianDate(),"6");
				result.addItem(constants.parseAsGregorianDate(),"5");
			}else{
				result.addItem(constants.parseAsGregorianDate(),"5");
				result.addItem(constants.parseAsPersianDate(),"6");
			}
			
		}else if(item.getDisplayType()==InputItem.DISPLAY_TYPE_LOV||
				item.getDisplayType()==InputItem.DISPLAY_TYPE_COMBOBOX){
			result.addItem(constants.mapById(),"7");
			result.addItem(constants.mapByName(),"1");
			result.addItem(constants.mapByCode(),"0");
		}else if(item.getDisplayType()==InputItem.DISPLAY_TYPE_CHECK_BOX){
			result.addItem(constants.booleanConversion(),"8");
		}else if(item.getDisplayType()==InputItem.DISPLAY_TYPE_LIST||
				item.getDisplayType()==InputItem.DISPLAY_TYPE_RADIO_BUTTON){
			result.addItem(constants.mapByFixedValue(),"4");	
		}
		else{
			result.addItem(constants.equal(),"2");
		}
		result.setName(item.getColumnName()+"_mapType");
		return result;
	}
//**********************************************************************************************************************
	public native String getPreviousPage()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImExDefinitionPage::PREVIOUS_PAGE_PARAMETER_NAME).value;
	}-*/;
//**********************************************************************************************************************
	public native String getUsecaseName()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImExDefinitionPage::USECASE_PARAMETER).value;
	}-*/;
	
//**********************************************************************************************************************
	public native String getFormClass()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImExDefinitionPage::FORM_CLASS_PARAMETER).value;
	}-*/;
//**********************************************************************************************************************
	public native String getActionName()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImExDefinitionPage::ACTION_NAME).value;
	}-*/;
//**********************************************************************************************************************
	public native String getRecordId()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImExDefinitionPage::SETTING_ID).value;
	}-*/;
//**********************************************************************************************************************
	public native String getAfterSaveURL()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImExDefinitionPage::AFTER_SAVE_URL).value;
	}-*/;
//**********************************************************************************************************************
	public native String getImportType()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImExDefinitionPage::IMPORT_TYPE).value;
	}-*/;
//**********************************************************************************************************************
	public native String getSettingInstanceId()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImExDefinitionPage::SETTING_INSTANCE_ID).value;
	}-*/;

//**********************************************************************************************************************
	public static void redirect(String url){
		Window.Location.replace(GWT.getHostPageBaseURL()+url);
	}

}
