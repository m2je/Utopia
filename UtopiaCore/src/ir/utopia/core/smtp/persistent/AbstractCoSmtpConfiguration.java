package ir.utopia.core.smtp.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoSmtpConfiguration entity provides the base persistence definition
 * of the CoSmtpConfiguration entity.
 * 
 * @author M2je
 */
@MappedSuperclass
public abstract class AbstractCoSmtpConfiguration extends AbstractUtopiaPersistent implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8326439735450019161L;
	private Long coSmtpConfigurationId;
	
	private String name;
	private String smtpHost;
	private int smtpPort;
	private String description;

	// Constructors

	/** default constructor */
	public AbstractCoSmtpConfiguration() {
	}

	

	// Property accessors
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="SMTPGenerator")
	@Id
	@Column(name = "CO_SMTP_CONFIGURATION_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoSmtpConfigurationId() {
		return this.coSmtpConfigurationId;
	}

	public void setCoSmtpConfigurationId(Long coSmtpConfigurationId) {
		this.coSmtpConfigurationId = coSmtpConfigurationId;
	}

	

	@Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SMTP_HOST", unique = false, nullable = false, insertable = true, updatable = true, length = 1000)
	public String getSmtpHost() {
		return this.smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	@Column(name = "SMTP_PORT", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public int getSmtpPort() {
		return this.smtpPort;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	@Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}