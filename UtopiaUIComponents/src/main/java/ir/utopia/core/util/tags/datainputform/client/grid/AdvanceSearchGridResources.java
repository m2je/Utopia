package ir.utopia.core.util.tags.datainputform.client.grid;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.DataGrid.Style;

public interface AdvanceSearchGridResources extends ClientBundle {
	@NotStrict
	@Source({Style.DEFAULT_CSS,"AdvanceSearchGrid.css"})
    DataGrid.Style advancedGrid();
}
