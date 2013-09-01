package ir.utopia.core.util.tags.utilitypanel.client;

import ir.utopia.core.util.tags.datainputform.client.timefield.TimeTextBox;
import ir.utopia.core.util.tags.utilitypanel.client.model.ScheduleInfo;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class HourlySchedulePanel extends AbstractWeekDaySupportPanel  {
	
	
	ListBox periodBox=new ListBox();
	TextBox interval=new TextBox();
	TimeTextBox startTime=new TimeTextBox();
	TimeTextBox endTime=new TimeTextBox();
	public HourlySchedulePanel(){
		ValueChangeHandler<String> handler=new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
					dirty=true;
			}
		};
		add(getDaysOfWeek());
		add(getStartFinishHourPanel());
		add(getIntervalTimePanel());
		interval.addValueChangeHandler(handler);
		startTime.addValueChangeHandler(handler);
		endTime.addValueChangeHandler(handler);
	}
//*************************************************************************************************************	
	private HorizontalPanel getIntervalTimePanel(){
		HorizontalPanel h1=new HorizontalPanel();
		h1.setSpacing(10);
		Label l1=new Label();
		l1.setText(constants.recurrence());
		l1.setStylePrimaryName("clsLabel");
		h1.add(l1);
		
		Label l2=new Label();
		l2.setText(constants.every());
		l2.setStylePrimaryName("clsLabel");
		h1.add(l2);
		interval.setStylePrimaryName("clsText");
		h1.add(interval);
		periodBox.setStylePrimaryName("clsSelect");
		periodBox.addItem(constants.minute(),"1");
		periodBox.addItem(constants.hour(),"2");
		h1.add(periodBox);
		h1.setCellHorizontalAlignment(l1, HorizontalPanel.ALIGN_CENTER);
		h1.setCellHorizontalAlignment(periodBox, HorizontalPanel.ALIGN_CENTER);
		h1.setCellHorizontalAlignment(l2, HorizontalPanel.ALIGN_CENTER);
		h1.setCellHorizontalAlignment(interval, HorizontalPanel.ALIGN_CENTER);
			
		h1.setCellVerticalAlignment(l1, HorizontalPanel.ALIGN_MIDDLE);
		h1.setCellVerticalAlignment(periodBox, HorizontalPanel.ALIGN_MIDDLE);
		h1.setCellVerticalAlignment(l2, HorizontalPanel.ALIGN_MIDDLE);
		h1.setCellVerticalAlignment(interval, HorizontalPanel.ALIGN_MIDDLE);
		
		return h1;
	}
//**********************************************************************************************	
	@Override
	public boolean validate() {
		if(super.validate()){
			if(startTime.getValue()!=null&&startTime.validate()){
				if(endTime.getValue()!=null&&endTime.validate()){
					if(interval.getValue()!=null&&interval.getText().matches("[0-9]+")){
						return true;
					}else{
						Window.alert(constants.invalidInterval());
					}	
				}else{
					Window.alert(constants.invalidEndTime());
				}
			}else{
				Window.alert(constants.invalidStartTime());
			}
		}
		return false;	   
	}

//**********************************************************************************************
	@Override
	public void reset() {
		super.reset();
		interval.setText("");
		periodBox.setSelectedIndex(0);
		startTime.setText("");
		endTime.setText("");
		interval.setText("");
		dirty=false;
	}
//**********************************************************************************************
	protected Widget getStartFinishHourPanel(){
		HorizontalPanel h1=new HorizontalPanel();
		HorizontalPanel h11=new HorizontalPanel();
		HorizontalPanel h12=new HorizontalPanel();
		Label l1=new Label();
		h11.setSpacing(5);
		h12.setSpacing(5);
		l1.setText(constants.startHour()+":");
		l1.setStylePrimaryName("clsLabel");
		h11.add(l1);
		startTime.setStylePrimaryName("clsText");
		h11.add(startTime);
		h1.add(h11);
		Label l2=new Label();
		l2.setText(constants.finishHour()+":");
		l2.setStylePrimaryName("clsLabel");
		h12.add(l2);
		endTime.setStylePrimaryName("clsText");
		h12.add(endTime);
		h1.add(h12);
		return h1;
		
	}
//**********************************************************************************************
	public void setValue(String startTimeValue,String endTimeValue,String intervalValue,int []daysOfWeek){
		super.setValue(daysOfWeek);
		startTime.setValue(startTimeValue);
		endTime.setValue(endTimeValue);
		interval.setText(intervalValue);
		dirty=false;
	}
//**********************************************************************************************
	@Override
	public void reload() {
		reset();
		
	}
//**********************************************************************************************
	public  String getStartTime(){
		return startTime.getValue();
	}
//**********************************************************************************************
	public String getEndHour(){
		return endTime.getValue();
	}
//**********************************************************************************************
	public int getInterval(){
		int interval2= Integer.parseInt(interval.getValue());
		if(periodBox.getSelectedIndex()==1){
			interval2*=2;
		}
		return interval2;
	}
//**********************************************************************************************
	@Override
	public void setValue(Object value) {
		if(value instanceof ScheduleInfo){
			ScheduleInfo info= (ScheduleInfo)value;
			super.setValue(info.getDaysOfWeek());
			startTime.setValue(info.getStartHour());
			endTime.setValue(info.getFinishHour());
			int intervalValue= info.getInterval();
			if(intervalValue%60==0){
				periodBox.setSelectedIndex(1);
				interval.setText(String.valueOf(intervalValue/60));
			}else{
				periodBox.setSelectedIndex(0);
				interval.setText(String.valueOf(intervalValue));
			}
			dirty=false;
		}
	}
}
