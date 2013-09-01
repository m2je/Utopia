package ir.utopia.core.customproperty.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.customproperty.persistent.CoCustomProperty;
import ir.utopia.core.exception.DeleteRecordExeption;
import ir.utopia.core.exception.SaveRecordException;
import ir.utopia.core.persistent.UtopiaBasicPersistent;

import java.util.List;

import javax.ejb.Remote;
@Remote
public interface CoCustomPropertyFacadeRemote extends
		UtopiaBasicUsecaseBean<CoCustomProperty, CoCustomProperty> {
	/**
	 * 
	 * @param persitentObject
	 * @param customProps
	 * @param action
	 */
	public void handleProperties(UtopiaBasicPersistent persitentObject,List<List<String>>customProps,predefindedActions action)throws SaveRecordException ;
	/**
	 * 
	 * @param usecaseId
	 * @param recordId
	 */
	public void deleteCustomProperties(Long usecaseId,Long recordId)throws DeleteRecordExeption;
}
