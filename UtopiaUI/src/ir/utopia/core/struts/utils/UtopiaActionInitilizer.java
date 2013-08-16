package ir.utopia.core.struts.utils;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.UIServiceFactory;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.form.UtopiaPageForm;
import ir.utopia.core.model.UsecaseUIInfo;
import ir.utopia.core.struts.ActionUtil;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.util.WebUtil;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class UtopiaActionInitilizer implements Interceptor {
	Logger logger=Logger.getLogger(UtopiaActionInitilizer.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = -655638108821782620L;

	@Override
	public void destroy() {
		
	}

	@Override
	public void init() {
		
		
	}

	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		UtopiaPageForm pageForm= initAction();
		ActionContext.getContext().getValueStack(). set(Constants.PAGE_CONFIG_FORM_NAME,pageForm);
		Object action= actionInvocation.getAction();
		
		String executionResult= actionInvocation.invoke();
		
		if(ActionSupport.class.isInstance(action)){
			 pageForm.setSuccess(!((ActionSupport)action).hasActionErrors());
		}
		patchResponse(pageForm, executionResult);
		return executionResult;
	}
//****************************************************************************************************************************************************
	private void patchResponse(UtopiaPageForm pageForm, String executionResult){
		if(executionResult!=null&&executionResult.toLowerCase().startsWith("json")&&
				(predefindedActions.save.equals(pageForm.getActionName())||predefindedActions.update.equals(pageForm.getActionName()))){
			HttpServletRequest request= ServletActionContext.getRequest();
			if(request.getHeader("user-agent").toLowerCase().indexOf("chrome")>=0){
				ServletActionContext.getResponse().setContentType("text/html");//FIX for GWT form panel submit listener in Chrome
			}
		}
	}
//****************************************************************************************************************************************************
	protected UtopiaPageForm createPageFormObject(UseCase usecase, String actionName,String currentUri){
		UtopiaPageForm pageForm=new UtopiaPageForm(usecase,actionName);
		pageForm.setSystemId(usecase.getSystemId());
		pageForm.setSubSystemId(usecase.getSubSystemId());
		
		ActionContext ctx=ActionContext.getContext();
		Long usecaseId=pageForm.getUsecase().getUsecaseId();
		Long []availableActions= ServiceFactory.getSecurityProvider().getUsecaseAvailableActions(WebUtil.
				getUser(ctx.getSession()),usecaseId);
		
		pageForm.setAvailableActions(availableActions);
		pageForm.setCurrentUri(currentUri);
		UsecaseUIInfo UIInfo= UIServiceFactory.getUsecase(usecaseId); 
		if(UIInfo!=null){
			pageForm.setMetaData(UIInfo.getMeta());
		}
		return pageForm;
	}
//*******************************************************************************************
		private UtopiaPageForm initAction(){
			UtopiaPageForm pageForm=null;
			try {
			HttpServletRequest request= ServletActionContext.getRequest();
			
			String currentUri=request.getRequestURI();
			Map<Integer, String>map= ActionUtil.parseClassAndMethod(currentUri);
			String usecaseName=map.get(ActionUtil.USECASE);
			String actionName=map.get(ActionUtil.METHOD);
			String systemName=map.get(ActionUtil.SYSTEM);
			String subSystemName=map.get(ActionUtil.SUB_SYSTEM);
			UseCase usecase= UsecaseUtil.getUseCase(systemName, subSystemName, usecaseName);
			pageForm=(UtopiaPageForm) ActionContext.getContext().getValueStack().findValue(Constants.PAGE_CONFIG_FORM_NAME);
			if(pageForm==null||usecase.getUsecaseId()!=pageForm.getUsecase().getUsecaseId()){
				pageForm=createPageFormObject(usecase,actionName,currentUri);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.ALL,"error",  e);
		}
		return pageForm;
		}

}
