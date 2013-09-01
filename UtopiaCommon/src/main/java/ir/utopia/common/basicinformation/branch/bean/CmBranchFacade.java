package ir.utopia.common.basicinformation.branch.bean;

import ir.utopia.common.basicinformation.branch.persistent.CmBranch;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import javax.ejb.Stateless;

@Stateless
public class CmBranchFacade extends AbstractBasicUsecaseBean<CmBranch,CmBranch> implements CmBranchFacadeRemote {

}