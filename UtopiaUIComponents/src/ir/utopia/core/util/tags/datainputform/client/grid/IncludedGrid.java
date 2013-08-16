package ir.utopia.core.util.tags.datainputform.client.grid;



import ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants;
import ir.utopia.core.util.tags.datainputform.client.EditorFactory;
import ir.utopia.core.util.tags.datainputform.client.IncludedGridDataHandler;
import ir.utopia.core.util.tags.datainputform.client.InputVerifier;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.WidgetContainer;
import ir.utopia.core.util.tags.datainputform.client.calendar.PersianCalendar;
import ir.utopia.core.util.tags.datainputform.client.calendar.PersianCalendarListener;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridMetaData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridValueModel;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVListenerAdapter;
import ir.utopia.core.util.tags.datainputform.client.lov.LOVWidget;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.searchwidget.model.ContextItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.grid.RowParams;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;


public class IncludedGrid extends SimplePanel implements InputVerifier,WidgetContainer{
	 public static final transient String SELECTING_COLUMN_NAME="__select";  
	 private String mySelecectColName=SELECTING_COLUMN_NAME+Random.nextInt();
	 Panel panel = new Panel(); 
	 Toolbar toolbar = new Toolbar(); 
	 ToolbarButton addRecordButton ;
	 EditorGridPanel grid;
	 RecordDef recordDef;
	 String configurationName;
	 String parentConfigurationName;
	 GridMetaData gridMetaData;
	 ArrayList<Record>removedRecord=new ArrayList<Record>();
	 ToolbarButton removeRecordsButton;
	 String gridColumnName;
	 String direction;
	 ToolbarButton []userDefindedToolBarButtons;
	 Map<Integer,String[][]>hiddenParametersMap=new HashMap<Integer, String[][]>();
	 private IncludedGridDataHandler dataListener;
	 HashMap<String,Widget>columnWidgetMap=new HashMap<String,Widget>();
	 
	 protected HashMap<String,Object>advancedCellMap=new HashMap<String, Object>();
	 DataInputFormConstants constants=(DataInputFormConstants)GWT.create(DataInputFormConstants.class);
	 int invalidRowIndex=-1;
	 GridView gridView;
	 int currentNewId=0;
	 protected WidgetContainer container;
	 protected HashMap<String, Boolean>columnHiddenStatus=new HashMap<String, Boolean>();
	 protected HashMap<String, IncludedGridCellRenderer>columnRendererMap=new HashMap<String, IncludedGridCellRenderer>();
	 protected HashMap<Integer, String>gridRowColorMap=new HashMap<Integer, String>();
//*******************************************************************************
	 public String getGridColumnName() {
		return gridColumnName;
	}
//*******************************************************************************	 
	public void setGridColumnName(String gridColumnName) {
		this.gridColumnName = gridColumnName;
	}
//*******************************************************************************	 
	public IncludedGrid(String gridColumnName,String direction,GridMetaData metadata){
		this(gridColumnName,direction,metadata,new ToolbarButton[0]);	
	}
//*******************************************************************************
	public IncludedGrid(String gridColumnName,String direction,GridMetaData metadata,ToolbarButton ...toolBarButtons){
		this.direction=direction;
		userDefindedToolBarButtons=toolBarButtons;
		setGridColumnName(gridColumnName);
		createGrid(metadata);
		this.add(panel);
	}
//*******************************************************************************
	public IncludedGridDataHandler getDataListener() {
		return dataListener;
	}
//*******************************************************************************
	public void setDataListener(IncludedGridDataHandler dataHandler) {
		this.dataListener = dataHandler;
	}
//*******************************************************************************
	public void setWidgetContainer(WidgetContainer container){
		this.container=container;
	}
//*******************************************************************************
	@Override
	public ContextItem getItem(String itemName) {
		InputItem item= gridMetaData.getColumn(itemName);
		if(item!=null){
			Record selecedRecord= grid.getSelectionModel().getSelected();
			ContextItem contextItem= new ContextItem();
			contextItem.setFieldName(itemName);
			contextItem.setValue(selecedRecord.getAsString(itemName));
			return  contextItem;
		}else if(container!=null){
			return container.getItem(itemName);
		}
		
		return null;
	}
//*******************************************************************************
	private void createGrid(GridMetaData meta){
		ensureDebugId("__includedGrid"+Random.nextInt());
		panel.setBorder(false);  
        panel.setPaddings(0);  
        setGridMetaData(meta);
        BaseColumnConfig[] columnConfigs = getColumnConfiguration();
        ColumnModel columnModel = new ColumnModel(columnConfigs);  
        initColumns(columnModel);
        columnModel.setDefaultSortable(true);  
        grid = new EditorGridPanel();  
        grid.ensureDebugId("grid_"+Random.nextInt());
        grid.setColumnModel(columnModel);  
        grid.setWidth(meta.getPreferedWidth());
        grid.setHeight(meta.getPreferedHeigth()); 
        int columnCount=gridMetaData.getColumnCount();
        String [][]data=new String[0][columnCount];
         recordDef=getRecordDef(gridMetaData);
        ArrayReader reader=new ArrayReader(recordDef);
        Store store=new Store(new MemoryProxy(data),reader);
        store.load();
        grid.setStore(store);
        initToolbarButtons(columnCount);
        grid.setTitle(gridMetaData.getGridTitle());  
        grid.setFrame(true);  
        grid.setClicksToEdit(1);  
        grid.setTopToolbar(toolbar);  
        initCellListeners(grid);
        String autoExpandColumn= gridMetaData.getAutoExpandColumn();
        if(autoExpandColumn!=null){
        	grid.setAutoExpandColumn(autoExpandColumn);
        }
        gridView= new GridView(){//UI rendering
        	  public String getRowClass(Record record, int index, RowParams rowParams, Store store) {
        		  if(invalidRowIndex==index)return "gridRowInvalidData";
        		  if(gridRowColorMap.containsKey(index))return gridRowColorMap.get(index);
        	        return "";
        	    }
        };  
        gridView.setForceFit(meta.isAutofitColumns());
        grid.setView(gridView);
        panel.add(grid);
    	
	}
//************************************************************************************
	public  EditorGridPanel getGrid(){
		return grid;
	}
//************************************************************************************
	private void initColumns(ColumnModel columnModel){
	int index=gridMetaData.isAllowRemoveRecord()?1:0;
		for(InputItem colMeta: gridMetaData.getDisplayColumns()){
				boolean readonly=colMeta.isReadOnly();
				columnModel.setEditable(index, !readonly);
				index++;
		}
	}
//************************************************************************************	
	private void initToolbarButtons(int columnCount) {
		   if(gridMetaData.isAllowRemoveRecord()){
			   	Checkbox selectAll=new Checkbox();
			   	selectAll.addListener(new CheckboxListenerAdapter(){
			   	 public void onCheck(Checkbox field, boolean checked) {
			   		 Store store=grid.getStore();
			   		 Record []records= store.getRecords();
			   		 for(Record record:records){
			   			 record.set(mySelecectColName, checked);
			   		 }
			   		
			   		grid.getView().refresh();
			     }
			   	});
			   	toolbar.addText(gridMetaData.getSelectAllStr());
	        	toolbar.addItem(new ToolbarItem(selectAll.getElement()));
	        	toolbar.addSeparator();
	        	removeRecordsButton=new  ToolbarButton(gridMetaData.getRemoveRecordString(),new IncludedGridRemoveRowListener(grid,selectAll));
	        	toolbar.addButton(removeRecordsButton);
	        	
	        }
	        if(gridMetaData.isAllowAddRecord()){
				String []defaultRow=gridMetaData.getDefaultRowData();
		        defaultRow=defaultRow==null?new String[columnCount]:defaultRow;
		        addRecordButton = new ToolbarButton(gridMetaData.getAddRecordString(), new IncludedGridAddRowListener(defaultRow)  );        
			    toolbar.addButton(addRecordButton);
		    }
	        if(userDefindedToolBarButtons!=null){
	        	for(ToolbarButton button:userDefindedToolBarButtons){
	        		toolbar.addButton(button);
	        	}
	        }
	}

//************************************************************************************
	private void setGridMetaData(GridMetaData meta){
		if(meta.isAllowRemoveRecord()){
			GridMetaData realMetaData=new GridMetaData(meta);
			int columnCount=meta.getColumnCount()+1;
			int visibleColCount=meta.getDisplayColumns().length;
			String []defaultRow=meta.getDefaultRowData();
			if(defaultRow==null)
				defaultRow=new String[visibleColCount];
			 else{
				 String []temp=new String[visibleColCount+1];
				 System.arraycopy(defaultRow, 0, temp, 1, defaultRow.length);
				 temp[0]="";
				 defaultRow=temp;
			}
			realMetaData.setDefaultRowData(defaultRow);
			InputItem []cols=new InputItem[columnCount];
			String seletionName=meta.getSelectColumnName();
			cols[0]=new InputItem(mySelecectColName,seletionName,InputItem.DISPLAY_TYPE_CHECK_BOX,0,5);
			System.arraycopy(meta.getColumns(), 0, cols, 1, columnCount-1);
			realMetaData.setColumns(cols);
			this.gridMetaData=realMetaData;
		}
		else{
			this.gridMetaData=meta;
			}
	}
//************************************************************************************
	public void setEnabled(boolean enabled){
//		grid.setDisabled(!enabled);
		if(gridMetaData!=null){
			for(int i=0;i<gridMetaData.getColumnCount();i++)
				setColumnEditable(i, enabled);
		}
	}
//************************************************************************************
	public void setGridData(GridRowData[]data){
		reset();
		if(data!=null){
			Store store=grid.getStore();
			int rowIndex=-1;
			for(GridRowData  cur:data){
				rowIndex++;
				String []rowData=cur.getData();
				rowData=formatRow(rowIndex,rowData,cur.getDisplayData());
				String[][] hiddenValues=cur.getHiddenParameters();
				 if(hiddenValues!=null){
						 hiddenParametersMap.put(store.getCount(),hiddenValues);
				 }
				 Record record = recordDef.createRecord(rowData);
				 record.setId(String.valueOf(cur.getRowId()));
				 store.insert(store.getCount(), record);
			 }
		}
		
		grid.getView().refresh();
	}
//************************************************************************************
	protected String[] formatRow(int rowIndex,String []input,String []rowDisplay){
		int columnCount=gridMetaData.getDisplayColumns().length;
		int displayOffset=0;
		String []formated;
		if(gridMetaData.isAllowRemoveRecord()&&input.length<columnCount){
			formated=new String[input.length+1];
			formated[0]="";
			System.arraycopy(input, 0, formated, 1, input.length);
			displayOffset=1;
		}else{
			formated=new String[input.length];
			System.arraycopy(input, 0, formated, 0, input.length);
		}
		int colIndex=0;
		
		for(InputItem col: this.gridMetaData.getDisplayColumns()){
			if(col.getDisplayType()==InputItem.DISPLAY_TYPE_COMBOBOX||col.getDisplayType()==InputItem.DISPLAY_TYPE_LIST){
				String key=formated[colIndex];
				int seperatorIndex=((String) formated[colIndex]).indexOf("|");
				if(seperatorIndex>0){
					key=((String) formated[colIndex]).substring(0,seperatorIndex);
					String displayValue=((String) formated[colIndex]).substring(seperatorIndex+1,((String)formated[colIndex]).length());
					formated[colIndex]=displayValue;
					advancedCellMap.put(getCellKey(rowIndex, col.getColumnName()), key);
				}else{
					if(rowDisplay!=null&&colIndex-displayOffset< rowDisplay.length&&rowDisplay[colIndex-displayOffset]!=null&&rowDisplay[colIndex-displayOffset].trim().length()>0){
						formated[colIndex]=rowDisplay[colIndex-displayOffset];
					}else{
						formated[colIndex]=getLookupString(col, formated[colIndex]);
					}
					advancedCellMap.put(getCellKey(rowIndex, col.getColumnName()), key);
				}
			}else if(col.getDisplayType()==InputItem.DISPLAY_TYPE_LOV){
				
				if(rowDisplay!=null&&colIndex-displayOffset< rowDisplay.length){
					advancedCellMap.put(getCellKey(rowIndex, col.getColumnName()), formated[colIndex]);
					formated[colIndex]=rowDisplay[colIndex-displayOffset];
				}else{
					int seperatorIndex=((String) formated[colIndex]).indexOf("|");
					if(seperatorIndex>0){
						String key=((String) formated[colIndex]).substring(0,seperatorIndex);
						String displayValue=((String) formated[colIndex]).substring(seperatorIndex+1,((String)formated[colIndex]).length());
						formated[colIndex]=displayValue;
						advancedCellMap.put(getCellKey(rowIndex, col.getColumnName()), key);
						}
				}
				}
			
			colIndex++;
		}
		return formated;
	}
//************************************************************************************
public void setAddRecordEnable(boolean enabled){
	if(addRecordButton!=null){
		addRecordButton.setDisabled(!enabled);
	}
}
//************************************************************************************
public void setRemoveRecordEnable(boolean enabled){
	if(removeRecordsButton!=null){
		removeRecordsButton.setDisabled(!enabled);
	}
}
//************************************************************************************
public String getHiddenValue(int rowIndex,String hiddenName){
	String[][]hiddens= hiddenParametersMap.get(rowIndex);
	if(hiddens!=null){
		for(String []hidden:hiddens){
			if(hidden[0].equals(hiddenName)){
				return hidden[1];
			}
		}
	}
	return null;
}
//************************************************************************************
public void setHiddenValue(int rowIndex,String hiddenName,String value){
	String[][]hiddens= hiddenParametersMap.get(rowIndex);
	if(hiddens!=null){
		for(String []hidden:hiddens){
			if(hidden[0].equals(hiddenName)){
				hidden[1]=value;
			}
		}
	}else{
		hiddens=new String[][]{{hiddenName,value}};
	}
	hiddenParametersMap.put(rowIndex, hiddens);
}
//************************************************************************************
	public void reset(){
		Store store=grid.getStore();
		grid.stopEditing();
		store.removeAll();
		hiddenParametersMap.clear();
		grid.getView().refresh();
		advancedCellMap.clear();
	}
//************************************************************************************
	public void setColumnEditable(int columnIndex,boolean editable){
		grid.getColumnModel().setEditable(columnIndex, editable);
	}
//************************************************************************************
	private String getLookupString(InputItem col,String key){
		if(key==null||key.trim().length()==0){
			return "";
		}
		for(String[]cur :col.getLookupInfo().getData()){
			if(key.equals(cur[0])){
				return cur[1];
			}
		}
		return key;
	} 
//************************************************************************************
	private String getLookupKey(InputItem col,String name){
		if(name==null||name.trim().length()==0){
			return null;
		}
		
		for(String[]cur :col.getLookupInfo().getData()){
			if(name.equals(cur[1])){
				return cur[0];
			}
		}
		return null;
	}
//************************************************************************************
	private void initCellListeners(final EditorGridPanel grid){
	final IncludedGrid that=this;
    grid.addGridCellListener(new GridCellListenerAdapter() {  
        public void onCellClick(GridPanel currentGrid, int rowIndex, int colIndex, EventObject e) {
        	try{
        		InputItem[] columns=gridMetaData.getDisplayColumns();
        	String columnName=columns[colIndex-1].//colIndex-1 is for row numbering column
					getColumnName();
            if (e.getTarget(".checkbox", 1) != null) {  
                Record record = currentGrid.getStore().getAt(rowIndex);
                record.set(columnName, !record.getAsBoolean(columnName));  
            }  
            int realColIndex=gridMetaData.isAllowRemoveRecord()?colIndex-1:colIndex;
            Object newValue=grid.getStore().getAt(rowIndex).getAsObject(columns[realColIndex].getColumnName());
            if(dataListener!=null) {//grid.getColumnModel().getColumnConfigs()[colIndex]
            	GridRowData row=getGridRowData(rowIndex);
            	dataListener.notifyCellEditing(rowIndex,realColIndex , columnWidgetMap.get(columns[realColIndex].getColumnName()), newValue, row,that,columns[realColIndex]);
            }
            fireNativeEvent(realColIndex, null, newValue, InputItem.TRIGGER_EVENT_TYPE_ON_CLICK);
            }catch(Exception ex){
            	GWT.log(ex.toString());
            	ex.printStackTrace();
            }
        }  
    });  
    grid.addEditorGridListener(new EditorGridListenerAdapter(){
    	 public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue, int rowIndex, int colIndex) {
    		 try {
				int realColIndex=gridMetaData.isAllowRemoveRecord()?colIndex-1:colIndex;
				 fireNativeEvent(realColIndex, oldValue, newValue,InputItem.TRIGGER_EVENT_TYPE_ON_VALUE_CHANGED );
			} catch (Exception e) {
				GWT.log(e.toString());
			}
    	    }
    	    });
    
    }
//***********************************************************************************
	public GridRowData getGridRowData(int rowIndex){
		Store store=grid.getStore();
    	Record record=store.getRecordAt(rowIndex);
    	Set<String> virtualColumns=getVirtualColumnNames();
		GridRowData row=convertRowData(record,virtualColumns, rowIndex);
		return row;
	}
//************************************************************************************
	public HashMap<String,Object> getGridRowContext(int row){
		HashMap<String,Object>extendedValues=new HashMap<String, Object>();
		InputItem[] gridCols= getMetaData().getColumns(true);
		GridRowData rowData=getGridRowData(row);
		for(int i=0;i<gridCols.length;i++){
			String colName= gridCols[i].getColumnName();
			Object cellValue= getValueAt(row, colName);
			extendedValues.put(colName,cellValue);
		}
		String [][]hiddens= rowData.getHiddenParameters();
		if(hiddens!=null){
			for(String []hidden:hiddens){
				extendedValues.put(hidden[0], hidden[1]);
			}
		}
		return extendedValues;
	}
//************************************************************************************
	public void fireNativeEvent(int coumnIndex,Object oldValue,Object newValue,int eventType){
		InputItem[] columns=gridMetaData.getDisplayColumns();
		 Integer[]triggerEvents= columns[coumnIndex].getTriggeringEvents();
		 if(triggerEvents!=null){
			 String []eventHandlers=columns[coumnIndex].getTriggerHandlers();
			 for(int i=0;i< triggerEvents.length;i++){
				 if(eventType==triggerEvents[i]){
					 if(InputItem.TRIGGER_EVENT_TYPE_ON_VALUE_CHANGED==triggerEvents[i]){
						 fireEvent(eventHandlers[i],"valueChanged",oldValue==null?"":oldValue.toString(),newValue==null?"":newValue.toString(),null,null);
					 }else if(InputItem.TRIGGER_EVENT_TYPE_ON_CLICK==triggerEvents[i]){
						 fireEvent(eventHandlers[i],"click",oldValue==null?"":oldValue.toString(),newValue==null?"":newValue.toString(),null,null);
					 }
				 }
			 }
		 }
	}
//************************************************************************************
public  void fireEvent(String handlerName,String eventType,String parameter1,String parameter2,String parameter3,String parameter4){
	try {
		fireEventN(handlerName, eventType, parameter1, parameter2, parameter3, parameter4);
	} catch (Exception e) {
		e.printStackTrace();
		GWT.log(e.toString());
	}
}
//************************************************************************************
public native void fireEventN(String handlerName,String eventType,String parameter1,String parameter2,String parameter3,String parameter4)/*-{
	$wnd[handlerName](eventType,parameter1,parameter2,parameter3,parameter4);
}-*/;
//************************************************************************************
	private BaseColumnConfig[]  getColumnConfiguration(){
		if(gridMetaData==null||gridMetaData.getColumns()==null)return new ColumnConfig[0];
		List<BaseColumnConfig >configs=new ArrayList<BaseColumnConfig>();
		RowNumberingColumnConfig rowNumConf= new RowNumberingColumnConfig();
		configs.add(rowNumConf); 
		int colIndex=0;
		for(InputItem colMeta: gridMetaData.getDisplayColumns()){
				
			 ColumnConfig conf = new ColumnConfig(colMeta.getHeader(), colMeta.getColumnName(), colMeta.isVisible()?colMeta.getLength():0,true);
			 if(!gridMetaData.isAutofitColumns()){
				 if(gridMetaData.isAllowRemoveRecord()&&colIndex==0){
					 conf.setWidth(30);
				 }else if(colMeta.getPreferedWidth()>0){
					 conf.setWidth(colMeta.getPreferedWidth());
				 }
			 }
			switch (colMeta.getDisplayType()) {
			case InputItem.DISPLAY_TYPE_CHECK_BOX:{
				initCheckBoxConfig(colMeta,conf,colIndex);
			}		
			break;
			case InputItem.DISPLAY_TYPE_DATE:{
				initDateColumnConfig(colMeta,conf,colIndex);
			}		
			break;
			case InputItem.DISPLAY_TYPE_CURRENCY:{
				initPriceColumnConfig(colMeta,conf,colIndex);
			}		
			break;
			case InputItem.DISPLAY_TYPE_LIST:
			case InputItem.DISPLAY_TYPE_COMBOBOX:{
				initComboColumnConfig(colMeta,conf,colIndex);
			}		
			break;
			case InputItem.DISPLAY_TYPE_NUMERIC:{
				initNumericfield(colMeta, conf,null,null,colIndex,true);
			}
			break;
			case InputItem.DISPLAY_TYPE_RATIO:{
				initRatioColumnConfig(colMeta, conf,colIndex);
			}
			break;
			case InputItem.DISPLAY_TYPE_LOV:{
				initLOVColumnConfig(colMeta, conf,colIndex);
			}
			break;
			default:{
				initTextConfig(colMeta,conf,colIndex,colMeta.getColumnName());
			}
				break;
			}
			configs.add(conf);
			colIndex++;
		}
	        
		BaseColumnConfig[] columnConfigs =(BaseColumnConfig[])configs.toArray(new BaseColumnConfig[configs.size()]);
            return columnConfigs;
	}
//************************************************************************************	
	private void initCheckBoxConfig(InputItem colMeta,ColumnConfig conf,int colIndex){
		IncludedGridCellRenderer renderer=new IncludedGridCellRenderer() {  
            public String render(Object value, CellMetadata cellMetadata, Record record,  
                                 int rowIndex, int colNum, Store store) {  
            	if(isHidden())return "";
                boolean checked = ((Boolean) value).booleanValue();
                String imageSrc;
                if(GWT.isScript()){
               	 imageSrc=GWT.getModuleBaseURL().replaceAll(GWT.getModuleName()+"/", "")+"/js/resources/images/default/menu/" +  
                    (checked ? "checked.gif" : "unchecked.gif");
                }else{
               	 imageSrc="/js/resources/images/default/menu/" +  
                    (checked ? "checked.gif" : "unchecked.gif");
               	 
                }
                return "<img class=\"checkbox\" src=\"" +imageSrc+ "\"/>";  
            }  
        }; 
		columnRendererMap.put(colMeta.getColumnName(), renderer);
		conf.setRenderer(renderer);
		Checkbox box=new Checkbox();
		columnWidgetMap.put(colMeta.getColumnName(), box);
	}
//************************************************************************************
	private TextField initTextConfig(InputItem colMeta,ColumnConfig conf,int colIndex,String columnName){
		 TextField w=new TextField();
		 if(colMeta.isMandatory()){
        	 w.setBlankText(constants.thisFieldIsManadatory());
         }
		 w.setDisabled(colMeta.isReadOnly()||!gridMetaData.isUpdateable());
		conf.setEditor(new GridEditor(w));  
		columnWidgetMap.put(columnName, w);
		return w;
	}
//************************************************************************************
	private void initDateColumnConfig(InputItem colMeta,ColumnConfig conf,int colIndex){
		if(LocaleInfo.getCurrentLocale().getLocaleName().equalsIgnoreCase("fa")){
			initPersianDateColumn(colMeta, conf, colIndex);
		}else{
			initGerogorianDateColumn(colMeta, conf, colIndex);
		}
		
	}
//************************************************************************************
	private void initGerogorianDateColumn(InputItem colMeta,ColumnConfig conf,int colIndex){
		DateField date=createDateField();
		 date.setDisabled(colMeta.isReadOnly()||!gridMetaData.isUpdateable());
		conf.setEditor(new GridEditor(date)); 
		columnWidgetMap.put(colMeta.getColumnName(), date);
	}
//************************************************************************************
	private void initPersianDateColumn(final InputItem colMeta,ColumnConfig conf,final int colIndex){
		InputItem hiddenDate=new InputItem(colMeta);
		hiddenDate.setColumnName("__"+gridColumnName+"."+colMeta.getColumnName());
		final PersianCalendar cal=new PersianCalendar(hiddenDate);
		columnWidgetMap.put(colMeta.getColumnName(), cal);
		cal.setVisible(false);
		panel.add(cal);
		TextField textField=initTextConfig(colMeta, conf, colIndex,gridColumnName+"."+colMeta.getColumnName()+"_F");
		
		textField.addListener("click", new Function() {
			
			@Override
			public void execute() {
				
			}
		});
		textField.addListener(new FieldListenerAdapter(){
			 public void onFocus(Field field) {
				 cal.showDialog(field.getAbsoluteLeft(),field.getAbsoluteTop()+20);
			    }
		});
		
		 cal.addListener(new PersianCalendarListener() {
			
			@Override
			public void valueChanged(PersianCalendar source,String newValue) {
				 int []cell=grid.getCellSelectionModel().getSelectedCell();
				 grid.getStore().getAt(cell[0]).set(colMeta.getColumnName(), newValue);
				 fireNativeEvent(colIndex, null, newValue,InputItem.TRIGGER_EVENT_TYPE_ON_VALUE_CHANGED );
			}
		});
	}
//************************************************************************************
	private  void initComboColumnConfig(final InputItem colMeta,ColumnConfig conf,int colIndex){
		ComboBox box=createBox(colMeta.getLookupInfo());
		 box.setDisabled(colMeta.isReadOnly()||!gridMetaData.isUpdateable());
         conf.setEditor(new GridEditor(box));  
         columnWidgetMap.put(colMeta.getColumnName(), box);
         box.addListener(new ComboBoxListenerAdapter(){
        	 public void onSelect(ComboBox comboBox, Record record, int index) {
        		 int []cell=grid.getCellSelectionModel().getSelectedCell();
        		 advancedCellMap.put(getCellKey(cell[0],colMeta.getColumnName()),record.getAsString( record.getFields()[0]));
        	    }
         });
	}
//************************************************************************************
private void initPriceColumnConfig(final InputItem colMeta,ColumnConfig conf,int colIndex){
	conf.setAlign(TextAlign.RIGHT);
	IncludedGridCellRenderer renderer=new IncludedGridCellRenderer() {  
        public String render(Object value, CellMetadata cellMetadata, Record record,  
                             int rowIndex, int colNum, Store store) {
       	 if(isHidden())return "";
            return "$" + value;  
        }  
    };
    columnRendererMap.put(colMeta.getColumnName(), renderer);
	conf.setRenderer(renderer);  
	initNumericfield(colMeta, conf,null,null,colIndex,false);
         }
	
//************************************************************************************
private void initRatioColumnConfig(final InputItem colMeta,ColumnConfig conf,int colIndex){
	conf.setAlign(TextAlign.RIGHT);
	IncludedGridCellRenderer renderer=new IncludedGridCellRenderer() {  
        public String render(Object value, CellMetadata cellMetadata, Record record,  
                             int rowIndex, int colNum, Store store) {
       	 if(isHidden())return "";
            return "%" + value;  
        }  
    };
    columnRendererMap.put(colMeta.getColumnName(), renderer);
	conf.setRenderer(renderer);  
	initNumericfield(colMeta, conf,0,100,colIndex,false);
         }
//************************************************************************************
private void initLOVColumnConfig(final InputItem colMeta,final ColumnConfig conf,final int colIndex){
	final LOVWidget widget=new LOVWidget(colMeta);
	columnWidgetMap.put(colMeta.getColumnName(), widget);
	widget.setContainer(this);
	TextField textField=initTextConfig(colMeta, conf, colIndex,colMeta.getColumnName());
	textField.addListener(new FieldListenerAdapter(){
		 public void onFocus(Field field) {
			 widget.showSearchDialog();
		    }
	});
	 widget.addListener(new LOVListenerAdapter() {
		 public void valueChanged(LOVWidget lov, Long key, String value) {
			 int []cell=grid.getCellSelectionModel().getSelectedCell();
			 grid.getStore().getAt(cell[0]).set(colMeta.getColumnName(), value);
			 advancedCellMap.put(getCellKey(cell[0],colMeta.getColumnName()),key );
			 fireNativeEvent(colIndex, null, key,InputItem.TRIGGER_EVENT_TYPE_ON_VALUE_CHANGED );
		 }

		@Override
		public void valueChanged(LOVWidget source, Set<Long> key,
				List<String> value) {
			 int []cell=grid.getCellSelectionModel().getSelectedCell();
			 grid.getStore().getAt(cell[0]).set(colMeta.getColumnName(), value);
			 String keys=source.getValueCommaSeperated();
			 advancedCellMap.put(getCellKey(cell[0],colMeta.getColumnName()),keys );
			 fireNativeEvent(colIndex, null, keys,InputItem.TRIGGER_EVENT_TYPE_ON_VALUE_CHANGED );
			
		}
	 });
         }
//************************************************************************************
protected String getCellKey(int row,String column){
	return row+"|"+column;
}
//************************************************************************************
protected Object getAdvanceCellValue(int rowIndex,String columnName){
	String key=getCellKey(rowIndex,columnName);
	if(advancedCellMap.containsKey(key)){
		return advancedCellMap.get(key);
	}
	return null;
}
//************************************************************************************
private void initNumericfield(InputItem colMeta,ColumnConfig conf,Integer minValue,Integer maxValue,int colIndex,boolean setRenderer){
	NumberField number= createNumberField(colMeta);
	if(maxValue!=null)
		number.setMaxValue(maxValue);
	if(minValue!=null){
		number.setMinValue(minValue);
		number.setAllowNegative(minValue<0);
	}
	number.setDisabled(colMeta.isReadOnly()||!gridMetaData.isUpdateable());
	conf.setEditor(new GridEditor(number));
	if(colMeta.isUseDecimalSeperator()&&setRenderer){
		IncludedGridCellRenderer renderer=new IncludedGridCellRenderer() {  
			NumberFormat formatter =NumberFormat.getDecimalFormat();
	        public String render(Object value, CellMetadata cellMetadata, Record record,  
	                             int rowIndex, int colNum, Store store) {
	       	 if(isHidden())return "";
	       	 if(value !=null&&value.toString().matches("([0-9]|[.])+")){
	       		 String result= formatter.format(Double.parseDouble(value.toString()));
	       		 return EditorFactory.getLocaleNumber(result);
	       	 }
	       	return value==null?"":value.toString();
	        }  
	    };
	    columnRendererMap.put(colMeta.getColumnName(), renderer);
		conf.setRenderer(renderer);  
	}
	
	columnWidgetMap.put(colMeta.getColumnName(), number);
}
//************************************************************************************
@Override
public boolean verify() {
	invalidRowIndex=-1;
	gridView.refresh();
	Store store= grid.getStore();
	for(int i=0;i<store.getCount();i++){
		Record r= store.getAt(i);
		for(InputItem col:gridMetaData.getColumns()){
			String cellValue=r.getAsString(col.getColumnName());
			if(col.getDisplayType()==InputItem.DISPLAY_TYPE_COMBOBOX){
				Object obj=getAdvanceCellValue(i,col.getColumnName());
				cellValue=(obj==null?getLookupKey(col, cellValue):obj.toString());
			}else if(col.getDisplayType()==InputItem.DISPLAY_TYPE_LIST){
				cellValue=getLookupKey(col, cellValue);
			}
			else if(col.getDisplayType()==InputItem.DISPLAY_TYPE_LOV){
				Object obj=getAdvanceCellValue(i,col.getColumnName());
				cellValue=obj==null?"":obj.toString();
			}
			else if(!validateMandatoryField(col, cellValue)||!validateInputData(col, cellValue, i)){
				invalidRowIndex=i;
				gridView.refresh();
				return false;
			}
		}
		
	}
	
	return true;
}
//************************************************************************************
protected boolean validateMandatoryField(InputItem col,String cellValue){
	if(col.isMandatory()){
		if(EditorFactory.valueIsNull(col.getDisplayType(), cellValue)){
			MessageUtility.error(constants.error(),constants.fieldIsMandatory().replace("@p1@", col.getHeader()));
			return false;
		}
	}
	return true;
}
//************************************************************************************
private boolean validateInputData(InputItem item,String value,int row){
	if(value!=null&&value.toString().trim().length()>0){
		if(item.getDisplayType()==InputItem.DISPLAY_TYPE_NUMERIC){
			 return EditorFactory.validateNumeric(constants,item, value);
		}else if(item.getDisplayType()==InputItem.DISPLAY_TYPE_DATE){
			return validateDate(item, value, row,EditorFactory.getDateFormat( columnWidgetMap.get(item.getColumnName())));
		}
	}
	return true;
}
//************************************************************************************
private boolean validateDate(InputItem item,String value,int row,int dateType){
	Object enddate=null;
	if(item.getDateInfo()!=null&& item.getDateInfo().getEndDateItem()!=null){
		try {
			enddate=grid.getStore().getAt(row).getAsObject(item.getDateInfo().getEndDateItem().getColumnName());
		} catch (Exception e) {
			GWT.log("fail to load endDate value from grid");
			e.printStackTrace();
		}
	}
	return EditorFactory.validateDate(item, enddate, constants, value, dateType);
}
//************************************************************************************
	private RecordDef getRecordDef(GridMetaData meta ){
		ArrayList<FieldDef>fields=new ArrayList<FieldDef>();
		for(InputItem colMeta:	meta.getDisplayColumns()){
			switch (colMeta.getDisplayType()) {
			case InputItem.DISPLAY_TYPE_CHECK_BOX:
				fields.add(new BooleanFieldDef(colMeta.getColumnName()));
				break;
			case InputItem.DISPLAY_TYPE_DATE:
				if(LocaleInfo.getCurrentLocale().getLocaleName().equalsIgnoreCase("fa")){
					fields.add(new StringFieldDef(colMeta.getColumnName()));
				}else{
					fields.add(new DateFieldDef(colMeta.getColumnName(),colMeta.getColumnName(),"m/d/Y"));
				}
				break;
			default:{
				fields.add(new StringFieldDef(colMeta.getColumnName()));
			}
				break;
			} 
		}
		 RecordDef recordDef = new RecordDef( (FieldDef[]) fields.toArray(new FieldDef[fields.size()]));
		 return recordDef;
	}
//************************************************************************************
	private ComboBox createBox(UILookupInfo lookupInfo){
		SimpleStore cbStore = new SimpleStore(lookupInfo.getColumns(),lookupInfo.getData());
		  cbStore.load();
		ComboBox cb = new ComboBox();  
        cb.setDisplayField(lookupInfo.getDisplayColumn());  
        cb.setStore(cbStore);
        return cb;
	}
//************************************************************************************
	private NumberField createNumberField(InputItem item){
		 NumberField numberField = new NumberField();
//		UtopiaNumberField numberField=new UtopiaNumberField();
		 numberField.setDecimalPrecision(item.getDecimalPrecision());
         numberField.setAllowBlank(false);  
         numberField.setAllowNegative(false);  
         if(item.isMandatory()){
        	 numberField.setBlankText(constants.thisFieldIsManadatory());
         }
         return numberField;
	}
//************************************************************************************
	private DateField createDateField(){
		 DateField dateField = new DateField();  
         dateField.setFormat("m/d/Y");  
         dateField.setMinValue("01/01/06");  
         dateField.setDisabledDays(new int[]{0, 6});  
         return dateField;
	}
	
//************************************************************************************
	public GridValueModel exportValue(){
		return getGridData(false);
	}
//************************************************************************************
	public GridValueModel getValueModel(){
		return getGridData(true);
	} 
//************************************************************************************
	private Set<String> getVirtualColumnNames(){
		if(hiddenParametersMap.size()==0){
			return null;
		}
		HashSet<String>result=new HashSet<String>();
		for(int rowIndex:hiddenParametersMap.keySet()){
			String [][]rowParams=hiddenParametersMap.get(rowIndex);
			if(rowParams!=null&&rowParams.length>0){
			 L1:	for(int i=0;i<rowParams.length;i++){
						for(InputItem col: gridMetaData.getColumns()){
							if(col.getColumnName().equals(rowParams[i][0])){
								continue L1;
							}
						}
						if(!result.contains(rowParams[i][0]))
							result.add(rowParams[i][0]);
				   }
			}
		}
		return result;
	}
//************************************************************************************	
	private GridValueModel getGridData(boolean modified){
		GridValueModel dataModel=new GridValueModel();
		dataModel.setPrimaryKey(gridMetaData.getPrimaryKeyColumn());
		Map<Integer,Record> records= getRecords(modified);
		
		ArrayList<GridRowData>modifiedRows=new ArrayList<GridRowData>();
		Set<String> virtualColumns=getVirtualColumnNames();
		for(int index:records.keySet()){
			Record record=records.get(index);
			GridRowData row=convertRowData(record,virtualColumns, index);
			modifiedRows.add(row);
		}
		ArrayList<String>columns=new ArrayList<String>();
		InputItem []mcolumns=gridMetaData.getColumns();
		for(int j=0;j<mcolumns.length;j++){
			if(mcolumns[j].getColumnName().equals(mySelecectColName)){
				continue;
			}
			columns.add(mcolumns[j].getColumnName());
		}
		if(virtualColumns!=null)
			columns.addAll(virtualColumns);
		if(modified){ 
			dataModel.setModifiedRows(modifiedRows);
		}else{
			dataModel.setRows(modifiedRows.toArray(new GridRowData[modifiedRows.size()]));
		}
		HashSet<Long>removeds=new HashSet<Long>();
		for(Record record:removedRecord){
			removeds.add(Long.parseLong(record.getId()));
		}
		if(!modified){
			Set<Record> modifieds=getModifiedRecords();
			dataModel.setModified((removedRecord!=null&&removedRecord.size()>0)||(modifieds!=null&&modifieds.size()>0));
		}
		dataModel.setRemovedIds(removeds);
		dataModel.setColumnNames(columns.toArray(new String[columns.size()]));
		return dataModel;
	}
//************************************************************************************
	private GridRowData convertRowData(Record record,Set<String> virtualColumns ,int index){
		GridRowData row=new GridRowData();
		row.setData(convertRecord(record,virtualColumns, index));
		if((record.getId().startsWith("n"))){
			row.setRowId(0l);
		}else{
			row.setRowId(Long.parseLong(record.getId()));
		}
		
		row.setRowIndex(index);
		return row;
	}
//************************************************************************************
	public String[] convertRecord(Record record,Set<String> virtualColumns,int index){
		ArrayList<String>rowData=new ArrayList<String>();
		for(InputItem column:gridMetaData.getColumns()){
			if(column.getColumnName().equals(mySelecectColName)){
				continue;
			}
			String o= record.getAsString(column.getColumnName());
			if(column.getDisplayType()==InputItem.DISPLAY_TYPE_COMBOBOX){
				Object obj=getAdvanceCellValue(index,column.getColumnName());
				o=((obj==null||obj.toString().trim().length()==0)?getLookupKey(column, o):obj.toString());
			}else if(column.getDisplayType()==InputItem.DISPLAY_TYPE_LIST){
				o=getLookupKey(column, o);
			}
			else if(column.getDisplayType()==InputItem.DISPLAY_TYPE_LOV){
				Object obj=getAdvanceCellValue(index,column.getColumnName());
				o=obj==null?"":obj.toString();
			}else if(column.getDisplayType()==InputItem.DISPLAY_TYPE_HIDDEN){
				o=getHiddenValue(index, column.getColumnName());
				o=o==null?"":o.toString();
			}
			rowData.add(o);
		}
		if(virtualColumns!=null){
				for(String vColName:virtualColumns){
					String value= getHiddenValue(index, vColName);
					rowData.add(value==null?"":value);
				}
			}
		return rowData.toArray(new String[rowData.size()]);
	}
//************************************************************************************
	public String[] getRow(int index){
		Record record=grid.getStore().getAt(index);
		Set<String>  virtualColumns=getVirtualColumnNames();
		return convertRecord(record,virtualColumns, index);
	}

//************************************************************************************
	private Map<Integer,Record>getRecords(boolean modified){
		Store store= grid.getStore();
		HashMap<Integer,Record>result=new HashMap<Integer,Record>();
		for(int row=store.getCount()-1;row>=0;row--){
			Record record=store.getRecordAt(row);
			if(!modified)
			{
				result.put(row,record);
				continue;	
			}else{
			String recordIdStr=record.getId();
			if(recordIdStr.startsWith("n")||"0".equals(recordIdStr)){
				record.setId("0");
				result.put(row,record);
			}else{
				if(record.isDirty()){
					result.put(row,record);
				}
			}
			}
		}
		return result;
	}
//************************************************************************************
public String getColumnName(int colIndex){
	InputItem[] colMeta=gridMetaData.getColumns(true);
	
	if(gridMetaData.isAllowRemoveRecord()){
		if(colIndex==0)return SELECTING_COLUMN_NAME;
		colIndex=colIndex-1;
	}
	if(colIndex>colMeta.length)return null;
	return colMeta[colIndex].getColumnName();
}
//************************************************************************************
public int getColumnIndex(String columnName){
	if(gridMetaData.isAllowRemoveRecord()){
		if(SELECTING_COLUMN_NAME.equals(columnName))return 0;
	}
	int colIndex=gridMetaData.isAllowRemoveRecord()?1:0;
	InputItem[] colMetas=gridMetaData.getDisplayColumns();
	for(InputItem item:colMetas){
		if(item.getColumnName().equals(columnName)){
			return colIndex;
		}
		colIndex++;
	}
	return -1;
}
//************************************************************************************
	public void setValueAt(int row,int column,Object value){
		setValueAt(row, getColumnName(column), value);
	}
//************************************************************************************
	public void setValueAt(int row,String column,Object value){
		if(value instanceof Boolean ){
			grid.getStore().getAt(row).set(column,((Boolean) value).booleanValue());
		}else{
			grid.getStore().getAt(row).set(column,value);
		}
		grid.getView().refresh();
	}
//************************************************************************************
	public Object getValueAt(int row,int column){
		return getValueAt(row, getColumnName(column));
	}
//************************************************************************************
	public Object getValueAt(int row,String column){
		return grid.getStore().getAt(row).getAsString(column);
	}
//***********************************************************************************
	public int getColumnCount(){
		return grid.getColumnModel().getColumnCount();
	}
//***********************************************************************************
	public int getRowCount(){
		return grid.getStore().getCount();
	}
//***********************************************************************************
	private Set<Record>getModifiedRecords(){
		return new HashSet<Record>(getRecords(true).values());
	}
//************************************************************************************
	class  IncludedGridAddRowListener extends ButtonListenerAdapter{
		String []defaultRowdata;
		public IncludedGridAddRowListener(String []defaultRowdata){
			this.defaultRowdata=defaultRowdata;
		}
		 public void onClick(Button button, EventObject e) {  
           addRow(defaultRowdata);
         }  
     
	}
//************************************************************************************
public void addRow(String []row){
	 Store store=grid.getStore();
	String []formatedRow=formatRow(store.getCount(),row,null);
	Record record = recordDef.createRecord(formatedRow);
    record.setId("n"+currentNewId++);
    grid.stopEditing(); 
    removeRecordsButton.setDisabled(false);
    store.insert(store.getCount(), record);  
    dataListener.notifyRowAdded(this, store.getCount()-1);
    grid.getView().refresh();
}
//************************************************************************************
public void removeRows(int ...rowIndexes){
	if(rowIndexes!=null&&rowIndexes.length>0){
		Store store=grid.getStore();
		ArrayList<Record>records=new ArrayList<Record>();
		for(int index:rowIndexes){
			records.add(store.getAt(index));
		}
		removeRows(records.toArray(new Record[records.size()]));
	}
}
//************************************************************************************
public void removeRows(Record []records){
	grid.stopEditing();
	Store store=grid.getStore();
	for(Record record:records){
			Long recordId;
			try {
				recordId = Long.parseLong(record.getId());
			} catch (Exception e1) {
				recordId=0l;// if its a new record,the record id is not numeric
			}
			if(recordId>0){
				removedRecord.add(record);
			}
			store.remove(record);
		}
	 removeRecordsButton.setDisabled(store.getCount()==0);
     grid.getView().refresh();
}

//************************************************************************************
	public boolean isSelectedRow(int rowIndex){
		 Store store=grid.getStore();
		 Record []records= store.getRecords();
		 return records[rowIndex].getAsBoolean(mySelecectColName);
	}
//************************************************************************************
	class  IncludedGridRemoveRowListener extends ButtonListenerAdapter{
		EditorGridPanel grid;
		Checkbox selectall;
		public IncludedGridRemoveRowListener(EditorGridPanel grid,Checkbox selectall){
			this.grid=grid;
			this.selectall=selectall;
		}
		 public void onClick(Button button, EventObject e) {  
			 if(gridMetaData.isAllowRemoveRecord()
					 ){
				 boolean hasSelectedRow=false;
				 for(int i=0;i<grid.getStore().getCount();i++){
					 if(isSelectedRow(i)){
						 	hasSelectedRow=true;
						 	break;
					 }
				 }
				 if(hasSelectedRow){
					 MessageBox.confirm(constants.warning(), gridMetaData.getRemoveRecordMessage(),new ConfirmCallback() {
						
						@Override
						public void execute(String btnID) {
							if("yes".equalsIgnoreCase(btnID)){
									Store store=grid.getStore();
									 Record []modified= store.getRecords();
									 ArrayList<Record>removedRecords=new ArrayList<Record>();
									 for(Record record:modified){
											if( record.getAsBoolean(mySelecectColName)){
												removedRecords.add(record);
											}
										 }
									 removeRows(removedRecords.toArray(new Record[removedRecords.size()]));
						             selectall.setValue(false);
									
								}
							}
					} );
					 
		         }  
			 }
			 }
     
	}
//************************************************************************************
	public void setColumnHidden(String columnName,boolean hidden){
		ColumnModel colModel= grid.getColumnModel();
		colModel.setHidden(getColumnIndex(columnName), hidden);
		
		if(hidden){
			columnHiddenStatus.put(columnName, hidden);
			if(!columnRendererMap.containsKey(columnName)){
				IncludedGridCellRenderer renderer=new IncludedGridCellRenderer();
				renderer.setHidden(hidden);
				columnRendererMap.put(columnName,renderer);
				colModel.setRenderer(columnName, renderer);
			}
		}
		if(columnRendererMap.containsKey(columnName)){
			IncludedGridCellRenderer renderer=columnRendererMap.get(columnName);
			renderer.setHidden(hidden);
		}
		grid.getView().refresh();
	}
//************************************************************************************
	public boolean isVisible(String colName){
		
		if(columnHiddenStatus.containsKey( colName)){
			return columnHiddenStatus.get( colName);
		}
		return false;
	}
//************************************************************************************
	public GridMetaData getMetaData(){
		return gridMetaData;
	}
//************************************************************************************
	public void setGridRowCSSClass(int row,String CSSclassName){
		if(row>=0&&CSSclassName!=null)
		gridRowColorMap.put(row, CSSclassName);
	}
//************************************************************************************
	public void removeRowCSSClass(int index){
		gridRowColorMap.remove(index);
	}
//************************************************************************************
	public boolean hasRowCSSClass(int index){
		return gridRowColorMap.containsKey(index);
	}
}
