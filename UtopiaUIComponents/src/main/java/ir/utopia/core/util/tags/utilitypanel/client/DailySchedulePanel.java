package ir.utopia.core.util.tags.utilitypanel.client;

import ir.utopia.core.util.tags.datainputform.client.timefield.TimeTextBox;
import ir.utopia.core.util.tags.utilitypanel.client.model.ScheduleInfo;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DailySchedulePanel extends AbstractWeekDaySupportPanel {
	TimeTextBox startTime=new TimeTextBox();
	public DailySchedulePanel(){
		add(getDaysOfWeek());
		add(getStartHourPanel());
	}
	
	protected Widget getStartHourPanel(){
		HorizontalPanel h1=new HorizontalPanel();
		HorizontalPanel h11=new HorizontalPanel();
		Label l1=new Label();
		h11.setSpacing(5);
		l1.setText(constants.startHour()+":");
		l1.setStylePrimaryName("clsLabel");
		h11.add(l1);
		startTime.setStylePrimaryName("clsText");
		h11.add(startTime);
		startTime.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				dirty=true;
			}});
		h1.add(h11);
		return h1;
	}
	
//********************************************************************************************
	@Override
	public void reset() {
		super.reset();
		startTime.setValue("");
		dirty=false;
	}
//********************************************************************************************
	@Override
	public boolean validate() {
		if(super.validate()){
			if(startTime.getValue()!=null&&startTime.validate()){
				return true;
			}else{
				Window.alert(constants.invalidStartTime());
			}
		}
		return false;
	}
//********************************************************************************************	
	public void setValue(String startTimeValue,int []daysOfTheweek){
		super.setValue(daysOfTheweek);
		startTime.setValue(startTimeValue);
		dirty=false;
	}
//********************************************************************************************		
	@Override
	public void reload() {
		reset();
	}
//********************************************************************************************
	public String getStartTime(){
		return startTime.getValue();
	}
//********************************************************************************************
	@Override
	public void setValue(Object value) {
		if(value instanceof ScheduleInfo){
			ScheduleInfo info= (ScheduleInfo)value;
			Integer []days=info.getDaysOfWeek();
			super.setValue(days);
			startTime.setValue(info.getStartHour());
			dirty=false;
		}

		
	}
}
