package ir.utopia.common.doctype.bean;

import ir.utopia.common.doctype.persistent.CmDoctype;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import javax.ejb.Remote;

/**
 * Remote interface for CmDoctypeFacade.
 * 
 * @author 
 */
@Remote
public interface CmDoctypeFacadeRemote extends UtopiaBasicUsecaseBean<CmDoctype, CmDoctype>{
	
}