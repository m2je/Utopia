package ir.utopia.core.zeroconfiguration.sequence.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.zeroconfiguration.sequence.persistent.CoSequence;
import ir.utopia.core.zeroconfiguration.sequence.persistent.CoVSequence;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CoSequenceFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoSequenceFacadeRemote extends UtopiaBasicUsecaseBean<CoSequence,CoVSequence> {

	public List<CoSequence> findByTablename(Object tablename,
			int... rowStartIdxAndCount);

	public List<CoSequence> findByCurrentid(Object currentid,
			int... rowStartIdxAndCount);

}