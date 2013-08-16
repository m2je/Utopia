package ir.utopia.core.util.tags.datainputform.client.grid.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GridRowData implements IsSerializable {
	
	
	protected long rowId;
	protected String []data;
	protected int rowIndex;
	protected String []displayData;
	protected String [][]hiddenParameters;
	protected boolean deleteable;
	protected boolean updateable;
	protected String cssClass;
	protected boolean selected;
	
	public static final String EVEN_UNREAD_ROW_CSS="clsTableEvenRow-unread";
	public static final String ODD_UNREAD_ROW_CSS="clsTableOddRow-unread";
	public GridRowData() {
		
	}
	public GridRowData(long rowId,int rowIndex, String[] data) {
		super();
		this.rowId = rowId;
		this.data = data;
		this.rowIndex=rowIndex;
		this.displayData=data;
	}
	public long getRowId() {
		return rowId;
	}
	public void setRowId(long rowId) {
		this.rowId = rowId;
	}
	public String[] getData() {
		return data;
	}
	public void setData(String[] data) {
		this.data = data;
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	public String[][] getHiddenParameters() {
		return hiddenParameters;
	}
	public void setHiddenParameters(String[][] hiddenParameters) {
		this.hiddenParameters = hiddenParameters;
	}
	public String[] getDisplayData() {
		return displayData;
	}
	public void setDisplayData(String[] displayData) {
		this.displayData = displayData;
	}
	public boolean isDeleteable() {
		return deleteable;
	}
	public void setDeleteable(boolean deleteable) {
		this.deleteable = deleteable;
	}
	public boolean isUpdateable() {
		return updateable;
	}
	public void setUpdateable(boolean updateable) {
		this.updateable = updateable;
	}
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}
