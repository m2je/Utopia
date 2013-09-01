package ir.utopia.common.basicinformation.employeesignature.bean;


import ir.utopia.common.basicinformation.employeesignature.bean.CmEmployeeSignatureFacadeRemote;
import ir.utopia.common.basicinformation.employeesignature.persistent.CmEmployeeSignature;
import ir.utopia.common.basicinformation.employeesignature.persistent.CmVEmployeeSignature;
import ir.utopia.core.bean.AbstractOrganizationSupportFacade;

import javax.ejb.Stateless;


@Stateless
public class CmEmployeeSignatureFacade extends AbstractOrganizationSupportFacade<CmEmployeeSignature, CmVEmployeeSignature> implements CmEmployeeSignatureFacadeRemote {

}