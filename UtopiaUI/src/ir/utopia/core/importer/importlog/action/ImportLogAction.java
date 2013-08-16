package ir.utopia.core.importer.importlog.action;

import ir.utopia.core.struts.AbstractUtopiaAction;

public class ImportLogAction extends AbstractUtopiaAction<ImportLogForm>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6173058122420907320L;

	@Override
	protected ImportLogForm createModelInstance() {
		return new ImportLogForm();
	}

}
