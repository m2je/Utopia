package ir.utopia.common.basicinformation.location.persistent;

import ir.utopia.core.persistent.AbstractOrganizationData;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
 * AbstractCmLocationOrg entity provides the base persistence definition of the
 * CmLocationOrg entity.
 * 
 * @author Arsalani
 */
@MappedSuperclass
public class AbstractCmLocationOrg extends AbstractOrganizationData implements Serializable {

	// Fields
	
	private static final long serialVersionUID = 6620113925382821879L;
	private Long cmLocationId;
	private String name;
	private String code;
	private CmLocationOrg parentId;
	private String description;
	private Set<CmLocationOrg> children = new HashSet<CmLocationOrg>(0);
	

	// Constructors



	/** default constructor */
	public AbstractCmLocationOrg() {
	}

	

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="LocationOrgSequenceGenerator")
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
	public CmLocationOrg getParentId() {
		return this.parentId;
	}

	public void setParentId(CmLocationOrg parentId) {
		this.parentId = parentId;
	}

	
	
	@Column(name = "DESCRIPTION", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@OneToMany( fetch = FetchType.LAZY, mappedBy = "parentId")
	public Set<CmLocationOrg> getChildren() {
		return children;
	}

	public void setChildren(Set<CmLocationOrg> children) {
		this.children = children;
	}

}
