package ir.utopia.core.security.persistent;

import ir.utopia.core.security.user.persistence.CoUser;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractCoUserAppToken 
 */
@MappedSuperclass
public abstract class AbstractCoUserAppToken implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7898517976814203390L;
	private Long coUserAppTokenId;
	private CoUser coUser;
	private CoApplication coApplication;
	private String token;
	private Date validTo;
	private Date created;
	private Date updated;

	// Constructors

	/** default constructor */
	public AbstractCoUserAppToken() {
	}

	
	// Property accessors
	@Id
	@Column(name = "CO_USER_APP_TOKEN_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoUserAppTokenId() {
		return this.coUserAppTokenId;
	}

	public void setCoUserAppTokenId(Long coUserAppTokenId) {
		this.coUserAppTokenId = coUserAppTokenId;
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
	@JoinColumn(name = "CO_APPLICATION_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoApplication getCoApplication() {
		return this.coApplication;
	}

	public void setCoApplication(CoApplication coApplication) {
		this.coApplication = coApplication;
	}

	@Column(name = "TOKEN", unique = false, nullable = false, insertable = true, updatable = true, length = 3000)
	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VALID_TO", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getValidTo() {
		return this.validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getUpdated() {
		return this.updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

}