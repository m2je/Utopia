package ir.utopia.core.util.tags;

import ir.utopia.core.ContextHolder;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.security.WindowController;
import ir.utopia.core.struts.UtopiaBasicAction;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.struts.UtopiaFormMethodMetaData;
import ir.utopia.core.struts.UtopiaTypeConverter;
import ir.utopia.core.util.WebUtil;
import ir.utopia.core.util.tags.datainputform.client.model.ExecutionResult;
import ir.utopia.core.util.tags.datainputform.client.model.InteractiveRemoteServiceCallSupport;
import ir.utopia.core.util.tags.datainputform.server.UtopiaUIServiceProxy;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opensymphony.xwork2.ActionContext;

public  abstract class AbstractUtopiaGWTServiceAction  extends UtopiaBasicAction implements InteractiveRemoteServiceCallSupport{
	private static final boolean IsDebugging=Boolean.getBoolean("GwtDebug");
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(UtopiaUIServiceProxy.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -1669530072432309710L;
	
//********************************************************************************************************	
	public Map<String,Object> getSession(){
		return  ActionContext.getContext().getSession();
	}
//********************************************************************************************************	
	public String getLanguage(){
		return WebUtil.getLanguage(getSession());
	}
//********************************************************************************************************	
	@Override
	public int requestWindowNumber() {
		return WindowController.getNextWindowNo(getSession());
	}
//********************************************************************************************************
	public String[] getErrorFields(int windowNo) {
		List<String>result=WindowController.removeErrorFields(getSession(), windowNo);
		if(result!=null){
			return result.toArray(new String[result.size()]);
		}
		return null; 
	}
//********************************************************************************************************
	public String[] getWindowErrorMesssages(int windowNo) {
		List<MessageNamePair>result=WindowController.removeWindowErrorMessages(getSession(), windowNo);
		return convertToString(result);
	}
//********************************************************************************************************
	public String[] getWindowInfoMessages(int windowNo) {
		List<MessageNamePair>result=WindowController.removeWindowInfoMessages(getSession(), windowNo);
		return convertToString(result);
	}
//********************************************************************************************************
	public String[] getWindowWarningMessages(int windowNo) {
		List<MessageNamePair>result=WindowController.removeWindowWarningMessages(getSession(), windowNo);
		return convertToString(result);
	}
//********************************************************************************************************	
	public boolean getActionStatus(int windowNo, String actionName) {
		boolean success= WindowController.removeActionStatus(getSession(), windowNo, actionName);
		return success;
	}
//********************************************************************************************************
	private String[] convertToString(List<MessageNamePair>pairs){
		if(pairs!=null){
			String[]res=new String[pairs.size()];
			int index=0;
			for(MessageNamePair pair:pairs){
				res[index++]=pair.getMessage();
			}
			return res;
		}
		return null;
	}
//********************************************************************************************************
	@Override
	public ExecutionResult getExecutionResult(String actionName,
			int windowNo) {
		ExecutionResult result=new ExecutionResult(getActionStatus(windowNo, actionName));
		result.setErrorMessages(getWindowErrorMesssages(windowNo));
		result.setInfoMessages(getWindowInfoMessages(windowNo));
		result.setWarningMessages(getWindowWarningMessages(windowNo));
		return result;
	}
//********************************************************************************************************
	public void initContextByRequestParameters(UtopiaFormMetaData meta){
		
		Map<String,Object>context=ContextHolder.getContext().getContextMap();
//		if(IsDebugging){
//			context= ContextUtil.createAdminContext();
//		}else{
//			context= ContextUtil.createContext(session);
//		}
		
		Map<String,Object>params=ActionContext.getContext().getParameters();
		String language=getLanguage();
		if(params!=null&&params.size()>0){
			try {
				if(meta==null){
					context.putAll(params);
				}else{
					for(String param : params.keySet()){
						Object value= params.get(param);
						if(value!=null){
							UtopiaFormMethodMetaData methodMeta= meta.getMethodMetaData(param);
							if(methodMeta==null){
								if(value.getClass().isArray()&&((Object[])value).length==1){
									context.put(param,((Object[])value)[0] );
								}else{
									context.put(param,value );
								}
							}else{
								if(String.class.isInstance(value)){
									context.put(param,UtopiaTypeConverter.convertFromString(methodMeta.getReturnType(), language, (String)value));
								}else if(value.getClass().isArray()){
									context.put(param,UtopiaTypeConverter.convertFromString(methodMeta.getReturnType(), language, (String[])value));
								}
								
							}

						}
					}
				}
				
			} catch (Exception e) {
				logger.log(Level.WARNING,"",e);
			}
		}
		
		
//		return context;
	}
//********************************************************************************************************
//	public Map<String, Object> createContext(){
//		return initContextByRequestParameters(null);
//	}
}
