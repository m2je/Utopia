package ir.utopia.core.security.userusecaseacss.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoUserUsecaseAcss entity provides the base persistence definition of
 * the CoUserUsecaseAcss entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVUserUsecaseAcss extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7561833609732818399L;
	private Long coUserUsecaseAcssId;
	private String username;
	private String usecaseName;

	// Constructors


	/** default constructor */
	public AbstractCoVUserUsecaseAcss() {
	}

	// Property accessors
	@Id
	@Column(name = "CO_USER_USECASE_ACSS_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUserUsecaseAcssId() {
		return this.coUserUsecaseAcssId;
	}

	public void setCoUserUsecaseAcssId(Long coUserUsecaseAcssId) {
		this.coUserUsecaseAcssId = coUserUsecaseAcssId;
	}

	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "USECASE_NAME")
	public String getUsecaseName() {
		return usecaseName;
	}

	public void setUsecaseName(String usecaseName) {
		this.usecaseName = usecaseName;
	}

}