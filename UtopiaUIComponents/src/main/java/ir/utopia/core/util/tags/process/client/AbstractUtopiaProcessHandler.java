package ir.utopia.core.util.tags.process.client;

import ir.utopia.core.util.tags.datainputform.client.UtopiaGWTUtil;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.model.ProcessMessageHandler;
import ir.utopia.core.util.tags.process.client.model.UtopiaProcessAction;
import ir.utopia.core.util.tags.process.client.model.UtopiaProcessActionAsync;
import ir.utopia.core.util.tags.process.client.model.ProcessExecutionResult;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.MessageBox.PromptCallback;

public abstract class AbstractUtopiaProcessHandler extends ProcessMessageHandler implements UtopiaProcessHandlerInterface {
	private Set<UtopiaProcessListener>listeners;
	
	private  void progressProcess( ProcessExecutionResult result, String processTitle, String submitpath,String []parameters,String []values, boolean alertForsuccess, ProcessConstants constants,Long startTime,boolean refreshAfterProcess,boolean isConfirm){
		notifyProcessStatusChanged(result);
		if(result==null||!result.isSuccess()||result.getProcessStatus()==ProcessExecutionResult.PROCESS_STATUS_FINISHED){
			
			if(result==null||!result.isSuccess()){
				MessageUtility.stopProgress();
				MessageUtility.alert(processTitle, constants.processFailed(),MessageBox.ERROR);
				showMessages(result,constants);
			}else{
				MessageUtility.stopProgress();
				if(!isConfirm){
						Long executionTime= System.currentTimeMillis()-startTime;
						String timeMessage=constants.processExecutionTime() ;
						String time=(executionTime/1000)/60+" "+constants.minute()+ " , "+(executionTime/1000)%60+" "+constants.secound();
						timeMessage=timeMessage.replace("@time@",time );
						if(alertForsuccess){
							MessageUtility.alert(processTitle,constants.processSuccess(),MessageBox.INFO);
						}
						if(refreshAfterProcess)
							refreshPage();
						processFinished(result.getProcessIdentifier(),result.isSuccess());
						showExecutionTime(timeMessage);
						showMessages(result, constants);
					
				}
			}
			
			return;
		}else{
			MessageUtility.stopProgress();
		}
		 	Long proccessIdentifier=result.getProcessIdentifier();
			showMessages(result,constants);
			if(result.getProcessStatus()!=ProcessExecutionResult.PROCESS_STATUS_FINISHED&&result.isSuccess()){
				
				int currentTaskIndex=result.getCurrentTask();
				ProcessStatusTimer updateStatusTimer=null;
				if(proccessIdentifier!=null&&proccessIdentifier>0l){
					currentTaskIndex=result.getCurrentTask();
					 updateStatusTimer=new ProcessStatusTimer(constants,processTitle,proccessIdentifier,parameters,values,alertForsuccess,submitpath,refreshAfterProcess,startTime,isConfirm);
						}
				updateProgress(processTitle,result.getCurrentTaskName(),constants , currentTaskIndex, result.getTotalTaskCount(),updateStatusTimer);
			}
		
	}
//**********************************************************************
	public abstract void refreshPage();
//**********************************************************************
	public abstract void processFinished(Long processUID,boolean status);
//**********************************************************************
	@Override
	public void addProcessListener(UtopiaProcessListener listener) {
		if(listeners==null){
			listeners=new HashSet<UtopiaProcessListener>();
		}
		listeners.add(listener);
	}
//**********************************************************************
	@Override
	public boolean removeProcessListener(UtopiaProcessListener listener) {
		if(listeners!=null){
			return listeners.remove(listener);
		}
		return false;
	}
//**********************************************************************
	private void notifyProcessStatusChanged(ProcessExecutionResult result){
		if(listeners!=null){
			for(UtopiaProcessListener l:listeners){
				l.notifyProcessStatusChanged(result);
			}
		}
	}
//**********************************************************************
	private void showExecutionTime(String message){
		final DecoratedPopupPanel   simplePopup = new DecoratedPopupPanel (true);
	    simplePopup.ensureDebugId("cwBasicPopup-simplePopup");
	    simplePopup.setWidth("150px");
	    simplePopup.setWidget(new HTML(
	        message));
	    simplePopup.center();
	    simplePopup.setAnimationEnabled(true);
        simplePopup.setAutoHideEnabled(true);
        simplePopup.show();
         Timer t=new Timer(){

			public void run() {
				simplePopup.setVisible(false);
				cancel();
			}};
			t.schedule(3000);
			
	}
	//**********************************************************************
	private class ProcessStatusTimer extends Timer{
		ProcessConstants constants;
		String processTitle;
		Long proccessIdentifier;
		boolean alertForsuccess;
		String submitpath;
		boolean refreshAfterProcess;
		Long startTime;
		boolean isConfirm;
		String []parameters;
		String []values;
		public ProcessStatusTimer(ProcessConstants constants,
				String processTitle, Long proccessIdentifier,String []parameters,
				String []values,
				boolean alertForsuccess, String submitpath,boolean refreshAfterProcess,Long startTime,boolean isConfirm) {
			super();
			this.constants = constants;
			this.processTitle = processTitle;
			this.proccessIdentifier = proccessIdentifier;
			this.alertForsuccess = alertForsuccess;
			this.submitpath = submitpath;
			this.refreshAfterProcess=refreshAfterProcess;
			this.startTime=startTime;
			this.isConfirm=isConfirm;
			this.parameters=parameters;
			this.values=values;
		}

		public void run() {
				doCallAction(proccessIdentifier,parameters,values,processTitle,submitpath,constants,alertForsuccess,SERVICE_ACTION_TYPE_UPDATE_STATUS,refreshAfterProcess,startTime,isConfirm,null);
		}
	}
//**********************************************************************
	public  void callAction(Long processIdentifier,String []params,String []values,String processTitle, String submitpath,ProcessConstants constants,boolean alertForsuccess,int executionType,boolean refreshAfterProcess) {
		doCallAction(processIdentifier, params, values, processTitle, submitpath, constants, alertForsuccess, executionType, 
				refreshAfterProcess,
				System.currentTimeMillis(),false,null);
	}
	
//**********************************************************************
	private  void doCallAction(Long processIdentifier,String []params,String []values,String processTitle, String submitpath,ProcessConstants constants,
					boolean alertForsuccess,int executionType,boolean refreshAfterProcess,Long startTime,boolean isConfirm,ProcessExecutionResult result) {
		UtopiaProcessActionAsync service=getService( submitpath);
		 AsyncCallback<ProcessExecutionResult> callback= new ProcessCalbackHandler(processIdentifier,params,values,processTitle,submitpath,constants
				 																	,alertForsuccess,refreshAfterProcess,startTime,isConfirm);
		switch (executionType) {
		case SERVICE_ACTION_TYPE_UPDATE_STATUS:
			service.updateStatus(processIdentifier,isConfirm, callback);
			break;
		case SERVICE_ACTION_TYPE_EXCUTE_WITH_CONFIRM:
			 confirmAction(processTitle,service, params, values, submitpath, constants,startTime,refreshAfterProcess,callback);
			break;
		case SERVICE_ACTION_TYPE_EXCUTE_WITHOUT_CONFIRM:
			 continueToProcess(result, service, params, values, callback);
			break;
		}
	}
//**********************************************************************
	private UtopiaProcessActionAsync  getService( String submitpath){
    		UtopiaProcessActionAsync service = (UtopiaProcessActionAsync) GWT.create(UtopiaProcessAction.class);
		    ServiceDefTarget endpoint = (ServiceDefTarget) service;
		    endpoint.setServiceEntryPoint(GWT.getHostPageBaseURL()+ submitpath);	
		    return service;
	}
//**********************************************************************
	private  void updateProgress(String title,String message,ProcessConstants constants,int currentTask,int totalTaskcount,ProcessStatusTimer updateStatusTimer){
		String totalMessage;
		int percent;
		if(totalTaskcount>0){
			message=message==null?"":message;
			float cf=(float)currentTask;
			float tf=(float)totalTaskcount;
			 percent=(int)((cf/tf)*100);
			 
			totalMessage=message+" "+currentTask+" "+ constants.from()+" "+totalTaskcount;
		}else{
			totalMessage=message;
			percent=50;
		}
		totalMessage=totalMessage==null?"("+percent+"%)":totalMessage+" ("+percent+"%)";
		totalMessage=UtopiaGWTUtil.getLocaleNumber(totalMessage, LocaleInfo.getCurrentLocale().getLocaleName());
		if(!MessageBox.isVisible()){
			MessageBox.progress(title==null?"":title, totalMessage);
		}
		MessageBox.updateProgress(percent,totalMessage);
		if(updateStatusTimer!=null){
			if(currentTask<=totalTaskcount){
				updateStatusTimer.schedule(3000);
			}else{
				MessageUtility.stopProgress();
				updateStatusTimer.cancel();
			}
		}
	}

//**********************************************************************
	private  class ProcessCalbackHandler implements AsyncCallback<ProcessExecutionResult>{
		Long startTime ;
		String processTitle;
		String submitpath;
		boolean alertForsuccess;
		ProcessConstants constants;
		boolean refreshAfterProcess;
		boolean isConfirm;
		Long processIdentifier;
		String []params,values;
		public ProcessCalbackHandler(Long processIdentifier,String []params,String []values,String processTitle,String submitpath,ProcessConstants constants,
				boolean alertForsuccess,boolean refreshAfterProcess,Long startTime,boolean isConfirm){
			this.processTitle=processTitle;
			this.submitpath=submitpath;
			this.constants=constants;
			this.alertForsuccess=alertForsuccess;
			this.startTime=startTime;
			this.refreshAfterProcess=refreshAfterProcess;
			this.isConfirm=isConfirm;
			this.processIdentifier=processIdentifier;
			this.params=params;
			this.values=values;
		}
		 public void onSuccess(ProcessExecutionResult result) {
	     	progressProcess(result,processTitle,submitpath,params,values,alertForsuccess,constants,startTime,refreshAfterProcess,isConfirm);
	     	if(isConfirm&&result!=null&&result.isSuccess()&&result.getProcessStatus()==ProcessExecutionResult.PROCESS_STATUS_FINISHED){
	     		doCallAction(processIdentifier, params, values, processTitle, submitpath, constants, 
	     				alertForsuccess,SERVICE_ACTION_TYPE_EXCUTE_WITHOUT_CONFIRM , refreshAfterProcess, startTime, false, result);
	     	}
	     }

	     public void onFailure(Throwable e) {
	        GWT.log("", e);
	        progressProcess(null,processTitle,submitpath,params,values,alertForsuccess,constants,startTime,refreshAfterProcess,isConfirm);
	     }
	}
//**********************************************************************
	private void confirmAction(final String processTitle,final UtopiaProcessActionAsync service,
			final String []params,final String []values,final String submitPath,final ProcessConstants constants,final Long startTime,final boolean refreshAfterProcess,
			final AsyncCallback<ProcessExecutionResult> callback) {
		MessageBox.progress(constants.pleaseWait(), constants.submitingData());
		getService(submitPath).confirm(params, values, new AsyncCallback<ProcessExecutionResult>(){

			public void onFailure(Throwable e) {
				GWT.log("", e);
				MessageUtility.stopProgress();
				MessageUtility.error(constants.error(), constants.failToAccessServer());
			}

			public void onSuccess(final ProcessExecutionResult result) {
				
				PromptCallback msgClalback=new PromptCallback(){
					public void execute(String btnID, String text) {
						continueProcess(processTitle,result, service, params, values,submitPath,constants,startTime,refreshAfterProcess,callback);		
					}};
				if(result!=null){
					showMessages(result, constants,msgClalback);
					}	
				else{
					 service.execute(params,values,callback);
				}
			}});
	}  
//**********************************************************************	
	private void continueProcess(final String processTitle,ProcessExecutionResult result, UtopiaProcessActionAsync service, String []params, String []values,String submitPath,  ProcessConstants constants,Long startTime,boolean refreshAfterProcess, AsyncCallback<ProcessExecutionResult> callback){
		if(result!=null&&result.isSuccess()){
			if(result.getProcessStatus()==ProcessExecutionResult.PROCESS_STATUS_PROCESSING){
				doCallAction(result.getProcessIdentifier(), params, values, processTitle, submitPath, 
						constants, false, SERVICE_ACTION_TYPE_UPDATE_STATUS, refreshAfterProcess, startTime, true,null);
				}
			else{
				continueToProcess(result, service, params, values, callback);
					}
			}else{
				MessageUtility.stopProgress();
				if(result==null)
					MessageUtility.error(constants.error(), constants.failToAccessServer());
				else
					showMessages(result, constants);
			}
	}
//**********************************************************************
	private void continueToProcess(ProcessExecutionResult result, UtopiaProcessActionAsync service, String []params, String []values, AsyncCallback<ProcessExecutionResult> callback){
		MessageUtility.stopProgress();
		if(result!=null&&result.isConfirmUser()){
			MessageBox.confirm(result.getConfirmMessages(), result.getConfirmMessages(),new ContinueToProcessConfirmCallBack(service,params,values,callback));
			}else{
				service.execute(params,values,callback);
			}
	}
//**********************************************************************
	private class ContinueToProcessConfirmCallBack implements ConfirmCallback{
		UtopiaProcessActionAsync service; 
		String []params; 
		String []values ;
		AsyncCallback<ProcessExecutionResult> callback;
		ContinueToProcessConfirmCallBack(UtopiaProcessActionAsync service, String []params, String []values ,AsyncCallback<ProcessExecutionResult> callback){
			this.params=params;
			this.values=values;
			this.callback=callback;
		}
		public void execute(String btnID) {
			if("yes".equalsIgnoreCase(btnID)){
				service.execute(params,values,callback);
			}
		}
	}
}
