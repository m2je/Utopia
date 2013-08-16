package ir.utopia.common.dashboard.persistent;

import ir.utopia.core.persistent.UtopiaBasicPersistent;

import java.io.Serializable;

public class TransitionRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1824229707299845094L;
	UtopiaBasicPersistent record;
	boolean viewed;
	boolean locked;
	String lastModifiedBy;
	public TransitionRecord(UtopiaBasicPersistent record){
		this.record=record;
	}
	
	public UtopiaBasicPersistent getRecord() {
		return record;
	}
	public void setRecord(UtopiaBasicPersistent record) {
		this.record = record;
	}
	public boolean isViewed() {
		return viewed;
	}
	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
}
