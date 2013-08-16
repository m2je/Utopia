package ir.utopia.core.util.tags.datainputform.client;

import ir.utopia.core.util.tags.datainputform.client.calendar.PersianCalendar;
import ir.utopia.core.util.tags.datainputform.client.calendar.PersianCalendarListener;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVListener;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVWidget;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVWindowEvent;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.radiobutton.RadioButtonGroup;
import ir.utopia.core.util.tags.datainputform.client.radiobutton.RadioButtonGroupListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.event.CheckboxListener;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class GlobalEditorDataChangeListener extends ComboBoxListenerAdapter implements ChangeListener,KeyUpHandler,ClickHandler,EventCallback,CheckboxListener,
																						LOVListener,PersianCalendarListener,RadioButtonGroupListener {
	EditorDataChangeListener listener;
	InputItem []items;
	public GlobalEditorDataChangeListener (EditorDataChangeListener listener,InputItem []items){
		this.listener=listener;
		this.items=items;
	}
//**********************************************************************************************************************	
	public void onSelect(ComboBox comboBox, Record record, int index) {
		handelComboBox(comboBox);
	}
//**********************************************************************************************************************
	private void handelComboBox(Widget comboBox){
		String widgetName=EditorFactory.getWidgetName(comboBox);
		int types[]=new int[]{InputItem.DEPENDENCY_TYPE_DATA_DISPLAY,InputItem.DEPENDENCY_TYPE_DATA_FILTER,InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY,InputItem.DEPENDENCY_TYPE_COLOR_LOGIC};
		String[][]dependencts=getDependents(widgetName);
		Object value= EditorFactory.getWidgetValue(comboBox);
		listener.dataChanges(comboBox, value,types, dependencts);
	}
//**********************************************************************************************************************
	private String[] getDependents(String widgetName,int dependencyType){
	if(items!=null){
		List<String>dependets=new ArrayList<String>();
		 for(int i=0;i<items.length;i++){
			 String filterDeps[]=null;
			 if(dependencyType==InputItem.DEPENDENCY_TYPE_DATA_FILTER){
				 filterDeps= items[i].getFilterDepenedents();
			 }else if(dependencyType==InputItem.DEPENDENCY_TYPE_DATA_DISPLAY){
				 filterDeps=items[i].getDisplayDepenedents();
			 }else if(dependencyType==InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY){
				 filterDeps=items[i].getReadOnlyDepenedents(); 
			 }else if(dependencyType==InputItem.DEPENDENCY_TYPE_COLOR_LOGIC){
				 filterDeps=items[i].getColorDependents(); 
			 }
			 
			if(filterDeps!=null){
				 for(String fdep:filterDeps){
						if(widgetName.equals(fdep)){
							dependets.add(items[i].getColumnName());	
						}
					}
				}
		}
		 return dependets.toArray(new String[dependets.size()]);
		}
	return null;
		}
//**********************************************************************************************************************
	public void onChange(Widget sender) {
		if(sender instanceof ListBox)
			handelComboBox(sender);
	}
//**********************************************************************************************************************
	@Override
	public void onKeyUp(KeyUpEvent event) {
		if(event.getSource() instanceof TextBoxBase&&!ifControlKey(event.getNativeKeyCode())){//for TextBox and TextArea
			TextBoxBase field= (TextBoxBase)event.getSource();
			String widgetName=EditorFactory.getWidgetName(field);
			int types[]=new int[]{InputItem.DEPENDENCY_TYPE_DATA_DISPLAY,InputItem.DEPENDENCY_TYPE_DATA_FILTER,InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY,InputItem.DEPENDENCY_TYPE_COLOR_LOGIC};
			String[][] dependencts=getDependents(widgetName);
			Object value= EditorFactory.getWidgetValue(field);
			listener.dataChanges(field, value,types, dependencts);
		}
		
	}
//**********************************************************************************************************************
	private String[][] getDependents(String widgetName) {
		String []displayDeps= getDependents(widgetName,InputItem.DEPENDENCY_TYPE_DATA_DISPLAY);
		String []readOnlyDeps= getDependents(widgetName,InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY);
		String []filterDeps= getDependents(widgetName,InputItem.DEPENDENCY_TYPE_DATA_FILTER);
		String []colorDependents=getDependents(widgetName, InputItem.DEPENDENCY_TYPE_COLOR_LOGIC);
		String[][]values=new String[4][3];
		displayDeps=displayDeps==null?new String[0]:displayDeps;
		readOnlyDeps=readOnlyDeps==null?new String[0]:readOnlyDeps;
		filterDeps=filterDeps==null?new String[0]:filterDeps;
		colorDependents=colorDependents==null?new String[0]:colorDependents;
		values[0]=displayDeps;
		values[1]=filterDeps;
		values[2]=readOnlyDeps;
		values[3]=colorDependents;
		return values;
	}
//**********************************************************************************************************************
private boolean ifControlKey(int eventkey){
	return eventkey==KeyCodes.KEY_ALT||
	eventkey== KeyCodes.KEY_CTRL ||
	eventkey== KeyCodes.KEY_DOWN||
	eventkey== KeyCodes.KEY_END ||
	eventkey== KeyCodes.KEY_ENTER ||
	eventkey== KeyCodes.KEY_ESCAPE||
	eventkey== KeyCodes.KEY_HOME ||
	eventkey== KeyCodes.KEY_LEFT||
	eventkey== KeyCodes.KEY_PAGEDOWN||
	eventkey== KeyCodes.KEY_PAGEUP ||
	eventkey== KeyCodes.KEY_RIGHT||
	eventkey== KeyCodes.KEY_SHIFT||
	eventkey== KeyCodes.KEY_TAB ||
	eventkey== KeyCodes.KEY_UP ;
}
//**********************************************************************************************************************
@Override
public void onClick(ClickEvent event) {
	handelComboBox((Widget)event.getSource());
	
}
//**********************************************************************************************************************
@Override
public void execute(EventObject e) {
	int types[]=new int[]{InputItem.DEPENDENCY_TYPE_DATA_DISPLAY,InputItem.DEPENDENCY_TYPE_DATA_FILTER,InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY,InputItem.DEPENDENCY_TYPE_COLOR_LOGIC};
	String widgetName=e.getTarget().getPropertyString("name");
	String[][] dependencts=getDependents(widgetName);
	listener.dataChanges(widgetName, null, types, dependencts);
}
//**********************************************************************************************************************
public void onCheck(Checkbox field, boolean checked){
	int types[]=new int[]{InputItem.DEPENDENCY_TYPE_DATA_DISPLAY,InputItem.DEPENDENCY_TYPE_DATA_FILTER,InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY,InputItem.DEPENDENCY_TYPE_COLOR_LOGIC};
	String widgetName=field.getName();
	String[][] dependencts=getDependents(widgetName);
	listener.dataChanges(widgetName, null, types, dependencts);
}
//**********************************************************************************************************************
@Override
public void valueChanged(LOVWidget source,Long key, String value) {
	int types[]=new int[]{InputItem.DEPENDENCY_TYPE_DATA_DISPLAY,InputItem.DEPENDENCY_TYPE_DATA_FILTER,InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY,InputItem.DEPENDENCY_TYPE_COLOR_LOGIC};
	String widgetName=source.getElement().getPropertyString("name");
	String[][] dependencts=getDependents(widgetName);
	listener.dataChanges(widgetName, key, types, dependencts);
	
}
//**********************************************************************************************************************
@Override
public void valueChanged(LOVWidget source, Set<Long> key, List<String> value) {
	int types[]=new int[]{InputItem.DEPENDENCY_TYPE_DATA_DISPLAY,InputItem.DEPENDENCY_TYPE_DATA_FILTER,InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY,InputItem.DEPENDENCY_TYPE_COLOR_LOGIC};
	String widgetName=source.getElement().getPropertyString("name");
	String[][] dependencts=getDependents(widgetName);
	listener.dataChanges(widgetName, source.getValueCommaSeperated(), types, dependencts);
}
//**********************************************************************************************************************
@Override
public void valueChanged(PersianCalendar source, String newValue) {
	int types[]=new int[]{InputItem.DEPENDENCY_TYPE_DATA_DISPLAY,InputItem.DEPENDENCY_TYPE_DATA_FILTER,InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY,InputItem.DEPENDENCY_TYPE_COLOR_LOGIC};
	String widgetName=source.getInputItemName();
	String[][] dependencts=getDependents(widgetName);
	listener.dataChanges(widgetName, newValue, types, dependencts);
}
//**********************************************************************************************************************
@Override
public void valueChange(RadioButtonGroup group, int selectedIndex, String value) {
	int types[]=new int[]{InputItem.DEPENDENCY_TYPE_DATA_DISPLAY,InputItem.DEPENDENCY_TYPE_DATA_FILTER,InputItem.DEPENDENCY_TYPE_DATA_READ_ONLY,InputItem.DEPENDENCY_TYPE_COLOR_LOGIC};
	String widgetName=group.getName();
	String[][] dependencts=getDependents(widgetName);
	listener.dataChanges(widgetName, value, types, dependencts);
	
}
@Override
public void LovWindowStatusChanged(LOVWindowEvent event) {
	
}
	
}
