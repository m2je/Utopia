package ir.utopia.core.util.tags.datainputform.client.searchwidget;

import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;

import com.google.gwt.user.client.ui.Widget;

public interface SearchWidgetListener {
	public static final int import_button_clicked=1;
	public static final int export_button_clicked=2;
	
	public void configurationLoaded(SearchPageModel model,Widget searchingWidget);
	
	public void rowSelected(int rowIndex,Long rowId,String []columnNames ,String []rowData,boolean selected);
	
	public void buttonClicked(int buttonType);
}
