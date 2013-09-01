package ir.utopia.core.util.tags.datainputform.client.lov;

import ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants;
import ir.utopia.core.util.tags.datainputform.client.ImageHyperlink;
import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;
import ir.utopia.core.util.tags.datainputform.client.WidgetContainer;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.ContextItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Window;

public class LOVWidget extends HorizontalPanel implements ClickListener{
	InputItem item;
	DataInputFormConstants constants=(DataInputFormConstants)GWT.create(DataInputFormConstants.class);
	UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	ImageHyperlink icon=new ImageHyperlink(images.LOVSearch().createImage());
	ListBox box;
	LOVSearchDialog dialog;
	ListBox valueHolder;
	List<LOVListener>listeners=new ArrayList<LOVListener>();
	WidgetContainer container;
	public LOVWidget(InputItem item){
		this.item=item;
		box=new ListBox(item.getLOVConfiguration().isMultiSelect());
		valueHolder=new ListBox(item.getLOVConfiguration().isMultiSelect());
		create();
	}
//*************************************************************************************************************
	@SuppressWarnings("deprecation")
	private void create(){
	 	box.setStylePrimaryName("clsSelect");
	 	box.setEnabled(false);
	 	box.setName(item.getColumnName()+"_C");
	 	add(box);
	 	add(icon);
	 	valueHolder.setName(item.getColumnName());
	 	valueHolder.setVisible(false);
	 	add(valueHolder);
	 	valueHolder.getElement().setId(item.getColumnName());
	 	setCellWidth(icon,"2%");
	 	setCellWidth(box, "98%");
	 	setWidth("100%");
	 	box.addClickListener(this);
	 	icon.addClickListener(this);
	 	box.getElement().setId(item.getColumnName()+"_C");
	}
//*************************************************************************************************************
public void setWidgetValue(Set<Long> ids,List<String> values){
	box.clear();
	valueHolder.clear();
	box.setTitle("");
	StringBuffer title=new StringBuffer();
	int index=0;
	for(Long id:ids){
		addItem(id.toString(), values.get(index));
		title.append(values.get(index)).append(" ").append((char)13);
		index++;
	}
	box.setTitle(title.toString());
	fireValueChanged(ids, values);
}
//*************************************************************************************************************
public void setWidgetValue(Long id,String value){
	if(box.getItemCount()>0){
		box.clear();
		valueHolder.clear();
	}
	addItem(id.toString(), value);
	fireValueChanged(id, value);
}
//*************************************************************************************************************
@Override
public void onClick(Widget sender) {
	showSearchDialog();
	} 
//*************************************************************************************************************
public void showSearchDialog(){
	if(dialog==null){
		dialog=new LOVSearchDialog(item.getLOVConfiguration(),this);
	}
	dialog.initiateAndShowLOVDialog();
	
}
//*************************************************************************************************************
public Object getValue(){
	if(box.getItemCount()>0){
		if(item.getLOVConfiguration().isMultiSelect()){
			ArrayList<Object>result=new ArrayList<Object>();
			for(int i=0;i<box.getItemCount();i++ ){
				result.add(result.add(box.getValue(i)));
			}
			return result.toArray();
		}else{
			return valueHolder.getValue(0);
		}
	}
	return null;
}
//*************************************************************************************************************
public void setValue(Object value){
	box.clear();
	valueHolder.clear();
	box.setTitle("");
	if(value!=null){
		if(value instanceof String){
			addItem((String)value);
		}else if(value.getClass().isArray()){
			StringBuffer title=new StringBuffer();
			for(String item:(String[])value){
				title.append( addItem(item));
				title.append(" ").append((char)13);
			}
			box.setTitle(title.toString());
		}
	}
	
}
//*************************************************************************************************************
protected String addItem(String value){
	int seperatorIndex=((String) value).indexOf("|");
	if(seperatorIndex>0){
		String key=((String) value).substring(0,seperatorIndex);
		String displayValue=((String) value).substring(seperatorIndex+1,((String)value).length());
		addItem(key, displayValue);
		return displayValue;
	}
	return "";
}
//*************************************************************************************************************
protected void addItem(String key,String displayValue){
	box.addItem(displayValue,key);
	valueHolder.addItem(displayValue,key);
	valueHolder.setItemSelected(valueHolder.getItemCount()-1, true);
}
//*************************************************************************************************************
public void setEnabled(Boolean enabled){
	icon.setVisible(enabled);
}
//*************************************************************************************************************
public void addListener(LOVListener listener){
	listeners.add(listener);
}
//*************************************************************************************************************
protected void fireValueChanged(Long key,String value){
	for(LOVListener listener:listeners){
		listener.valueChanged(this,key, value);
	}
}
//*************************************************************************************************************
protected void fireValueChanged(Set<Long> keys,List<String> value){
	for(LOVListener listener:listeners){
		listener.valueChanged(this,keys, value);
	}
}
//*************************************************************************************************************
public void fireWindowStatusChanged(LOVWindowEvent event){
	for(LOVListener listener:listeners){
		listener.LovWindowStatusChanged(event);
	}
}
//*************************************************************************************************************
public String getName(){
	return item.getColumnName();
}
//*************************************************************************************************************
public String getId(){
	return item.getColumnName()+"_C";
}
//*************************************************************************************************************
public String getValueCommaSeperated(){
	 StringBuffer result=new StringBuffer();
	 if(item.getLOVConfiguration().isMultiSelect()){
		 Object[] keys=(Object[])getValue();
			if(keys!=null&&keys.length>0){
				int index=0;
				for(Object l:keys){
					if(index>0){
						result.append(",");
					}
					result.append(l.toString());
				}
			}
	 }else{
		 result.append(getValue().toString());
	 }
	 return result.toString();
}
//*************************************************************************************************************
public void setContainer(WidgetContainer container){
	this.container=container;
}
//*************************************************************************************************************
public WidgetContainer getContainer(){
	return this.container;
}
//*************************************************************************************************************
public List<ContextItem> getContextItems(){
	String []dependences= item.getFilterDepenedents();
	if(dependences!=null&&dependences.length>0&&container!=null){
		List<ContextItem>result=new ArrayList<ContextItem>();
		for(String dependent:dependences){
			ContextItem item= container.getItem(dependent);
			if(item!=null){
				result.add(item);
			}
		}
		return result;
	}else{
		return null;
	}
	
}
}
