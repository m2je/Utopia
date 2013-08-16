package ir.utopia.core.persistent;

import ir.utopia.core.constants.Constants.IsActive;

public interface SoftDeletePersistent extends UtopiaBasicPersistent{
	
	public IsActive getIsactive() ;
	//************************************************************************************************
		public void setIsactive(IsActive isactive); 
	//************************************************************************************************
}
