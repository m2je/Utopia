package ir.utopia.core.util.tags.datainputform.client.grid.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OrderBy implements Serializable, IsSerializable {

	public static final int SORT_ORDER_ASCENDING=1;
	public static final int SORT_ORDER_DESCENDING=2;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String columnName;
	
	private int dir=SORT_ORDER_ASCENDING;
	public OrderBy(){
		
	}
	public OrderBy(String columnName, int dir) {
		super();
		this.columnName = columnName;
		this.dir = dir;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}
	
}
