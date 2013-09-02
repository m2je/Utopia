package ir.utopia.core.struts;

import ir.utopia.core.persistent.UtopiaPersistent;

import java.util.Date;


public interface UtopiaForm<P extends UtopiaPersistent>extends UtopiaSoftDeleteForm<P> {
	
	
	public Long getCreatedby() ;
	public void setCreatedby(Long createdBy) ;
	public Long getUpdatedby() ;
	public void setUpdatedby(Long updatedBy) ;	
	public Date getCreated() ;
	public void setCreated(Date created) ;
	public Date getUpdated() ;
	public void setUpdated(Date updated) ;
}
