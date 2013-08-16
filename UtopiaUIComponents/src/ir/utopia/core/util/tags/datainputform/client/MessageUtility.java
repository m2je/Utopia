package ir.utopia.core.util.tags.datainputform.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.core.NameValuePair;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.WaitConfig;
import com.gwtext.client.widgets.MessageBox.PromptCallback;

public abstract class MessageUtility {
	public static MessageBoxConfig alert(final String title,final String []messages,final String type){
		return alert(title, messages, type,null);
	}
//**********************************************************************
	public static MessageBoxConfig alert(final String title,final String []messages,final String type,final PromptCallback externalCallback){
		MessageBoxConfig conf;
		if(messages!=null&&messages.length>0){
			conf= alert(title, messages[0], type);
			if(messages.length>1){
				final String []remainMessages=new String[messages.length-1];
				System.arraycopy(messages, 1, remainMessages, 0, remainMessages.length);
				conf.setCallback(new PromptCallback(){
					public void execute(String btnID, String text) {
						alert(title, remainMessages, type,externalCallback);
					}});
			}else{
				if(externalCallback!=null){
					conf.setCallback(externalCallback);	
				}
				
			}
			return conf;
		}
		return null;
	}
//**********************************************************************
	public static MessageBoxConfig error(final String title,final String message){
		return alert(title,message,MessageBox.ERROR);
	}
//**********************************************************************
	public static MessageBoxConfig warn(final String title,final String message){
		return alert(title,message,MessageBox.WARNING);
	} 
//**********************************************************************
	public static MessageBoxConfig info(final String title,final String message){
		return alert(title,message,MessageBox.INFO);
	}
//**********************************************************************
	public static MessageBoxConfig alert(final String title,final String message,final String type){
		
		MessageBoxConfig conf=new MessageBoxConfig() {  
	        {  
	        	setModal(true);
	        	setTitle(title);
	            setMsg(message);  
	            setIconCls(type);
	            setButtons(new NameValuePair[]{new NameValuePair("ok",true)});
	        }  
	    }  ;
	    
			 MessageBox.show(conf);
			return conf;
		
	}
	
//************************************************************************
	public static void showAutoHideInfoMessage(String message){
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
//************************************************************************
	public static void progress(final String waitingMessage,final String action){
		  MessageBox.show(new MessageBoxConfig() {  
			                     {  
			                         setMsg(waitingMessage);  
			                         setProgressText(action);
			                         setWidth(300);  
			                         setWait(true);  
			                         setWaitConfig(new WaitConfig() {  
			                             {  
			                                 setInterval(200);  
			                             }  
			                         });  
			                        
			                     }  
			                 });  
			   
			                
			             }
//***********************************************************************	
	public static void stopProgress(){
		MessageBox.hide();
		patchUI();
	}
//***********************************************************************	
	public static void patchUI(){
		patchEXTComponent();
	}
//**********************************************************************
	private static boolean patchEXTComponent(){
		try {
			for(int i=0;i<15;i++){
				Element e= Document.get().getElementById("ext-comp-100"+i);
				   if(e!=null){
					   if( e.getStyle()!=null&&e.getStyle().getLeft().startsWith("-")){
						   e.getStyle().setLeft(0, Unit.PX);
						   return true;
					   }
				   }

			}
			return false;
		} catch (Exception e) {
			GWT.log(e.toString());
		}
	       return true;
	}
}
