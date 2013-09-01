package ir.utopia.core.util.tags.datainputform.client.grid.model;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchPageData extends AbstractGridData implements Serializable,IsSerializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9141191357153021344L;
	public static final int DEFAULT_PAGE_SIZE=25;
	public SearchPageData(){}

	private List<? extends GridRowData> rows;
	int totalResultCount;
	
	
	public int getTotalResultCount() {
		return totalResultCount;
	}
	public void setTotalResultCount(int totalResultCount) {
		this.totalResultCount = totalResultCount;
	}
	public List<? extends GridRowData> getRows() {
		return rows;
	}
	public void setRows(List<? extends GridRowData> rows) {
		this.rows = rows;
	}
	
}
