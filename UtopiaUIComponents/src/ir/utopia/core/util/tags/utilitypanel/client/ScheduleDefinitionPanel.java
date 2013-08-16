package ir.utopia.core.util.tags.utilitypanel.client;

import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.customproperty.CustomPropertyPanel;
import ir.utopia.core.util.tags.datainputform.client.model.DateInfo;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.ProcessMessageHandler;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.radiobutton.RadioButtonGroup;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;
import ir.utopia.core.util.tags.utilitypanel.client.model.RecepientInfo;
import ir.utopia.core.util.tags.utilitypanel.client.model.ScheduleInfo;
import ir.utopia.core.util.tags.utilitypanel.client.model.SchedulerConfigurationModel;
import ir.utopia.core.util.tags.utilitypanel.client.model.SchedulerDefinitionService;
import ir.utopia.core.util.tags.utilitypanel.client.model.SchedulerDefinitionServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ScheduleDefinitionPanel  extends AbstractUtilityPanel implements UtilityPanelTab{
	
	private boolean dirty;
	InputItem startDateItem=new InputItem();
	InputItem endDateItem=new InputItem();
	ListBox type=new ListBox();
	ListBox configurationNameBox=new ListBox();
	TextBox configName=new TextBox();
	TextArea configDesc=new TextArea();
	private ListBox systemBox=new ListBox(),subSystemBox=new ListBox(),usecaseBox=new ListBox(),usecaseActionBox=new ListBox();
	CustomPropertyPanel customProps=new CustomPropertyPanel();
	Widget scheduleConfigPanel;
	Widget currentSchedulePanel;
	String currentScheduleType;
	HourlySchedulePanel hourlySchedulePanel;
	DailySchedulePanel dailySchedulePanel;
	MonthlySchedulePanel monthlySchedulePanel;
	YearlySchedulePanel yearlySchedulePanel;
	MailRecipientPanel mailRecipientPanel;
	final VerticalPanel schedulePanel=new VerticalPanel();
	Widget startDate,endDate;
	UILookupInfo scheduleTypeInfo=new UILookupInfo(new String[]{"key","value"},new String [][]{
			{"1",constants.hourly()},{"2",constants.daily()},{"3",constants.monthly()},{"4",constants.yearly()}
			},1,false);
	RadioButtonGroup scheduleType;
	SchedulerDefinitionServiceAsync service=(SchedulerDefinitionServiceAsync)GWT.create(SchedulerDefinitionService.class);
	public ScheduleDefinitionPanel(){
		HorizontalPanel h1=new HorizontalPanel();
		type.setStylePrimaryName("clsSelect");
		type.addItem(constants.email(), "1");
		type.addItem(constants.process(), "2");
		type.setWidth("200px");
		Label l1=new Label();
		l1.setText(constants.type()+":");
		l1.setStylePrimaryName("clsLabel");
		h1.setSpacing(5);
		h1.add(l1);
		h1.add(type);
		
		add(h1);
		
		type.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				initConfigurationBox();
				mailRecipientPanel.setVisible(type.getSelectedIndex()==0);
				
			}
		});
		VerticalPanel h2=new VerticalPanel();
		HorizontalPanel h21=new HorizontalPanel();
		initConfigurationBox();
		configurationNameBox.setStylePrimaryName("clsSelect");
		Label l2=new Label();
		l2.setText(constants.configurationName()+":");
		l2.setStylePrimaryName("clsLabel");
		h21.setSpacing(5);
		h21.add(l2);
		configurationNameBox.setWidth("400px");
		configurationNameBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				clean(false);
				if(configurationNameBox.getSelectedIndex()!=0){
					lodConfiguration(type.getSelectedIndex()==0?SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_MAIL:SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_PROCESS, configurationNameBox.getValue(configurationNameBox.getSelectedIndex()));
				}
				
			}
		});
		h21.add(configurationNameBox);
		
		setBorderWidth(1);
		Button deleteConfiguration=new Button();
		deleteConfiguration.setStylePrimaryName("clsDeleteButton");
		deleteConfiguration.setText(constants.delete());
		deleteConfiguration.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(configurationNameBox.getSelectedIndex()>0&&Window.confirm(constants.configurationDeleteConfirm())){
					int typeValue=type.getSelectedIndex()==0?SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_MAIL:SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_PROCESS;
					 String value= configurationNameBox.getValue(configurationNameBox.getSelectedIndex());
					deleteSchedule(typeValue,value );
				}
				
			}

			
		});
		h21.add(deleteConfiguration);
		h2.add(h21);
		HorizontalPanel h22=new HorizontalPanel();
		h22.setSpacing(5);
		Label l3=new Label();
		l3.setStylePrimaryName("clsLabel");
		l3.setText(constants.configurationName()+":");
		h22.add(l3);
		configName.setStylePrimaryName("clsText");
		configName.setWidth("400px");
		h22.add(configName);
		h2.add(h22);
		HorizontalPanel h23=new HorizontalPanel();
		h23.setSpacing(5);
		Label l4=new Label();
		l4.setStylePrimaryName("clsLabel");
		l4.setText(constants.description()+":");
		h23.add(l4);
		configDesc.setStylePrimaryName("clsTextArea");
		configDesc.setWidth("800px");
		h23.add(configDesc);
		h2.add(h23);
		add(h2);
		
		VerticalPanel h3=new VerticalPanel();
		HorizontalPanel h31=new HorizontalPanel();
		h31.setSpacing(5);
		Label l5=new Label();
		l5.setWidth("60px");
		l5.setStylePrimaryName("clsLabel");
		l5.setText(constants.system()+":");
		h31.add(l5);
		systemBox.setStylePrimaryName("clsSelect");
		systemBox.setWidth("400px");
		systemBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				dirty=true;
				initSubSystemBox();
				
			}
		});
		h31.add(systemBox);
		
		Label l6=new Label();
		l6.setStylePrimaryName("clsLabel");
		l6.setWidth("70px");
		l6.setText(constants.subsystem()+":");
		h31.add(l6);
		subSystemBox.setStylePrimaryName("clsSelect");
		subSystemBox.setWidth("400px");
		subSystemBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				initUsecaseBox();
				dirty=true;
			}
		});
		h31.add(subSystemBox);
		h3.add(h31);
		
		HorizontalPanel h32=new HorizontalPanel();
		h32.setSpacing(5);
		Label l7=new Label();
		l7.setWidth("60px");
		l7.setStylePrimaryName("clsLabel");
		l7.setText(constants.usecase()+":");
		h32.add(l7);
		usecaseBox.setStylePrimaryName("clsSelect");
		usecaseBox.setWidth("400px");
		usecaseBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				dirty=true;
				initUsecaseActionBox();
			}
		});
		h32.add(usecaseBox);
		
		Label l8=new Label();
		l8.setStylePrimaryName("clsLabel");
		l8.setWidth("70px");
		l8.setText(constants.usecaseMethod()+":");
		h32.add(l8);
		usecaseActionBox.setStylePrimaryName("clsSelect");
		usecaseActionBox.setWidth("400px");
		usecaseActionBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				dirty=true;
				
			}
		});
		h32.add(usecaseActionBox);
		
		
		h3.add(h32);
		HorizontalPanel h33=new HorizontalPanel();
		h33.setSpacing(5);
		Label l9=new Label();
		l9.setStylePrimaryName("clsLabel");
		l9.setText(constants.processParameters()+":");
		h33.add(l9);
		h33.add(customProps);
		h3.add(h33);
		
		add(h3);
		
		scheduleConfigPanel=getSchedulerConfigPanel();
		add(scheduleConfigPanel);
		add(getMailRecipientPanel());
		add(getActionButtons());
		initSystemBox();
		
	}
//********************************************************************************************************
	private void lodConfiguration(int configType,String configName){
		SchedulerDefinitionServiceAsync service=(SchedulerDefinitionServiceAsync)GWT.create(SchedulerDefinitionService.class);
		 AsyncCallback<SchedulerConfigurationModel> callback= new AsyncCallback<SchedulerConfigurationModel>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(caught.toString());
				}

				@Override
				public void onSuccess(SchedulerConfigurationModel result) {
					if(result==null){
						Window.alert(constants.failToAccessServer());
						return;
					}
					setValue(result);
				}
			};
			 ((ServiceDefTarget)service).setServiceEntryPoint("load_Co_Ut_Scheduler.htm");
			 service.loadConfiguration(configType, configName, callback);
	}
//********************************************************************************************************
	public void setValue(SchedulerConfigurationModel value){
		ScheduleDefinitionPanel.this.configName.setText(value.getConfigurationName());
		configDesc.setText(value.getDescription());
		EditorFactory.setWidgetValue(systemBox, value.getSystems());
		EditorFactory.setWidgetValue(subSystemBox, value.getSubsystems());
		EditorFactory.setWidgetValue(usecaseBox, value.getUsecases());
		EditorFactory.setWidgetValue(usecaseActionBox, value.getUsecaseActions());
		customProps.setValue(value.getCustomPropertyNames(), value.getCustomPropertyValues());
		ScheduleInfo scheduleInfo=value.getScheduleInfo();
		EditorFactory.setWidgetValue(startDate, value.getStartDate());
		EditorFactory.setWidgetValue(endDate, value.getEnddate());
		
		switch (scheduleInfo.getScheduleType()) {
		case ScheduleInfo.SCHEDULE_TYPE_HOURLY:
			scheduleType.setValue("1");
			((HourlySchedulePanel)getHourlySchedulePanel()).setValue(scheduleInfo);
			break;
		case ScheduleInfo.SCHEDULE_TYPE_DAILY:
			scheduleType.setValue("2");
			((DailySchedulePanel)getDailySchedulePanel()).setValue(scheduleInfo);
			break;
		case ScheduleInfo.SCHEDULE_TYPE_MONTHLY:
			scheduleType.setValue("3");
			((MonthlySchedulePanel)getMonthlySchedulePanel()).setValue(scheduleInfo);
			break;	
		case ScheduleInfo.SCHEDULE_TYPE_YEARLY:
			scheduleType.setValue("4");
			((YearlySchedulePanel)getYearlySchedulePanel()).setValue(scheduleInfo);
			break;
		} 
		handleNewScheduleType();
		RecepientInfo recepeitsInfo=value.getRecepients();
		if(recepeitsInfo!=null){
			mailRecipientPanel.setValue(recepeitsInfo);
		}
	}
//********************************************************************************************************
	private boolean isDirty(){
		return dirty||((InnerPanel)currentSchedulePanel).isModified()||
		mailRecipientPanel.isModified()||customProps.isDirty();
	}
//********************************************************************************************************	
	private void deleteSchedule(int typeValue,String name) {
		 SchedulerDefinitionServiceAsync service=(SchedulerDefinitionServiceAsync)GWT.create(SchedulerDefinitionService.class);
		 AsyncCallback<ProcessExecutionResult> callback= new AsyncCallback<ProcessExecutionResult>() {

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
							if(result.isSuccess())
								reload(true);
					}
					return;
					
				}
			};
			 ((ServiceDefTarget)service).setServiceEntryPoint("delete_Co_Ut_Scheduler.htm");
			 
			 service.deleteConfiguration(typeValue,name, callback);
	}
//********************************************************************************************************
	private void initConfigurationBox(){
		AsyncCallback<UILookupInfo>callback= initBox(configurationNameBox,"loadConfigLookup_Co_Ut_Scheduler.htm",service);
		service.loadConfigLookup(type.getSelectedIndex()==0?SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_MAIL:SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_PROCESS,callback);
	}
//********************************************************************************************************
	private void initSystemBox(){
		AsyncCallback<UILookupInfo>callback= initBox(systemBox, "loadSystemLookup_Co_Ut_Scheduler.htm",service);
		service.loadSystemLookup(callback);
	}
//********************************************************************************************************
	private void initSubSystemBox(){
		AsyncCallback<UILookupInfo>callback= initBox(subSystemBox, "loadSubSystemLookup_Co_Ut_Scheduler.htm",service);
		service.loadSubSystemLookup(Long.parseLong(systemBox.getValue(systemBox.getSelectedIndex())), callback);
	}
//********************************************************************************************************
	private void initUsecaseBox(){
		AsyncCallback<UILookupInfo>callback= initBox(usecaseBox, "loadUsecaseLookup_Co_Ut_Scheduler.htm",service);
		service.loadUsecaseLookup(Long.parseLong(subSystemBox.getValue( subSystemBox.getSelectedIndex())), callback);
	}
//********************************************************************************************************
	private void initUsecaseActionBox(){
		AsyncCallback<UILookupInfo>callback= initBox(usecaseActionBox, "loadUsecaseActionLookup_Co_Ut_Scheduler.htm",service);
		service.loadUsecaseActionLookup(Long.parseLong(usecaseBox.getValue( usecaseBox.getSelectedIndex())), callback);
	}
//********************************************************************************************************
	private Widget getSchedulerConfigPanel(){
		final HorizontalPanel root=new HorizontalPanel();
		
		scheduleType=new RadioButtonGroup(constants.scheduleType(), constants.scheduleType(), scheduleTypeInfo);
		
		startDateItem.setHeader(constants.startDate());
		startDateItem.setColumnName("startDate");
		endDateItem.setHeader(constants.endDate());
		endDateItem.setColumnName("endDate");
		DateInfo startDateInfo=new DateInfo();
		DateInfo endDateInfo=new DateInfo();
		if(LocaleInfo.getCurrentLocale().getLocaleName().equals("fa")){
			startDateInfo.setDateType(DateInfo.DATE_TYPE_SOLAR);
			endDateInfo.setDateType(DateInfo.DATE_TYPE_SOLAR);
		}else{
			startDateInfo.setDateType(DateInfo.DATE_TYPE_GREGORIAN);
			endDateInfo.setDateType(DateInfo.DATE_TYPE_GREGORIAN);
		}
		startDateItem.setDateInfo(startDateInfo);
		endDateItem.setDateInfo(endDateInfo);
		startDateInfo.setEndDateItem(endDateItem);
		startDateItem.setDisplayType(InputItem.DISPLAY_TYPE_DATE);
		endDateItem.setDisplayType(InputItem.DISPLAY_TYPE_DATE);
		
		
		String direction=LocaleInfo.getCurrentLocale().isRTL()?"RTL":"LTR";
		startDate=EditorFactory.createEditor(startDateItem, direction);
		endDate=EditorFactory.createEditor(endDateItem, direction);
		startDate.setWidth("120px");
		endDate.setWidth("120px");
		HorizontalPanel h1=new HorizontalPanel();
		h1.setSpacing(5);
		Label l1=new Label();
		l1.setText(constants.startDate()+":");
		l1.setStylePrimaryName("clsLabel");
		h1.add(l1);
		h1.add(startDate);
		
		Label l2=new Label();
		l2.setText(constants.endDate()+":");
		l2.setStylePrimaryName("clsLabel");
		h1.add(l2);
		h1.add(endDate);
		
		schedulePanel.add(h1);
		
		scheduleType.setValue("2");
		scheduleType.setWidth("200px");
		root.add(scheduleType);
		root.setWidth("200px");
		root.setSpacing(50);
		scheduleType.setValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				handleNewScheduleType();
			}
		});
		currentScheduleType="2";
		currentSchedulePanel=getDailySchedulePanel();
		schedulePanel.add(currentSchedulePanel);
		root.add(schedulePanel);
		return root;
	}
//********************************************************************************************************
	private void handleNewScheduleType(){
		schedulePanel.remove(currentSchedulePanel);
		if("1".equals( scheduleType.getValue())&&currentScheduleType!="1"){
			currentSchedulePanel=getHourlySchedulePanel();
			currentScheduleType="1";
		}else if("2".equals( scheduleType.getValue())&&currentScheduleType!="2"){
			currentSchedulePanel=getDailySchedulePanel();
			currentScheduleType="2";
		}else if("3".equals( scheduleType.getValue())&&currentScheduleType!="3"){
			currentSchedulePanel=getMonthlySchedulePanel();
			currentScheduleType="3";
		}else if("4".equals( scheduleType.getValue())&&currentScheduleType!="4"){
			currentSchedulePanel=getYearlySchedulePanel();
			currentScheduleType="4";
		} 
		schedulePanel.add(currentSchedulePanel);
	}
//********************************************************************************************************	
	@Override
	public boolean discardPanel() {
		return(!isDirty()||Window.confirm(constants.discardConfigurationConfirm()));
	}
//********************************************************************************************************
	@Override
	public void reload(boolean ignoreDirty) {
		if(ignoreDirty||!isDirty()||Window.confirm(constants.discardConfigurationConfirm())){
			clean(true);
			initConfigurationBox();
			initSystemBox();
			scheduleType.setValue("2");
		}
	}
//********************************************************************************************************
	private Widget getHourlySchedulePanel(){
		if(hourlySchedulePanel==null){
			hourlySchedulePanel =new HourlySchedulePanel();
		}
		return hourlySchedulePanel;
	}
//********************************************************************************************************
	private Widget getDailySchedulePanel(){
		if(dailySchedulePanel==null){
			dailySchedulePanel=new DailySchedulePanel();	
		}
		return dailySchedulePanel;
	}
//********************************************************************************************************
	private Widget getMonthlySchedulePanel(){
		if(monthlySchedulePanel==null){
			monthlySchedulePanel= new MonthlySchedulePanel();
		}
		return monthlySchedulePanel;
	}
//********************************************************************************************************
	private Widget getYearlySchedulePanel(){
		if(yearlySchedulePanel==null){
			yearlySchedulePanel=new YearlySchedulePanel();
		}
		return yearlySchedulePanel;
	}
//********************************************************************************************************
	private MailRecipientPanel getMailRecipientPanel(){
		if(mailRecipientPanel==null){
			mailRecipientPanel=new MailRecipientPanel();
		}
		return mailRecipientPanel;
	}
//********************************************************************************************************
	private void clean(boolean clearConfigurationBox ) {
		if(clearConfigurationBox)
			configurationNameBox.setSelectedIndex(0);
		configName.setText("");
		configDesc.setText("");
		systemBox.setSelectedIndex(0);
		subSystemBox.clear();
		usecaseBox.clear();
		usecaseActionBox.clear();
		customProps.clearComponent();
		((InnerPanel)currentSchedulePanel).reset();
		EditorFactory.setWidgetValue(startDate, null);
		EditorFactory.setWidgetValue(endDate, null);
		mailRecipientPanel.reset();
		scheduleType.setValue("2");
		dirty=false;
	}
//********************************************************************************************************
	private void submit() {
		if(validate()){
			String current=configurationNameBox.getValue(configurationNameBox.getSelectedIndex());
			 SchedulerDefinitionServiceAsync service=(SchedulerDefinitionServiceAsync)GWT.create(SchedulerDefinitionService.class);
			 AsyncCallback<ProcessExecutionResult> callBack= new AsyncCallback<ProcessExecutionResult>() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log(caught.toString());
						
					}

					@Override
					public void onSuccess(ProcessExecutionResult result) {
						if(result==null||!result.isSuccess()){
							Window.alert(constants.failToSave());
						}
						if(result!=null){
								ProcessMessageHandler.showExecutionMessages(result, constants);
								if(result.isSuccess()){
									clean(true);
									reload(true);
								}
									
						}
						
						return;
						
					}
				};
				SchedulerConfigurationModel value=	new SchedulerConfigurationModel();
				ScheduleInfo scheduleInfo=new ScheduleInfo();
				value.setType(type.getSelectedIndex()==0?SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_MAIL:SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_PROCESS);
				if(currentSchedulePanel instanceof AbstractWeekDaySupportPanel){
					scheduleInfo.setDaysOfWeek(((AbstractWeekDaySupportPanel)currentSchedulePanel).getValue());
				}
				if(currentSchedulePanel instanceof HourlySchedulePanel){
					scheduleInfo.setStartHour(((HourlySchedulePanel)currentSchedulePanel).getStartTime());
					scheduleInfo.setFinishHour(((HourlySchedulePanel)currentSchedulePanel).getEndHour());
					scheduleInfo.setInterval(((HourlySchedulePanel)currentSchedulePanel).getInterval());
					scheduleInfo.setScheduleType(ScheduleInfo.SCHEDULE_TYPE_HOURLY);
					
				}
				if(currentSchedulePanel instanceof DailySchedulePanel){
					scheduleInfo.setStartHour(((DailySchedulePanel)currentSchedulePanel).getStartTime());
					scheduleInfo.setScheduleType(ScheduleInfo.SCHEDULE_TYPE_DAILY);
				}
				if(currentSchedulePanel instanceof MonthlySchedulePanel){
					scheduleInfo.setDaysOfMounth(((MonthlySchedulePanel)currentSchedulePanel).getDayOfMonth());
					scheduleInfo.setStartHour(((MonthlySchedulePanel)currentSchedulePanel).getStartTime());
					scheduleInfo.setScheduleType(ScheduleInfo.SCHEDULE_TYPE_MONTHLY);
				}
				if(currentSchedulePanel instanceof YearlySchedulePanel){
					scheduleInfo.setStartHour(((YearlySchedulePanel)currentSchedulePanel).getStartTime());
					scheduleInfo.setDayOfYear(((YearlySchedulePanel)currentSchedulePanel).getStartDate());
					scheduleInfo.setScheduleType(ScheduleInfo.SCHEDULE_TYPE_YEARLY);
				}
				if(value.getType()==SchedulerConfigurationModel.SCHEDULE_TASK_TYPE_MAIL){
					RecepientInfo recepientInfo=mailRecipientPanel.getValue();
					value.setRecepients(recepientInfo);
				}
				Object startDateValue=EditorFactory.getWidgetValue(startDate);
				Object endDateValue=EditorFactory.getWidgetValue(endDate);
				value.setStartDate(startDateValue==null?null:String.valueOf(startDateValue));
				value.setEnddate(endDateValue==null?null:String.valueOf(endDateValue));
				value.setScheduleInfo(scheduleInfo);
				value.setConfigurationNewName(configName.getValue());
				value.setConfigurationName(configurationNameBox.getItemText(configurationNameBox.getSelectedIndex()));
				value.setDescription(configDesc.getValue());
				value.setCustomPropertyNames(customProps.getKeyArray());
				value.setCustomPropertyValues(customProps.getValueArray());
				if(usecaseActionBox.getSelectedIndex()>0)
					value.setUsecaseActionId(Long.parseLong(usecaseActionBox.getValue(usecaseActionBox.getSelectedIndex())));
			 if("-1".equals(current)){
				 ((ServiceDefTarget)service).setServiceEntryPoint("save_Co_Ut_Scheduler.htm");
				 service.saveConfiguration(value, callBack);
			 }else{
				 ((ServiceDefTarget)service).setServiceEntryPoint("update_Co_Ut_Scheduler.htm");
				 service.updateConfiguration(value, callBack);
			 }
		}
	}
//********************************************************************************************************
	public boolean validate(){
		//return true;
		///*
		if(configName.getValue()!=null&&configName.getValue().trim().length()>0){
			if(type.getSelectedIndex()==0){
				if(customProps.validate()&&((InnerPanel)currentSchedulePanel).validate()){
					return validateStartEndDate()&&validateMailConfiguration();
				}
				return validateStartEndDate()&&validateMailConfiguration();
			}else{
				if(validateUsecaseAction()&&customProps.validate()&&((InnerPanel)currentSchedulePanel).validate()){
					return validateStartEndDate()&&validateMailConfiguration();
				}
			}
		}else{
			Window.alert(constants.mandatoryConfigurationName());
		}
		
		return false;//*/
	}
//********************************************************************************************************
	private boolean validateStartEndDate(){
		Object startDateValue=EditorFactory.getWidgetValue(startDate);
		Object endDateValue=EditorFactory.getWidgetValue(endDate);
		boolean validated;
		if(startDateValue!=null){
			 validated= EditorFactory.validateDate(startDateItem,endDateValue , constants, startDateValue, startDateItem.getDateInfo().getDateType());
		}else{
			MessageUtility.warn(constants.warning(), constants.startDateIsMandatory());
			validated=false;
		}
		
		//TODO check monthly , yearly start-end date to contain at list one recurrence
//		if(validated&&endDate!=null){
//			int d1[];
//			int d2[];
//			boolean solar=startDateItem.getDateType()==DateInputItem.DATE_TYPE_SOLAR;
//			if(solar){
//				d1=ValidationFunctions.getDateFromSolar((String)startDateValue);
//				d2=ValidationFunctions.getDateFromSolar((String)endDateValue);
//			}else{
//				d1=ValidationFunctions.getDate((Date)startDateValue);
//				d2=ValidationFunctions.getDate((Date)endDateValue);
//			}
//			
//			if(DailySchedulePanel.class.isInstance(currentSchedulePanel)||
//					HourlySchedulePanel.class.isInstance(currentSchedulePanel)	){
//				
//			}
//		}
		return validated;
	} 
//********************************************************************************************************
	private boolean validateMailConfiguration(){
		return type.getSelectedIndex()>0||mailRecipientPanel.validate();
	}
//********************************************************************************************************
	private boolean validateUsecaseAction(){
		if(systemBox.getSelectedIndex()>0){
			if(subSystemBox.getSelectedIndex()>0){
				if(usecaseBox.getSelectedIndex()>0){
					if(usecaseActionBox.getSelectedIndex()>0){
						return true;
					}else{
						Window.alert(constants.selectUsecaseAction());
					}
				}else{
					Window.alert(constants.selectUsecase());
				}
			}else{
				Window.alert(constants.selectSubsystem());
			}
		}else{
			Window.alert(constants.selectSystem());
		}
		return false;
	}
//********************************************************************************************************
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
}
