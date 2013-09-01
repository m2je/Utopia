package ir.utopia.core.persistent.lookup.model;

import ir.utopia.core.persistent.UtopiaBasicPersistent;

import java.io.Serializable;

public class DetailPersistentValueInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4776925863055853260L;

	private UtopiaBasicPersistent value;
	private boolean deleted;
	public DetailPersistentValueInfo( UtopiaBasicPersistent value,boolean deleted) {
		this.value=value;
		this.deleted=deleted;
	}

	public UtopiaBasicPersistent getValue() {
		return value;
	}

	public void setValue(UtopiaBasicPersistent value) {
		this.value = value;
	}


	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	
}
