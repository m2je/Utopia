package ir.utopia.core.util.tags.datainputform.client.grid;

import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.Renderer;

public class IncludedGridCellRenderer implements Renderer{
	boolean hidden=false;
	@Override
	public String render(Object value, CellMetadata cellMetadata,
			Record record, int rowIndex, int colNum, Store store) {
		if(hidden)return "";
		return value==null?"":value.toString();
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

}
