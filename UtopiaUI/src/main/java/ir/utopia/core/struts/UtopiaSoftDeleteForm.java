package ir.utopia.core.struts;
import ir.utopia.core.persistent.SoftDeletePersistent;

public interface UtopiaSoftDeleteForm<P extends SoftDeletePersistent> extends UtopiaBasicForm<P> {

	public boolean isActive();
	public void setActive(boolean isactive) ;
}
