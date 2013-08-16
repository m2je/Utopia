package ir.utopia.core.util.tags.datainputform.client;

import com.google.gwt.user.client.ui.Widget;

public interface EditorDataChangeListener {

	public void dataChanges(Widget widget,Object newValue,int []depenedetTypes,String [][] dependentfields);
	
	public void dataChanges(String widgetName,Object newValue,int []depenedetTypes,String [][] dependentfields);
}
