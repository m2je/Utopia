package ir.utopia.core.util.tags.datainputform.client.calendar;

import ir.utopia.core.util.tags.datainputform.client.ImageHyperlink;
import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PersianCalendar extends HorizontalPanel {
	InputItem inputIem;
	String inputItemName;
	Widget div;
	TextBox text=new TextBox();
	 UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	ImageHyperlink calImage=new ImageHyperlink(images.popupCalendarIcon().createImage());
	private ArrayList<PersianCalendarListener>listeners=new ArrayList<PersianCalendarListener>();
//***********************************************************************
	@SuppressWarnings("deprecation")
	public PersianCalendar(InputItem inputItem){
		super.setWidth("100%");
		this.inputIem=inputItem;
		inputItemName=inputItem.getColumnName();
		div=new HTML("<div id=\"_dateDiv\" style=\"position: absolute; visibility: hidden; background-color: white;z-index:2\"></div>");
		add(div);
		text.setName(inputItemName);
		text.setStylePrimaryName("clsText");
		add(text);
		text.getElement().setId(inputItemName);
		add(calImage);
		calImage.setTitle(inputItem.getHeader());
		calImage.addClickListener(new ClickListener() {
			
			@Override
			public void onClick(Widget sender) {
				doShowDialog(inputItemName);
				
			}
		});
		text.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				fireValueChanged(event.getValue());
				
			}
		});
		if(getNativeFunction("fireCalendarValueChange_"+inputItemName)==null)//to prevent overriding 
			registerNativeFunctions();
	}
//***********************************************************************
	
	public void setValue(String value){
		text.setValue(value);
	}
//***********************************************************************
	public String getValue(){
			return text.getValue();
	}
//***********************************************************************
	public String getInputItemName() {
		return inputItemName;
	}
//***********************************************************************
	public void setInputItemName(String inputItemName) {
		this.inputItemName = inputItemName;
	}
//***********************************************************************
	public void setEnabled(boolean enabled){
		text.setEnabled(enabled);
	}
//***********************************************************************
	public boolean isEnabled(){
		return text.isEnabled();
	}
//***********************************************************************	
	public String getId(){
		return inputItemName;
	}
//***********************************************************************
	public void addListener(PersianCalendarListener listener){
		listeners.add(listener);
	}
//***********************************************************************
	protected void fireValueChanged(String newValue){
		for(PersianCalendarListener listener:listeners){
			listener.valueChanged(this,newValue);
		}
	}
//***********************************************************************
	public void showDialog(){
		doShowDialog(getId());
	}
//***********************************************************************
	public void showDialog(int x,int y){
		doShowDialog(getId(),x,y);
	}
//***********************************************************************
	public native void doShowDialog(String itemId)/*-{
	 	$wnd.showDate(itemId);
	}-*/;
//***********************************************************************
	public native void doShowDialog(String itemId,int x,int y)/*-{
 		$wnd.showDate(itemId,x,y);
	}-*/;
//***********************************************************************
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		text.setVisible(visible);
		calImage.setVisible(visible);
	} 
	
//***********************************************************************
		private static  JavaScriptObject getNativeFunction(String functionName){
			try {
				return getNativeFunctionN(functionName);
			} catch (Exception e) {
				return null;
			}
		}
//***********************************************************************
		private static native JavaScriptObject getNativeFunctionN(String functionName)/*-{
	    return  $wnd[functionName];
		}-*/;
//***********************************************************************
		public void registerNativeFunctions(){
			addValueChangeFunction("fireCalendarValueChange_"+inputItemName,this);
		}
//***********************************************************************
		public native void addValueChangeFunction(String functionName,PersianCalendar x)/*-{

		$wnd[functionName] = function ( t) {

		return x.@ir.utopia.core.util.tags.datainputform.client.calendar.PersianCalendar::fireValueChanged(Ljava/lang/String;)(t);

		};

		}-*/;
}
