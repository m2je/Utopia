package ir.utopia.core.smtp.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoSmtpAddresses entity provides the base persistence definition of
 * the CoSmtpAddresses entity.
 * 
 * @author M2je
 */
@MappedSuperclass
public abstract class AbstractCoSmtpAddresses extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8180206990888905743L;
	private Long coSmtpAddressesId;
	private CoSmtpConfiguration coSmtpConfiguration;
	private String username;
	private String password;
	private String description;

	// Constructors

	/** default constructor */
	public AbstractCoSmtpAddresses() {
	}

	
	// Property accessors
	@GeneratedValue(strategy=GenerationType.TABLE,generator="CoSMTPAddressGenerator")
	@Id
	@Column(name = "CO_SMTP_ADDRESSES_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoSmtpAddressesId() {
		return this.coSmtpAddressesId;
	}

	public void setCoSmtpAddressesId(Long coSmtpAddressesId) {
		this.coSmtpAddressesId = coSmtpAddressesId;
	}

	@ManyToOne(cascade = {})
	@JoinColumn(name = "CO_SMTP_CONFIGURATION_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoSmtpConfiguration getCoSmtpConfiguration() {
		return this.coSmtpConfiguration;
	}

	public void setCoSmtpConfiguration(CoSmtpConfiguration coSmtpConfiguration) {
		this.coSmtpConfiguration = coSmtpConfiguration;
	}


	@Column(name = "USERNAME", unique = false, nullable = false, insertable = true, updatable = true, length = 300)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD", unique = false, nullable = false, insertable = true, updatable = true, length = 300)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}