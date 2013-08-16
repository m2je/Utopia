package ir.utopia.core.zeroconfiguration.sequence.persistent;

import ir.utopia.core.persistent.AbstractUtopiaPersistent;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoSequence entity provides the base persistence definition of the
 * CoSequence entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoVSequence extends AbstractUtopiaPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4391797525049922821L;
	private Long coSequenceId;
	private String tablename;
	private String subsysName;
	private Long currentid;

	// Constructors

	/** default constructor */
	public AbstractCoVSequence() {
	}


	// Property accessors
	@Id
	@Column(name = "CO_SEQUENCE_ID")
	public Long getCoSequenceId() {
		return this.coSequenceId;
	}

	public void setCoSequenceId(Long coSequenceId) {
		this.coSequenceId = coSequenceId;
	}

	@Column(name = "TABLENAME")
	public String getTablename() {
		return this.tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	@Column(name = "CURRENTID")
	public Long getCurrentid() {
		return this.currentid;
	}

	public void setCurrentid(Long currentid) {
		this.currentid = currentid;
	}
	
	@Column(name = "SUBSYS_NAME")
	public String getSubsysName() {
		return subsysName;
	}


	public void setSubsysName(String subsysName) {
		this.subsysName = subsysName;
	}


}