package ir.utopia.core.controlpanel.log.action;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.log.bean.CoCpLogConfigFacadeRemote;
import ir.utopia.core.log.persistent.CoCpLogConfig;
import ir.utopia.core.struts.AbstractUtopiaInteractiveAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogConfigurationAction extends AbstractUtopiaInteractiveAction {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(LogConfigurationAction.class.getName());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1024201982311535397L;
	LogConfigurationForm form;	
	public LogConfigurationForm getModel() {
		if(form==null){
			form=new LogConfigurationForm();
		}
		return form;
	}

//***************************************************************************************************

	public String execute() throws Exception {
		String res="SUCCESS";
		Throwable ex=null;
		try {
			Collection<LogConfigurationDetail>details=form.getConfigRow();
			if(details==null||details.size()==0){
				super.initSession(true, null, "save", form.getWindowNo());
				return res;
				}
				List<CoCpLogConfig>conf=new ArrayList<CoCpLogConfig>();
				List<CoCpLogConfig>removed=new ArrayList<CoCpLogConfig>();
				for(LogConfigurationDetail detail: details){
					if(detail.isRemoved()){
						removed.add((CoCpLogConfig)detail.convertToPersistent());
					}else{
						conf.add((CoCpLogConfig)detail.convertToPersistent());
					}
				}
				Map<String,Object>context= createContext();
				CoCpLogConfigFacadeRemote bean=(CoCpLogConfigFacadeRemote)ServiceFactory.lookupFacade(CoCpLogConfigFacadeRemote.class.getName());
				if(conf.size()>0)
					bean.save(conf, context);
				if(removed.size()>0)
					bean.delete(removed, context);
				res="SUCCESS";
		} catch (Exception e) {
			logger.log(Level.WARNING,"", e);
			ex=e;
			res="ERROR";
		}
		super.initSession("SUCCESS".equals(res), ex, "save", form.getWindowNo());
		return res;
		
	}
//***************************************************************************
	
}
