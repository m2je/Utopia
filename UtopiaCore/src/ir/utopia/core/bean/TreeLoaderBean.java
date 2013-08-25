package ir.utopia.core.bean;

import ir.utopia.core.persistent.lookup.model.LookupConfigurationModel;
import ir.utopia.core.persistent.parentchild.ParentChildPair;

import java.util.List;

public interface TreeLoaderBean extends UtopiaBean{
	/**
	 * 
	 * @param parendId
	 * @param context
	 * @param rowStartEndIndex
	 * @return
	 */
	public List<ParentChildPair> loadChildren(Long parendId,int ...rowStartEndIndex);
	/**
	 * 
	 * @param context
	 * @param model
	 * @param rowStartEndIndex
	 * @return
	 */
	public List<ParentChildPair> loadChildren(LookupConfigurationModel model,Long parendId,int ...rowStartEndIndex);
}
