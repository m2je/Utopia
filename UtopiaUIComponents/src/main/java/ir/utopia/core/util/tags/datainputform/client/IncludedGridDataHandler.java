package ir.utopia.core.util.tags.datainputform.client;

import ir.utopia.core.util.tags.datainputform.client.grid.IncludedGrid;
import ir.utopia.core.util.tags.datainputform.client.grid.model.GridRowData;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

import com.google.gwt.user.client.ui.Widget;

public interface IncludedGridDataHandler {

	public void notifyCellEditing(int row,int column,Widget editingwidget,Object value,GridRowData rowData,IncludedGrid that,InputItem item);
	
	public void notifyRowAdded(IncludedGrid grid,int row);
}
