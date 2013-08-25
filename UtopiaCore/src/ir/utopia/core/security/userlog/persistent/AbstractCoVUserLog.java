package ir.utopia.core.security.userlog.persistent;

import ir.utopia.core.persistent.AbstractBasicPersistent;
import ir.utopia.core.security.userlogdtl.persistent.CoVUserLogDtl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractCoVUserLog entity provides the base persistence definition of the
 * CoVUserLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVUserLog extends AbstractBasicPersistent implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3046311391373392121L;
	private Long coUserLogId;
	private Long coUserId;
	private Date loginDate;
	private Date logOutDate;
	private String clientAddress;
	private String username;
	private String name;
	private Set<CoVUserLogDtl> coUserLogDtls = new HashSet<CoVUserLogDtl>(0);
	// Constructors

	// Property accessors
	@Id
	@Column(name = "CO_USER_LOG_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUserLogId() {
		return this.coUserLogId;
	}

	public void setCoUserLogId(Long coUserLogId) {
		this.coUserLogId = coUserLogId;
	}

	@Column(name = "CO_USER_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUserId() {
		return this.coUserId;
	}

	public void setCoUserId(Long coUserId) {
		this.coUserId = coUserId;
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

	@Column(name = "CLIENT_ADDRESS", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getClientAddress() {
		return this.clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	@Column(name = "USERNAME", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 401)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@OneToMany( fetch = FetchType.LAZY, mappedBy = "coUserLog")
	public Set<CoVUserLogDtl> getCoUserLogDtls() {
		return this.coUserLogDtls;
	}

	public void setCoUserLogDtls(Set<CoVUserLogDtl> coUserLogDtls) {
		this.coUserLogDtls = coUserLogDtls;
	}
}