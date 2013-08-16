package ir.utopia.core.util.tags.importpage.client;

import ir.utopia.core.util.tags.datainputform.client.DataInputServerService;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.GWTConstants;
import ir.utopia.core.util.tags.datainputform.client.ImageHyperlink;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.UIComponentsConstants;
import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputFormModel;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.ProcessMessageHandler;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.radiobutton.RadioButtonGroup;

import java.util.Arrays;
import java.util.Comparator;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.NumberField;

public class ImportPage implements EntryPoint {
	ImportFormConstants constants=(ImportFormConstants)GWT.create(ImportFormConstants.class);
	UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	public static final String USECASE_PARAMETER=UIComponentsConstants.USE_CASE_NAME_PARAMETER_NAME;
	public static final String FORM_CLASS_PARAMETER=UIComponentsConstants.USE_CASE_FORM_CLASS_PARAMETER_NAME;
	public static final String ACTION_NAME_PARAMETER=UIComponentsConstants.USE_ACTION_NAME_PARAMETER_NAME;
	public static final String SELECTED_SETTING_ID="__settingId";
	public static final String SETTING_INSTANCE_ID="settingInstanceId";
	public static final String IMPORT_TYPE="importType";
	public static final int IMPORT_ITEM_DISPLAY_COLUMN_COUNT=3;
	
	private static final String LABELS_WIDTH="100";
	ListBox importSetting=new ListBox();
	ListBox importInstances=new ListBox();
	ListBox importTypes=new ListBox();
	FlexTable importSetupView=new FlexTable();
	DataInputFormModel dataInputModel;
	Object [][]mappingData;
	UILookupInfo configs;
	VerticalPanel rootPane=new VerticalPanel();
	FormPanel fPanel=new FormPanel(new NamedFrame("menuFrame"));
	String [][]currentInstancesData;
	VerticalPanel instanceConfigPanel=new VerticalPanel();
	TextBox resourceName=new TextBox(),serverFileName=new TextBox();
	NumberField fromRow=new NumberField(),toRow=new NumberField(),sheetIndex=new NumberField();
	TextArea description=new TextArea(),sql=new TextArea();
	FileUpload localFile=new FileUpload();
	CheckBox firstLineTitle=new CheckBox();
	Widget excelImportWidget,SQLimportWidget;
	
	RadioButtonGroup excelFileLocation=new RadioButtonGroup("excelFileLocation", constants.fileLocation(),
				new UILookupInfo(new String[]{"key","value"}, new String[][]{{"1",constants.uploadTheFile()},{"2",constants.fileOnServer()}}));
	@Override
	public void onModuleLoad() {
		createPage();
	}
//**********************************************************************************************************************
	protected void createPage(){
		initImportConfigs();
		initImportTypes();
		Widget h1=createConfigurationLoadPanel();
		rootPane.add(fPanel);
		rootPane.add(h1); 
		rootPane.setCellHorizontalAlignment(h1,HasHorizontalAlignment.ALIGN_CENTER);
		rootPane.add(importSetupView);
		
		Widget h2=createImportTypePanel();
		rootPane.add(h2);
		rootPane.setCellHorizontalAlignment(h2,HasHorizontalAlignment.ALIGN_CENTER);
		
		Widget h3=createImportInstancePanel();
		rootPane.add(h3);
		rootPane.setCellHorizontalAlignment(h3,HasHorizontalAlignment.ALIGN_CENTER);
		
		if(LocaleInfo.getCurrentLocale().isRTL()){
			rootPane.getElement().setDir("RTL");
		}
		rootPane.add(instanceConfigPanel);
		rootPane.setCellHorizontalAlignment(instanceConfigPanel,HasHorizontalAlignment.ALIGN_CENTER);
		RootPanel.get().add(rootPane);
	}
//**********************************************************************************************************************
	@SuppressWarnings("deprecation")
	protected Widget createConfigurationLoadPanel(){
		HorizontalPanel h1=new HorizontalPanel();
		h1.setSpacing(5);
		Label configL=new Label(constants.configurationName()+"*:");
		configL.setStylePrimaryName("clsLabel");
		h1.add(configL);
		h1.add(importSetting);
		importSetting.setStylePrimaryName("clsSelect");
		importSetupView.setWidth("100%");
		rootPane.setStyleName("clsTableBody");
		importSetupView.setStylePrimaryName("clsTableBody");
		final ImageHyperlink editLink=new ImageHyperlink(images.edit().createImage());
		editLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				redirectToExImConfigurationPage();
			}
		});
		h1.add(editLink);
		editLink.setVisible(false);
		final ImageHyperlink deleteLink=new ImageHyperlink(images.drop().createImage());
		deleteLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MessageBox.confirm(constants.warning(), constants.deleteConfigurationConfirm(),new MessageBox.ConfirmCallback() {
					
					@Override
					public void execute(String btnID) {
						if("yes".equalsIgnoreCase(btnID)){
									ImExServiceAsync proxy=(ImExServiceAsync) GWT.create(ImExService.class);
									((ServiceDefTarget) proxy).setServiceEntryPoint(GWT.getHostPageBaseURL() + "delete_Co_Ut_ImportExportConfiguration"+GWTConstants.STRUTS_EXTENSION);
									proxy.deleteSetting(Long.parseLong((String)EditorFactory.getWidgetValue(importSetting)),new AsyncCallback<ExecutionResult>(){

										@Override
										public void onFailure(Throwable e) {
											MessageUtility.error(constants.error(), constants.failToAccessServer());
											GWT.log(e.toString());
											
										}

										@Override
										public void onSuccess(ExecutionResult result) {
											if(result==null){
												MessageUtility.error(constants.error(), constants.failToAccessServer());
											}else{
												ProcessMessageHandler.showExecutionMessages(result, constants);
											}
											
										}
										
									} );		
						}
						
					}
				});
			}
		});
		h1.add(deleteLink);
		deleteLink.setVisible(false);
		importSetting.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				importSetupView.clear();
				deleteLink.setVisible(false);
				editLink.setVisible(false);
				String currentKey= importSetting.getValue(importSetting.getSelectedIndex());
				for(String data[]:configs.getData()){
					if(currentKey.equals(data[0])){
						deleteLink.setVisible(true);
						editLink.setVisible(true);
						JSONValue jsonValue = JSONParser.parseStrict(data[4]);
						JSONArray setupItems= jsonValue.isArray();
						Object [][]rows=new Object[setupItems.size()][4];
						for(int i=0;i<setupItems.size();i++){
							 JSONObject item= setupItems.get(i).isObject();
							 int type= Integer.parseInt(item.get("type").isString().stringValue());
							 Object defaultValue= item.get("defaultValue").isObject();
							 Double index= item.get("index").isNumber().doubleValue();
							 String fieldName= item.get("fieldName").isString().stringValue();
							 rows[i]=new Object[]{index.intValue(),defaultValue,type,fieldName};
							
						}
						 Arrays.sort(rows,new Comparator<Object>(){
								@Override
								public int compare(Object o1, Object o2) {
									int i1=(Integer) (((Object[])o1)[0]);
									int i2=(Integer) (((Object[])o2)[0]);		
									return i1-i2;
								}} );
						 mappingData=rows;
						 recreateImportSetupView(rows);
						break;
						
					}
				}
				
			}
		});
		return h1;
	}
//**********************************************************************************************************************
	protected Widget createImportTypePanel(){
		HorizontalPanel h2=new HorizontalPanel();
		h2.setSpacing(5);
		Label setupL=new Label(constants.importType()+"*:");
		setupL.setStylePrimaryName("clsLabel");
		h2.add(setupL);
		h2.add(importTypes);
		importTypes.setStylePrimaryName("clsSelect");
		h2.setSpacing(5);
		return h2;
	}
//**********************************************************************************************************************
	@SuppressWarnings("deprecation")
	protected Widget createImportInstancePanel(){
		HorizontalPanel h3=new HorizontalPanel();
		Label instanceL=new Label(constants.importInstances()+":");
		instanceL.setStylePrimaryName("clsLabel");
		h3.setSpacing(5);
		h3.add(instanceL);
		importInstances.setStylePrimaryName("clsSelect");
		h3.add(importInstances);
		loadInstances();
		final ImageHyperlink deleteLink=new ImageHyperlink(images.drop().createImage());
		deleteLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MessageBox.confirm(constants.warning(), constants.deleteConfigurationConfirm(),new MessageBox.ConfirmCallback() {
					
					@Override
					public void execute(String btnID) {
						if("yes".equalsIgnoreCase(btnID)){
							ImportInstanceServiceAsync proxy=(ImportInstanceServiceAsync) GWT.create(ImportInstanceService.class);
									((ServiceDefTarget) proxy).setServiceEntryPoint(GWT.getHostPageBaseURL() + "delete_Co_Ut_ImportInstance"+GWTConstants.STRUTS_EXTENSION);
									proxy.deleteSettingInstance(Long.parseLong((String)EditorFactory.getWidgetValue(importInstances)),new AsyncCallback<ExecutionResult>(){

										@Override
										public void onFailure(Throwable e) {
											MessageUtility.error(constants.error(), constants.failToAccessServer());
											GWT.log(e.toString());
											
										}

										@Override
										public void onSuccess(ExecutionResult result) {
											if(result==null){
												MessageUtility.error(constants.error(), constants.failToAccessServer());
											}else{
												ProcessMessageHandler.showExecutionMessages(result, constants);
											}
											
										}
										
									} );		
						}
						
					}
				});
			}
		});
		h3.add(deleteLink);
		deleteLink.setVisible(false);
		importInstances.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				updateInstanceConfigs();
				if(importInstances.getSelectedIndex()>0){
					deleteLink.setVisible(true);
				}else{
					deleteLink.setVisible(false);
				}
				
			}
		});
		updateInstanceConfigs();
		return h3;
	}
//**********************************************************************************************************************
	private void loadInstances(){
		importInstances.clear();
		importInstances.addItem("---", "-1");
		if(importSetting.getSelectedIndex()>0){
			ImportInstanceServiceAsync proxy=(ImportInstanceServiceAsync) GWT.create(ImportInstanceService.class);
			((ServiceDefTarget) proxy).setServiceEntryPoint(GWT.getHostPageBaseURL() + "load_Co_Ut_ImportInstance"+GWTConstants.STRUTS_EXTENSION);
			proxy.loadImportInstances(Long.parseLong((String)EditorFactory.getWidgetValue(importSetting)), 
					  Integer.parseInt((String)EditorFactory.getWidgetValue(importTypes)), new AsyncCallback<UILookupInfo>() {

						@Override
						public void onFailure(Throwable e) {
							MessageUtility.error(constants.error(), constants.failToAccessServer());
							GWT.log(e.toString());
							
						}

						@Override
						public void onSuccess(UILookupInfo result) {
							if(result==null){
								MessageUtility.error(constants.error(), constants.failToAccessServer());
							}else{
								currentInstancesData=result.getData();
								for (String row[]:result.getData()){
									importInstances.addItem(row[1],row[0]);
								}
							}
							
						}
					});
		}
	}
//**********************************************************************************************************************
	protected void initImportConfigs(){
		MessageUtility.progress(constants.pleaseWait(), constants.loadingData());
		DataImportServerService.getServer().loadImportConfigurations(getUsecaseName(), new AsyncCallback<UILookupInfo>() {
			
			@Override
			public void onSuccess(UILookupInfo result) {
				MessageUtility.stopProgress();
				if(result==null){
					MessageUtility.error(constants.error(), constants.failToAccessServer());
					return;
				}
				configs=result;
				EditorFactory.setListBoxValue(importSetting, result);
				loadDataInputFormDataModel();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				MessageUtility.stopProgress();
				MessageUtility.error(constants.error(), constants.failToAccessServer());
				GWT.log(caught.toString());
			}
		});
		
	}
//**********************************************************************************************************************
	protected void updateInstanceConfigs(){
		instanceConfigPanel.clear();
		Object []data=null; 
		if(importInstances.getSelectedIndex()>0){
			data=currentInstancesData[importInstances.getSelectedIndex()-1];
		}
		Widget panel;
		if(excelImportWidget==null){
			excelImportWidget=createExcelImportPanel(data);
		}
		if(SQLimportWidget==null){
			SQLimportWidget=createSQLImportPanel(data);
		}
		if(importTypes.getSelectedIndex()==0){
			panel=excelImportWidget;
		}else{
			panel=SQLimportWidget;
		}
		instanceConfigPanel.add(panel);
		instanceConfigPanel.setCellHorizontalAlignment(instanceConfigPanel,HasHorizontalAlignment.ALIGN_CENTER );
		Label descriotionL=new Label(constants.description()+":");
		descriotionL.setWidth(LABELS_WIDTH);
		descriotionL.setStylePrimaryName("clsLabel");
		description.setStylePrimaryName("clsText");
		HorizontalPanel h=new HorizontalPanel();
		h.setSpacing(5);
		h.add(descriotionL);
		h.add(description);
		description.setWidth("500");
		instanceConfigPanel.add(h);
	}
//**********************************************************************************************************************
	protected Widget createSQLImportPanel(Object []data){
		VerticalPanel root=new VerticalPanel();
		Label resourceL=new Label(constants.resourceName()+"*:");
		resourceL.setWidth(LABELS_WIDTH);
		resourceL.setStylePrimaryName("clsLabel");
		HorizontalPanel h1=new HorizontalPanel();
		h1.setSpacing(5);
		h1.add(resourceL);
		resourceName.setStylePrimaryName("clsText");
		resourceName.setWidth("200");
		h1.add(resourceName);
		root.add(h1);
		
		HorizontalPanel h2=new HorizontalPanel();
		Label l2=new Label();
		l2.setWidth(LABELS_WIDTH);
		l2.setStylePrimaryName("clsLabel");
		l2.setText(constants.sql()+"*:");
		h2.setSpacing(5);
		sql.setStylePrimaryName("clsText");
		sql.setWidth("500");
		sql.setHeight("300");
		h2.add(l2);
		h2.add(sql);
		
		root.add(h2);
		return root;
	}
//**********************************************************************************************************************
	protected Widget createExcelImportPanel(Object []data){
		VerticalPanel root=new VerticalPanel();
		root.add(excelFileLocation);
		final HorizontalPanel localFilePanel=new HorizontalPanel();
		final HorizontalPanel serverFilePanel=new HorizontalPanel();
		excelFileLocation.setValue("1");
		excelFileLocation.setValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if("1".equals(excelFileLocation.getValue())){
					localFilePanel.setVisible(true);
					serverFilePanel.setVisible(false);
				}else{
					serverFilePanel.setVisible(true);
					localFilePanel.setVisible(false);
				}
				
			}
		});
		
		Label serverFileNameL=new Label(constants.fileLocation()+"*:");
		serverFileNameL.setStylePrimaryName("clsLabel");
		serverFileNameL.setWidth(LABELS_WIDTH);
		localFilePanel.add(serverFileNameL);
		localFilePanel.setSpacing(5);
		localFilePanel.add(localFile);
		localFile.setStyleName("clsText");
		root.add(localFilePanel);
		
		Label localFileNameL=new Label(constants.fileLocation()+"*:");
		localFileNameL.setStylePrimaryName("clsLabel");
		localFileNameL.setWidth(LABELS_WIDTH);
		serverFilePanel.add(localFileNameL);
		serverFilePanel.setVisible(false);
		serverFilePanel.setSpacing(5);
		serverFilePanel.add(serverFileName);
		serverFileName.setStylePrimaryName("clsText");
		serverFileName.setWidth("500");
		root.add(serverFilePanel);
		
		HorizontalPanel h1=new HorizontalPanel();
		h1.setSpacing(5);
		Label sheetIndexL=new Label(constants.sheetIndex()+"*:");
		sheetIndexL.setStyleName("clsLabel");
		sheetIndexL.setWidth(LABELS_WIDTH);
		h1.add(sheetIndexL);
		sheetIndex.setMinValue(1);
		sheetIndex.setDecimalPrecision(0);
		sheetIndex.setValue(1);
		sheetIndex.setStyleName("clsText");
		h1.add(sheetIndex);
		root.add(h1);
		
		firstLineTitle.setText(constants.firstLineLable());
		firstLineTitle.setWidth(LABELS_WIDTH);
		firstLineTitle.setStyleName("clsLabel");
		firstLineTitle.setValue(true);
		firstLineTitle.getElement().setAttribute("value", "true");
		root.add(firstLineTitle);
		
		
		HorizontalPanel h2=new HorizontalPanel();
		h2.setSpacing(5);
		Label fromRowL=new Label(constants.fromRow()+":");
		fromRowL.setStylePrimaryName("clsLabel");
		fromRowL.setWidth(LABELS_WIDTH);
		h2.add(fromRowL);
		fromRow.setCls("clsText");
		fromRow.setDecimalPrecision(0);
		fromRow.setMinValue(1);
		fromRow.setValue(1);
		h2.add(fromRow);
		
		Label toRowL=new Label(constants.toRow()+":");
		toRowL.setStylePrimaryName("clsLabel");
		toRowL.setWidth(LABELS_WIDTH);
		h2.add(toRowL);
		toRow.setCls("clsText");
		toRow.setDecimalPrecision(0);
		toRow.setMinValue(1);
		h2.add(toRow);
		root.add(h2);
		return root;
	} 
//**********************************************************************************************************************
	protected void loadDataInputFormDataModel(){
		MessageUtility.progress(constants.pleaseWait(), constants.loadingData());
		DataInputServerService.getServer().getDataInputForm(getFormClass(), getUsecaseName(), getActionName(), new AsyncCallback<DataInputFormModel>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageUtility.error(constants.error(), constants.failToAccessServer());
				
			}

			@Override
			public void onSuccess(DataInputFormModel result) {
				MessageUtility.stopProgress();
				if(result==null){
					MessageUtility.error(constants.error(), constants.failToAccessServer());
				}else{
					dataInputModel=result;
				}
				
			}
		});
	}
//**********************************************************************************************************************
	protected void recreateImportSetupView(Object [][]rows){
		int index=0;
		int rowIndex=1;
		CellFormatter formatter= importSetupView.getCellFormatter();
		int lastColIndex=-1;
		int colWidth=100/IMPORT_ITEM_DISPLAY_COLUMN_COUNT;
		colWidth=colWidth/4;
		L1: while(index<rows.length){
			for(int j=0;j<IMPORT_ITEM_DISPLAY_COLUMN_COUNT;j++){
				if(index>=rows.length){
					lastColIndex=j;
					break L1;
				}
				createItemColumn(rows[index], rowIndex, j,String.valueOf(colWidth)+"%");
				index++;
			}
			rowIndex++;
		}
		for(int j=lastColIndex;lastColIndex>0&&j<IMPORT_ITEM_DISPLAY_COLUMN_COUNT;j++){
			createItemColumn(null, rowIndex, j,String.valueOf(colWidth)+"%");
		}
		for(int j=0;j<IMPORT_ITEM_DISPLAY_COLUMN_COUNT;j++){
			importSetupView.setWidget(0, (j*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+j, new Label(constants.fieldName()));
			formatter.setStylePrimaryName(0, (j*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+j, "clsTableHeader");
			formatter.setWidth(rowIndex, (j*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+j, colWidth+"%");
			
			importSetupView.setWidget(0, (j*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+j+1, new Label(constants.mapType()));
			formatter.setStylePrimaryName(0, (j*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+j+1, "clsTableHeader");
			formatter.setWidth(rowIndex, (j*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+j+1, colWidth+"%");
			
			importSetupView.setWidget(0, (j*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+j+2, new Label(constants.index()));
			formatter.setStylePrimaryName(0, (j*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+j+2, "clsTableHeader");
			formatter.setWidth(rowIndex, (j*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+j+2, colWidth+"%");
			
			importSetupView.setWidget(0, (j*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+j+3, new Label(constants.defaultValue()));
			formatter.setStylePrimaryName(0, (j*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+j+3, "clsTableHeader");
			formatter.setWidth(rowIndex, (j*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+j+3, colWidth+"%");
		}
	}
//**********************************************************************************************************************
	protected void createItemColumn(Object []rows,int rowIndex,int columnIndex,String colWidth){
		CellFormatter formatter= importSetupView.getCellFormatter();
		String header,mapType,index,defaultValue;
		if(rows!=null){
			InputItem item= dataInputModel.getInputItem((String)rows[3]);
			header=item.getHeader();
			mapType=translateMapType((Integer)rows[2]);
			index=String.valueOf(rows[0]);
			defaultValue=writeDefaultValue((String)rows[1]);
		}else{
			header="";
			mapType="";
			index="";
			defaultValue="";
		}
		Label headerl= new Label(header);
		headerl.setStylePrimaryName("clsLabel");
		importSetupView.setWidget(rowIndex, (columnIndex*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+columnIndex, headerl);
		formatter.setWidth(rowIndex, (columnIndex*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+columnIndex, colWidth);
		
		Label mapTypeL= new Label(mapType);
		mapTypeL.setStylePrimaryName("clsLabel");
		importSetupView.setWidget(rowIndex, (columnIndex*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+columnIndex+1, mapTypeL);
		formatter.setWidth(rowIndex, (columnIndex*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+columnIndex+1, colWidth);
		
		Label indexL=new Label(index);
		indexL.setStylePrimaryName("clsLabel");
		importSetupView.setWidget(rowIndex, (columnIndex*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+columnIndex+2, indexL);
		formatter.setWidth(rowIndex, (columnIndex*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+columnIndex+2, colWidth);
		
		Label defaultValueL=new Label(defaultValue);
		defaultValueL.setStylePrimaryName("clsLabel");
		importSetupView.setWidget(rowIndex, (columnIndex*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+columnIndex+3, defaultValueL);
		formatter.setWidth(rowIndex, (columnIndex*IMPORT_ITEM_DISPLAY_COLUMN_COUNT)+columnIndex+3, colWidth);
	}
//**********************************************************************************************************************
	protected String translateMapType(int type){
		switch (type) {
		case 0:
			return constants.mapByCode();
		case 1:
			return constants.mapByName();
		case 2:
		case 3:
			return constants.equal();
		case 4:
			return constants.mapByFixedValue();	
		case 5:
			return constants.parseAsGregorianDate();
		case 6:
			return constants.parseAsGregorianDate();
		case 7:
			return constants.mapById();
		case 8:
			return constants.booleanConversion();
		
		default:return "";
			
		}
	}
//**********************************************************************************************************************
	protected String writeDefaultValue(String defaultValue){
		if(defaultValue==null)return "";
		if("true".equals(defaultValue))return constants.booleanTrue();
		if("false".equals(defaultValue))return constants.booleanFalse();
		return defaultValue;
	}
//**********************************************************************************************************************
	protected void loadDataInputModel(){
		MessageUtility.progress(constants.pleaseWait(), constants.loading());
		DataInputServerService.getServer().getDataInputForm(getFormClass(),getUsecaseName(),
				getActionName(), new AsyncCallback<DataInputFormModel>() {
					
					@Override
					public void onSuccess(DataInputFormModel result) {
						MessageUtility.stopProgress();
						if(result!=null){
							dataInputModel=result;
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
	protected void initImportTypes(){
		importTypes.addItem(constants.importTypeExcel(), "0");
		importTypes.addItem(constants.importTypeSQL(), "2");
		importTypes.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if(importInstances.getSelectedIndex()==0){
					if(importTypes.getSelectedIndex()==0){
						updateInstanceConfigs();
					}else{
						updateInstanceConfigs();
					}
				}
			}
		});
	}

//**********************************************************************************************************************
	public native String getUsecaseName()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImportPage::USECASE_PARAMETER).value;
	}-*/;
	
//**********************************************************************************************************************
	public native String getFormClass()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImportPage::FORM_CLASS_PARAMETER).value;
	}-*/;
//**********************************************************************************************************************
	public native String getSettingId()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImportPage::SELECTED_SETTING_ID).value;
	}-*/;
//**********************************************************************************************************************
//	public native String getSettingIntanceId()/*-{
//	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImportPage::SETTING_INSTANCE_ID).value;
//	}-*/;
//**********************************************************************************************************************
	public native String getActionName()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImportPage::ACTION_NAME_PARAMETER).value;
	}-*/;
//**********************************************************************************************************************
	public native String getImportType()/*-{
	return $doc.getElementById(@ir.utopia.core.util.tags.importpage.client.ImportPage::IMPORT_TYPE).value;
	}-*/;
//**********************************************************************************************************************
	public static void redirect(String url){
		Window.Location.replace(GWT.getHostPageBaseURL()+url);
	}
//************************************************************************************************************************
	public void redirectToExImConfigurationPage(){
			fPanel.clear();
			VerticalPanel vpanel=new VerticalPanel();
			fPanel.setWidget(vpanel);
			vpanel.add(new Hidden(UIComponentsConstants.USE_CASE_FORM_CLASS_PARAMETER_NAME, getFormClass()));
			vpanel.add(new Hidden(UIComponentsConstants.USE_CASE_NAME_PARAMETER_NAME, getUsecaseName()));
			vpanel.add(new Hidden("__settingId",EditorFactory.getWidgetValue(importSetting).toString()));
			vpanel.add(new Hidden("__forwardURL","ImportPage"+GWTConstants.STRUTS_EXTENSION+"?"+SETTING_INSTANCE_ID+"="+EditorFactory.getWidgetValue(importInstances)));
			vpanel.add(new Hidden("__importType", EditorFactory.getWidgetValue(importTypes).toString()));
			vpanel.add(new Hidden(UIComponentsConstants.USE_ACTION_NAME_PARAMETER_NAME, "import"));
			fPanel.setMethod(FormPanel.METHOD_POST);
			fPanel.setAction("ImportExportDefinition"+GWTConstants.STRUTS_EXTENSION);
			fPanel.submit();
		}
}
