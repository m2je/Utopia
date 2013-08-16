package ir.utopia.core.security.userlog.persistent;

import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.security.userlogdtl.persistent.CoUserLogDtl;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoUserLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_USER_LOG",  uniqueConstraints = {})
@TableGenerator(name = "UserLogSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_USER_LOG")
public class CoUserLog extends AbstractCoUserLog implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 4289099719923999148L;

	/** default constructor */
	public CoUserLog() {
	}

	/** minimal constructor */
	public CoUserLog(Long coUserLogId, CoUser coUser, Date loginDate) {
		super(coUserLogId, coUser, loginDate);
	}

	/** full constructor */
	public CoUserLog(Long coUserLogId, CoUser coUser, Date loginDate,
			Date logOutDate, String description, Set<CoUserLogDtl> coUserLogDtls) {
		super(coUserLogId, coUser, loginDate, logOutDate, description,
				coUserLogDtls);
	}

}
