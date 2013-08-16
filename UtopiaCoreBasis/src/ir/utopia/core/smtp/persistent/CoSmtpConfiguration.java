package ir.utopia.core.smtp.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoSmtpConfiguration entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_SMTP_CONFIGURATION",  uniqueConstraints = {})
@TableGenerator(name = "SMTPGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_CUSTOM_PROPERTY")
public class CoSmtpConfiguration extends AbstractCoSmtpConfiguration implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = -8557289380328057339L;

	/** default constructor */
	public CoSmtpConfiguration() {
	}

	

}
