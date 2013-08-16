package ir.utopia.core.util.scheduler;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.core.mailtemplate.persistent.CoMailTemplate;

import java.io.File;

public interface ScheduledEmailHandler {

	public static final String ATTACHMENT_ROOT_PATH_PARAMETER="@ROOT_PATH@";
	
	public void setRecepient(CmBpartner recepient);
	
	public void setSender(String sender);
	
	public void setMailTemplate(CoMailTemplate template);
	
	public void setAttachmentRootPath(String attachmentRootPath);
	
	public String getBody();
	
	public String getSubject();
	
	public File[] getAttachments();
	
	
}
