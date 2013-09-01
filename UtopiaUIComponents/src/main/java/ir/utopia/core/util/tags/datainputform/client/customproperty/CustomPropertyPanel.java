package ir.utopia.core.util.tags.datainputform.client.customproperty;

import ir.utopia.core.util.tags.datainputform.client.EditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.GlobalEditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.ImageHyperlink;
import ir.utopia.core.util.tags.datainputform.client.UtopiaImages;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CustomPropertyPanel extends VerticalPanel {
	 FlexTable propertyPanel;
	 private int columnCount=2;
	 UtopiaImages images = (UtopiaImages) GWT.create(UtopiaImages.class);
	 CustomPropertyConstants constants=(CustomPropertyConstants)GWT.create(CustomPropertyConstants.class);
	 private HashMap<Integer, Integer>propertyRealMapping=new HashMap<Integer, Integer>();
	 private boolean dirty=false;
	 EditorDataChangeListener listener=new EditorDataChangeListener() {
			
			@Override
			public void dataChanges(Widget widget, Object newValue,
					int[] depenedetTypes, String[][] dependentfields) {
					dirty=true;
				
			}

			@Override
			public void dataChanges(String widgetName, Object newValue,
					int[] depenedetTypes, String[][] dependentfields) {
				dirty=true;
				
			}
		};
		GlobalEditorDataChangeListener changeListener=new GlobalEditorDataChangeListener(listener, null);
//***********************************************************************************
	 public CustomPropertyPanel(){
			this(null);
		}	
//***********************************************************************************
	public CustomPropertyPanel(String []fieldNames){
		if(fieldNames==null){
			fieldNames=new String []{constants.propertyName(),constants.propertyValue()};
		}
		propertyPanel=new FlexTable();
		CellFormatter cf=propertyPanel.getCellFormatter();
		columnCount=fieldNames.length;
		for(int i=0;i<columnCount;i++){
			Label l1=new Label(fieldNames[i]);
			l1.setStylePrimaryName("clsLabel");
			propertyPanel.setWidget(0,i, l1);
			cf.setWidth(0,i, 100/columnCount+"%");
		}
		propertyPanel.setWidth("100%");
		addCutomProperty();
		add(propertyPanel);
		setBorderWidth(1);
	}
//***********************************************************************************	
	
	@SuppressWarnings("deprecation")
	public void addCutomProperty(){
		int newRowIndex=propertyPanel.getRowCount();
		CellFormatter cf=propertyPanel.getCellFormatter();
		for(int j=0;j<columnCount;j++){
			TextBox newItem=new TextBox();
			newItem.setName("item"+newRowIndex+""+j);
			newItem.setStylePrimaryName("clsText");
			propertyPanel.setWidget(newRowIndex, j, newItem);
			EditorFactory.addListener(newItem, changeListener);
			cf.setWidth(newRowIndex,0, 100/columnCount+"%");
		}
		final ImageHyperlink addRow=new ImageHyperlink(images.addAttachment().createImage());
		final ImageHyperlink removeRow=new ImageHyperlink(images.removeAttachment().createImage());
		addRow.addClickHandler(new ClickHandler() {
			ImageHyperlink me=addRow;
			@Override
			public void onClick(ClickEvent event) {
				addCutomProperty();
				me.setVisible(false);
			}
		});
		addRow.setTitle(constants.addCustomProperty());
		
		removeRow.setTitle(constants.removeCustomProperty());
		
		removeRow.addClickHandler(new ClickHandler() {
			int myIndex=propertyRealMapping.size();
			@Override
			public void onClick(ClickEvent event) {
				int myRealIndex= propertyRealMapping.get(myIndex);
				
				removeCustomeProperty(propertyRealMapping.get(myRealIndex));
			}
		});
		propertyRealMapping.put(propertyRealMapping.size(), newRowIndex);
		propertyPanel.setWidget(newRowIndex, columnCount, addRow);
		propertyPanel.setWidget(newRowIndex, columnCount+1,removeRow);
		propertyPanel.setWidget(newRowIndex, columnCount+2, null);
		
		for(int i=1;i<=newRowIndex;i++){
			propertyPanel.getWidget(i, columnCount+1).setVisible(true);
		}
		if(newRowIndex==1){	
			propertyPanel.getWidget(newRowIndex, columnCount+1).setVisible(false);
		}	
		
		
	}
//***********************************************************************************
	protected void removeCustomeProperty(int rowIndex){
		propertyPanel.removeRow(rowIndex);
		if(propertyPanel.getRowCount()-1>0){
			propertyPanel.getWidget(propertyPanel.getRowCount()-1, columnCount).setVisible(true);
			propertyPanel.getWidget(propertyPanel.getRowCount()-1, columnCount+1).setVisible(propertyPanel.getRowCount()-1!=0);
		}
	}
//***********************************************************************************
	public HashMap<String, String> getValue(){
		HashMap<String, String> result=new HashMap<String, String>();
		for(int i=1;i<propertyPanel.getRowCount();i++){
			Widget v1= propertyPanel.getWidget(i, 0);
			Widget v2= propertyPanel.getWidget(i, 1);
			if(!EditorFactory.valueIsNull(InputItem.DISPLAY_TYPE_STRING, v1)&&
					!EditorFactory.valueIsNull(InputItem.DISPLAY_TYPE_STRING, v2)){
				result.put(((TextBox)v1).getValue().trim(), ((TextBox)v2).getValue().trim());
			}
			
		}
		return result;
	}
//***********************************************************************************
	public List<List<String>> getValueList(){
		List<List<String>>result=new ArrayList<List<String>>();
		for(int i=1;i<propertyPanel.getRowCount();i++){
			List<String>row=new ArrayList<String>();
			for(int j=0;j<columnCount;j++){
				Widget v1= propertyPanel.getWidget(i, j);
				row.add(((TextBox)v1).getValue().trim());
			}
			result.add(row);
		}
		return result;
	}
//***********************************************************************************
	public String [] getKeyArray(){
		ArrayList<String>result=new ArrayList<String>();
		for(int i=1;i<propertyPanel.getRowCount();i++){
			Widget v1= propertyPanel.getWidget(i, 0);
			if(!EditorFactory.valueIsNull(InputItem.DISPLAY_TYPE_STRING, v1)){
				result.add(((TextBox)v1).getValue().trim());
			}
		}
		return result.toArray(new String[result.size()]);
	}
//***********************************************************************************	
	public String [] getValueArray(){
		ArrayList<String>result=new ArrayList<String>();
		for(int i=1;i<propertyPanel.getRowCount();i++){
			Widget v1= propertyPanel.getWidget(i, 1);
			if(!EditorFactory.valueIsNull(InputItem.DISPLAY_TYPE_STRING, v1)){
				result.add(((TextBox)v1).getValue().trim());
			}
		}
		return result.toArray(new String[result.size()]);
	}
//***********************************************************************************
	public boolean validate(){
		for(int i=1;i<propertyPanel.getRowCount();i++){
			Widget v1= propertyPanel.getWidget(i, 0);
			Widget v2= propertyPanel.getWidget(i, 1);
			if(EditorFactory.valueIsNull(InputItem.DISPLAY_TYPE_STRING, v1)){
				Window.alert(constants.invalidPropertyName().replaceAll("@row@", String.valueOf(i)));
				return false;
			}
			if(EditorFactory.valueIsNull(InputItem.DISPLAY_TYPE_STRING, v2)){
				Window.alert(constants.invalidPropertyValue().replaceAll("@row@", String.valueOf(i)));
				return false;
			}
			HashMap<String, String>map=getValue();
			Set<String> s=new HashSet<String>();
			for(String key:map.keySet()){
				if(s.contains(key)){
					Window.alert(constants.dublicatedPropertyName().replaceAll("@row@", String.valueOf(i)));
					return false;
				}
			}
		}
		return true;
	}
//***********************************************************************************
	public void clearComponent(){
		dirty=false;
		for(int i=0;i<propertyPanel.getRowCount();i++){
			int myRealIndex= propertyRealMapping.get(i);
			removeCustomeProperty(myRealIndex);
		}
		addCutomProperty();
	}
//***********************************************************************************
	public void setValue(String []propertyNames,String []propertyValues){
		clearComponent();
		if(propertyNames!=null&&propertyNames.length>0&&propertyValues!=null&&propertyValues.length==propertyNames.length){
			for(int i=0;i<propertyNames.length;i++){
				if(i>0)
					addCutomProperty();
				TextBox b1=(TextBox) propertyPanel.getWidget(i+1, 0);
				b1.setValue(propertyNames[i]);
				TextBox b2=(TextBox) propertyPanel.getWidget(i+1, 1);
				b2.setValue(propertyValues[i]);
			}
		}
	}
//***********************************************************************************
	public void setValue(List<List<String>>values){
		clearComponent();
		if(values!=null){
			for(int i=0;i<values.size();i++){
				if(i>0)
					addCutomProperty();
				List<String>row=values.get(i);
				for(int j=0;j<columnCount;j++){
					TextBox b1=(TextBox) propertyPanel.getWidget(i+1, j);
					b1.setValue(row.get(j));
				}
			}
		}
	}
//***********************************************************************************
	public boolean isDirty() {
		return dirty;
	}
//***********************************************************************************
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
}
