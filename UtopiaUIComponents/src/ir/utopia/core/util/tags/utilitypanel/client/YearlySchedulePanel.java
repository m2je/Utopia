package ir.utopia.core.util.tags.utilitypanel.client;

import ir.utopia.core.util.tags.datainputform.client.EditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.GlobalEditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.model.DateInfo;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.timefield.TimeTextBox;
import ir.utopia.core.util.tags.utilitypanel.client.model.ScheduleInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class YearlySchedulePanel extends VerticalPanel implements InnerPanel,
																	EditorDataChangeListener,ValueChangeHandler<String>{
	UtilityConstants constant=GWT.create(UtilityConstants.class);
	TimeTextBox hour=new TimeTextBox();
	Widget dateWidget;
	boolean dirty=false;
	InputItem item;
	public YearlySchedulePanel(){
		 item=new InputItem();
		item.setColumnName("dayOftheYear");
		item.setHeader(constant.startDate());
		item.setDisplayType(InputItem.DISPLAY_TYPE_DATE);
		DateInfo info=new DateInfo();
		if(LocaleInfo.getCurrentLocale().getLocaleName().equals("fa")){
			info.setDateType(DateInfo.DATE_TYPE_SOLAR);
		}else{
			info.setDateType(DateInfo.DATE_TYPE_GREGORIAN);
		}
		item.setDateInfo(info);
		dateWidget=EditorFactory.createEditor(item, LocaleInfo.getCurrentLocale().isRTL()?"RTL":"LTR");
		EditorFactory.addListener(dateWidget, new GlobalEditorDataChangeListener(this, null));
		HorizontalPanel h1=new HorizontalPanel();
		h1.setSpacing(5);
		Label l1=new Label();
		l1.setWidth("80px");
		l1.setStylePrimaryName("clsLabel");
		l1.setText(constant.startDate()+":");
		h1.add(l1);
		dateWidget.setWidth("120px");
		h1.add(dateWidget);
		
		HorizontalPanel h2=new HorizontalPanel();
		h2.setSpacing(5);
		Label l2=new Label();
		l2.setWidth("80px");
		l2.setStylePrimaryName("clsLabel");
		l2.setText(constant.startHour()+":");
		h2.add(l2);
		hour.setStylePrimaryName("clsText");
		hour.setWidth("120px");
		h2.add(hour);
		hour.addValueChangeHandler(this);
		add(h1);
		add(h2);
	}
//************************************************************************************************	
	@Override
	public boolean validate() {
		if(EditorFactory.validateDate(item, null, constant, EditorFactory.getWidgetValue(dateWidget),item.getDateInfo().getDateType())){
			if(hour.validate()){
				return true;
			}else{
				Window.alert(constant.invalidStartTime());
			}
		}else{
			Window.alert(constant.invalidStartDate());
		}
		return false;
	}
//************************************************************************************************	
	@Override
	public void reset() {
		EditorFactory.setWidgetValue(dateWidget, null);
		hour.setText("");
		dirty=false;
	}
//************************************************************************************************	
	@Override
	public boolean isModified() {
		return dirty;
	}
//************************************************************************************************
	public void setValue(Object startDateValue,String hourValue){
		reset();
		EditorFactory.setWidgetValue(dateWidget, startDateValue);
		hour.setText(hourValue);
		dirty=false;
	}
//************************************************************************************************	
	@Override
	public void dataChanges(Widget widget, Object newValue,
			int[] depenedetTypes, String[][] dependentfields) {
			dirty=true;
	}
//************************************************************************************************
	@Override
	public void dataChanges(String widgetName, Object newValue,
			int[] depenedetTypes, String[][] dependentfields) {
		dirty=true;
		
	}
//************************************************************************************************	
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		dirty=true;
	}
//************************************************************************************************
	@Override
	public void reload() {
		reset();
	}
//************************************************************************************************
	public String getStartTime(){
		return hour.getValue();
	}
//************************************************************************************************
	public String getStartDate(){
		Object result=EditorFactory.getWidgetValue(dateWidget);
		return (String)result;
	}
//************************************************************************************************	
	@Override
	public void setValue(Object value) {
		if(value instanceof ScheduleInfo){
			 ScheduleInfo info= (ScheduleInfo)value;
			 EditorFactory.setWidgetValue(dateWidget, info.getDayOfYear()) ;
			 hour.setValue(info.getStartHour());
			}
		dirty=false;
	}
	

}
