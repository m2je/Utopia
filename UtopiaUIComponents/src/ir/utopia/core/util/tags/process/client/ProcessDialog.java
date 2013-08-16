package ir.utopia.core.util.tags.process.client;

import ir.utopia.core.util.tags.datainputform.client.AbstractUtopiaGWTEditor;
import ir.utopia.core.util.tags.datainputform.client.EditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.GlobalEditorDataChangeListener;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVListenerAdapter;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVWidget;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVWindowEvent;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.password.PasswordEdior;
import ir.utopia.core.util.tags.process.client.model.ProcessConfigurationModel;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

public class ProcessDialog extends DialogBox  {
	
	UtopiaProcessHandlerInterface handler;
	GlobalEditorDataChangeListener editorListener;
	ProcessConstants constants;
	ProcessConfigurationModel model;
	int loadingComboLeft=0;
	HashMap<String,Widget>widgets=new HashMap<String, Widget>();
	
	public ProcessDialog(UtopiaProcessHandlerInterface handler){
		super(false,true);
		this.handler=handler;
	}
//********************************************************************************	
	public ProcessDialog(ProcessConfigurationModel model,UtopiaProcessHandlerInterface handler,ProcessConstants constants,String direction){
		this.handler=handler;
		InputItem []items= model.getItems();
		this.constants=constants;
		this.model=model;
	if(items!=null&&items.length>0){
		EditorDataChangeListener listener= AbstractUtopiaGWTEditor.createProcessChangeHandler(items, model.getProcessId(), widgets);
		editorListener=new GlobalEditorDataChangeListener(listener,items);
	    ensureDebugId("cwDialogBox");
	    setText(model.getTitle());
	    FlexTable editorsPane=new FlexTable();
	    editorsPane.setStylePrimaryName("clsTableBody");
	    VerticalPanel dialogContents=new VerticalPanel();
	    dialogContents.setStylePrimaryName("clsTableBody");
	    setWidget(dialogContents);
	    int row=0;
	    for(InputItem item:items){
		    Widget widget= EditorFactory.createEditor(item, direction,true);
		    EditorFactory.addListener(widget, editorListener); 
		    if(widget instanceof LOVWidget ){
		    	((LOVWidget)widget).addListener(new LOVListenerAdapter(){
		    		@Override
		    		public void LovWindowStatusChanged(LOVWindowEvent event) {
		    			if(event.getWindowStatus()==LOVWindowEvent.WINDOW_STATUS_CLOSED){
		    				show();
		    			}else{
		    				hide();
		    			}
		    		}
		    	});
		    }
		    widgets.put(item.getColumnName(), widget);
		    if(widget instanceof PasswordEdior){
		    	Label l= ((PasswordEdior)widget).getFirstLabel();
		    	l.setStylePrimaryName("clsLabel");
		    	editorsPane.setWidget(row, 0, l);
		    	editorsPane.setWidget(row, 1, ((PasswordEdior)widget).getFirst());
		    	row++;
		    	l=((PasswordEdior)widget).getSecounLabel();
		    	l.setStylePrimaryName("clsLabel");
		    	editorsPane.setWidget(row, 0, l);
		    	editorsPane.setWidget(row, 1, ((PasswordEdior)widget).getSecound());
		    }else{
		    	Label label=getLable(item);
		    	label.setStylePrimaryName("clsLabel");
		    	editorsPane.setWidget(row, 0, label);
		    	editorsPane.setWidget(row,1, widget);
		    }
		   row++;
		    
	    }

	
	    Button cancelButton = new Button(constants.cancelProcess(),
	        new ButtonListenerAdapter() {
	    	public void onClick(Button button, EventObject e) {
	    		 hide();
	        }
	        });
	    Button startButton = new Button(constants.startProcess(),
		        new ButtonListenerAdapter() {
	    	public void onClick(Button button, EventObject e) {
		        	  if(validate()){
		    			  hide();
			        	  startProcess();
		        	  }
		          }
		        });
	    HorizontalPanel buttomPanel=new HorizontalPanel();
	   if("rtl".equalsIgnoreCase(direction)){
		   buttomPanel.add(startButton);
		   buttomPanel.add(cancelButton);
	    }else{
	    	buttomPanel.add(cancelButton);
	    	buttomPanel.add(startButton);
	    }
	   dialogContents.add(editorsPane);
	    dialogContents.add(buttomPanel);
	    getElement().setDir(direction);
	    }
	
	assignDefaultValues(model);
	}
//***********************************************************************************
private void assignDefaultValues(ProcessConfigurationModel model) {
	HashMap<String,String>valuesMap=model.getDefaultValues();
	for(String key: valuesMap.keySet()){
		Widget widget= widgets.get(key);
		if(widget!=null){
			String value= valuesMap.get(key);
			EditorFactory.setWidgetValue(widget, value);
		}
	}
}
//***********************************************************************************
	private boolean valueIsNull(InputItem item,Object value){
		if(value==null||value.toString().trim().length()==0){
			return true;
		}
		if(item.getDisplayType()==InputItem.DISPLAY_TYPE_COMBOBOX||item.getDisplayType()==InputItem.DISPLAY_TYPE_LIST){
			try {
				long lvalue= Long.parseLong(value.toString());
				return lvalue<0;
			} catch (NumberFormatException e) {
				GWT.log("", e);
				return true;
			}
		}
		return false;
	}
//**********************************************************************
	private boolean validate(){
		for(String key:widgets.keySet()){
			Widget widget= widgets.get(key);
			for(InputItem item: model.getItems()){
				if(item.getColumnName().equals(EditorFactory.getWidgetName(widget))){
					if(EditorFactory.isVisible(widget)&&EditorFactory.isWidgetEnabled(widget)){
						Object value=EditorFactory.getWidgetValue(widget);
						if(item.isMandatory()&&valueIsNull(item, value)){
							Window.alert(constants.fieldIsMandatory().replace("@p1@", item.getHeader()));
							return false;
						}
					}
					
				}
			}
			if(widget instanceof PasswordEdior){
				if(!  ((PasswordEdior)widget).validate()){
					return false;
				}else{
					((PasswordEdior)widget).clearError();
				}
			}
		}
		return true;
	}
//**********************************************************************
	private void startProcess(){
		MessageUtility.progress(constants.pleaseWait(),constants.submitingData());
		String []params=new String[widgets.size()];
		String []parameterValue=new String[params.length];
		int index=0;
		for(String key :widgets.keySet()){
			Widget widget=widgets.get(key);
			params[index]= EditorFactory.getWidgetName(widget);
			Object value=EditorFactory.getWidgetValue(widget);
			parameterValue[index]=value!=null?value.toString():null;
			index++;
		}
		handler.callAction(-1l,params,parameterValue,model.getTitle(), model.getProcessSubmitPath(),constants, model.isAlertForSuccess(),UtopiaProcessHandlerInterface.SERVICE_ACTION_TYPE_EXCUTE_WITH_CONFIRM,model.isRefreshPageAfterProcess());
	}
//**********************************************************************
	public static void redirect(String url){
		Window.Location.reload();
		
	}

//**********************************************************************
	private Label getLable(InputItem item){
		String header=item.getHeader();
		if(item.isMandatory()){
			header+="*";
		}
		return new Label(header+" :");
	}
//**********************************************************************
	
}
