package ir.utopia.core.security.userorganizationaccess.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoVValidOrgAccss 
 */
@MappedSuperclass
public abstract class AbstractCoVValidOrgAccss extends AbstractUtopiaPersistent implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5163732626598770107L;
	// Fields

	private CoVValidOrgAccssId id;
	private String name;

	// Constructors

	/** default constructor */
	public AbstractCoVValidOrgAccss() {
	}

	/** full constructor */
	public AbstractCoVValidOrgAccss(CoVValidOrgAccssId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "coUserId", column = @Column(name = "CO_USER_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)),
			@AttributeOverride(name = "cmOrganizationId", column = @Column(name = "CM_ORGANIZATION_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)) })
	public CoVValidOrgAccssId getId() {
		return this.id;
	}

	public void setId(CoVValidOrgAccssId id) {
		this.id = id;
	}
	
	@Column(name = "NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}