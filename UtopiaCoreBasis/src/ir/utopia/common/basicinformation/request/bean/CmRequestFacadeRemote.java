package ir.utopia.common.basicinformation.request.bean;

import ir.utopia.common.basicinformation.request.persistent.CmRequest;
import ir.utopia.common.basicinformation.request.persistent.CmVRequest;
import ir.utopia.core.bean.OrganizationSupportFacade;

import javax.ejb.Remote;


@Remote
public interface CmRequestFacadeRemote extends OrganizationSupportFacade<CmRequest,CmVRequest> {

}
