package ir.utopia.core.mailtemplate.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoMailTemplate entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_MAIL_TEMPLATE",  uniqueConstraints = {})
@TableGenerator(name = "MailTemplateGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_MAIL_TEMPLATE")
public class CoMailTemplate extends AbstractCoMailTemplate implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2631782630799364327L;

	// Constructors

	
}
