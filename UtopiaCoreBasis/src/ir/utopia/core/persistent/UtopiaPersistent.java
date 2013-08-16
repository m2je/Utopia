/**
 * 
 */
package ir.utopia.core.persistent;

import java.util.Date;

/**
 * @author salarkia
 *
 */
public interface UtopiaPersistent  extends SoftDeletePersistent{
	public String CODE_FIELD_NAME = "code"; 
	public String NAME_FIELD_NAME = "name"; 
	
	
	
	public Date getCreated() ;
//************************************************************************************************
	public void setCreated(Date created) ;
//************************************************************************************************
	public Long getCreatedby() ;
//************************************************************************************************
	public void setCreatedby(Long createdby) ;
//***********************************************************************************************
	public Date getUpdated() ;
//************************************************************************************************
	public void setUpdated(Date updated) ;
//************************************************************************************************
	public Long getUpdatedby() ;
//************************************************************************************************
	public void setUpdatedby(Long updatedby); 
//************************************************************************************************

	
}