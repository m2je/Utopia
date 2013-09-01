package ir.utopia.core.util.attachment;

import ir.utopia.core.ServiceFactory;
import ir.utopia.core.attachment.bean.CoAttachmentFacadeRemote;
import ir.utopia.core.attachment.persistent.CoAttachment;

import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class AttachmentHandler extends ActionSupport {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(AttachmentHandler.class.getName());
	}
	private String recordId;
	/**
	 * 
	 */
	private static final long serialVersionUID = 9110762604285435222L;

	
	@Override
	public String execute() throws Exception {
		if(recordId==null||recordId.trim().length()==0){
			throw new IllegalArgumentException("invalid recocrdId:"+recordId);
		}
		Long realReordId;
		try {
			 realReordId=Long.parseLong(ServiceFactory.getSecurityProvider().decrypt(recordId));
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			throw new IllegalArgumentException("invalid recordId:"+recordId);
		}
		CoAttachmentFacadeRemote attachmentFacade=(CoAttachmentFacadeRemote) ServiceFactory.lookupFacade(CoAttachmentFacadeRemote.class);
		CoAttachment attachment= attachmentFacade.getAttachment(realReordId);
		if(attachment==null){
			logger.log(Level.WARNING,"unable to find attachment record with id:"+realReordId);
			throw new IllegalArgumentException("invalid attachment recordId:"+recordId);
		}
		
		HttpServletResponse response= ServletActionContext.getResponse();
		response.setContentType(getContetType(attachment.getFileName()));
		response.addHeader("Content-Disposition","attachment; filename=\"" + attachment.getFileName() + "\"");
		OutputStream outStr= response.getOutputStream();
		outStr.write(attachment.getAttachFile());
		outStr.close();
		response.flushBuffer();
		 super.execute();
		 return NONE;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getContetType(String fileName){
		if(fileName!=null&&fileName.trim().length()>0){
			fileName=fileName.trim();
			if(fileName.endsWith(".xsl")||fileName.endsWith(".xslx")){
				return "application/ms-excel";
			}else if(fileName.endsWith(".doc")||fileName.endsWith(".docx")){
				return "application/msword";
			}else if(fileName.endsWith(".txt")||fileName.endsWith(".rtf")){
				return "application/text";
			}else if(fileName.endsWith(".pdf")){
				return "application/pdf";
			}else if(fileName.endsWith(".htm")||fileName.endsWith(".html")){
				return "application/html";
			}
			
		}
		return "APPLICATION/OCTET-STREAM";
	}
}
