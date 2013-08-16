/**
 * 
 */
package ir.utopia.core.struts;

import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.MappedSuperForm;
import ir.utopia.core.persistent.UtopiaPersistent;

import java.util.Date;


/**
 * @author Salarkia
 *
 */
@MappedSuperForm
public abstract class AbstractUtopiaForm<P extends UtopiaPersistent> extends AbstractUtopiaSoftDeleteForm<P> implements UtopiaForm<P> {

	/**
	 * 
	 */
	public AbstractUtopiaForm() {
		
	}
	
	
	private Long createdby;
	private Long updatedby;
	private Date created;
	private Date updated;
	
//*******************************************************************************************
	@FormPersistentAttribute
	public Long getCreatedby() {
		return createdby;
	}
//*******************************************************************************************
	public void setCreatedby(Long createdby) {
		this.createdby = createdby;
	}
//*******************************************************************************************
	@FormPersistentAttribute
	public Long getUpdatedby() {
		return updatedby;
	}
//*******************************************************************************************
	public void setUpdatedby(Long updatedby) {
		this.updatedby = updatedby;
	}
//*******************************************************************************************	
	@FormPersistentAttribute
	public Date getCreated() {
		return created;
	}
//*******************************************************************************************
	public void setCreated(Date created) {
		this.created = created;
	}
//*******************************************************************************************
	@FormPersistentAttribute
	public Date getUpdated() {
		return updated;
	}
//*******************************************************************************************
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
//*******************************************************************************************
	
}
