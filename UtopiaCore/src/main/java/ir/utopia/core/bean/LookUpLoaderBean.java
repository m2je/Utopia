package ir.utopia.core.bean;

import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.persistent.lookup.model.NamePair;

import java.util.List;

public interface LookUpLoaderBean extends UtopiaBean {

	/**
	 * @param rowStartEndIndex
	 * @param context
	 * @return
	 */
	public List<NamePair> loadLookup(int ...rowStartEndIndex);
	/**
	 * 
	 * @param model
	 * @param rowStartEndIndex
	 * @param context
	 * @return
	 */
	public List<NamePair> loadLookup(LookupConfigurationModel model,int ...rowStartEndIndex);
}
