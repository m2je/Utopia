package ir.utopia.core.security.roleusecaseacss.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.security.role.persistence.CoRole;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoRoleUsecaseAcss entity provides the base persistence definition of
 * the CoRoleUsecaseAcss entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoRoleUsecaseAcss extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2794261476045135321L;
	private Long coRoleUsecaseAcssId;
	private CoRole coRole;
	private CoUsecase coUsecase;

	// Constructors


	/** default constructor */
	public AbstractCoRoleUsecaseAcss() {
	}


	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="RoleUsecaseAcssSequenceGenerator")
	@Column(name = "CO_ROLE_USECASE_ACSS_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoRoleUsecaseAcssId() {
		return this.coRoleUsecaseAcssId;
	}

	public void setCoRoleUsecaseAcssId(Long coRoleUsecaseAcssId) {
		this.coRoleUsecaseAcssId = coRoleUsecaseAcssId;
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
	@JoinColumn(name = "CO_USECASE_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUsecase getCoUsecase() {
		return coUsecase;
	}


	public void setCoUsecase(CoUsecase coUsecase) {
		this.coUsecase = coUsecase;
	}


}