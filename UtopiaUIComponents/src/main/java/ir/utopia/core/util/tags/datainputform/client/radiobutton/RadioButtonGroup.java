package ir.utopia.core.util.tags.datainputform.client.radiobutton;

import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RadioButtonGroup extends SimplePanel implements ValueChangeHandler<Boolean>{
	String name;
	String header;
	UILookupInfo lookupInfo;
	int selectedIndex=-1;
	CellPanel rootPane; 
	RadioButton []buttons;
	String []buttonValues;
	ValueChangeHandler<Boolean> handler;
	List<RadioButtonGroupListener> listeners;
//*********************************************************************************************************	
	public RadioButtonGroup(String name,String header,UILookupInfo lookupInfo){
		this.name=name;
		this.header=header;
		this.lookupInfo=lookupInfo;
		create();
	}
//*********************************************************************************************************
	@Override
	public void setHeight(String height) {
		super.setHeight(height);
		rootPane.setHeight(height);
	}
//*********************************************************************************************************
	@Override
	public void setWidth(String width) {
		super.setWidth(width);
		rootPane.setWidth(width);
	}

//*********************************************************************************************************	
	private void create(){
		
		String [][]data=lookupInfo.getData();
		buttonValues=new String[data.length];
		buttons=new RadioButton[data.length];
		boolean isLinear=false;
		if(rootPane==null){
			if(buttons.length<3){
				String overAllText=buttons[0]!=null?buttons[0].getText():"";
				overAllText=overAllText==null?"":overAllText;
				overAllText=buttons[1]!=null?overAllText+buttons[1].getText():overAllText;
				if(overAllText!=null&&overAllText.length()<300){
					rootPane=new HorizontalPanel();
					isLinear=true;
				}else{
					rootPane=new VerticalPanel();
				}
				
			}else{
				rootPane=new VerticalPanel();
			}
			 
		}
		if(header!=null){
			rootPane.add(new HTML("<p align=center class=\"clsLabel\">"+header+(isLinear?":":"")+"</p>"));
		}
		for(int i=0;i<data.length;i++){
			buttons[i] = new RadioButton(name,data[i][1] );
		    buttonValues[i]=  data[i][0];
		    buttons[i].setStylePrimaryName("clsRadioButton");
		    buttons[i].ensureDebugId(name + i);
		    buttons[i].addValueChangeHandler(this);
		    buttons[i].setFormValue(data[i][0]);
		    rootPane.add(buttons[i]);
		}
		rootPane.setSpacing(5);
		rootPane.setStylePrimaryName("clsRadioButtonGroup");
		add(rootPane);
	}

//*********************************************************************************************************
	public String getValue(){
		return selectedIndex>=0&&selectedIndex<buttonValues.length? buttonValues[selectedIndex]:null;
	}
//*********************************************************************************************************	
	public void setValue(String value){
		if(buttonValues!=null){
			int index=0;
			for(String cur:buttonValues){
				if(cur.equals(value)){
					buttons[index].setValue(true);
				}
				else if(selectedIndex>=0){
					buttons[index].setValue(false);
				    }
				index++;
			}
			}
	}
//*********************************************************************************************************
	public void setValueChangeHandler(ValueChangeHandler<Boolean> handler){
		this.handler=handler;
	}
//*********************************************************************************************************
@Override
public void onValueChange(ValueChangeEvent<Boolean> event) {
	RadioButton button=(RadioButton)event.getSource();
	for(int i=0;i<buttons.length;i++){
		if(buttons[i].equals(button)){
			selectedIndex=i;
			break;
		}
	}
	if(handler!=null){
		handler.onValueChange(event);
	}
	fireValueChanged();
}
//*********************************************************************************************************
public void addListener(RadioButtonGroupListener listener){
	if(this.listeners==null){
		this.listeners=new ArrayList<RadioButtonGroupListener>();
	}
	listeners.add(listener);
}
//*********************************************************************************************************
public String getName(){
	return name;
}
//*********************************************************************************************************
protected void fireValueChanged(){
	if(this.listeners!=null){
		for(RadioButtonGroupListener listener:listeners){
			listener.valueChange(this, selectedIndex, getValue());
		}
	}
}
}
