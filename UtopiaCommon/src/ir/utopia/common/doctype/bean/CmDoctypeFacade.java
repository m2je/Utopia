package ir.utopia.common.doctype.bean;

import ir.utopia.common.doctype.persistent.CmDoctype;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import javax.ejb.Stateless;

/**
 * Facade for entity CmDoctype.
 * 
 * @see CmDoctype
 * @author 
 */
@Stateless
public class CmDoctypeFacade extends AbstractBasicUsecaseBean<CmDoctype, CmDoctype> implements CmDoctypeFacadeRemote {

}