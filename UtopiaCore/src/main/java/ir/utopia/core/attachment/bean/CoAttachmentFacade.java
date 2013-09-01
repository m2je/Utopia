package ir.utopia.core.attachment.bean;

import ir.utopia.core.attachment.persistent.CoAttachment;
import ir.utopia.core.bean.BeanUtil;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.persistent.UtopiaAttachmentInfo;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.usecase.UsecaseUtil;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.usecase.actionmodel.UsecaseAction;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class CoAttachmentFacade implements CoAttachmentFacadeRemote {
	private static final Logger logger;
	
	static {
		logger = Logger.getLogger(CoAttachmentFacade.class.getName());
	}
	@PersistenceContext
	protected EntityManager entityManager; 
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public CoAttachment createAttachment(Long coUsecaseId,
			Long CoUsecaseActionId, Long recordId, File attachFile,String fileName) {
		if(coUsecaseId==null||coUsecaseId.longValue()<=0){
			throw new IllegalArgumentException("Invalid usecaseId:"+coUsecaseId);
		}
		if(recordId==null||recordId.longValue()<=0){
			throw new IllegalArgumentException("Invalid recordId:"+recordId);
		}
		if(attachFile==null||!attachFile.exists()){
			throw new IllegalArgumentException("Invalid file:"+attachFile);
		}
		CoAttachment attachment=new CoAttachment();
		attachment.setAttachFile(readFile(attachFile));
		attachment.setCoUsecase(entityManager.find(CoUsecase.class, coUsecaseId));
		if(CoUsecaseActionId!=null&&CoUsecaseActionId.longValue()>0)
			attachment.setCoUsecaseAction(entityManager.find(CoUsecaseAction.class, CoUsecaseActionId));
		attachment.setRecordId(recordId);
		attachment.setFileName(fileName);
		entityManager.persist(attachment);
		return attachment;
	}
//******************************************************************************************************
	private byte[] readFile(File file){
		try {
			byte []lob =new byte[(int)file.length()] ;
			FileInputStream fin=new FileInputStream(file);
			fin.read(lob);
			fin.close();
			return lob;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.WARNING,"",e);
		}
		return null;
	}
//******************************************************************************************************

	@Override
	public List<CoAttachment> getAttachments(Long coUsecaseId,
			Long CoUsecaseActionId, Long recordId) {
		return doGetAttachments(coUsecaseId, CoUsecaseActionId, recordId,true);
	}
//******************************************************************************************************
	@SuppressWarnings("unchecked")
	protected List<CoAttachment> doGetAttachments(Long coUsecaseId,
			Long CoUsecaseActionId, Long recordId,boolean loadFile) {
		if(coUsecaseId==null||coUsecaseId.longValue()<=0){
			throw new IllegalArgumentException("Invalid usecaseId:"+coUsecaseId);
		}
		if(recordId==null||recordId.longValue()<=0){
			throw new IllegalArgumentException("Invalid recordId:"+recordId);
		}
		StringBuffer queryStr=new StringBuffer("select new ir.utopia.core.attachment.persistent.CoAttachment(model.coAttachmentId," ).
		append(" model.coUsecase, model.coUsecaseAction, model.recordId ,model.fileName");
		if(loadFile)
			queryStr.append(", model.attachFile");
		
		queryStr.append(") from  CoAttachment model where ").
		append(" model.coUsecase.coUsecaseId=:coUsecaseId and model.recordId=:recordId");
		if(CoUsecaseActionId!=null&&CoUsecaseActionId.longValue()>0){
			queryStr.append(" and model.coUsecaseAction.coUsecaseActionId=:coUsecaseActionId");
		}
		
		Query query= entityManager.createQuery(queryStr.toString());
		query.setParameter("coUsecaseId", coUsecaseId);
		query.setParameter("recordId", recordId);
		if(CoUsecaseActionId!=null&&CoUsecaseActionId.longValue()>0){
			query.setParameter("coUsecaseActionId", CoUsecaseActionId);
		}
		
		return query.getResultList();
	}
//******************************************************************************************************
	@Override
	public List<CoAttachment> getAttachments(Long coUsecaseId, Long recordId) {
		return getAttachments(coUsecaseId,null, recordId);
	}
//******************************************************************************************************
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteAttachment(Long attachmentId) {
		if(attachmentId!=null&&attachmentId>0){
			Query q= entityManager.createNamedQuery(CoAttachment.DELETE_ATTACHMENT);
			q.setParameter("coAttachmentId", attachmentId);
			q.executeUpdate();
		}
	}
//******************************************************************************************************
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteAttachments(Long coUsecaseId, Long coUsecaseActionId,
			Long recordId) {
		
		if(coUsecaseId==null||coUsecaseId<=0){
			logger.log(Level.WARNING,"invalid usecaseId:"+coUsecaseId);
			return;
		}
		if(recordId==null||recordId<=0){
			logger.log(Level.WARNING,"invalid recordId:"+recordId);
			return;
		}
		
		Query query=entityManager.createNamedQuery((coUsecaseActionId!=null&&coUsecaseActionId>0)? CoAttachment.DELETE_USECASE_ACTION_ATTACHMENT:CoAttachment.DELETE_USECASE_ATTACHMENT);
		query.setParameter("recordId",recordId);
		query.setParameter("coUsecaseId", coUsecaseId);
		if(coUsecaseActionId!=null&&coUsecaseActionId>0){
			query.setParameter("coUsecaseActionId", coUsecaseActionId);
			logger.info("Deleteing All attachment for usecase:"+coUsecaseId+" ,usecaseAction:"+coUsecaseId+" ,recordId:"+recordId);
		}else{
			logger.info("Deleteing All attachment for usecase:"+coUsecaseId+" ,recordId:"+recordId);
		}
		query.executeUpdate();
	}
//******************************************************************************************************
	@Override
	public CoAttachment getAttachment(Long attachmentId) {
		if(attachmentId!=null)
			return entityManager.find(CoAttachment.class, attachmentId);
		return null;
	}
//******************************************************************************************************
	@Override
	public List<CoAttachment> getAttachmentInfos(Long coUsecaseId, Long recordId) {
		return doGetAttachments(coUsecaseId, null, recordId, false);
	}
//******************************************************************************************************
	@Override
	public List<CoAttachment> getAttachmentInfos(Long coUsecaseId,
			Long CoUsecaseActionId, Long recordId) {
		return doGetAttachments(coUsecaseId, CoUsecaseActionId, recordId, false);
	}
//******************************************************************************************************
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void handleAttachments(UtopiaBasicPersistent persitentObject,List<UtopiaAttachmentInfo>attachments,
			predefindedActions action) {
		try {
			
			if(persitentObject!=null&&attachments!=null&&attachments.size()>0){
				Class<?>remoteClass=BeanUtil.findRemoteClassFromPersistent(persitentObject.getClass());
				if(remoteClass==null){
					logger.log(Level.WARNING,"fail to find remoteClass for persistent class:"+persitentObject.getClass()+" ,no attachment change will be done!");
					return;
				}
				UseCase usecase= UsecaseUtil.getUseCase(remoteClass.getName());
				if(usecase==null){
					logger.log(Level.WARNING,"fail to find usecase for remote class:"+persitentObject.getClass()+" ,no attachment change will be done!");
					return;
				}
				UsecaseAction usAction= usecase.getUsecaseAction(action.toString());
				for(UtopiaAttachmentInfo info: attachments){
					if(info.isRemoved()){
						if(info.getAttachmentId()>0){
							deleteAttachment(info.getAttachmentId());
						}
					}
					else if(info.getFile()!=null){
							createAttachment(usecase.getUsecaseId(), usAction==null?null:usAction.getActionId(), persitentObject.getRecordId(), info.getFile(),info.getFileName());
						}
				}
			}
		} catch (Exception e) {
			logger.log(Level.WARNING,"Fail to save atachment",e);
		}
	}
	
}
