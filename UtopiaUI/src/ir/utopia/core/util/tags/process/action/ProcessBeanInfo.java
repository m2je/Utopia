package ir.utopia.core.util.tags.process.action;

import ir.utopia.core.bean.AbstractBasicUsecaseBean;
import ir.utopia.core.bean.UtopiaProcessBean;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.process.BeanProcess;
import ir.utopia.core.process.BeanProcessExcutionResult;
import ir.utopia.core.process.ProcessListener;
import ir.utopia.core.process.ProcessStatusChangeEvent;
import ir.utopia.core.process.ProcessStatusChangeEvent.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

  public class ProcessBeanInfo implements ProcessListener{
	  private static final Logger logger;
		
		static {
			logger = Logger.getLogger(ProcessBeanInfo.class.getName());
		}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8050896379214447148L;
	private Long launchTime;
	private int step=0;
	private int status;
	private Throwable e;
	private boolean finished=false;
	private boolean success=true;
	private transient UtopiaProcessBean bean;
	private int totalStepCount;
	private transient BeanProcess proc;
	private Map<String, Object> context;
	private Long key;
	private transient List<ir.utopia.core.util.tags.process.action.ProcessListener> listenerList;
	public ProcessBeanInfo(Long key,Long launchTime, UtopiaProcessBean bean,BeanProcess proc,Map<String,Object>context,int totalStepCount) {
		this.key=key;
		this.launchTime = launchTime;
		this.bean=bean;
		this.totalStepCount=totalStepCount;
		this.proc=proc;
		this.context=context;
		
	}
	@Override
	public void notifyStatusChanged(ProcessStatusChangeEvent e) {
		if(EventType.processEvent.equals(e.getEventType())){
			List<MessageNamePair>pairs= AbstractUtopiaProcessAction.messageMap.get(key);
			pairs=pairs==null?new ArrayList<MessageNamePair>():pairs;
			pairs.add(e.getMessage());
			AbstractUtopiaProcessAction.messageMap.put(key, pairs);
			
		}
		
		this.step=e.getProcessStatus();
		success=success&&e.getException()==null&&!e.getEventType().equals(EventType.processFailed);
		this.e=e.getException();
		AbstractUtopiaProcessAction.beanMap.put(key, this);
		if(listenerList!=null){
			e.setKey(key);
			for(ir.utopia.core.util.tags.process.action.ProcessListener listener:listenerList){
				listener.notifyProcessStatusChanged(e);
			}
		}
	} 
	public void runProcess(String processName) {
		final ProcessListener listener=this;
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					BeanProcessExcutionResult <?>beanRes= bean.startProcess(proc, context, listener);
					success=true;
					step=totalStepCount;
					finished=true;
					ProcessStatusChangeEvent res=new ProcessStatusChangeEvent(EventType.processFinished);
					res.setBeanExecutionResult(beanRes);	
					notifyStatusChanged(res);
					return;
				} catch (Exception ex) {
					success=false;
					logger.log(Level.WARNING,"", ex);
					e=ex;
					step=totalStepCount;
					finished=true;
					ProcessStatusChangeEvent ev=new ProcessStatusChangeEvent(EventType.processFailed);
					ev.setException(ex);
					notifyStatusChanged(ev);
				}
				
			}
		
		},processName).start();
	}
	public int getStep() {
		return step;
	}
	public int getStatus() {
		return status;
	}
	public boolean isSuccess() {
		return success;
	}
	public int getTotalStepCount() {
		return totalStepCount;
	}
	public Long getLaunchTime() {
		return launchTime;
	}
	public UtopiaProcessBean getBean() {
		return bean;
	}
	public boolean isFinished() {
		return finished;
	}
	public Throwable getException() {
		return e;
	}
	
	public void addListener(ir.utopia.core.util.tags.process.action.ProcessListener listener){
		if(listenerList==null){
			listenerList=new ArrayList<ir.utopia.core.util.tags.process.action.ProcessListener>();
		}
		listenerList.add(listener);
	}
	
	public boolean removeListener(ProcessListener listener){
		if(listenerList==null){
			return false;
		}
		return listenerList.remove(listener);
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	
}