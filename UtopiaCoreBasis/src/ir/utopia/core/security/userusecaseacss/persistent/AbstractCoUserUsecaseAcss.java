package ir.utopia.core.security.userusecaseacss.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.security.user.persistence.CoUser;
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
 * AbstractCoUserUsecaseAcss entity provides the base persistence definition of
 * the CoUserUsecaseAcss entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoUserUsecaseAcss extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3344607570387708840L;
	private Long coUserUsecaseAcssId;
	private CoUser coUser;
	private CoUsecase coUsecase;

	// Constructors


	/** default constructor */
	public AbstractCoUserUsecaseAcss() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="UsrUscsAccsSequenceGenerator")
	@Column(name = "CO_USER_USECASE_ACSS_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUserUsecaseAcssId() {
		return this.coUserUsecaseAcssId;
	}

	public void setCoUserUsecaseAcssId(Long coUserUsecaseAcssId) {
		this.coUserUsecaseAcssId = coUserUsecaseAcssId;
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
	@JoinColumn(name = "CO_USECASE_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUsecase getCoUsecase() {
		return coUsecase;
	}

	public void setCoUsecase(CoUsecase coUsecase) {
		this.coUsecase = coUsecase;
	}

}