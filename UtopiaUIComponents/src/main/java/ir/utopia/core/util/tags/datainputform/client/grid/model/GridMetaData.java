package ir.utopia.core.util.tags.datainputform.client.grid.model;

import ir.utopia.core.util.tags.datainputform.client.grid.IncludedGrid;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GridMetaData implements IsSerializable {
	
	public GridMetaData(){
		
	}
	public GridMetaData(GridMetaData meta){
		setGridTitle(meta.getGridTitle());
		setAddRecordString(meta.getAddRecordString());
		setRemoveRecordMessage(meta.getRemoveRecordMessage());
		setRemoveRecordString(meta.getRemoveRecordString());
		setAllowAddRecord(meta.isAllowAddRecord());
		setAllowRemoveRecord(meta.isAllowRemoveRecord());
		setAutoExpandColumn(meta.getAutoExpandColumn());
		setSelectAllStr(meta.getSelectAllStr());
		setPrimaryKeyColumn(meta.getPrimaryKeyColumn());
		setUpdateable(meta.isUpdateable());
		setAutofitColumns(meta.isAutofitColumns());
	}
	public GridMetaData(InputItem []columns,String []defaultRowData){
		if(columns==null){
			throw new IllegalArgumentException("columns array cannot be null");
		}if(defaultRowData==null){
			throw new IllegalArgumentException("default row data cannot be null");
		}
		if(columns.length!=defaultRowData.length){
			throw new IllegalArgumentException("columns count and default row dataSize should be eqaul in size ");
		}
		this.columns=columns;
		this.defaultRowData=defaultRowData;
	}
	
	public int getPreferedWidth() {
		return preferedWidth;
	}
	public void setPreferedWidth(int preferedWidth) {
		this.preferedWidth = preferedWidth;
	}
	public int getPreferedHeigth() {
		return preferedHeigth;
	}
	public void setPreferedHeigth(int preferedHeigth) {
		this.preferedHeigth = preferedHeigth;
	}
	private int preferedHeigth=300;
	private int preferedWidth=500;
	private String gridTitle;
	private String addRecordString;
	private String []defaultRowData;
	private String autoExpandColumn;
	private String removeRecordString;
	private String removeRecordMessage;
	private boolean allowRemoveRecord;
	private boolean allowAddRecord;
	private String selectColumnName;
	private String selectAllStr;
	private String primaryKeyColumn;
	private boolean updateable=true;
	private boolean autofitColumns=true;
	private List<ColorLogic>colorLogic;
	public String getPrimaryKeyColumn() {
		return primaryKeyColumn;
	}
	public void setPrimaryKeyColumn(String primaryKeyColumn) {
		this.primaryKeyColumn = primaryKeyColumn;
	}
	public String getSelectAllStr() {
		return selectAllStr;
	}
	public void setSelectAllStr(String selectAllStr) {
		this.selectAllStr = selectAllStr;
	}
	public String getSelectColumnName() {
		return selectColumnName;
	}
	public void setSelectColumnName(String selectColumnName) {
		this.selectColumnName = selectColumnName;
	}
	public boolean isAllowRemoveRecord() {
		return allowRemoveRecord;
	}
	public void setAllowRemoveRecord(boolean allowRemoveRecord) {
		this.allowRemoveRecord = allowRemoveRecord;
	}
	public boolean isAllowAddRecord() {
		return allowAddRecord;
	}
	public void setAllowAddRecord(boolean allowAddRecord) {
		this.allowAddRecord = allowAddRecord;
	}
	public String getRemoveRecordString() {
		return removeRecordString;
	}
	public void setRemoveRecordString(String removeRecordString) {
		this.removeRecordString = removeRecordString;
	}
	public String getRemoveRecordMessage() {
		return removeRecordMessage;
	}
	public void setRemoveRecordMessage(String removeRecordMessage) {
		this.removeRecordMessage = removeRecordMessage;
	}
	public String[] getDefaultRowData() {
		return defaultRowData;
	}
	public void setDefaultRowData(String[] defaultRowData) {
		this.defaultRowData = defaultRowData;
	}
	public String getAddRecordString() {
		return addRecordString;
	}
	public void setAddRecordString(String addRecordString) {
		this.addRecordString = addRecordString;
	}
	public String getGridTitle() {
		return gridTitle;
	}
	public void setGridTitle(String gridTitle) {
		this.gridTitle = gridTitle;
	}
	
	private InputItem []columns;
	
	public InputItem[] getColumns() {
		return getColumns(false);
	}
	
	public InputItem[] getColumns(boolean dataColumns) {
		if(dataColumns&& isAllowRemoveRecord()&&columns[0].getColumnName().startsWith(IncludedGrid.SELECTING_COLUMN_NAME)){
			InputItem []res=new InputItem[columns.length-1];
			System.arraycopy(columns, 1, res, 0, res.length);
			return res;
		}
		return columns;
	}
	
	public InputItem[] getDisplayColumns() {
		ArrayList<InputItem>result=new ArrayList<InputItem>();
		for(InputItem col:columns){
			if(col.isHidden())continue;
			result.add(col);
		}
		return result.toArray(new InputItem[result.size()]);
	}
	public void setColumns(InputItem[] columns) {
		this.columns = columns;
	}
	public int getColumnCount(){
		return columns ==null?0:columns.length;
	}
	public String getAutoExpandColumn() {
		return autoExpandColumn;
	}
	public void setAutoExpandColumn(String autoExpandColumn) {
		this.autoExpandColumn = autoExpandColumn;
	}
	public boolean isUpdateable() {
		return updateable;
	}
	public void setUpdateable(boolean updateable) {
		this.updateable = updateable;
	}
	public boolean isAutofitColumns() {
		return autofitColumns;
	}
	public void setAutofitColumns(boolean autofitColumns) {
		this.autofitColumns = autofitColumns;
	}
	
	public InputItem getColumn(String columnName){
		if(columns!=null){
			for(InputItem item:columns){
				if(item.getColumnName().equals(columnName)){
					return item;
				}
			}
		}
		return null;
	}
	
	public int getDisplayIndex(InputItem item){
		for(int i=0;i<getDisplayColumns().length;i++){
			if(columns[i].equals(item)){
				return i;
			}
		}
		return -1;
	}
	public List<ColorLogic> getColorLogic() {
		return colorLogic;
	}
	public void setColorLogic(List<ColorLogic> colorLogic) {
		this.colorLogic = colorLogic;
	}
	
}
