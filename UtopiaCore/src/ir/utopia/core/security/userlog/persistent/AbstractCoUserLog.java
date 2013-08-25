package ir.utopia.core.security.userlog.persistent;

import ir.utopia.core.persistent.AbstractBasicPersistent;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.security.userlogdtl.persistent.CoUserLogDtl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractCoUserLog entity provides the base persistence definition of the
 * CoUserLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoUserLog extends AbstractBasicPersistent implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8036455343083592262L;
	// Fields

	private Long coUserLogId;
	private CoUser coUser;
	private Date loginDate;
	private Date logOutDate;
	private String description;
	private Set<CoUserLogDtl> coUserLogDtls = new HashSet<CoUserLogDtl>(0);
	private String clientAddress;
	// Constructors

	/** default constructor */
	public AbstractCoUserLog() {
	}

	/** minimal constructor */
	public AbstractCoUserLog(Long coUserLogId, CoUser coUser, Date loginDate) {
		this.coUserLogId = coUserLogId;
		this.coUser = coUser;
		this.loginDate = loginDate;
	}

	/** full constructor */
	public AbstractCoUserLog(Long coUserLogId, CoUser coUser, Date loginDate,
			Date logOutDate, String description, Set<CoUserLogDtl> coUserLogDtls) {
		this.coUserLogId = coUserLogId;
		this.coUser = coUser;
		this.loginDate = loginDate;
		this.logOutDate = logOutDate;
		this.description = description;
		this.coUserLogDtls = coUserLogDtls;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="UserLogSequenceGenerator")
	@Column(name = "CO_USER_LOG_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUserLogId() {
		return this.coUserLogId;
	}

	public void setCoUserLogId(Long coUserLogId) {
		this.coUserLogId = coUserLogId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USER_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUser getCoUser() {
		return this.coUser;
	}

	public void setCoUser(CoUser coUser) {
		this.coUser = coUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGIN_DATE", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getLoginDate() {
		return this.loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOG_OUT_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getLogOutDate() {
		return this.logOutDate;
	}

	public void setLogOutDate(Date logOutDate) {
		this.logOutDate = logOutDate;
	}

	@Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coUserLog")
	public Set<CoUserLogDtl> getCoUserLogDtls() {
		return this.coUserLogDtls;
	}

	public void setCoUserLogDtls(Set<CoUserLogDtl> coUserLogDtls) {
		this.coUserLogDtls = coUserLogDtls;
	}
	@Column(name="CLIENT_ADDRESS")
	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	

}