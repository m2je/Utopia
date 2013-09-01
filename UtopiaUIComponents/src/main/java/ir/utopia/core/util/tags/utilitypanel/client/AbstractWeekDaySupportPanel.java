package ir.utopia.core.util.tags.utilitypanel.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class AbstractWeekDaySupportPanel extends VerticalPanel implements InnerPanel,ValueChangeHandler<Boolean>{
	protected boolean dirty=false;
	UtilityConstants constants=GWT.create(UtilityConstants.class);
	CheckBox []daysOfWeek=new CheckBox[]{new CheckBox(),new CheckBox(),new CheckBox(),new CheckBox(),new CheckBox(),new CheckBox(),new CheckBox()};

	protected HorizontalPanel getDaysOfWeek(){
		HorizontalPanel h1=new HorizontalPanel();
		h1.setSpacing(10);
		
		daysOfWeek[0].setStylePrimaryName("clsCheckBox");
		daysOfWeek[0].setText(constants.sunday());
		daysOfWeek[1].setStylePrimaryName("clsCheckBox");
		daysOfWeek[1].setText(constants.monday());
		daysOfWeek[2].setStylePrimaryName("clsCheckBox");
		daysOfWeek[2].setText(constants.tuesday());
		daysOfWeek[3].setStylePrimaryName("clsCheckBox");
		daysOfWeek[3].setText(constants.wednesday());
		daysOfWeek[4].setStylePrimaryName("clsCheckBox");
		daysOfWeek[4].setText(constants.thursday());
		daysOfWeek[5].setStylePrimaryName("clsCheckBox");
		daysOfWeek[5].setText(constants.friday());
		daysOfWeek[6].setStylePrimaryName("clsCheckBox");
		daysOfWeek[6].setText(constants.saturday());
		Label l1=new Label();
		l1.setText(constants.daysOfWeek()+":");
		l1.setStylePrimaryName("clsLabel");
		h1.add(l1);
		
		if("fa".equals(LocaleInfo.getCurrentLocale().getLocaleName())){
			daysOfWeek[6].addValueChangeHandler(this);
			h1.add(daysOfWeek[6]);
			for(int i=0;i<daysOfWeek.length-1;i++){
				h1.add(daysOfWeek[i]);
				daysOfWeek[i].addValueChangeHandler(this);
			}
		}else{
			for(int i=0;i<daysOfWeek.length;i++){
				h1.add(daysOfWeek[i]);
				daysOfWeek[i].addValueChangeHandler(this);
			}
			
		}
		return h1;
	}

//*****************************************************************************	
	@Override
	public boolean isModified() {
		return dirty;
	}
//*****************************************************************************
	public void reset() {
		for(int i=0;i<daysOfWeek.length;i++){
			daysOfWeek[i].setValue(false);
		}
		dirty=false;
	}
//*****************************************************************************
	public boolean validate(){
		for(int i=0;i<daysOfWeek.length;i++){
			if(daysOfWeek[i].getValue()){
				return true;
			}
		}
		return false;
	}
//*****************************************************************************
	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		dirty=true;
	}
//*****************************************************************************
	protected void setValue(Integer []selectedDays){
		int []ndays=new int[selectedDays.length];
		int index=0;
		for(int day:selectedDays){
			ndays[index++]=day;
		}
		setValue(ndays);
	}
//*****************************************************************************
	protected void setValue(int []selectedDays){
		reset();
		if(selectedDays!=null){
			for(int i: selectedDays){
				daysOfWeek[i-1].setValue(true);
			}
			dirty=false;
		}
	}
	
//*****************************************************************************
	public Integer[] getValue(){
		List<Integer>result=new ArrayList<Integer>();
		int index=1;
		for(CheckBox i: daysOfWeek){
			if(i.getValue()){
				result.add(index);
			}
			index++;
		}
		return result.toArray(new Integer[result.size()]);
	}
}
