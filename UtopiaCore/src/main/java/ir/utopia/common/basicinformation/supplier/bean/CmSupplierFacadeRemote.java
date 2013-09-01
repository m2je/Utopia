package ir.utopia.common.basicinformation.supplier.bean;

import ir.utopia.common.basicinformation.supplier.persistent.CmSupplier;
import ir.utopia.common.basicinformation.supplier.persistent.CmVSupplier;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import javax.ejb.Remote;


/**
 * Remote interface for CmSupplier.
 * 
 * @author Arsalani
 */
@Remote
public interface CmSupplierFacadeRemote  extends UtopiaBasicUsecaseBean<CmSupplier,CmVSupplier> {

}
