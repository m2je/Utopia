package ir.utopia.core.security.persistent;


// default package

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoVRoleValidAccess entity provides the base persistence definition of
 * the CoVRoleValidAccess entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVRoleValidAccess implements
		java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3371057459228856256L;
	private CoVRoleValidAccessId id;

	// Constructors

	/** default constructor */
	public AbstractCoVRoleValidAccess() {
	}

	/** full constructor */
	public AbstractCoVRoleValidAccess(CoVRoleValidAccessId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "coRoleId", column = @Column(name = "CO_ROLE_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)),
			@AttributeOverride(name = "coUsecaseId", column = @Column(name = "CO_USECASE_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)),
			@AttributeOverride(name = "coUsecaseActionId", column = @Column(name = "CO_USECASE_ACTION_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)),
			@AttributeOverride(name = "coActionId", column = @Column(name = "CO_ACTION_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)),
			@AttributeOverride(name = "cmSubsystemId", column = @Column(name = "CM_SUBSYSTEM_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)),
			@AttributeOverride(name = "cmSystemId", column = @Column(name = "CM_SYSTEM_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)) })
	public CoVRoleValidAccessId getId() {
		return this.id;
	}

	public void setId(CoVRoleValidAccessId id) {
		this.id = id;
	}

}