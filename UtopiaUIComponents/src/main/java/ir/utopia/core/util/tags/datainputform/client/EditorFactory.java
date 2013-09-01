/**
 * 
 */
package ir.utopia.core.util.tags.datainputform.client;

import ir.utopia.core.util.tags.datainputform.client.calendar.PersianCalendar;
import ir.utopia.core.util.tags.datainputform.client.calendar.PersianCalendarListener;
import ir.utopia.core.util.tags.datainputform.client.grid.AbstractSearchGrid.AbstractSearchGridContainer;
import ir.utopia.core.util.tags.datainputform.client.grid.DefaultSearchGrid;
import ir.utopia.core.util.tags.datainputform.client.grid.IncludedGrid;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridValueModel;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVListener;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVWidget;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVWindowEvent;
import ir.utopia.core.util.tags.datainputform.client.model.DateInfo;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.password.PasswordEdior;
import ir.utopia.core.util.tags.datainputform.client.radiobutton.RadioButtonGroup;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.TimeField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;


/**
 * @author Salarkia
 *
 */
public class EditorFactory {
	public static final transient int DATE_TYPE_SOLAR=DateInfo.DATE_TYPE_SOLAR;
	public static final transient int DATE_TYPE_GREGORIAN=DateInfo.DATE_TYPE_GREGORIAN;
	public static final DataInputFormConstants constants=GWT.create(DataInputFormConstants.class);
//*****************************************************************************
	public static Widget createEditor(InputItem item,String direction){
		return createEditor(item, direction, false);
	}
	public static Widget createEditor(InputItem item,String direction,boolean basicComponents){
		Widget result;
		String columnName=item.getColumnName();
		switch (item.getDisplayType()) {
		case InputItem.DISPLAY_TYPE_GRID:
			result= new IncludedGrid(item.getColumnName(),direction,item.getGridMetaData());
			((IncludedGrid)result).setEnabled(!item.isReadOnly());
			break;
		case InputItem.DISPLAY_TYPE_CHECK_BOX:
			 result =new Checkbox("",item.getColumnName());
			 ((Checkbox)result).setDisabled(item.isReadOnly());
			 result.getElement().setAttribute("value", "true");
			 ((Checkbox)result).setValue(true);
			break;
		case InputItem.DISPLAY_TYPE_LIST:
		case InputItem.DISPLAY_TYPE_COMBOBOX:
			result=createBox(item,direction,basicComponents);
			if(!basicComponents)
			columnName=columnName+"_C";
			break;
		case InputItem.DISPLAY_TYPE_CURRENCY:
			if(basicComponents){
				result=  new TextBox();
				((TextBox)result).setStylePrimaryName("clsText");
				((TextBox)result).setName(item.getColumnName());
				((TextBox)result).setEnabled(!item.isReadOnly());
			}else{
				result=new NumberField("",item.getColumnName());
				((NumberField)result).setDisabled(item.isReadOnly());
				((NumberField)result).setDecimalPrecision(item.getDecimalPrecision());
				}
			break;
		case InputItem.DISPLAY_TYPE_DATE:{
			int dateType=item.getDateInfo().getDateType();
				if(dateType==DateInfo.DATE_TYPE_GREGORIAN){
					result= new DateField("",item.getColumnName(),100);
					((DateField)result).setDisabled(item.isReadOnly());
				}
				else{
					result=new PersianCalendar(item);
					((PersianCalendar)result).setEnabled(!item.isReadOnly());
				}
			
			}
			break;
		case InputItem.DISPLAY_TYPE_NUMERIC:
			if(basicComponents){
				result=  new TextBox();
				((TextBox)result).setStylePrimaryName("clsText");
				((TextBox)result).setName(item.getColumnName());
				((TextBox)result).setEnabled(!item.isReadOnly());
			}else{
//				result=new NumberField("",item.getColumnName());
//				((NumberField)result).setCls("clsText");
//				((NumberField)result).setDisabled(item.isReadOnly());
//				((NumberField)result).setDecimalPrecision(item.getDecimalPrecision());
				result=  new NumericEditor(item);
				((TextBox)result).setStylePrimaryName("clsText");
				((TextBox)result).setName(item.getColumnName());
				((TextBox)result).setEnabled(!item.isReadOnly());
				
			}
			break;
		case InputItem.DISPLAY_TYPE_LARGE_STRING:
			result=new TextArea("",item.getColumnName());
			((TextArea)result).setDisabled(item.isReadOnly());
			break;
		case InputItem.DISPLAY_TYPE_RADIO_BUTTON:
			result=new RadioButtonGroup(item.getColumnName(),null,item.getLookupInfo());//note in data input form the 
																						//item header is being written by to the label
			break;
		case InputItem.DISPLAY_TYPE_PASSWORD:
			result=item.isConfirmRequired()? new PasswordEdior(item,direction):new PasswordTextBox();
			break;
		case InputItem.DISPLAY_TYPE_SEARCH_GRID:
			SearchPageModel meta=item.getSearchModel();
			result=new DefaultSearchGrid(meta, "100%", "150px",true,false).getWidget();
			break;
		case InputItem.DISPLAY_TYPE_HIDDEN:
			result=new Hidden(item.getColumnName());
			break;
		case InputItem.DISPLAY_TYPE_LOV:
			result=createLov(item);
			break;	
		case InputItem.DISPLAY_TYPE_FILE:
			result=new FileUpload();
			((FileUpload)result).setName(item.getColumnName());
			break;
		default:
			if(basicComponents){
				result=  new TextBox();
				((TextBox)result).setStylePrimaryName("clsText");
				((TextBox)result).setName(item.getColumnName());
				((TextBox)result).setEnabled(!item.isReadOnly());
			}else{
				result=  new TextField("",item.getColumnName());
				((TextField)result).setCls("clsText");
				((TextField)result).setDisabled(item.isReadOnly());
				((TextField)result).setBlankText(constants.thisFieldIsManadatory());
			}
			break;
		}
		if(result instanceof Field){
//			((Field)result).setHideLabel(true);
		}
		result.setVisible(item.isVisible());
		result.getElement().setDir(direction);
		result.getElement().setPropertyString("name", columnName);
		if(item.getDisplayType() !=	InputItem.DISPLAY_TYPE_DATE &&item.getDisplayType() !=	InputItem.DISPLAY_TYPE_LOV)
			result.getElement().setId(columnName);
		result.setWidth("100%");
		addUserDefindListeners(result, item);
		return result;
	}

//*****************************************************************************
	public static int getDateFormat(Widget widget){
		if(widget instanceof PersianCalendar){
			return DATE_TYPE_SOLAR;
		}else if(widget instanceof DateField){
			return DATE_TYPE_GREGORIAN;
		}
		return -1;
	}
//*****************************************************************************
	public static Widget createLov(InputItem item){
		return new LOVWidget(item);
	}
//*****************************************************************************
	public static Label createLable(InputItem item,String direction,boolean starMandatory){
		String labelStr=item.getHeader();
		if(labelStr==null||labelStr.trim().length()==0)return null;
		if(starMandatory&&item.isMandatory()){
			labelStr+="*";
		}
		labelStr+=":";
		Label label=new Label(labelStr);
		label.setStyleName("clsLabel");
		label.setVisible(item.isVisible());
		
		return label;
	}
//*****************************************************************************	
		private static Widget createBox(InputItem item,String direction,boolean basicComboBox){
		if(basicComboBox){
			ListBox box=new ListBox();
			box.setName(item.getColumnName());
			UILookupInfo lookupInfo=item.getLookupInfo();
			if(lookupInfo !=null){
				String[][]data= lookupInfo.getData();
				if(data!=null){
					box.addItem("---");
					for(int i=0;i<data.length;i++ ){
						box.addItem(data[i][1],data[i][0]);
					}
				}
			}
			box.setEnabled(!item.isReadOnly());
			box.setStylePrimaryName("clsSelect");
			return box;
		}else{
			return createAdvanceComboBox(item,direction);
		}
	}
//*****************************************************************************
		public static void setListBoxValue(ListBox box,UILookupInfo value){
			box.clear();
			box.addItem("---");
			if(value!=null){
				String [][]data=value.getData();
				String selectedId= value.getSelectedId();
				if(data!=null&&data.length>0)
				{
					
					for(int i=0;i<data.length;i++){
						box.addItem(data[i][1], data[i][0]);	
						if(selectedId!=null&&selectedId.equals(data[i][0])){
							box.setSelectedIndex(i);
						}
					}
				}
			}
		}
		
//**********************************************************************
		public static void setLookupValue(Widget box,UILookupInfo value){
			if(box instanceof ComboBox){
				setAdvanceComboValue((ComboBox)box, value);
			}else{
				ListBox b=(ListBox)box;
				b.clear();
				b.addItem("---");
				if(value!=null){
					String [][]data=value.getData();
					if(data!=null&&data.length>0)
					{
						
						for(int i=0;i<data.length;i++){
						b.addItem(data[i][1], data[i][0]);	
						}
					}
				}
			}
		}
//**********************************************************************
		public static void setAdvanceComboValue(ComboBox box,UILookupInfo value){
			Store store= box.getStore();
			store.removeAll();
			if(value!=null){
				String [][]data=value.getData();
				if(data!=null&&data.length>0)
					{
						String []fields= store.getFields();
						int index=0;
						StringFieldDef []defs=new StringFieldDef[fields.length];
						for(String field:fields){
							defs[index]=new StringFieldDef(field);
							index++;
						}
						RecordDef rdef=new RecordDef(defs);
						
						for(int i=0;i<data.length;i++){
							store.add(rdef.createRecord(data[i]));
						}
						if(value.getSelectedId()!=null){
							box.setValue(value.getSelectedId());
						}else{
							if(store.getCount()>0){
								box.setValue(store.getAt(0).getAsString("key"));
								
							}
						}
				}
				}
		}
//*****************************************************************************
		private static Widget createAdvanceComboBox(InputItem item,String direction){
			UILookupInfo lookupInfo=item.getLookupInfo();
			SimpleStore cbStore = new SimpleStore(lookupInfo.getColumns(),lookupInfo.getData());
			cbStore.load();
			ComboBox cb = new ComboBox();  
	        cb.setDisplayField(lookupInfo.getDisplayColumn());  
	        cb.setStore(cbStore);
	        cb.setDisabled(item.isReadOnly());
	        cb.setMode(ComboBox.LOCAL);  
	        cb.setTriggerAction(ComboBox.ALL);  
	        cb.setForceSelection(true);  
	        cb.setValueField(lookupInfo.getKeyColumn());  
	        cb.setForceSelection(true);  
	        cb.setMinChars(1);  
	        cb.setEmptyText(lookupInfo.getEmptyName());  
	        cb.setTypeAhead(true);  
	        cb.setHideTrigger(false); 
	        cb.setName(item.getColumnName()+"_C");
	        cb.setValueField(lookupInfo.getKeyColumn());
	        cb.setDisabled(item.isReadOnly());
	        return cb;
		}
//*****************************************************************************		
	public static void setWidgetValue(Widget widget,Object value){
		if(widget instanceof PersianCalendar){
			String strValue=value==null?"":value.toString().trim();
			((PersianCalendar)widget).setValue(strValue);
		}
		else if(widget instanceof PasswordEdior){
			String strValue=value==null?"":value.toString().trim();
			((PasswordEdior)widget).setValue(strValue);
		}else if(widget instanceof DateField){
			 try {
				 if(value==null){
					 ((DateField)widget).setValue("");
				 }else{
					 DateTimeFormat df = DateTimeFormat.getFormat(DateInfo.GERIGORIAN_DATE_FORMAT);
					 Date myDate=new Date(df.parse(value.toString()).getTime());
					 ((DateField)widget).setValue(DateTimeFormat.getFormat("yy/MM/dd").format(myDate));
				 }
			} catch (Exception e) {
			}
		}
		else if(widget instanceof TextField){
			 String strValue=value==null?"":value.toString().trim();
			((TextField)widget).setValue(strValue);
			
		}else if(widget instanceof IncludedGrid){
			if(value!=null){
				GridValueModel model=(GridValueModel)value;
				GridRowData []data=model.getRows();
				((IncludedGrid)widget).setGridData(data);
			}
		}else if(widget instanceof AbstractSearchGridContainer){
			if(value!=null){
				SearchPageData model=(SearchPageData)value;
				((AbstractSearchGridContainer)widget).getGrid().setData(0, SearchPageData.DEFAULT_PAGE_SIZE, model);
			}
		}else if(widget instanceof Checkbox){
			boolean selected=value!=null&&"true".equalsIgnoreCase(value.toString());
			((Checkbox)widget).setChecked(selected);
		}else if(widget instanceof PasswordTextBox){
			 String strValue=value==null?"":value.toString().trim();
				((PasswordTextBox)widget).setText(strValue);
		}else if(widget instanceof RadioButtonGroup){
			 String strValue=value==null?"":value.toString().trim();
				((RadioButtonGroup)widget).setValue(strValue);
		}else if(widget instanceof Hidden){
			 String strValue=value==null?"":value.toString().trim();
			((Hidden)widget).setValue(strValue);
		}
		else if(widget instanceof ListBox){
			ListBox box=((ListBox)widget);
			if(value==null){
				if(box.getItemCount()>0)
					box.setSelectedIndex(0);
				return ;
			}
			int itemCount=box.getItemCount();
			for(int i=0;i<itemCount;i++){
				if(value.equals(box.getValue(i))){
						box.setSelectedIndex(i);
					return ;
				}
			}
		}else if(widget instanceof LOVWidget){
			 	((LOVWidget)widget).setValue(value);
		}else if(widget instanceof TextArea){
			((TextArea)widget).setValue(value==null?"":(String)value);
		}else if(widget instanceof com.google.gwt.user.client.ui.TextArea){
			((com.google.gwt.user.client.ui.TextArea)widget).setValue(value==null?"":(String)value);
		}else if(widget instanceof TextBox){
			((TextBox)widget).setValue(value==null?"":(String)value);
		}
	}
//*****************************************************************************
	public static void setWidgetEnabled(Widget w,boolean enabled){
	if(w instanceof FocusWidget){
		((FocusWidget)w).setEnabled(enabled);
	}else if(w instanceof Field){
		((Field)w).setDisabled(!enabled);
	}
	else if(w instanceof PersianCalendar){
		((PersianCalendar)w).setEnabled(enabled);
	}else if (w instanceof LOVWidget){
		((LOVWidget)w).setEnabled(enabled);
	}
	}
//*****************************************************************************
	public static boolean isWidgetEnabled(Widget w){
		if(w instanceof FocusWidget){
			return ((FocusWidget)w).isEnabled();
		}else if(w instanceof Field){
			return !((Field)w).isDisabled();
		}
		if(w instanceof PersianCalendar){
			return ((PersianCalendar)w).isEnabled();
		}
		return true;
	}
//*****************************************************************************
	public static boolean valueIsNull(int displayType,Object value){
		if(value==null||value.toString().trim().length()==0){
			return true;
		}
		if(displayType==InputItem.DISPLAY_TYPE_COMBOBOX||displayType==InputItem.DISPLAY_TYPE_LIST){
			try {
				long lvalue= Long.parseLong(value.toString());
				return lvalue<0;
			} catch (NumberFormatException e) {
				GWT.log("", e);
				return true;
			}
		}else if(displayType==InputItem.DISPLAY_TYPE_STRING||displayType==InputItem.DISPLAY_TYPE_NUMERIC||
				displayType==InputItem.DISPLAY_TYPE_LARGE_STRING||displayType==InputItem.DISPLAY_TYPE_PASSWORD||displayType==InputItem.DISPLAY_TYPE_DATE){
			return value==null||value.toString().trim().length()==0;
		}
		return false;
	}
//*****************************************************************************	
	public static boolean validateNumeric(DataInputFormConstants constants, InputItem item,Object value){
		 try {
			 boolean validated;
			 if((value==null ||value.toString().length()==0) &&!item.isMandatory()){
				 return true;
			 }
			 if(item.getDecimalPrecision()>0){
			 		Double d=Double.parseDouble(value.toString().trim());
			 		validated= d.longValue()<item.getMaxValue()&&d.longValue()>item.getMinValue();
			 	}else{
			 		long lvalue=  Long.parseLong(value.toString().trim());
					validated= lvalue<=item.getMaxValue()&&lvalue>=item.getMinValue();	
			 	}
				
				if(!validated){
					MessageUtility.error(constants.error(),constants.dataBetweenError().replace("@p1@", item.getHeader()).
							replace("@p2@", String.valueOf(item.getMinValue()+1)).
							replace("@p3@",String.valueOf(item.getMaxValue()-1)));
				}
				return validated;
			} catch (NumberFormatException e) {
				MessageUtility.error(constants.error(),constants.invalidData().replace("@p1@", item.getHeader()));
				return false;
			}
	}
//***********************************************************************************
	public static boolean validateDate(InputItem item,Object enddate,DataInputFormConstants constants,Object value,int dateType){
		boolean validated=true;
		boolean solar=false;
		
		if(dateType==DateInfo.DATE_TYPE_SOLAR){
			solar=true;
			validated= ValidationFunctions.validateSolarDate(item.getHeader(), (String)value,constants);
		}else if(dateType==EditorFactory.DATE_TYPE_GREGORIAN){
			validated= ValidationFunctions.validateGregorianDate(item.getHeader(),(Date)value,constants);
		}
		if(validated&&item.getDateInfo().getEndDateItem()!=null){
			
			if(enddate!=null){
				int d1[];
				int d2[];
				if(solar){
					d1=ValidationFunctions.getDateFromSolar((String)value);
					d2=ValidationFunctions.getDateFromSolar((String)enddate);
				}else{
					d1=ValidationFunctions.getDate((Date)value);
					d2=ValidationFunctions.getDate((Date)enddate);
				}
				if(d1!=null&&d2!=null)
					return ValidationFunctions.validateStartEndDate(item.getHeader(),item.getDateInfo().getEndDateItem().getHeader(), 
							d1[0], d1[1], d1[2], d2[0], d2[1], d2[2],constants);
			}
		}
		return validated;
	}
//*****************************************************************************	
	public static Object getWidgetValue(Widget widget){
		if(widget instanceof PersianCalendar){
			return ((PersianCalendar)widget).getValue();
		}
		else if(widget instanceof PasswordEdior){
			return ((PasswordEdior)widget).getPassword();
		}if(widget instanceof ComboBox){
			return ((ComboBox)widget).getValue();
		}
		else if(widget instanceof IncludedGrid){
			return ((IncludedGrid)widget).getValueModel();
		}else if(widget instanceof Checkbox){
			return ((Checkbox)widget).getValue();
		}else if(widget instanceof PasswordTextBox){
			return	((PasswordTextBox)widget).getText();
		}else if(widget instanceof DateField){
			return ((DateField)widget).getValue();
		}
		else if(widget instanceof NumberField){
			return revertRightToLeft(((NumberField)widget).getValueAsString());
		}
		else if(widget instanceof TextField){
			return ((TextField)widget).getValueAsString();
		}else if(widget instanceof ListBox){
			int selectedItem=((ListBox)widget).getSelectedIndex();
			if(selectedItem>=0)
			return ((ListBox)widget).getValue(selectedItem);
			return -1;
		}else if(widget instanceof TextBox){
			return ((TextBox)widget).getValue();
		}else if(widget instanceof com.google.gwt.user.client.ui.TextArea){
			return ((com.google.gwt.user.client.ui.TextArea)widget).getText();
		}else if(widget instanceof TimeField){
			return ((TimeField)widget).getText();
		}else if(widget instanceof LOVWidget){
			return ((LOVWidget)widget).getValue();
		}else if(widget instanceof Hidden){
			return ((Hidden)widget).getValue();
		}else if(widget instanceof FileUpload){
			return ((FileUpload)widget).getFilename();
		}
		return null;
	}
//*****************************************************************************
	public static String getWidgetName(Widget widget){
		if(widget instanceof PersianCalendar){
			return ((PersianCalendar)widget).getInputItemName();
		}
		else if(widget instanceof IncludedGrid){
			return ((IncludedGrid)widget).getGridColumnName();
		}else if(widget instanceof Checkbox){
			return ((Checkbox)widget).getName();
		}else if(widget instanceof PasswordTextBox){
			return	((PasswordTextBox)widget).getName();
		}else if(widget instanceof ComboBox){
			String name=((ComboBox)widget).getName();
				return name.substring(0,name.length()-2);
		}else if(widget instanceof TextField){
				return ((TextField)widget).getName();
			}
		else if(widget instanceof ListBox){
			return ((ListBox)widget).getName();
		}else if(widget instanceof PasswordEdior){
			return ((PasswordEdior)widget).getName();
		}else if(widget instanceof TextBox){
			return ((TextBox)widget).getName();
		}else if(widget instanceof LOVWidget){
			return ((LOVWidget)widget).getName();
		}
		
		return null;
	}
//*****************************************************************************
	@SuppressWarnings("rawtypes")
	public static void addUserDefindListeners(Widget widget,InputItem item){
		if(widget!=null&&item!=null&&item.getTriggeringEvents()!=null&&item.getTriggeringEvents().length>0){
			Integer[] trigerEvents= item.getTriggeringEvents();
			String [] methodName=item.getTriggerHandlers();
			for(int i=0;i<trigerEvents.length;i++){
				int event=trigerEvents[i];
				String handlerMethod=methodName[i]; 
				EventAdapter eventAdapter=new EventAdapter(handlerMethod,event);
				switch (event) {
				case InputItem.TRIGGER_EVENT_TYPE_ON_CLICK:
					if(widget instanceof Field){
						((Field)widget).addListener(eventAdapter);
					}else{
						widget.addHandler(eventAdapter,MouseUpEvent.getType());
					}
					break;
				case InputItem.TRIGGER_EVENT_TYPE_ON_KEY_RELEASED:
					if(widget instanceof TextField){
						((TextField)widget).addKeyPressListener(eventAdapter);
					}else{
						widget.addHandler(eventAdapter,KeyUpEvent.getType());
					}
					break;	
				case InputItem.TRIGGER_EVENT_TYPE_ON_MOUSE_OVER:
					if(widget instanceof Field){
						((Field)widget).addListener(eventAdapter);//TODO does not work
					}else{
						widget.addHandler(eventAdapter,MouseOverEvent.getType());
					}
					break;
				case InputItem.TRIGGER_EVENT_TYPE_ON_VALUE_CHANGED:
					if(widget instanceof Field){
						((Field)widget).addListener(eventAdapter);
					}else if(widget instanceof LOVWidget){
						((LOVWidget)widget).addListener(eventAdapter);
					}else if(widget instanceof PersianCalendar){
						((PersianCalendar)widget).addListener(eventAdapter);
					}else if(widget instanceof Hidden){
						//do nothing
					}
					else{
						((ValueBoxBase)widget).addChangeHandler(eventAdapter);
					}
					break;
				default:
					GWT.log("unknown event hadler found :["+event+"]");
					break;
				}
			}
		}
	}
//*****************************************************************************
	private static class EventAdapter extends FieldListenerAdapter implements MouseUpHandler,MouseOverHandler,KeyUpHandler,ChangeHandler,EventCallback,LOVListener,PersianCalendarListener{
		NativeEventHandler eventHandler;
		int eventType;
		public EventAdapter(String handlerMethod,int eventType){
			 eventHandler=new NativeEventHandler(handlerMethod);
			 this.eventType=eventType;
		}
		
		public void onFocus(Field field) {
			if(InputItem.TRIGGER_EVENT_TYPE_ON_CLICK==eventType)
				eventHandler.fireEvent("click",null,null,null,null);
	    }
		
		public void onChange(Field field,Object newVal, Object oldVal){
			if(InputItem.TRIGGER_EVENT_TYPE_ON_VALUE_CHANGED==eventType)
				try {
					eventHandler.fireEvent("valueChanged",String.valueOf(oldVal),String.valueOf(newVal),null,null);
				} catch (Exception e) {
					GWT.log(e.toString());
					e.printStackTrace();
				}
		}

		@Override
		public void onMouseUp(MouseUpEvent event) {
			eventHandler.fireEvent("click",null,null,null,null);			
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			eventHandler.fireEvent("hover",null,null,null,null);	
			
		}
		
		

		@Override
		public void onKeyUp(KeyUpEvent event) {
			eventHandler.fireEvent("keyRealsed",String.valueOf(event.getNativeKeyCode()),null,null,null);
			
		}

		@Override
		public void onChange(ChangeEvent event) {
			eventHandler.fireEvent("valueChanged",null,String.valueOf(EditorFactory.getWidgetValue((Widget) event.getSource())),null,null);
		}

		@Override
		public void execute(EventObject e) {
			if(InputItem.TRIGGER_EVENT_TYPE_ON_KEY_RELEASED==eventType)
				eventHandler.fireEvent("keyRealsed",String.valueOf(e.getKey()),null,null,null);	
			
		}

		@Override
		public void valueChanged(LOVWidget source, Long key, String value) {
			eventHandler.fireEvent("valueChanged", key+"", value, null, null);
			
		}

		@Override
		public void valueChanged(LOVWidget source, Set<Long> keys,
				List<String> values) {
			StringBuffer keysStr=new StringBuffer();
			StringBuffer valuesStr=new StringBuffer();
			if(keys!=null){
				int index=0;
				for(Long key:keys){
					if(index>0){
						keysStr.append(",");
					}
					keysStr.append(key);
					index++;
				}
			}
			if(values!=null){
				int index=0;
				for(String value:values){
					if(index>0){
						valuesStr.append(",");
					}
					valuesStr.append(value);
					index++;
				}
			}
			
			eventHandler.fireEvent("valueChanged", keysStr.toString(), valuesStr.toString(), null, null);
			
		}

		@Override
		public void valueChanged(PersianCalendar source, String newValue) {
			eventHandler.fireEvent("valueChanged", newValue, null, null, null);
			
		}

		@Override
		public void LovWindowStatusChanged(LOVWindowEvent event) {
			
		}
	}
//*****************************************************************************	
	public static void addListener(Widget widget,GlobalEditorDataChangeListener listener ){
		if(widget instanceof ComboBox){
			((ComboBox)widget).addListener(listener);
		}
		else if(widget instanceof ListBox){
			((ListBox)widget).addChangeListener(listener);
		}else if(widget instanceof TextBox){
			((TextBox)widget).addKeyUpHandler(listener);
		}else if(widget instanceof com.google.gwt.user.client.ui.TextArea){
			((com.google.gwt.user.client.ui.TextArea)widget).addKeyUpHandler(listener);
		}else if(widget instanceof RichTextArea){
			((RichTextArea)widget).addKeyUpHandler(listener);
		}else if (widget instanceof TextField){
			((TextField)widget).addKeyPressListener(listener);
		}else if(widget instanceof TextArea){
			((TextArea)widget).addKeyPressListener(listener);
		}else if(widget instanceof Checkbox ){
			((Checkbox)widget).addListener(listener);
		}else if(widget instanceof RadioButtonGroup){
			((RadioButtonGroup)widget).addListener(listener);
		}else if(widget instanceof LOVWidget){
			((LOVWidget)widget).addListener(listener);
		}else if(widget instanceof PersianCalendar){
			((PersianCalendar)widget).addListener(listener);
		}
	}
//*****************************************************************************	
	public static boolean isVisible(Widget widget){
		if(widget instanceof Component){
			return  !((Component)widget).isHidden();
		}else{
			return widget.isVisible();
		}
	}
//********************************************************************************************************
	public static String getLocaleNumber(String number,String language){
		if(number==null||number.trim().length()==0)return "";
		if("fa".equals(language)||"ar".equals(language)){
			char [] input=number.toCharArray();
			StringBuffer result=number.startsWith("-")?//if negative number
					new StringBuffer("\u200E"):new StringBuffer();
			for(int i=0;i<input.length;i++){
				char c=input[i];
				if(Character.isDigit(c)){
						result.append(toRigthToLeftNumber(Integer.parseInt(c+"")));
				}else{
					result.append(c);
				}
			}
			return result.toString().trim();
		}
		return number;
	}
//********************************************************************************************************
	public static String getLocaleNumber(Object number){
		if(number==null)return "";
		return getLocaleNumber(number.toString(), LocaleInfo.getCurrentLocale().getLocaleName());
	}
//********************************************************************************************************
	public static String toRigthToLeftNumber(int c){
		switch (c) {
		case 0:return "\u06f0";
		case 1:return "\u06f1";
		case 2:return "\u06f2";
		case 3:return "\u06f3";
		case 4:return "\u06f4";
		case 5:return "\u06f5";
		case 6:return "\u06f6";
		case 7:return "\u06f7";
		case 8:return "\u06f8";
		case 9:return "\u06f9";
		}
		return "" ;
	} 
//********************************************************************************************************
	public static String revertRightToLeft(String c){
		if(c==null||c.length()<"\u06f0".length())return "";
		
		StringBuffer result=new StringBuffer();
		char [] cc= c.toCharArray();
		for(char ccc:cc){
			result.append(doRevertRightToLeft(ccc));
		}
		return result.toString();
	}
//********************************************************************************************************
		private static char doRevertRightToLeft(char c){
			
		if('\u06f0'==c){
			return '0';
		}else if('\u06f1'==c){
			return '1';
		}else if('\u06f2'==c){
			return '2';
		}else if('\u06f3'==c){
			return '3';
		}else if('\u06f4'==c){
			return '4';
		}else if('\u06f5'==c){
			return '5';
		}else if('\u06f6'==c){
			return '6';
		}else if('\u06f7'==c){
			return '7';
		}else if('\u06f8'==c){
			return '8';
		}else if('\u06f9'==c){
			return '9';
		}
		return c;
		}
}
