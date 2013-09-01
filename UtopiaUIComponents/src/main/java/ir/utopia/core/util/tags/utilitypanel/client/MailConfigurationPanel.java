package ir.utopia.core.util.tags.utilitypanel.client;

import ir.utopia.core.util.tags.datainputform.client.EditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.GWTConstants;
import ir.utopia.core.util.tags.datainputform.client.GlobalEditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.ProcessMessageHandler;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.password.PasswordEdior;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.utilitypanel.client.model.MailConfiguration;
import ir.utopia.core.util.tags.utilitypanel.client.model.MailConfigurationService;
import ir.utopia.core.util.tags.utilitypanel.client.model.MailConfigurationServiceAsync;
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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MailConfigurationPanel extends VerticalPanel implements UtilityPanelTab{
	UtilityConstants constants;
	ListBox servers=new ListBox();
	ListBox usersMailBox=new ListBox();
	TextBox username=new TextBox();
	InputItem passwordItem=new InputItem();
	PasswordEdior password; 
	TextArea description=new TextArea();
	boolean dirty =false;
	public MailConfigurationPanel(final UtilityConstants constants){
		this.constants=constants;
		
		VerticalPanel r1=new VerticalPanel();
		r1.setSpacing(5);
		HorizontalPanel r11=new HorizontalPanel();
		r11.setSpacing(5);
		Label serversLabel=new Label();
		serversLabel.setText(constants.serverName()+":");
		serversLabel.setStylePrimaryName("clsLabel");
		
		r11.add(serversLabel);
		servers.setStylePrimaryName("clsSelect");
//		r11.setCellWidth(serversLabel, "30%");
//		r11.setCellWidth(servers,"50%");
		servers.setWidth("400px");
		servers.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				initMailAdresses();
				
			}
		});
		r11.add(servers);
		
		HorizontalPanel r12=new HorizontalPanel();
		r12.setSpacing(5);
		Label userNameLabel=new Label();
		userNameLabel.setStylePrimaryName("clsLabel");
		userNameLabel.setText(constants.username()+":");
		r12.add(userNameLabel );
		usersMailBox.setStylePrimaryName("clsSelect");
		usersMailBox.setWidth("400px");
		r12.add(usersMailBox);
		Button deleteMail=new Button();
		deleteMail.setStylePrimaryName("clsDeleteButton");
		deleteMail.setText(constants.delete());
		r12.add(deleteMail);
		deleteMail.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(usersMailBox.getSelectedIndex()>0&&Window.confirm(constants.configurationDeleteConfirm())){
					deleteMailAddress(Long.parseLong( usersMailBox.getValue(usersMailBox.getSelectedIndex())));
				}
				
			}
		});
		usersMailBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if(usersMailBox.getSelectedIndex()>0){
					MailConfigurationServiceAsync service=(MailConfigurationServiceAsync)GWT.create(MailConfigurationService.class);
					((ServiceDefTarget)service).setServiceEntryPoint("loadConfiguration_Co_Ut_SmtpAddresses"+GWTConstants.STRUTS_EXTENSION);
					if(discardPanel()){
						service.loadConfiguration(Long.parseLong( usersMailBox.getValue( usersMailBox.getSelectedIndex())), new AsyncCallback<MailConfiguration>(){

							@Override
							public void onFailure(Throwable caught) {
								GWT.log(caught.toString());
								
							}

							@Override
							public void onSuccess(MailConfiguration result) {
								if(result==null){
									Window.alert(constants.failToAccessServer());
									return;
								}
								description.setText( result.getDescription());
								username.setText(result.getUser());
								password.setValue(result.getPasssword());
								dirty=false;
							}});

					}
				}
				
			}
		});
//		r12.setCellWidth(userNameLabel, "20%");
//		r12.setCellWidth(usersMailBox,"60%");
//		r12.setCellWidth(deleteMail, "20%");
		r11.setWidth("100%");
		r12.setWidth("100%");
		r1.setWidth("70%");
		r12.setWidth("70%");
		r1.setSpacing(5);
		r1.add(r11);
		r1.add(r12);
		r1.setBorderWidth(1);
		
		HorizontalPanel r2=new HorizontalPanel();
		r2.setSpacing(5);
		Label userNameLabel2=new Label();
		userNameLabel2.setStylePrimaryName("clsLabel");
		userNameLabel2.setText(constants.username()+":");
		r2.add(userNameLabel2 );
		username.setStylePrimaryName("clsText");
		username.setWidth("400px");
		r2.setWidth("100%");
		r2.add(username);
		
		
		
		add(r1);
		add(r2);

		passwordItem.setDisplayType(InputItem.DISPLAY_TYPE_PASSWORD);
		passwordItem.setConfirmRequired(true);
		passwordItem.setHeader(constants.password());
		password=(PasswordEdior)EditorFactory.createEditor(passwordItem, LocaleInfo.getCurrentLocale().isRTL()?"RTL":"LTR");
		add(password);
		
		HorizontalPanel r3=new HorizontalPanel();
		r3.setSpacing(5);
		Label descLabel=new Label();
		descLabel.setStylePrimaryName("clsLabel");
		descLabel.setText(constants.description()+":");
		r3.add(descLabel );
		description.setStylePrimaryName("clsText");
		r3.setWidth("700px");
		r3.add(description);
		r3.setCellWidth(descLabel, "10%");
		r3.setCellWidth(description,"50%");
		r3.setCellWidth(new HTML(""), "40%");
		add(r3);
		
		add(getActionButtons());
		setWidth("100%");
		if(LocaleInfo.getCurrentLocale().isRTL()){
			getElement().setDir("RTL");
		}
		initServerList();
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
		EditorFactory.addListener(username, changeListener);
		EditorFactory.addListener(password, changeListener);
		EditorFactory.addListener(description, changeListener);
	}
//*******************************************************************************************	
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
				
			}

			});
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
//*******************************************************************************************	
	private void submit() {
		if(validate()){
			int index= usersMailBox.getSelectedIndex();
			MailConfigurationServiceAsync service=(MailConfigurationServiceAsync)GWT.create(MailConfigurationService.class);
			AsyncCallback<ProcessExecutionResult> callback=new  AsyncCallback<ProcessExecutionResult>(){

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(constants.failToAccessServer());
					GWT.log(caught.toString());
					
				}

				@Override
				public void onSuccess(ProcessExecutionResult result) {
					if(result==null||!result.isSuccess()){
						Window.alert(constants.failToSave());
						if(result!=null)
							ProcessMessageHandler.showExecutionMessages(result, constants);
						return;
					}
						clean();
				}
				
			};
			if(index==0){
				((ServiceDefTarget)service).setServiceEntryPoint("save_Co_Ut_SmtpAddresses"+GWTConstants.STRUTS_EXTENSION);
				service.saveMailConfiguration(Long.parseLong(servers.getValue(servers.getSelectedIndex())), username.getValue().trim(), password.getValue(), description.getValue()==null?"":description.getValue().trim(), callback);
			}else{
				((ServiceDefTarget)service).setServiceEntryPoint("update_Co_Ut_SmtpAddresses"+GWTConstants.STRUTS_EXTENSION);
				service.updateMailConfiguration(Long.parseLong(usersMailBox.getValue(index)), username.getValue().trim(), password.getValue(), description.getValue()==null?"":description.getValue().trim(), callback);
			}
		}
		
	}
//*******************************************************************************************
	private boolean validate(){
		if(servers.getSelectedIndex()==0){
			Window.alert(constants.invalidServerAddress());
			return false;
		}
		if(username.getValue()==null||username.getValue().trim().length()==0){
			Window.alert(constants.invalidUserName());
			return false;
		}

		return password.validate();
	}
//*******************************************************************************************
	public void reload(boolean ignoreDirty) {
		if(ignoreDirty||!dirty||Window.confirm(constants.discardConfigurationConfirm())){
			clean();
		}
	}
//*******************************************************************************************
	private void deleteMailAddress(long mailId){
		MailConfigurationServiceAsync service=(MailConfigurationServiceAsync)GWT.create(MailConfigurationService.class);
		((ServiceDefTarget)service).setServiceEntryPoint("delete_Co_Ut_SmtpAddresses"+GWTConstants.STRUTS_EXTENSION);
		service.deleteMailConfiguration(mailId, new AsyncCallback<ProcessExecutionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(constants.failToAccessServer());
				GWT.log(caught.toString());
			}

			@Override
			public void onSuccess(ProcessExecutionResult result) {
				if(!result.isSuccess()){
					Window.alert(constants.failToSave());
					ProcessMessageHandler.showExecutionMessages(result, constants);
				}else{
					initMailAdresses(); 
				}
			}
		});


	}
//*******************************************************************************************
	private void initServerList(){
		servers.clear();
		servers.addItem("----", "-1");
		UtilityPanelConfigurationExchangeAsync mailConfigService=(UtilityPanelConfigurationExchangeAsync)GWT.create(UtilityPanelConfigurationExchange.class);
		((ServiceDefTarget)mailConfigService).setServiceEntryPoint(GWT.getHostPageBaseURL()+"loadCurrentConfigurations_Co_Ut_SmtpConfiguration.htm");
		mailConfigService.loadCurrentConfigurations(new AsyncCallback<UILookupInfo>() {
			@Override
			public void onSuccess(UILookupInfo result) {
				if(result!=null){
					String [][]data= result.getData();
					if(data!=null){
						for(int i=0;i<data.length;i++ ){
							servers.addItem(data[i][1],data[i][0]);
						}
					}
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(constants.failToAccessServer());
				GWT.log(caught.toString());
			}
		});
	}
//*******************************************************************************************
	private void initMailAdresses(){
		usersMailBox.clear();
		usersMailBox.addItem("----","-1");
		if(servers.getSelectedIndex()>0){
			if(!dirty||Window.confirm(constants.discardConfigurationConfirm())){
				MailConfigurationServiceAsync service=(MailConfigurationServiceAsync)GWT.create(MailConfigurationService.class);
				((ServiceDefTarget)service).setServiceEntryPoint("loadMailAccounts_Co_Ut_SmtpAddresses.htm");
				service.loadMailAccounts(Long.parseLong(servers.getValue(servers.getSelectedIndex())), new AsyncCallback<UILookupInfo>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(constants.failToAccessServer());
						GWT.log(caught.toString());
					}

					@Override
					public void onSuccess(UILookupInfo result) {
						if(result!=null){
							String [][]data= result.getData();
							if(data!=null){
								for(int i=0;i<data.length;i++ ){
									usersMailBox.addItem(data[i][1],data[i][0]);
								}
							}
						}
					}
				});

			}
			
		}
	}
//*******************************************************************************************
	private void clean(){
		servers.clear();
		initMailAdresses();
		username.setValue("");
		password.setValue("");
		description.setValue("");
		initServerList();
		dirty=false;
	}
//*******************************************************************************************
	public boolean discardPanel(){
		return(!dirty||Window.confirm(constants.discardConfigurationConfirm()));
	}
}
