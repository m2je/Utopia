package ir.utopia.core.util.scheduler.persistent;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * QrtzJobDetails entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "QRTZ_JOB_DETAILS",  uniqueConstraints = {})
public class QrtzJobDetails extends AbstractQrtzJobDetails implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 7077736861245293811L;

	/** default constructor */
	public QrtzJobDetails() {
	}

	/** minimal constructor */
	public QrtzJobDetails(QrtzJobDetailsId id, String jobClassName,
			String isDurable, String isNonconcurrent, String isUpdateData,
			String requestsRecovery) {
		super(id, jobClassName, isDurable, isNonconcurrent, isUpdateData,
				requestsRecovery);
	}

	/** full constructor */
	public QrtzJobDetails(QrtzJobDetailsId id, String description,
			String jobClassName, String isDurable, String isNonconcurrent,
			String isUpdateData, String requestsRecovery, String jobData,
			Set<QrtzTriggers> qrtzTriggerses) {
		super(id, description, jobClassName, isDurable, isNonconcurrent,
				isUpdateData, requestsRecovery, jobData, qrtzTriggerses);
	}

}
