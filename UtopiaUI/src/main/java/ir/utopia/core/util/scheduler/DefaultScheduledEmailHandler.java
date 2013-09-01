package ir.utopia.core.util.scheduler;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.core.mailtemplate.persistent.CoMailTemplate;
import ir.utopia.core.persistent.AbstractBasicPersistent;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultScheduledEmailHandler implements ScheduledEmailHandler{
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(AbstractBasicPersistent.class.getName());
	}
	private CmBpartner recepient;
	private String sender;
	private CoMailTemplate template;
	private String attachmentRootPath;
	@Override
	public String getBody() {
		
		return null;
	}

	@Override
	public String getSubject() {
		
		return null;
	}

	@Override
	public void setRecepient(CmBpartner recepient) {
		this.recepient=recepient;
	}

	@Override
	public void setSender(String sender) {
		this.sender=sender;
	}

	@Override
	public void setMailTemplate(CoMailTemplate template) {
		this.template=template;
		
	}
	
	public File[] getAttachments(){
		if(template!=null){
			Map<String,String>attachments= template.getCustomProperties();
			if(attachments!=null){
				for(String fileName:attachments.keySet()){
					String file=attachments.get(fileName);
					if(file!=null){
						if(attachmentRootPath!=null){
							file=file.replace(ATTACHMENT_ROOT_PATH_PARAMETER, attachmentRootPath);
						}
						File cFile= new File(file);
						if(!cFile.exists()){
							logger.log(Level.WARNING,"attachment file :"+file+" does not exists on the application server,skipping");
							continue;
						}
						if(file.endsWith("jrxml")){
							logger.log(Level.INFO,"attachment file :"+file+" is of type of report file ,startting report engine");
						}
					}
					
				}
			}
		}
		return null;
	}

	@Override
	public void setAttachmentRootPath(String attachmentRootPath) {
		this.attachmentRootPath=attachmentRootPath;
	}
	
	
}
