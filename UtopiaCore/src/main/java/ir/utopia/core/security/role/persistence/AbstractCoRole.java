package ir.utopia.core.security.role.persistence;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.security.rolesubsystemacss.persistence.CoRoleSubsystemAcss;
import ir.utopia.core.security.roleuscsactnaccs.persistence.CoRoleUscsActnAccs;
import ir.utopia.core.security.userrole.persistence.CoUserRoles;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * AbstractCoRole entity provides the base persistence definition of the CoRole
 * entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoRole extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7000866511878044342L;
	private Long coRoleId;
	private String name;
	
	private Set<CoUserRoles> coUserRoleses = new HashSet<CoUserRoles>(0);
	private Set<CoRoleUscsActnAccs> coRoleUscsActnAccses = new HashSet<CoRoleUscsActnAccs>(
			0);

	private Set<CoRoleSubsystemAcss> coRoleSubsystemAcsses = new HashSet<CoRoleSubsystemAcss>(
			0);


	// Constructors

	/** default constructor */
	public AbstractCoRole() {
	}

	
	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="RoleSequenceGenerator")
	@Column(name = "CO_ROLE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoRoleId() {
		return this.coRoleId;
	}

	public void setCoRoleId(Long coRoleId) {
		this.coRoleId = coRoleId;
	}

	@Column(name = "NAME", unique = true, nullable = false, insertable = true, updatable = true, length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coRole")
	public Set<CoUserRoles> getCoUserRoleses() {
		return this.coUserRoleses;
	}

	public void setCoUserRoleses(Set<CoUserRoles> coUserRoleses) {
		this.coUserRoleses = coUserRoleses;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coRole")
	public Set<CoRoleUscsActnAccs> getCoRoleUscsActnAccses() {
		return this.coRoleUscsActnAccses;
	}

	public void setCoRoleUscsActnAccses(
			Set<CoRoleUscsActnAccs> coRoleUscsActnAccses) {
		this.coRoleUscsActnAccses = coRoleUscsActnAccses;
	}

	

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "coRole")
	public Set<CoRoleSubsystemAcss> getCoRoleSubsystemAcsses() {
		return this.coRoleSubsystemAcsses;
	}

	public void setCoRoleSubsystemAcsses(
			Set<CoRoleSubsystemAcss> coRoleSubsystemAcsses) {
		this.coRoleSubsystemAcsses = coRoleSubsystemAcsses;
	}



}