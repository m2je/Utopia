package ir.utopia.core.util.tags.datainputform.client.lov;

public class LOVDialogValueModel {
	private Long []ids;
	private String []columns;
	private String []columnHeader;
	private String [][]rowValues;
	public Long[] getIds() {
		return ids;
	}
	public void setIds(Long[] ids) {
		this.ids = ids;
	}
	public String[] getColumns() {
		return columns;
	}
	public void setColumns(String[] columns) {
		this.columns = columns;
	}
	public String[] getColumnHeader() {
		return columnHeader;
	}
	public void setColumnHeader(String[] columnHeader) {
		this.columnHeader = columnHeader;
	}
	public String[][] getRowValues() {
		return rowValues;
	}
	public void setRowValues(String[][] rowValues) {
		this.rowValues = rowValues;
	}
	
	
}
