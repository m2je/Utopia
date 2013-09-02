package ir.utopia.core.struts;

import java.util.List;

public class UtopiaFormNativeConfiguration {

	private String nativeScripts;
	private String [] validationFunctions;
	private List<NativeScriptMessage>nativeMessages;
public UtopiaFormNativeConfiguration(String nativeScripts,
			String[] validationFunctions,List<NativeScriptMessage>nativeMessages) {
		super();
		this.nativeScripts = nativeScripts;
		this.validationFunctions = validationFunctions;
		this.nativeMessages=nativeMessages;
	}
//*************************************************************************************	
	public String getNativeScripts() {
		return nativeScripts;
	}
//*************************************************************************************
public void setNativeScripts(String nativeScripts) {
		this.nativeScripts = nativeScripts;
	}
//*************************************************************************************
	public String[] getValidationFunctions() {
		return validationFunctions;
	}
//*************************************************************************************
	public void setValidationFunctions(String[] validationFunctions) {
		this.validationFunctions = validationFunctions;
	}
//*************************************************************************************	
	public List<NativeScriptMessage> getNativeMessages() {
		return nativeMessages;
	}
//*************************************************************************************
	public void setNativeMessages(List<NativeScriptMessage> nativeMessages) {
		this.nativeMessages = nativeMessages;
	}
//*************************************************************************************
	public static class NativeScriptMessage{
		String message;
		String bundle;
		String messageName;
		public NativeScriptMessage(String messageName,String message,String bundle){
			this.bundle=bundle;
			this.message=message;
			this.messageName=messageName;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getBundle() {
			return bundle;
		}
		public void setBundle(String bundle) {
			this.bundle = bundle;
		}
		public String getMessageName() {
			return messageName;
		}
		public void setMessageName(String messageName) {
			this.messageName = messageName;
		}
		
	}
	
}
