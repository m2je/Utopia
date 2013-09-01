package ir.utopia.core.util.tags.utilitypanel.client;

import ir.utopia.core.util.tags.datainputform.client.EditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.GlobalEditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.customproperty.CustomPropertyPanel;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.ProcessMessageHandler;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.utilitypanel.client.model.SMTPConfiguration;
import ir.utopia.core.util.tags.utilitypanel.client.model.UtilityPanelConfigurationExchange;
import ir.utopia.core.util.tags.utilitypanel.client.model.UtilityPanelConfigurationExchangeAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SMTPConfiguarytionPanel extends VerticalPanel implements UtilityPanelTab {
	ListBox savedServers;
	TextBox name,serverAddress,serverPort;
	UtilityConstants constants;
	CheckBox advancebox;
	CustomPropertyPanel customProps;
	TextArea description;
	boolean dirty=false;
	boolean panelDataModified=false;
	public SMTPConfiguarytionPanel(final UtilityConstants constants){
		this.constants=constants;
		HorizontalPanel p1 = getSelectServerBox();
		add(p1);
		add(getServerParameters());
		add(getDynamicParametersPanel());
		add(getActionButtons());
		
		if(LocaleInfo.getCurrentLocale().isRTL()){
		    	getElement().setDir("rtl");
		}
		savedServers.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if(dirty||customProps.isDirty() ){
					if(Window.confirm(constants.discardConfigurationConfirm()))
							load();
				}else{
							load();
				}
				
			}
		});
		EditorDataChangeListener listener=new EditorDataChangeListener() {
			
			@Override
			public void dataChanges(Widget widget, Object newValue,
					int[] depenedetTypes, String[][] dependentfields) {
					dirty=true;
				
			}

			@Override
			public void dataChanges(String widgetName, Object newValue,
					int[] depenedetTypes, String[][] dependentfields) {
				dirty=true;
				
			}
		};
		GlobalEditorDataChangeListener changeListener=new GlobalEditorDataChangeListener(listener, null);
		EditorFactory.addListener(name, changeListener);
		EditorFactory.addListener(serverAddress, changeListener);
		EditorFactory.addListener(serverPort, changeListener);
		EditorFactory.addListener(description, changeListener);
		loadCurrentConfigurations();
	}
//**************************************************************************************
	public boolean isPanelDataModified(){
		return panelDataModified;
	}
//**************************************************************************************
	public void cleaDataModification(){
		panelDataModified=false;
	}
//**************************************************************************************
	public void reload(boolean ignoreDirty){
		clearPage();
		loadCurrentConfigurations();
		
	}
//**************************************************************************************
	private void loadCurrentConfigurations(){
		String serverPath;
		serverPath="loadCurrentConfigurations_Co_Ut_SmtpConfiguration.htm";
		UtilityPanelConfigurationExchangeAsync service = (UtilityPanelConfigurationExchangeAsync) GWT.create(UtilityPanelConfigurationExchange.class);
	    ServiceDefTarget endpoint = (ServiceDefTarget) service;
	    endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL()+ serverPath);
	    service.loadCurrentConfigurations(new AsyncCallback<UILookupInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(constants.failToAccessServer());
				GWT.log(caught.toString());
			}

			@Override
			public void onSuccess(UILookupInfo info) {
				initSavedServers(info);
			}
		});
	}
//**************************************************************************************
	private void load(){
		int index= savedServers.getSelectedIndex();
		if(index==0){
			clearPage();
		}else{
			Long currentId=Long.parseLong(savedServers.getValue(index));
			String serverPath="loadConfiguration_Co_Ut_SmtpConfiguration.htm";
			UtilityPanelConfigurationExchangeAsync service = (UtilityPanelConfigurationExchangeAsync) GWT.create(UtilityPanelConfigurationExchange.class);
		    ServiceDefTarget endpoint = (ServiceDefTarget) service;
		    endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL()+ serverPath);
		    service.loadConfiguration(currentId, new AsyncCallback<SMTPConfiguration>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(constants.failToAccessServer());
					GWT.log(caught.toString());
				}

				@Override
				public void onSuccess(SMTPConfiguration result) {
					name.setText(result.getServerName());
					serverAddress.setText(result.getServerAdrress());
					serverPort.setText(String.valueOf(result.getSmtpPort()));
					customProps.setValue(result.getCustomPropertyNames(), result.getCustomPropertyValues()); 
					description.setValue(result.getDescription());
				}
			});
		}
		 dirty=false;
	}
//**************************************************************************************
	private void clearPage(){
		loadCurrentConfigurations();
		name.setValue("");
		serverAddress.setValue("");
		serverPort.setValue("");
		customProps.clearComponent();
		description.setText("");
		dirty=false;
	}
//**************************************************************************************
	private HorizontalPanel getDescriptionPanel(){
		description=new TextArea();
		description.setStylePrimaryName("clsTextArea");
		description.setName("description");
		HorizontalPanel result=new HorizontalPanel();
		result.setWidth("100%");
		Label l=new Label();
		l.setStylePrimaryName("clsLabel");
		l.setText(constants.description()+":");
		result.add(l);
		result.add(description);
		return result;
	}
//*************************************************************************************
	private HorizontalPanel getSelectServerBox() {
		HorizontalPanel p1=new HorizontalPanel();
		Label label=new Label(constants.serverName()+":");
		label.setStylePrimaryName("clsLabel");
		
		savedServers=new ListBox();
		savedServers.setName("configurationSelect");
		savedServers.setStylePrimaryName("clsSelect");
		
		p1.setSpacing(5);
		p1.add(label);
		p1.add(savedServers);
		Button deleteConfiguration=new Button();
		deleteConfiguration.setStylePrimaryName("clsDeleteButton");
		deleteConfiguration.setText(constants.delete());
		deleteConfiguration.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(Window.confirm(constants.configurationDeleteConfirm())){
					UtilityPanelConfigurationExchangeAsync service = (UtilityPanelConfigurationExchangeAsync) GWT.create(UtilityPanelConfigurationExchange.class);
				    ServiceDefTarget endpoint = (ServiceDefTarget) service;
				    endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL()+ "delete_Co_Ut_SmtpConfiguration.htm");
				    
				    AsyncCallback<ProcessExecutionResult> callback= new AsyncCallback<ProcessExecutionResult>(){

						@Override
						public void onFailure(Throwable e) {
							Window.alert(constants.failToAccessServer());
							GWT.log(e.toString());
						}

						@Override
						public void onSuccess(ProcessExecutionResult result) {
							if(!result.isSuccess()){
								Window.alert(constants.failToDelete());
								ProcessMessageHandler.showExecutionMessages(result, constants);
								return;
							}
							clearPage();
							panelDataModified=true;
						}};
						service.deleteSMTPConfiguration(Long.parseLong( savedServers.getValue(savedServers.getSelectedIndex())) , callback);
				}
				
			}
		});
		p1.setSpacing(5);
		p1.add(deleteConfiguration);
		initSavedServers(null);
		return p1;
	}

//**************************************************************************************
	private void initSavedServers(UILookupInfo info){
		savedServers.clear();
		savedServers.addItem("-------------------------", "-1");
		if(info!=null){
			String[][]data= info.getData();
			if(data!=null){
				for(int i=0;i<data.length;i++ ){
					savedServers.addItem(data[i][1],data[i][0]);
				}
			}
		}
	}
//**************************************************************************************
	private Widget getServerParameters(){
		VerticalPanel result=new VerticalPanel();
		HorizontalPanel h1=new HorizontalPanel();
		Label label=new Label(constants.serverName()+":");
		label.setStylePrimaryName("clsLabel");
		h1.add(label);
		h1.setSpacing(5);
		name=new TextBox();
		name.setWidth("250px");
		name.setStylePrimaryName("clsText");
		h1.add(name);
		
		
		HorizontalPanel h2=new HorizontalPanel();
		Label label2=new Label(constants.serverAddress()+":");
		label2.setStylePrimaryName("clsLabel");
		h2.add(label2);
		h2.setSpacing(5);
		serverAddress=new TextBox();
		serverAddress.setWidth("400px");
		serverAddress.setStylePrimaryName("clsText");
		h2.add(serverAddress);
		
		HorizontalPanel h3=new HorizontalPanel();
		Label label3=new Label(constants.serverPort()+":");
		label3.setStylePrimaryName("clsLabel");
		h3.add(label3);
		h3.setSpacing(5);
		serverPort=new TextBox();
		serverPort.setWidth("50px");
		serverPort.setStylePrimaryName("clsText");
		h3.add(serverPort);
		VerticalPanel root=new VerticalPanel();
		root.add(h1);
		root.add(h2);
		root.add(h3);
		root.add(getDescriptionPanel());
		result.add(root);
		result.setBorderWidth(1);
		return result;
	}	
//**************************************************************************************
	public Widget getDynamicParametersPanel(){
			VerticalPanel result=new VerticalPanel();
			customProps=new CustomPropertyPanel();
			result.add(customProps);
		    return result;
	}
//**************************************************************************************
	private Widget getActionButtons( ){
		Grid result=new Grid(1,3);
		result.setWidth("100%");
		result.getColumnFormatter().setWidth(0, "30%");
		result.getColumnFormatter().setWidth(1, "30%");
		result.getColumnFormatter().setWidth(2, "30%");
		Button submit=new Button(constants.save());
		submit.setStyleName("clsSaveButton");
		submit.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				submit();
				
			}});
		HorizontalPanel panel=new HorizontalPanel();
		panel.getElement().setDir(LocaleInfo.getCurrentLocale().isRTL()?"rtl":"ltr");
		panel.add(submit);
		
		Button reset=new Button(constants.reload());
		reset.setStyleName("clsReloadButton");
		reset.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				reload(true);
			}
		});
		panel.add(reset);
		result.setWidget(0, 1, panel);
		return result;
	}
//**************************************************************************************
private void submit(){
		if(validate()){
			
			String serverPath;
			if(savedServers.getSelectedIndex()==0){
				serverPath="save_Co_Ut_SmtpConfiguration.htm";
			}else{
				serverPath="update_Co_Ut_SmtpConfiguration.htm";
			}
			UtilityPanelConfigurationExchangeAsync service = (UtilityPanelConfigurationExchangeAsync) GWT.create(UtilityPanelConfigurationExchange.class);
		    ServiceDefTarget endpoint = (ServiceDefTarget) service;
		    endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL()+ serverPath);
		    AsyncCallback<ProcessExecutionResult> callback= new AsyncCallback<ProcessExecutionResult>(){

				@Override
				public void onFailure(Throwable e) {
					Window.alert(constants.failToAccessServer());
					GWT.log(e.toString());
				}

				@Override
				public void onSuccess(ProcessExecutionResult result) {
					if(!result.isSuccess()){
						Window.alert(constants.failToSave());
						ProcessMessageHandler.showExecutionMessages(result, constants);
					}else{
						reload(true);
						panelDataModified=true;
					}
					
					
				}};
				
				if(savedServers.getSelectedIndex()==0){
					service.saveSMTPConfiguration( name.getValue(), serverAddress.getValue(),Integer.parseInt( serverPort.getValue()), description.getValue() ,customProps.getValue(), callback);
				}else{
					Long smtpConfigurationId=Long.parseLong(savedServers.getValue(savedServers.getSelectedIndex()));
					service.updateSMTPConfiguration(smtpConfigurationId, name.getValue(), serverAddress.getValue(),Integer.parseInt( serverPort.getValue()),description.getValue(), customProps.getValue(), callback);
				}
		    
		}
	}
//**************************************************************************************
private boolean validate(){
	if(EditorFactory.valueIsNull(InputItem.DISPLAY_TYPE_STRING, name.getValue())){
		Window.alert(constants.invalidServerName());
		return false;
	}
	if(EditorFactory.valueIsNull(InputItem.DISPLAY_TYPE_STRING, serverAddress.getValue())){
		Window.alert(constants.invalidServerAddress());
		return false;
	}
	if(EditorFactory.valueIsNull(InputItem.DISPLAY_TYPE_NUMERIC, serverPort.getValue())){
		Window.alert(constants.invalidServerPort());
		return false;
	}
	try {
		Integer.parseInt(serverPort.getValue().trim());
	} catch (NumberFormatException e) {
		Window.alert(constants.invalidServerPort());
		return false;
	}
	return customProps.validate();
}
//**************************************************************************************
public boolean discardPanel(){
	return(!dirty||Window.confirm(constants.discardConfigurationConfirm()));
}
//**************************************************************************************
}
