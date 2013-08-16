package ir.utopia.core.util.tags.datainputform.converter;

import ir.utopia.core.ContextHolder;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;

import java.util.Map;

public class DefaultUtopiaUIHandler extends AbstractUtopiaUIHandler {
	public InputItem[] getInputItems(
			UseCase usecase,predefindedActions action,  String language,boolean loadLookups) {
		Map<String,Object>context=ContextHolder.getContextMap();
		return convertMethodMetaToInputItem(meta,null, context,usecase,action, language,loadLookups);
	}

	

	

	
	}
