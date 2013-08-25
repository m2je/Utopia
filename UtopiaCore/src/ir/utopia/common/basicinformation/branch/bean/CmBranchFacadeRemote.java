package ir.utopia.common.basicinformation.branch.bean;

import ir.utopia.common.basicinformation.branch.persistent.CmBranch;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import javax.ejb.Remote;

@Remote
public interface CmBranchFacadeRemote extends UtopiaBasicUsecaseBean<CmBranch,CmBranch> {

}