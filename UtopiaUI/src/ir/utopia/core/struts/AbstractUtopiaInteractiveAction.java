package ir.utopia.core.struts;

import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.security.WindowController;
import ir.utopia.core.util.exceptionhandler.ExceptionResult;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionContext;

public abstract class AbstractUtopiaInteractiveAction extends UtopiaBasicAction implements ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6101897816012995617L;
	HttpServletRequest request;
	@SuppressWarnings("unchecked")
	protected void initSession(boolean sucess,Throwable e,String actionName,int windowNumber){
		Map session=ActionContext.getContext().getSession();
		WindowController.setActionStatus(session,
				windowNumber, actionName, sucess&&e==null);
		if(e!=null){
		ExceptionResult exResult= getExceptionHandler().handel(e, createContext());
		List<MessageNamePair>messages= exResult.getMessages();
		if(messages!=null){
			for(MessageNamePair pair:messages){
				WindowController.addWindowMessage(session, windowNumber, pair);
			}
		}
		 }
		
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		
	}
	
//******************************************************************************************************
	
}
