package ir.utopia.core.util.tags.datainputform.client.grid.model;

import java.util.HashSet;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GridValueModel extends AbstractGridData  implements IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2469451515574186167L;
	GridRowData[] rows;
	HashSet<Long>removedIds=new HashSet<Long>();	
	List<GridRowData> modifiedRows;
	String []columnNames;
	String primaryKey;
	boolean isModified=false;
	public void setModified(boolean isModified) {
		this.isModified = isModified;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	public GridValueModel(){
		
	}
	public List<GridRowData> getModifiedRows() {
		return modifiedRows;
	}

	public void setModifiedRows(List<GridRowData> modifiedRows) {
		this.modifiedRows = modifiedRows;
	}

	public HashSet<Long> getRemovedIds() {
		return removedIds;
	}

	public void setRemovedIds(HashSet<Long> removedIds) {
		this.removedIds = removedIds;
	}
	
	public GridRowData[] getRows() {
		return rows;
	}

	public void setRows(GridRowData[] rows) {
		this.rows = rows;
	}
	public boolean isModified(){
		return isModified|| (modifiedRows!=null&&modifiedRows.size()>0)||
		(removedIds!=null&&removedIds.size()>0);
	}
	
}
