package ir.utopia.core.smtp.persistent;

import ir.utopia.core.persistent.annotation.LookupConfiguration;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 * CoSmtpAddresses entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_SMTP_ADDRESSES",  uniqueConstraints = { @UniqueConstraint(columnNames = {
		"CO_SMTP_CONFIGURATION_ID", "USERNAME" }) })
@TableGenerator(name = "CoSMTPAddressGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_USER")
@LookupConfiguration(displayColumns={"coSmtpConfiguration.name" ,"username"},displayItemSeperator="-")		
public class CoSmtpAddresses extends AbstractCoSmtpAddresses implements
		java.io.Serializable {

	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 5778642934971350659L;

	/** default constructor */
	public CoSmtpAddresses() {
	}

	

}
