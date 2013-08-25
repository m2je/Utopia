package ir.utopia.common.basicinformation.employeesignature.bean;

import ir.utopia.common.basicinformation.employeesignature.persistent.CmEmployeeSignature;
import ir.utopia.common.basicinformation.employeesignature.persistent.CmVEmployeeSignature;
import ir.utopia.core.bean.OrganizationSupportFacade;

import javax.ejb.Remote;

@Remote
public interface CmEmployeeSignatureFacadeRemote extends OrganizationSupportFacade<CmEmployeeSignature, CmVEmployeeSignature>{

}