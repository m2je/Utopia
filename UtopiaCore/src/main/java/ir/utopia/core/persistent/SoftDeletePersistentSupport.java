package ir.utopia.core.persistent;

import ir.utopia.core.constants.Constants.IsActive;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
@MappedSuperclass
public abstract class SoftDeletePersistentSupport extends AbstractBasicPersistent implements SoftDeletePersistent{

	protected IsActive isactive=IsActive.active;
	
	//************************************************************************************************
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ISACTIVE", unique = false, nullable = false, insertable = true, updatable = true, precision = 1, scale = 0)
	public IsActive getIsactive() {
		return this.isactive;
	}
//************************************************************************************************
	public void setIsactive(IsActive isactive) {
		this.isactive = isactive;
	}
}
