package ir.utopia.core.util.tags.datainputform.client.grid;

public interface SearchGridListener {

	public void rowSelected(int rowIndex,Long rowId,String []columnNames,String []rowData,boolean selected);
}
