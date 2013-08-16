package ir.utopia.core.security.usruscsactnaccs.persistence;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoUsrUscsActnAccs entity provides the base persistence definition of
 * the CoUsrUscsActnAccs entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoUsrUscsActnAccs extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2672455068015163789L;
	private Long coUsrUscsActnAccsId;
	private CoUser coUser;
	private CoUsecaseAction coUsecaseAction;
	

	// Constructors

	/** default constructor */
	public AbstractCoUsrUscsActnAccs() {
	}

	

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="UsrUscsActnAccsSequenceGenerator")
	@Column(name = "CO_USR_USCS_ACTN_ACCS_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUsrUscsActnAccsId() {
		return this.coUsrUscsActnAccsId;
	}

	public void setCoUsrUscsActnAccsId(Long coUsrUscsActnAccsId) {
		this.coUsrUscsActnAccsId = coUsrUscsActnAccsId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USER_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUser getCoUser() {
		return this.coUser;
	}

	public void setCoUser(CoUser coUser) {
		this.coUser = coUser;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USECASE_ACTION_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUsecaseAction getCoUsecaseAction() {
		return this.coUsecaseAction;
	}

	public void setCoUsecaseAction(CoUsecaseAction coUsecaseAction) {
		this.coUsecaseAction = coUsecaseAction;
	}


}