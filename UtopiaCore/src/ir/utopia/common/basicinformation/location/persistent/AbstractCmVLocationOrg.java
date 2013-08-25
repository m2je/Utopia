package ir.utopia.common.basicinformation.location.persistent;

import java.io.Serializable;

import ir.utopia.core.persistent.AbstractOrganizationData;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


/**
 * AbstractCmVLocationOrg entity provides the base persistence definition of the 
 * CmVLocationOrg entity.
 * 
 * @author Arsalani
 */
@MappedSuperclass
public class AbstractCmVLocationOrg extends AbstractOrganizationData implements Serializable {

	// Fields
	
	private static final long serialVersionUID = 7978670817329116934L;
	private Long cmLocationId;
	private String name;
	private String description;
	private String parentLocation;
	private String organ;
	
	
	// Constructors

	/** default constructor */
	public AbstractCmVLocationOrg(){
		
	}


	// Property accessors
	@Id
	@Column(name = "CM_LOCATION_ID")
	public Long getCmLocationId() {
		return cmLocationId;
	}
	public void setCmLocationId(Long cmLocationId) {
		this.cmLocationId = cmLocationId;
	}


	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name = "PARENT_LOCATION")
	public String getParentLocation() {
		return parentLocation;
	}
	public void setParentLocation(String parentLocation) {
		this.parentLocation = parentLocation;
	}


	@Column(name = "ORGAN")
	public String getOrgan() {
		return organ;
	}
	public void setOrgan(String organ) {
		this.organ = organ;
	}
}
