package ir.utopia.core.util.tags.utilitypanel.client;

import java.util.ArrayList;
import java.util.List;
import ir.utopia.core.util.tags.datainputform.client.EditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.GlobalEditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.customproperty.CustomPropertyPanel;
import ir.utopia.core.util.tags.datainputform.client.model.ProcessMessageHandler;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.richtexttoolbar.RichTextToolbar;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.utilitypanel.client.model.MailTemplate;
import ir.utopia.core.util.tags.utilitypanel.client.model.MailTemplateService;
import ir.utopia.core.util.tags.utilitypanel.client.model.MailTemplateServiceAsync;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MailTemplatePanel extends AbstractUtilityPanel implements UtilityPanelTab{
	
	private boolean dirty;
	ListBox currentConfigurations=new ListBox();
	TextArea description=new TextArea();
	RichTextArea area = new RichTextArea();
	TextBox name=new TextBox();
	TextArea subject=new TextArea();
	 RichTextToolbar toolbar = new RichTextToolbar(area);
	 CustomPropertyPanel attachmentsPanel=new CustomPropertyPanel(new String[]{constants.fileName(),constants.filePath(),constants.connectionResourceName()});
	public MailTemplatePanel(){
		HorizontalPanel h1=new HorizontalPanel();
		Label l1=new Label();
		l1.setText(constants.mailTemplateName()+":");
		h1.setStylePrimaryName("clsLabel");
		h1.add(l1);
		h1.add(currentConfigurations);
		currentConfigurations.setWidth("400px");
		h1.setSpacing(5);
		Button deleteConfig=new Button();
		deleteConfig.setText(constants.delete());
		deleteConfig.setStylePrimaryName("clsDeleteButton");
		h1.add(deleteConfig);
		deleteConfig.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(currentConfigurations.getSelectedIndex()>0&&Window.confirm(constants.configurationDeleteConfirm())){
					deleteMailTemplate(Long.parseLong( currentConfigurations.getValue(currentConfigurations.getSelectedIndex())));
				}
				
			}

			
		});
		
		currentConfigurations.setStylePrimaryName("clsSelect");
		
		currentConfigurations.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				int index= currentConfigurations.getSelectedIndex();
				if(index==0){
					if(!dirty||Window.confirm(constants.discardConfigurationConfirm())){
						dirty=false;
						name.setText("");
						description.setText("");
						area.setText("");
					}
				}else{
					if(!dirty||Window.confirm(constants.discardConfigurationConfirm())){
						MailTemplateServiceAsync service=(MailTemplateServiceAsync)GWT.create(MailTemplateService.class);
						((ServiceDefTarget)service).setServiceEntryPoint("load_Co_Ut_MailTemplate.htm");
						service.loadMailTemplate(new Long(Long.parseLong(currentConfigurations.getValue(index))), new AsyncCallback<MailTemplate>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert(constants.failToAccessServer());
								
							}

							@Override
							public void onSuccess(MailTemplate result) {
								if(result==null){
									Window.alert(constants.failToAccessServer());
								}else{
									dirty=false;
									name.setText(result.getName());
									description.setText(result.getDescription());
									area.setHTML(result.getTemplate());
									subject.setText(result.getSubject());
									String []attachmentNames=result.getAttachmentNames();
									String []attachmentPaths=result.getAttachmetPaths();
									String []connectionResources=result.getResourceNames();
									if(attachmentNames!=null){
										List<List<String>>values=new ArrayList<List<String>>();
										for(int i=0;i<attachmentNames.length;i++){
											ArrayList<String>row=new ArrayList<String>();
											row.add(attachmentNames[i]);
											row.add(attachmentPaths[i]);
											row.add(connectionResources[i]);
											values.add(row);
										}
											attachmentsPanel.setValue(values);
									}else{
										attachmentsPanel.clearComponent();
									}
								}
								
							}
						});
					}
				}
			}
		});
		
		HorizontalPanel h2=new HorizontalPanel();
		Label l2=new Label();
		l2.setStylePrimaryName("clsLabel");
		l2.setText(constants.mailTemplateName()+":");
		h2.setSpacing(5);
		h2.add(l2);
		h2.add(name);
		name.setWidth("400px");
		name.setStylePrimaryName("clsText");
		
		
		HorizontalPanel h3=new HorizontalPanel();
		Label l3=new Label();
		l3.setStylePrimaryName("clsLabel");
		l3.setText(constants.description()+":");
		h3.setSpacing(5);
		h3.add(l3);
		h3.add(description);
		description.setWidth("400px");
		description.setStylePrimaryName("clsTextArea");
		


		Label l4=new Label();
		l4.setStylePrimaryName("clsLabel");
		l4.setText(constants.mailTemplate()+":");
		
		setSpacing(5);
		add(h1);
		add(h2);
		add(h3);
		Label sbLabel=new Label(constants.mailSubject()+":");
		sbLabel.setStylePrimaryName("clsLabel");
		HorizontalPanel h=new HorizontalPanel();
		h.setSpacing(5);
		h.add(sbLabel);
		h.add(subject);
		subject.setWidth("400px");
		subject.setStylePrimaryName("clsTextArea");
		add(h);
		
		add(attachmentsPanel);
		add(l4);
		toolbar.setWidth("700px");
		add(toolbar);
		area.setStylePrimaryName("clsTextArea");
		add(area);
		add(getActionButtons());
		initCurrentTemplatesLookup();
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
		EditorFactory.addListener(area, changeListener);
		EditorFactory.addListener(description, changeListener);
		EditorFactory.addListener(subject, changeListener);
	}
//**************************************************************************************
	@Override
	public boolean discardPanel() {
		return(!isDirty()||Window.confirm(constants.discardConfigurationConfirm()));
	}
//**************************************************************************************
	private boolean isDirty(){
		return dirty||attachmentsPanel.isDirty();
	}
//**************************************************************************************
	private void initCurrentTemplatesLookup(){
		MailTemplateServiceAsync service=(MailTemplateServiceAsync)GWT.create(MailTemplateService.class);
		((ServiceDefTarget)service).setServiceEntryPoint("loadlookup_Co_Ut_MailTemplate.htm");
		currentConfigurations.clear();
		currentConfigurations.addItem("----", "-1");
		service.getCurrentTemplates(new AsyncCallback<UILookupInfo>(){

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				Window.alert(constants.failToAccessServer());
			}

			@Override
			public void onSuccess(UILookupInfo result) {
				if(result==null){
					Window.alert(constants.failToAccessServer());
				}else if(result.getData()!=null){
					for(String []row:result.getData()){
						currentConfigurations.addItem(row[1],row[0]);
					}	
				}
				return;
				
			}});
	}
//**************************************************************************************
	@Override
	public void reload(boolean ignoreDirty) {
		if(ignoreDirty||!dirty||Window.confirm(constants.discardConfigurationConfirm())){
			clean();
		}
	}
//**************************************************************************************	
	private void deleteMailTemplate(long templateId) {
		MailTemplateServiceAsync service=(MailTemplateServiceAsync)GWT.create(MailTemplateService.class);
		((ServiceDefTarget)service).setServiceEntryPoint("delete_Co_Ut_MailTemplate.htm");
		service.deleteMailTemplate(templateId, new AsyncCallback<ProcessExecutionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
			}

			@Override
			public void onSuccess(ProcessExecutionResult result) {
				if(result==null||!result.isSuccess()){
					Window.alert(constants.failToDelete());
				}
				if(result!=null){
						ProcessMessageHandler.showExecutionMessages(result, constants);
						clean();
				}
				return;
				
			}
		});
		
	}
//**************************************************************************************
	private void clean(){
		dirty=false;
		initCurrentTemplatesLookup();
		name.setText("");
		area.setText("");
		description.setText("");
		subject.setText("");
		attachmentsPanel.clearComponent();
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
//**************************************************************************************************	
	private void submit() {
		if(validate()){
			MailTemplateServiceAsync service=(MailTemplateServiceAsync)GWT.create(MailTemplateService.class);
			Long mailTemplateId=-1l;
			AsyncCallback<ProcessExecutionResult> callback= new AsyncCallback<ProcessExecutionResult>() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log(caught.toString());
						Window.alert(constants.failToAccessServer());
					}

					@Override
					public void onSuccess(ProcessExecutionResult result) {
						if(result==null||!result.isSuccess()){
							Window.alert(constants.failToSave());
							if(result!=null){
								ProcessMessageHandler.showExecutionMessages(result, constants);
							}
						}else{
							ProcessMessageHandler.showExecutionMessages(result, constants);
							clean();
						}
					}
				};
			if(currentConfigurations.getSelectedIndex()>0){
				mailTemplateId=Long.parseLong( currentConfigurations.getValue(currentConfigurations.getSelectedIndex()));
				((ServiceDefTarget)service).setServiceEntryPoint("update_Co_Ut_MailTemplate.htm");
				service.updateMailTemplate(mailTemplateId, name.getValue(),subject.getText(),  description.getValue(), area.getHTML(),attachmentsPanel.getValueList(),callback);
			}else{
				((ServiceDefTarget)service).setServiceEntryPoint("save_Co_Ut_MailTemplate.htm");
				service.saveMailTemplate(name.getValue(),subject.getText(), description.getValue(), area.getHTML(),attachmentsPanel.getValueList(),callback);
			}
		}
		
		
		
	}
//*****************************************************************************************************	
	private boolean validate() {
		if(name.getText()==null||name.getText().trim().length()==0){
			Window.alert(constants.invalidName());
			return false;
		}
		if(area.getText()==null||area.getText().trim().length()==0){
			Window.alert(constants.notNullTemplate());
			return false;
		}
		return true;
	}
}
