package ir.utopia.core.zeroconfiguration.sequence.bean;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.zeroconfiguration.sequence.persistent.CoSequence;
import ir.utopia.core.zeroconfiguration.sequence.persistent.CoVSequence;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CoSequence.
 * 
 * @see sequence.CoSequence
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoSequenceFacade extends AbstractBasicUsecaseBean<CoSequence,CoVSequence> implements CoSequenceFacadeRemote {
	// property constants
	public static final String TABLENAME = "tablename";
	public static final String CURRENTID = "currentid";

	public List<CoSequence> findByTablename(Object tablename,
			int... rowStartIdxAndCount) {
		return findByProperty(TABLENAME, tablename, rowStartIdxAndCount);
	}

	public List<CoSequence> findByCurrentid(Object currentid,
			int... rowStartIdxAndCount) {
		return findByProperty(CURRENTID, currentid, rowStartIdxAndCount);
	}

}