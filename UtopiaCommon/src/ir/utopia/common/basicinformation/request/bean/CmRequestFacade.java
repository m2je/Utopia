package ir.utopia.common.basicinformation.request.bean;

import ir.utopia.common.basicinformation.request.persistent.CmRequest;
import ir.utopia.common.basicinformation.request.persistent.CmVRequest;
import ir.utopia.core.bean.AbstractOrganizationSupportFacade;

import javax.ejb.Stateless;


@Stateless
public class CmRequestFacade  extends AbstractOrganizationSupportFacade<CmRequest,CmVRequest> implements CmRequestFacadeRemote {

}
