package ir.utopia.core.zeroconfiguration.sequence.persistent;

import ir.utopia.common.systems.subsystem.persistent.CmSubsystem;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoSequence entity provides the base persistence definition of the
 * CoSequence entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoSequence extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8557729641644275503L;
	private Long coSequenceId;
	private String tablename;
	private CmSubsystem cmSubsystem;
	private Long currentid;

	// Constructors



	/** default constructor */
	public AbstractCoSequence() {
	}


	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "CO_SEQUENCE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoSequenceId() {
		return this.coSequenceId;
	}

	public void setCoSequenceId(Long coSequenceId) {
		this.coSequenceId = coSequenceId;
	}

	@Column(name = "TABLENAME", unique = true, nullable = false, insertable = true, updatable = true, length = 50)
	public String getTablename() {
		return this.tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	@ManyToOne	
	@JoinColumn(name = "CM_SUBSYSTEM_ID", referencedColumnName = "CM_SUBSYSTEM_ID" )
	public CmSubsystem getCmSubsystem() {
		return cmSubsystem;
	}


	public void setCmSubsystem(CmSubsystem cmSubsystem) {
		this.cmSubsystem = cmSubsystem;
	}

	@Column(name = "CURRENTID", unique = false, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
	public Long getCurrentid() {
		return this.currentid;
	}

	public void setCurrentid(Long currentid) {
		this.currentid = currentid;
	}

}