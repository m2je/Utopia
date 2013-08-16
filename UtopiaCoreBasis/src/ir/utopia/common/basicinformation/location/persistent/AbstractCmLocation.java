package ir.utopia.common.basicinformation.location.persistent;

import java.util.HashSet;
import java.util.Set;

import ir.utopia.common.basicinformation.organization.persistent.CmOrganization;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * AbstractCmLocation entity provides the base persistence definition of the
 * CmLocation entity.
 * 
 * @author Arsalani
 */
@MappedSuperclass
public abstract class AbstractCmLocation extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -1071001404864903512L;
	private Long cmLocationId;
	private String name;
	private String code;
	private CmLocation parentId;
	private String description;
	private CmOrganization cmOrganization;
	private Set<CmLocation> children = new HashSet<CmLocation>(0);
	

	// Constructors



	/** default constructor */
	public AbstractCmLocation() {
	}

	

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="LocationSequenceGenerator")
	@Column(name = "CM_LOCATION_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCmLocationId() {
		return this.cmLocationId;
	}

	public void setCmLocationId(Long cmLocationId) {
		this.cmLocationId = cmLocationId;
	}

	
	
	@Column(name = "NAME", unique = true, nullable = false, insertable = true, updatable = true, length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	@Column(name = "CODE", unique = true, nullable = false, insertable = true, updatable = true, length = 200)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}



	@ManyToOne(cascade = {})
	@JoinColumn(name = "PARENT_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmLocation getParentId() {
		return this.parentId;
	}

	public void setParentId(CmLocation parentId) {
		this.parentId = parentId;
	}

	
	
	@Column(name = "DESCRIPTION", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CM_ORGANIZATION_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CmOrganization getCmOrganization() {
		return cmOrganization;
	}
	public void setCmOrganization(CmOrganization cmOrganization) {
		this.cmOrganization = cmOrganization;
	}


	@OneToMany( fetch = FetchType.LAZY, mappedBy = "parentId")
	public Set<CmLocation> getChildren() {
		return children;
	}

	public void setChildren(Set<CmLocation> children) {
		this.children = children;
	}

}