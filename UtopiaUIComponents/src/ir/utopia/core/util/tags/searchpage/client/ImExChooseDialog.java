package ir.utopia.core.util.tags.searchpage.client;

import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.GWTConstants;
import ir.utopia.core.util.tags.datainputform.client.ImageHyperlink;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageConstants;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.datainputform.client.model.ProcessMessageHandler;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.SearchPageImExServices;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.SearchPageImExServicesAsync;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.SearchPageServerService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

public class ImExChooseDialog extends DialogBox {
	SearchPageConstants constants=(SearchPageConstants)GWT.create(SearchPageConstants.class);
	UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	boolean export;
	ListBox type=new ListBox();
	ListBox configs=new ListBox();
	String usecaseName;
	SearchPage searchWidget;
	ImageHyperlink editLink,deleteLink;
	public ImExChooseDialog(boolean export,String usecaseName,SearchPage searchPage){
		super(true,true);
		this.export=export;
		this.usecaseName=usecaseName;
		this.searchWidget=searchPage;
		creat();
	}
//*****************************************************************************************************************************	
	protected void creat(){
		createDialog();
		initConfigType();
    }
//*****************************************************************************************************************************
	protected void createDialog() {
		VerticalPanel rootPane=new VerticalPanel();
		setWidget(rootPane);
		if(export){
			HorizontalPanel h1=new HorizontalPanel();
			h1.setSpacing(5);
			Label typeLabel=new Label(constants.type()+":");
			typeLabel.setStylePrimaryName("clsLabel");
			h1.add(typeLabel);
			h1.add(type);
			rootPane.add(h1);
		}
		
	
		rootPane.add(createConfigBoxPanel());
		Button cancelButton = new Button(constants.cancelProcess(),
			        new ButtonListenerAdapter() {
			    	public void onClick(Button button, EventObject e) {
			    		 hide();
			        }
			        });
	    Button startButton = new Button(constants.startProcess(),
				        new ButtonListenerAdapter() {
			    	public void onClick(Button button, EventObject e) {
			    		hide();
			    		String value=(String)EditorFactory.getWidgetValue(configs);
			    		Long settingId="-1".equals(value)?null:Long.parseLong(value);
			    		if(settingId==null)	
			    			searchWidget.redirectToExImConfigurationPage(settingId,export);
			    		else if(export){
			    			String typeValue=(String) EditorFactory.getWidgetValue(type);
		        			 //TODO export data
		        		 }else{
		        			 searchWidget.redirectToImportPage(settingId);
		        		 }
			        	 
				          }
				        });
	    HorizontalPanel buttomPanel=new HorizontalPanel();
	    buttomPanel.setSpacing(5);
	   if(LocaleInfo.getCurrentLocale().isRTL()){
		   buttomPanel.add(startButton);
		   buttomPanel.add(cancelButton);
	    }else{
	    	buttomPanel.add(cancelButton);
	    	buttomPanel.add(startButton);
	    }
	  
	    rootPane.add(buttomPanel);
	}
//*****************************************************************************************************************************
	@SuppressWarnings("deprecation")
	protected Widget createConfigBoxPanel(){
		HorizontalPanel h1=new HorizontalPanel();
		h1.setSpacing(5);
		Label configsLabel=new Label(constants.configurationName()+":");
		configsLabel.setStylePrimaryName("clsLabel");
		h1.add(configsLabel);
		h1.add(configs);
		editLink=new ImageHyperlink(images.edit().createImage());
		editLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String value=(String)EditorFactory.getWidgetValue(configs);
				searchWidget.redirectToExImConfigurationPage(Long.parseLong(value),export);
				hide();
			}
		});
		h1.add(editLink);
		editLink.setVisible(false);
		deleteLink=new ImageHyperlink(images.drop().createImage());
		deleteLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MessageBox.confirm(constants.warning(), constants.deleteConfigurationConfirm(),new MessageBox.ConfirmCallback() {
					
					@Override
					public void execute(String btnID) {
						if("yes".equalsIgnoreCase(btnID)){
									SearchPageImExServicesAsync proxy=(SearchPageImExServicesAsync) GWT.create(SearchPageImExServices.class);
									((ServiceDefTarget) proxy).setServiceEntryPoint(GWT.getHostPageBaseURL() + "delete_Co_Ut_ImportExportConfiguration"+GWTConstants.STRUTS_EXTENSION);
									proxy.deleteSetting(Long.parseLong((String)EditorFactory.getWidgetValue(configs)),new AsyncCallback<ExecutionResult>(){

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
												if(result.isSuccess()){
													configs.removeItem(configs.getSelectedIndex());
													deleteLink.setVisible(configs.getSelectedIndex()==configs.getItemCount());
													editLink.setVisible(configs.getSelectedIndex()==configs.getItemCount());
												}
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
		configs.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				String value=(String)EditorFactory.getWidgetValue(configs);
    			deleteLink.setVisible(!"-1".equals(value));
				editLink.setVisible(!"-1".equals(value));
				}
			});
		return h1;
	}
//*****************************************************************************************************************************
	protected void initConfigType(){
		MessageUtility.progress(constants.pleaseWait(), constants.loadingData());
		SearchPageServerService.getServer().loadFileFormats(new AsyncCallback<UILookupInfo>() {
			
			@Override
			public void onSuccess(UILookupInfo result) {
				MessageUtility.stopProgress();
				if(result==null){
					MessageUtility.error(constants.error(), constants.failToAccessServer());
				}else{
					type.clear();
					
					for(String []data:result.getData()){
						type.addItem(data[1], data[0]);
					}
					String defaultValue= result.getSelectedId();
					EditorFactory.setWidgetValue(type, defaultValue);
					loadConfiguration();
				}
				
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				MessageUtility.stopProgress();
				MessageUtility.error(constants.error(), constants.failToAccessServer());
			}
		});
		
	}
//*****************************************************************************************************************************
	protected void loadConfiguration(){
		configs.clear();
		MessageUtility.progress(constants.pleaseWait(), constants.loadingData());
		SearchPageServerService.getServer().getImportConfigurations( usecaseName, new AsyncCallback<UILookupInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.toString());
				MessageUtility.stopProgress();
				MessageUtility.error(constants.error(), constants.failToAccessServer());
				
			}

			@Override
			public void onSuccess(UILookupInfo result) {
				MessageUtility.stopProgress();
				if(result==null){
					MessageUtility.error(constants.error(), constants.failToAccessServer());
				}else{
					for(String []data:result.getData()){
						configs.addItem(data[1], data[0]);
					}
					configs.addItem(constants.newConfig(), "-1");
					editLink.setVisible(configs.getItemCount()>1);
					deleteLink.setVisible(configs.getItemCount()>1);
					String defaultValue= result.getSelectedId();
					EditorFactory.setWidgetValue(configs, defaultValue);
				}
				
			}
		});
	}
}