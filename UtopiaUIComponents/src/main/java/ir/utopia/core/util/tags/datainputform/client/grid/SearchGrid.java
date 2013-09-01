package ir.utopia.core.util.tags.datainputform.client.grid;

import com.google.gwt.user.client.ui.Widget;

import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;

public interface SearchGrid {

	public void setData(int from, int pageSize, SearchPageData data);

	public void addGridListener(SearchGridListener listener);

	public void setMultiSelectable(boolean multiselect);

	public Long[] getSelectedIds();

	public void setHighlightRowOnMouseOver(boolean highlightRowOnMouseOver);

	public void unCheckRow(Long rowId);

	public void checkRow(Long rowId);
	
	public Widget getWidget();
}
