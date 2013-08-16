package ir.utopia.core.security.roleuscsactnaccs.persistence;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.security.role.persistence.CoRole;
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
 * AbstractCoRoleUscsActnAccs entity provides the base persistence definition of
 * the CoRoleUscsActnAccs entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoRoleUscsActnAccs extends AbstractUtopiaPersistent implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6808880490343303047L;
	private Long coRoleUscsActnAccsId;
	private CoRole coRole;
	private CoUsecaseAction coUsecaseAction;

	// Constructors

	/** default constructor */
	public AbstractCoRoleUscsActnAccs() {
	}
	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="RoleUscsActnAccsSequenceGenerator")
	@Column(name = "CO_ROLE_USCS_ACTN_ACCS_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoRoleUscsActnAccsId() {
		return this.coRoleUscsActnAccsId;
	}

	public void setCoRoleUscsActnAccsId(Long coRoleUscsActnAccsId) {
		this.coRoleUscsActnAccsId = coRoleUscsActnAccsId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_ROLE_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoRole getCoRole() {
		return this.coRole;
	}

	public void setCoRole(CoRole coRole) {
		this.coRole = coRole;
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