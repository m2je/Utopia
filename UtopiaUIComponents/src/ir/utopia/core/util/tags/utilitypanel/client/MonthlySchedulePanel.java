package ir.utopia.core.util.tags.utilitypanel.client;

import ir.utopia.core.util.tags.datainputform.client.timefield.TimeTextBox;
import ir.utopia.core.util.tags.utilitypanel.client.model.ScheduleInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class MonthlySchedulePanel extends VerticalPanel implements InnerPanel,ValueChangeHandler<String> {
	UtilityConstants constant=GWT.create(UtilityConstants.class);
	TimeTextBox hour=new TimeTextBox();
	TextBox dayOfMonth=new TextBox();
	private boolean dirty;
	public MonthlySchedulePanel(){
	
		HorizontalPanel h1=new HorizontalPanel();
		h1.setSpacing(5);
		Label l1=new Label();
		l1.setStylePrimaryName("clsLabel");
		l1.setText(constant.dayOfMonth()+":");
		h1.add(l1);
		l1.setWidth("80px");
		dayOfMonth.setStylePrimaryName("clstext");
		dayOfMonth.setWidth("120px");
		h1.add(dayOfMonth);
		dayOfMonth.addValueChangeHandler(this);
		
		HorizontalPanel h2=new HorizontalPanel();
		h2.setSpacing(5);
		Label l2=new Label();
		l2.setStylePrimaryName("clsLabel");
		l2.setText(constant.startHour()+":");
		l2.setWidth("80px");
		h2.add(l2);
		hour.setStylePrimaryName("clstext");
		hour.setWidth("120px");
		h2.add(hour);
		hour.addValueChangeHandler(this);
		add(h1);
		add(h2);
	}
//******************************************************************************************
	@Override
	public boolean validate() {
		if(dayOfMonth.getValue()!=null&&dayOfMonth.getValue().matches("[0-9]{1,2}")&&
				Integer.parseInt(dayOfMonth.getValue())>0&&Integer.parseInt(dayOfMonth.getValue())<31){
			if(hour.getValue()!=null&&hour.validate()){
				return true;
			}else{
				Window.alert(constant.invalidStartTime());
			}
			
		}else{
			Window.alert(constant.invalidDay());
		}
		return false;
	}
//******************************************************************************************
	@Override
	public void reset() {
		hour.setText("");
		dayOfMonth.setText("");
		dirty=false;
	}
//******************************************************************************************
	@Override
	public boolean isModified() {
		return dirty;
	}
//******************************************************************************************
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		dirty=true;
	}
//******************************************************************************************
	public void setValue(String dayOfMonthValue,String hourValue){
		dayOfMonth.setValue(dayOfMonthValue);
		hour.setValue(hourValue);
		dirty=false;
	}
//******************************************************************************************
	@Override
	public void reload() {
		reset();
	}
//******************************************************************************************
	public String getStartTime(){
		return hour.getValue();
	}
//******************************************************************************************	
	public int getDayOfMonth(){
		return Integer.parseInt(dayOfMonth.getValue());
	}
//******************************************************************************************	
	@Override
	public void setValue(Object value) {
		if(value instanceof ScheduleInfo){
			ScheduleInfo info= (ScheduleInfo)value;
			int []daysOfMonth=info.getDaysOfMounth();
			if(daysOfMonth!=null){
				dayOfMonth.setValue("");
				for(int i=0;i<daysOfMonth.length;i++){
					dayOfMonth.setValue(dayOfMonth.getValue()+(i>0?",":"")+ String.valueOf(daysOfMonth[i]));
				}
				
			}
			
			hour.setValue(info.getStartHour());
			}
		dirty=false;
	}
}
