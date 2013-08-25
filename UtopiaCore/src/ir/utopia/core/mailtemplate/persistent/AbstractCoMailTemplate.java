package ir.utopia.core.mailtemplate.persistent;

import ir.utopia.core.persistent.AbstractBasicPersistent;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoMailTemplate entity provides the base persistence definition of the
 * CoMailTemplate entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoMailTemplate extends AbstractBasicPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -353414014448252196L;
	private Long coMailTemplateId;
	private String name;
	private String content;
	private String description;
	private String subject;
	// Constructors

	/** default constructor */
	public AbstractCoMailTemplate() {
	}

	

	// Property accessors
	@GeneratedValue(generator="MailTemplateGenerator")
	@Id
	@Column(name = "CO_MAIL_TEMPLATE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoMailTemplateId() {
		return this.coMailTemplateId;
	}

	public void setCoMailTemplateId(Long coMailTemplateId) {
		this.coMailTemplateId = coMailTemplateId;
	}

	@Column(name = "NAME", unique = false, nullable = false, insertable = true, updatable = true)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CONTENT", unique = false, nullable = false, insertable = true, updatable = true)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "DESCRIPTION", unique = false, nullable = true, insertable = true, updatable = true, length = 3000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name = "subject", unique = false, nullable = true, insertable = true, updatable = true, length = 1000)
	public String getSubject() {
		return subject;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}

}