package ir.utopia.core.util.tags.model;

import ir.utopia.core.util.tags.datainputform.client.model.AbstractPageDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
public class ViewPageModel extends AbstractPageDataModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7544569255036755278L;
	private InputItem[] reportItems;

	public InputItem[] getReportItems() {
		return reportItems;
	}

	public void setReportItems(InputItem[] reportItems) {
		this.reportItems = reportItems;
	}
	
}
