package ir.utopia.core.smtp.bean;

import ir.utopia.common.basicinformation.businesspartner.bean.CmBpartnerFacadeRemote;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.core.ContextUtil;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants.ReportType;
import ir.utopia.core.customproperty.bean.CoCustomPropertyFacadeRemote;
import ir.utopia.core.customproperty.persistent.CoCustomProperty;
import ir.utopia.core.logic.util.LogicParser;
import ir.utopia.core.mailtemplate.bean.CoMailTemplateFacadeRemote;
import ir.utopia.core.mailtemplate.persistent.CoMailTemplate;
import ir.utopia.core.messagehandler.MessageHandler;
import ir.utopia.core.messagehandler.MessageNamePair;
import ir.utopia.core.messagehandler.MessageNamePair.MessageType;
import ir.utopia.core.process.BeanProcessParameter;
import ir.utopia.core.process.ExecutionResult;
import ir.utopia.core.smtp.persistent.CoSmtpAddresses;
import ir.utopia.core.smtp.persistent.CoSmtpConfiguration;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.util.report.ReportHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

@Stateless
public class SMTPService implements SMTPServiceRemote {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(SMTPService.class.getName());
	}
	@EJB
	private CoSmtpAddressesFacadeRemote addressFacade;
	@EJB
	private CoCustomPropertyFacadeRemote customPropsFacade;
	
	@Override
	public ExecutionResult sendMail(Long SMTPSenderId,Long templateId, Long bpartnerId,
			BeanProcessParameter[] parameters,String attchmentsRootPath, Map<String, Object> context) {
		try {
			CoMailTemplateFacadeRemote mailTemplateBean=(CoMailTemplateFacadeRemote)ServiceFactory.lookupFacade(CoMailTemplateFacadeRemote.class);
			CoMailTemplate template= mailTemplateBean.findById(templateId);
			CmBpartnerFacadeRemote bpFacade=(CmBpartnerFacadeRemote)ServiceFactory.lookupFacade(CmBpartnerFacadeRemote.class);
			CmBpartner reciver= bpFacade.findById(bpartnerId);
			CoSmtpAddresses sender= addressFacade.loadById(SMTPSenderId,"coSmtpConfiguration");
			return creatMailAndSendEmail(sender,reciver, template,parameters,attchmentsRootPath, context);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.WARNING,"",e);
			ExecutionResult result= new ExecutionResult();
			result.addMessage(e.toString(),MessageType.error);
			return result;
		}
		
	}
//***********************************************************************************************
	@Override
	public ExecutionResult sendMail(Long SMTPSenderId,Long templateId, Long[] bpartnerIds,
			BeanProcessParameter[] parameters,String attchmentsRootPath, Map<String, Object> context) {
		ExecutionResult result=new ExecutionResult();
		ArrayList<MessageNamePair>messages=new ArrayList<MessageNamePair>();
		for(Long bpartner:bpartnerIds){
			ExecutionResult cresult= sendMail(SMTPSenderId, templateId, bpartner,  parameters,attchmentsRootPath, context);
			if(cresult!=null){
				if(cresult.getMessages()!=null){
					messages.addAll(cresult.getMessages());
				}
			}
		}
		result.setMessages(messages);
		return result;
	}
//***********************************************************************************************
	private Properties loadConfigurations(CoSmtpConfiguration config,Map<String,Object>context)throws Exception{
		Properties props=new Properties();
		props.put("mail.smtp.host", config.getSmtpHost());
		props.put("mail.smtp.socketFactory.port",String.valueOf(config.getSmtpPort()));
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port",String.valueOf(config.getSmtpPort()));
		Map<String,String>customProps= config.getCustomProperties();
		if(customProps!=null)
			props.putAll(customProps);
		return props;
	}
//***********************************************************************************************
	private ExecutionResult creatMailAndSendEmail(CoSmtpAddresses sender,CmBpartner reciver,CoMailTemplate template,BeanProcessParameter[] parameters,String attchmentsRootPath, Map<String, Object> context){
		ExecutionResult result=new ExecutionResult();
		try {
			final String senderAdress=sender.getUsername();
			String password=sender.getPassword();
			if(senderAdress==null||password==null||senderAdress.trim().length()==0||password.trim().length()==0){
				logger.log(Level.WARNING,"invalid email setup for with emailAddress:"+senderAdress+" and password: "+password+" sending mail failed");
				result.addMessage(MessageHandler.getMessage("invalidSenderEmailAddresss", "ir.utopia.core.smtp.bean.SMTPService", ContextUtil.getLoginLanguage(context)), MessageType.error);
				return result;
			}
			final String decriptedPasswrd=ServiceFactory.getSecurityProvider().decrypt(password);
			String reciverEmailAdress=reciver.getEmailaddress();
			if(reciverEmailAdress==null||reciverEmailAdress.trim().length()==0){
				logger.log(Level.WARNING,"invalid email setup for :"+reciver.getName()+" with emailAddress:"+reciverEmailAdress+" sending mail failed");
				result.addMessage(MessageHandler.getMessage("invalidRecepientEmailAddresss", "ir.utopia.core.smtp.bean.SMTPService", ContextUtil.getLoginLanguage(context)), MessageType.error);
				return result;
			}
			String body= template.getContent();
			if(body!=null){
				body=LogicParser.getFlatExpression(body, new SMTPBodyParameterHandler(parameters, context,reciver));
			}
			String subject =template.getSubject();
			if(subject!=null){
				subject=LogicParser.getFlatExpression(subject, new SMTPSubjectParameterHandler(parameters, context,reciver));
			}
			Properties props=loadConfigurations(sender.getCoSmtpConfiguration(), context);
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
						        return new PasswordAuthentication(senderAdress,decriptedPasswrd);
						    }
			});
			MimeMessage message = new MimeMessage(session);
			message.addHeaderLine("charset=UTF-8");
			message.setFrom(new InternetAddress(senderAdress));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(reciver.getEmailaddress()));
			message.setSubject(subject,"UTF-8");
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(
			new ByteArrayDataSource(body, "text/HTML; charset=UTF-8")));
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			BodyPart []parts=loadAttachments(template,reciver,parameters,attchmentsRootPath,context);
			if(parts!=null){
				for(BodyPart part:parts){
					multipart.addBodyPart(part);	
				}
			}
			message.setContent(multipart);
			logger.log(Level.INFO,"System is about to send email from address:"+senderAdress+" with mail template id: "+template.getCoMailTemplateId()+" to: "+reciverEmailAdress);
			Transport.send(message);
			MessageNamePair namePair=new MessageNamePair( MessageType.info,MessageHandler.getMessage("sendingSuccessfullyFinished", "ir.utopia.core.smtp.bean.SMTPService", ContextUtil.getLoginLanguage(context)));
			namePair.setReferenceKey1(senderAdress);
			namePair.setReferenceKey2(reciver.getEmailaddress());
			namePair.setReferenceKey3(template.getName());
			result.addMessage(namePair);
			logger.log(Level.INFO,"Sending email from address:"+senderAdress+" with mail template id: "+template.getCoMailTemplateId()+" to: "+reciverEmailAdress+" successfully finished");
		} catch (Exception e) {
			logger.log(Level.WARNING,"",e);
			e.printStackTrace();
			MessageNamePair namePair=new MessageNamePair( MessageType.error,MessageHandler.getMessage("sendingFaiedDueToInternalError", "ir.utopia.core.smtp.bean.SMTPService", ContextUtil.getLoginLanguage(context)));
			namePair.setReferenceKey1(sender.getUsername());
			namePair.setReferenceKey2(reciver.getEmailaddress());
			namePair.setReferenceKey3(template.getName());
			result.addMessage(namePair);
			result.addMessage(e.toString(),MessageType.warning);
		}
		return result;
	}
//***********************************************************************************************
	private BodyPart[] loadAttachments(CoMailTemplate template,CmBpartner reciver,BeanProcessParameter[] parameters,String attchmentsRootPath,Map<String,Object>context){
		logger.log(Level.INFO,"Loading/creating attachment files");	
		try {
				Long recordId=template.getRecordId();
				UseCase usecase= UsecaseUtil.getUseCase(CoMailTemplateFacadeRemote.class.getName());
				List<CoCustomProperty>props= customPropsFacade.findByProperties(new String[]{"coUsecase.coUsecaseId","recordId"}, new Object[]{usecase.getUsecaseId(),recordId},  null);
				if(props!=null&&props.size()>0){
					List<BodyPart>result=new ArrayList<BodyPart>();
					
					for(CoCustomProperty prop:props){
						MimeBodyPart messageBodyPart = new MimeBodyPart();
						messageBodyPart.setHeader("Content-Type","Application/PDF;charset=\"utf-8\"");
						File f=File.createTempFile("MailAttachment_", "PDF");
						ReportHelper helper=new ReportHelper(new FileOutputStream(f),ReportType.pdf) ;
						Map<Object,Object>reportParameters=new HashMap<Object, Object>();
						reportParameters.putAll(context);
						reportParameters.put(SMTP_SERVICE_CONTEXT_VARIABLE_RECEPIENT_EMAIL, reciver.getEmailaddress());
						reportParameters.put(SMTP_SERVICE_CONTEXT_VARIABLE_RECEPIENT_NAME, reciver.getName()==null?"":reciver.getName()+" "+
																						   reciver.getSecoundName()==null?"":reciver.getSecoundName());
						reportParameters.put(SMTP_SERVICE_CONTEXT_VARIABLE_RECEPIENT_ID,reciver.getRecordId());
						if(parameters!=null){
							for(BeanProcessParameter parameter:parameters){
								if(parameter.getName()!=null&&parameter.getName().trim().length()>0)
									reportParameters.put(parameter.getName(), parameter.getValue());
							}
						}
						helper.setReportParameters(reportParameters);
						helper.setResourceName(prop.getExtendedProps1());
						helper.setReportPath(attchmentsRootPath+File.separatorChar+prop.getPropertyValue());
						helper.RunReport();
						messageBodyPart.setDataHandler(new DataHandler(
						new ByteArrayDataSource(new FileInputStream(f), "application/octet-stream; charset=UTF-8")));
						messageBodyPart.setFileName(MimeUtility.encodeText(prop.getPropertyName()+".pdf","UTF-8","B"));
						messageBodyPart.setHeader("charset", "UTF-8");
						result.add(messageBodyPart);
					}
					return result.toArray(new BodyPart[result.size()]); 
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				logger.log(Level.WARNING,"",e);
			}
			
			return null;
	}
//***********************************************************************************************	
}
