package ir.utopia.common.doctype.bean;

import ir.utopia.common.doctype.persistent.CmDoctypeDimension;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import javax.ejb.Remote;

/**
 * Remote interface for CmDoctypeDimensionFacade.
 * 
 * @author 
 */
@Remote
public interface CmDoctypeDimensionFacadeRemote extends UtopiaBasicUsecaseBean<CmDoctypeDimension, CmDoctypeDimension> {
	}