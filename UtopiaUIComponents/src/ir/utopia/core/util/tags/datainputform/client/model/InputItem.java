package ir.utopia.core.util.tags.datainputform.client.model;

import ir.utopia.core.util.tags.datainputform.client.grid.model.GridMetaData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InputItem implements IsSerializable {
	public static final transient int TRIGGER_EVENT_TYPE_ON_CLICK=1;
	public static final transient int TRIGGER_EVENT_TYPE_ON_MOUSE_OVER=2;
	public static final transient int TRIGGER_EVENT_TYPE_ON_VALUE_CHANGED=3;
	public static final transient int TRIGGER_EVENT_TYPE_ON_KEY_RELEASED=4;
	public static final transient int DISPLAY_TYPE_CHECK_BOX=1;
	public static final transient int DISPLAY_TYPE_STRING=2;
	public static final transient int DISPLAY_TYPE_COMBOBOX=3;
	public static final transient int DISPLAY_TYPE_DATE=4;
	public static final transient int DISPLAY_TYPE_CURRENCY=5;
	public static final transient int DISPLAY_TYPE_NUMERIC=6;
	public static final transient int DISPLAY_TYPE_PASSWORD=7;
	public static final transient int DISPLAY_TYPE_LOV=8;
	public static final transient int DISPLAY_TYPE_RATIO=9;
	public static final transient int DISPLAY_TYPE_RADIO_BUTTON=10;
	public static final transient int DISPLAY_TYPE_HIDDEN=11;
	public static final transient int DISPLAY_TYPE_LIST=12;
	public static final transient int DISPLAY_TYPE_FILE=13;
	
	public static final transient int DISPLAY_TYPE_GRID=20;
	public static final transient int DISPLAY_TYPE_LARGE_STRING=21;
	public static final transient int DISPLAY_TYPE_SEARCH_GRID=22;
	
	public static final transient int DEPENDENCY_TYPE_DATA_FILTER=100;
	public static final transient int DEPENDENCY_TYPE_DATA_READ_ONLY=101;
	public static final transient int DEPENDENCY_TYPE_DATA_DISPLAY=102;
	public static final transient int DEPENDENCY_TYPE_COLOR_LOGIC=103;
	private int displayType;
	private int index;
	private String header;
	private boolean breakLineAfer;
	private boolean mandatory;
	private GridMetaData gridMetaData;
	private boolean readOnly=false;
	private int decimalPrecision=0; 
	private long minValue=0l;
	private long maxValue=Long.MAX_VALUE;
	private String[] readOnlyDepenedents;
	private String[] displayDepenedents;
	private String[] filterDepenedents;
	private String [] colorDependents;
	private boolean visible=true;
	protected String columnName;
	protected int length;
	private UILookupInfo lookupInfo;
	boolean confirmRequired=false;
	private SearchPageModel searchModel;
	Integer []triggeringEvents;
	String []triggerHandlers;
	UILOVConfiguration LOVConfiguration;
	private boolean parentLinkItem;
	private int preferedWidth;
	private String readOnlyLogic,displayLogic;
	private DateInfo dateInfo;
	private boolean useDecimalSeperator;
	private String decimalSeperator;
	public InputItem(){
		
	}
	public InputItem(InputItem item){
		setBreakLineAfer(item.isBreakLineAfer());
		setColumnName(item.getColumnName());
		setDecimalPrecision(item.getDecimalPrecision());
		setDisplayType(item.getDisplayType());
		setHeader(item.getHeader());
		setIndex(item.getIndex());
		setLength(item.getLength());
		setLookupInfo(item.getLookupInfo());
		setMandatory(item.isMandatory());
		setMaxValue(item.getMaxValue());
		setMinValue(item.getMinValue());
		setReadOnly(item.isReadOnly());
		setVisible(item.isVisible());
		setReadOnlyDepenedents(item.getReadOnlyDepenedents());
		setDisplayDepenedents(item.getDisplayDepenedents());
		setFilterDepenedents(item.getFilterDepenedents());
		setLOVConfiguration(item.getLOVConfiguration());
		setTriggeringEvents(item.getTriggeringEvents());
		setTriggerHandlers(item.getTriggerHandlers());
		setPreferedWidth(item.getPreferedWidth());
		setReadOnlyLogic(item.getReadOnlyLogic());
		setDisplayLogic(item.getDisplayLogic());
		setDateInfo(item.getDateInfo());
		setDecimalSeperator(item.getDecimalSeperator());
		setUseDecimalSeperator(item.isUseDecimalSeperator());
	}
	public InputItem(String columnName, int displayType, int index) {
		super();
		this.columnName = columnName;
		setHeader(columnName);
		setDisplayType(displayType);
		setIndex(index);
	}
	
	public InputItem(String columnName, String header,
			int displayType, int index, int length) {
		super();
		this.columnName = columnName;
		setHeader(header);
		setDisplayType(displayType);
		setIndex(index);
		this.length = length;
	}
	public Integer[] getTriggeringEvents() {
		return triggeringEvents;
	}

	public void setTriggeringEvents(Integer[] triggeringEvents) {
		this.triggeringEvents = triggeringEvents;
	}

	public String[] getTriggerHandlers() {
		return triggerHandlers;
	}

	public void setTriggerHandlers(String[] triggerHandlers) {
		this.triggerHandlers = triggerHandlers;
	}

	public int getDecimalPrecision() {
		return decimalPrecision;
	}

	public void setDecimalPrecision(int decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * 
	 */
	
	public boolean isBreakLineAfer() {
		return breakLineAfer;
	}

	public void setBreakLineAfer(boolean breakLineAfer) {
		this.breakLineAfer = breakLineAfer;
	}

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public UILookupInfo getLookupInfo() {
		return lookupInfo;
	}

	public void setLookupInfo(UILookupInfo lookupInfo) {
		this.lookupInfo = lookupInfo;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	
	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public GridMetaData getGridMetaData() {
		return gridMetaData;
	}

	public void setGridMetaData(GridMetaData gridMetaData) {
		this.gridMetaData = gridMetaData;
	}

	public long getMinValue() {
		return minValue;
	}

	public void setMinValue(long minValue) {
		this.minValue = minValue;
	}

	public long getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(long maxValue) {
		this.maxValue = maxValue;
	}
//**************************************************************************************
	public void mapDependency(int type, String ...dependents){
		switch (type) {
		case DEPENDENCY_TYPE_DATA_DISPLAY:
			displayDepenedents=mapDependency(displayDepenedents, dependents);
			break;
		case DEPENDENCY_TYPE_DATA_FILTER:
			filterDepenedents=mapDependency(filterDepenedents, dependents);
				break;
		case DEPENDENCY_TYPE_DATA_READ_ONLY:
			readOnlyDepenedents=mapDependency(readOnlyDepenedents, dependents);
			break;
		case DEPENDENCY_TYPE_COLOR_LOGIC:
			colorDependents=mapDependency(colorDependents, dependents);
		break;
		}
	}
//**************************************************************************************	
	private String[] mapDependency(String []currentDeps,String ...dependents){
		if(dependents==null||dependents.length==0)return null;
		if(currentDeps==null){
			currentDeps=dependents;
		}else{
			String []temp=new String[currentDeps.length+dependents.length];
			System.arraycopy(currentDeps, 0, temp,0, currentDeps.length);
			System.arraycopy(dependents, 0, temp,currentDeps.length, dependents.length);
			currentDeps=temp;
		}
		return currentDeps;
	}
//**************************************************************************************
	public String[] getReadOnlyDepenedents() {
		return readOnlyDepenedents;
	}
//**************************************************************************************
	public String[] getDisplayDepenedents() {
		return displayDepenedents;
	}
//**************************************************************************************
	public String[] getFilterDepenedents() {
		return filterDepenedents;
	}
//**************************************************************************************
	public boolean isConfirmRequired() {
		return confirmRequired;
	}
//**************************************************************************************
	public void setConfirmRequired(boolean confirmRequired) {
		this.confirmRequired = confirmRequired;
	}
//**************************************************************************************
	public SearchPageModel getSearchModel() {
		return searchModel;
	}
//**************************************************************************************
	public void setSearchModel(SearchPageModel searchModel) {
		this.searchModel = searchModel;
	}
//**************************************************************************************
	public boolean isHidden(){
		return DISPLAY_TYPE_HIDDEN==displayType;
	}
//**************************************************************************************
	protected void setReadOnlyDepenedents(String[] readOnlyDepenedents) {
		this.readOnlyDepenedents = readOnlyDepenedents;
	}
//**************************************************************************************
	protected void setDisplayDepenedents(String[] displayDepenedents) {
		this.displayDepenedents = displayDepenedents;
	}
//**************************************************************************************
	protected void setFilterDepenedents(String[] filterDepenedents) {
		this.filterDepenedents = filterDepenedents;
	}
//**************************************************************************************
	public UILOVConfiguration getLOVConfiguration() {
		return LOVConfiguration;
	}
//**************************************************************************************
	public void setLOVConfiguration(UILOVConfiguration lOVConfiguration) {
		this.LOVConfiguration = lOVConfiguration;
	}
//**************************************************************************************
	public boolean isParentLinkItem() {
		return parentLinkItem;
	}
//**************************************************************************************
	public void setParentLinkItem(boolean parentLinkItem) {
		this.parentLinkItem = parentLinkItem;
	}
//**************************************************************************************
	public int getPreferedWidth() {
		return preferedWidth;
	}
//**************************************************************************************
	public void setPreferedWidth(int preferedWidth) {
		this.preferedWidth = preferedWidth;
	}
//**************************************************************************************
	public String getReadOnlyLogic() {
		return readOnlyLogic;
	}
//**************************************************************************************
	public void setReadOnlyLogic(String readOnlyLogic) {
		this.readOnlyLogic = readOnlyLogic;
	}
//**************************************************************************************
	public String getDisplayLogic() {
		return displayLogic;
	}
//**************************************************************************************
	public void setDisplayLogic(String displayLogic) {
		this.displayLogic = displayLogic;
	}
//**************************************************************************************
	public DateInfo getDateInfo() {
		return dateInfo;
	}
//**************************************************************************************
	public void setDateInfo(DateInfo dateInfo) {
		this.dateInfo = dateInfo;
	}
//**************************************************************************************
	public boolean isUseDecimalSeperator() {
		return useDecimalSeperator;
	}
//**************************************************************************************
	public void setUseDecimalSeperator(boolean useDecimalSeperator) {
		this.useDecimalSeperator = useDecimalSeperator;
	}
//**************************************************************************************
	public String getDecimalSeperator() {
		return decimalSeperator;
	}
//**************************************************************************************
	public void setDecimalSeperator(String decimalSeperator) {
		this.decimalSeperator = decimalSeperator;
	}
//**************************************************************************************
	public String[] getColorDependents(){
		return colorDependents;
	}
}
