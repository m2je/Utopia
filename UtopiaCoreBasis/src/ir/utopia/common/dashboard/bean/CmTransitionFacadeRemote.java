package ir.utopia.common.dashboard.bean;

import ir.utopia.common.dashboard.persistent.CmTransition;
import ir.utopia.common.dashboard.persistent.CmTransitionHistory;
import ir.utopia.common.dashboard.persistent.TransitionRecord;
import ir.utopia.common.doctype.persistent.CmDocStatus;
import ir.utopia.core.bean.OrderBy;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.security.user.persistence.CoUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;
@Remote
public interface CmTransitionFacadeRemote extends UtopiaBasicUsecaseBean<CmTransition, CmTransition>{

	public List<CmTransition> getAccessibleTransitions(Long userId,Long fiscalYearId);
	
	public List<TransitionRecord> getTransitionRecords(Long userId,Long usecaseId,Long fiscalYearId,Long docStausFromId,OrderBy []orderBy,int... rowStartIdxAndCount);
	
	public int getTranistionalRecordCount(Long userId,Long usecaseId,Long fiscalYearId,Long docStausFromId);
	
	public Map<Integer,Set<Integer>>getUserAccesisbleStatusChanges(Long userId,Long usecaseId);
	
	public List<CmDocStatus> getUsecaseDocStatues(Long usecaseId,Long fiscalyearId);
	
	public Set<Long> getDashboardAccessibleUsecases(Long userId,Long fiscalYearId);
	
	public void markAs(Long userId,Long usecaseId,Long recordId,boolean read);
	
	public CmTransitionHistory saveTransitionHistory(Long transitionId,Long userId,Long recordId);
	
	public CoUser getRecordLockedBy(Long usecaseId,Long recordId,Long userId );
}
