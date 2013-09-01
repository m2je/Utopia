package ir.utopia.core.util.tags.datainputform.client;

import com.google.gwt.core.client.GWT;


public class NativeEventHandler {

	String nativeMethodName;
	public NativeEventHandler(String nativeMethodName){
		this.nativeMethodName=nativeMethodName;
	}
	public  void fireEvent(String eventType,String parameter1,String parameter2,String parameter3,String parameter4){
		try {
			fireEventNative(eventType, parameter1, parameter2, parameter3, parameter4);
		} catch (Exception e) {
			if(e!=null)
				GWT.log(e.getMessage());
		}
	}
	public native void fireEventNative(String eventType,String parameter1,String parameter2,String parameter3,String parameter4)/*-{
			$wnd[this.@ir.utopia.core.util.tags.datainputform.client.NativeEventHandler::nativeMethodName](eventType,parameter1,parameter2,parameter3,parameter4);
	}-*/;

	
}
