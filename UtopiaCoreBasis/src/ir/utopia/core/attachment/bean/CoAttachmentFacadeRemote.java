package ir.utopia.core.attachment.bean;

import ir.utopia.core.attachment.persistent.CoAttachment;
import ir.utopia.core.bean.UtopiaBean;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.persistent.UtopiaAttachmentInfo;
import ir.utopia.core.persistent.UtopiaBasicPersistent;

import java.io.File;
import java.util.List;

import javax.ejb.Remote;
@Remote
public interface CoAttachmentFacadeRemote extends UtopiaBean {
	/**
	 * 
	 * @param coUsecaseId
	 * @param CoUsecaseActionId
	 * @param recordId
	 * @param attachFile
	 * @return
	 */
	public CoAttachment createAttachment(Long coUsecaseId,Long CoUsecaseActionId,Long recordId, File attachFile,String fileName);
	/**
	 * 
	 * @param coUsecaseId
	 * @param CoUsecaseActionId
	 * @param recordId
	 * @return
	 */
	public List<CoAttachment> getAttachments(Long coUsecaseId,Long CoUsecaseActionId,Long recordId);
	/**
	 * 
	 * @param coUsecaseId
	 * @param recordId
	 * @return
	 */
	public List<CoAttachment> getAttachments(Long coUsecaseId,Long recordId);
	/**
	 * 
	 * @param coUsecaseId
	 * @param CoUsecaseActionId
	 * @param recordId
	 */
	public void deleteAttachments(Long coUsecaseId,Long CoUsecaseActionId,Long recordId);
	/**
	 * 
	 * @param attachmentId
	 */
	public void deleteAttachment(Long attachmentId);
	/**
	 * 
	 * @param attachmentId
	 * @return
	 */
	public CoAttachment getAttachment(Long attachmentId);
	/**
	 * will not load the files
	 * @param coUsecaseId
	 * @param recordId
	 * @return
	 */
	public List<CoAttachment> getAttachmentInfos(Long coUsecaseId,Long recordId);
	/**
	 * 
	 * @param coUsecaseId
	 * @param recordId
	 * @return
	 */
	public List<CoAttachment> getAttachmentInfos(Long coUsecaseId,Long CoUsecaseActionId,Long recordId);
	/**
	 * 
	 * @param persitentObject
	 * @param action
	 */
	public void handleAttachments(UtopiaBasicPersistent persitentObject,List<UtopiaAttachmentInfo>attachments,predefindedActions action);
}
