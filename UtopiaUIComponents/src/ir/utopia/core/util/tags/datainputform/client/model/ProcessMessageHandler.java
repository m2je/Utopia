package ir.utopia.core.util.tags.datainputform.client.model;

import ir.utopia.core.util.tags.datainputform.client.DataInputFormConstants;
import ir.utopia.core.util.tags.datainputform.client.MessageUtility;

import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.MessageBox.PromptCallback;

public class ProcessMessageHandler {
//**********************************************************************
	public static  void  showExecutionMessages(final ExecutionResult result,DataInputFormConstants constants){
		 new ProcessMessageHandler().showMessages(result, constants, null);	
	}
//**********************************************************************
	protected  void  showMessages(final ExecutionResult result,DataInputFormConstants constants){
		 showMessages(result, constants, null);	
	}
//**********************************************************************
	protected  void  showMessages(ExecutionResult result,DataInputFormConstants constants,final PromptCallback externalCallback){
		
		if(result!=null){
			final String title=result.isSuccess()?constants.processSuccess():constants.processFailed();
			String []errors=result.getErrorMessages();
			final String []warnings=result.getWarningMessages();
			final String []infos=result.getInfoMessages();
			
			if(errors!=null&&errors.length>0){
				MessageUtility.alert(title, errors, MessageBox.ERROR,new PromptCallback(){
					public void execute(String btnID, String text) {
						showWarnMessage(externalCallback, title, warnings, infos);
					}
					});
			}else{
				showWarnMessage(externalCallback, title, warnings, infos);
			}
		}
	    else{
	    	MessageBoxConfig conf= MessageUtility.alert("",constants.processFailed(),MessageBox.ERROR);
	    	if(conf!=null){
	    		conf.setCallback(externalCallback );
	    	}
	    }
		}

	//**********************************************************************
		private static void showWarnMessage(
				final PromptCallback externalCallback,
				final String title, final String[] warnings,
				final String[] infos) {
			if(warnings!=null&&warnings.length>0){
				MessageUtility.alert(title, warnings, MessageBox.WARNING,new PromptCallback(){
					public void execute(String btnID, String text) {
						if(infos!=null&&infos.length>0){
							MessageUtility.alert(title, infos, MessageBox.INFO,externalCallback);
						}else if(externalCallback!=null){
							externalCallback.execute(null, null);
						}
					}
					
				});
			}else{
				if(infos!=null&&infos.length>0){
					MessageUtility.alert(title, infos, MessageBox.INFO,externalCallback);
				}else if(externalCallback!=null){
					externalCallback.execute(null, null);
				}
			}
		}
}
