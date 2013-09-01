package ir.utopia.common.basicinformation.supplier.bean;

import ir.utopia.common.basicinformation.supplier.persistent.CmSupplier;
import ir.utopia.common.basicinformation.supplier.persistent.CmVSupplier;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import javax.ejb.Stateless;

/**
 * Facade for entity CmSupplier.
 * 
 * @author Arsalani 
 */
@Stateless
public class CmSupplierFacade extends AbstractBasicUsecaseBean<CmSupplier,CmVSupplier> implements CmSupplierFacadeRemote {

}
