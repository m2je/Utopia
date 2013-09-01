package ir.utopia.core.util.tags.process.client;

import ir.utopia.core.util.tags.datainputform.client.MessageUtility;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.process.client.model.ProcessConfigurationModel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.widgets.MessageBox;

public class ProcessHandler extends AbstractUtopiaProcessHandler implements EntryPoint {
	ProcessConstants constants;
	String direction;
	Long usecaseActionId;

	public void onModuleLoad() {
		constants=(ProcessConstants)GWT.create(ProcessConstants.class);
		direction=RootPanel.getBodyElement().getDir();
		setShowTrigger(this);
	}
//**********************************************************************	  
	public void runApp(String l,String c) {
		final boolean confirm=Boolean.parseBoolean(c);
		
		usecaseActionId=-1l;
		try {
			usecaseActionId=Long.parseLong(l);
		} catch (Exception e1) {
			GWT.log("", e1);
			return;
		}
		MessageUtility.progress(constants.pleaseWait(),constants.loading());

		ProcessHandlerServerService.getServer().loadProcessConfiguration(usecaseActionId, new AsyncCallback<ProcessConfigurationModel>(){

			public void onFailure(Throwable e) {
				MessageUtility.stopProgress();
				MessageUtility.alert(constants.error(), constants.failToAccessServer(),MessageBox.ERROR);
				GWT.log("", e);
			}
			public void onSuccess(final ProcessConfigurationModel model) {
				MessageUtility.stopProgress();
				if(model!=null){
					if(confirm&&model.isRefreshPageAfterProcess()){
						MessageBox.confirm(model.getTitle(), constants.discardCurrentOperation(),
								new MessageBox.ConfirmCallback(){
									public void execute(String btnID) {
										if("yes".equals( btnID)){
											displayProcessDialog(model);
										}
									}} );
					}else{
						displayProcessDialog(model);
					}
				}
			}
		});
	}
//**********************************************************************
	private void displayProcessDialog(ProcessConfigurationModel model){
			InputItem []items =model.getItems();
			if(items==null||items.length==0){
				this.callAction(model.getProcessId(), new String[]{"processIdentifier"}, new String[]{model.getProcessId()==null?"-1":model.getProcessId().toString()},
						model.getTitle(),model.getProcessSubmitPath(), constants, model.isAlertForSuccess(), SERVICE_ACTION_TYPE_EXCUTE_WITH_CONFIRM,model.isRefreshPageAfterProcess());
			}else{
			ProcessDialog dialog=new ProcessDialog(model,this,constants,direction);
		    dialog.center();
		    dialog.setAnimationEnabled(true);
		    dialog.show();
		    }
		    
	}

//**********************************************************************
		public native void setShowTrigger(ProcessHandler x)/*-{

		$wnd.startProcess = function ( t,c) {

		x.@ir.utopia.core.util.tags.process.client.ProcessHandler::runApp(Ljava/lang/String;Ljava/lang/String;)(t,c);

		};

		}-*/;
//**********************************************************************

//**********************************************************************
@Override
public void refreshPage() {
	doRefreshPage();
	
}
//**********************************************************************
public static native void doRefreshPage()/*-{
	$wnd.refreshPage();
}-*/;
//**********************************************************************
public  void processFinished(Long processUID,boolean status){
	notifyProcessFinished(String.valueOf(processUID),String.valueOf(usecaseActionId), String.valueOf(status));
}
//**********************************************************************

private static native void notifyProcessFinished(String processUID,String usecaseActionId,String status)/*-{
	$wnd.processFinished(processUID,usecaseActionId,status);
}-*/;


//**********************************************************************
}
   

 


