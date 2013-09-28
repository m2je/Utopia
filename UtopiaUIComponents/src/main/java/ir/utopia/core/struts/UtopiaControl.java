/**
 * 
 */
package ir.utopia.core.struts;

import ir.utopia.core.messagehandler.MessageNamePair.MessageType;
import ir.utopia.core.persistent.UtopiaBasicPersistent;

import java.util.Collection;
import java.util.List;

/**
 * @author Salarkia
 *
 */
public interface UtopiaControl<T>  {
	public static final int FORM_SUBMIT_MODE_CLASSIC=1;
	public static final int FORM_SUBMIT_MODE_AJAX=2;
	public static final String JSON_RESPONSE_NAME="json";
	public List<T>convertFormToPersistent(Collection<T> form);
	
	public UtopiaBasicPersistent convertFormToPersistent(T form);
	
	public List<UtopiaBasicForm<?>>getExectionResults();
	
	public UtopiaBasicForm<?> convertPersistentToForm(UtopiaBasicPersistent persistence);
	
	public MessageType getMessageType(String message);
}
