package ir.utopia.core.attachment.persistent;

import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoAttachment entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_ATTACHMENT", uniqueConstraints = {})
@TableGenerator(name = "AttachmentSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_ATTACHMENT")
@NamedQueries(
{
@NamedQuery(name="DeleteUsecaseAttachmentByUsecase",query="DELETE  from CoAttachment model where model.coUsecase.coUsecaseId=:coUsecaseId and model.recordId=:recordId")
,@NamedQuery(name="DeleteUsecaseActionAttachmentByUsecaseActionId",query="DELETE from CoAttachment model where model.coUsecase.coUsecaseId=:coUsecaseId and model.recordId=:recordId and model.coUsecaseAction.coUsecaseActionId=:coUsecaseActionId"),
@NamedQuery(name="DeleteUsecaseActionAttachmentByRecordId",query="DELETE  from CoAttachment model where model.coAttachmentId=:coAttachmentId")
})		
public class CoAttachment extends AbstractCoAttachment implements
		java.io.Serializable {
	public static final String DELETE_USECASE_ATTACHMENT="DeleteUsecaseAttachmentByUsecase";
	public static final String DELETE_USECASE_ACTION_ATTACHMENT="DeleteUsecaseActionAttachmentByUsecaseActionId";
	public static final String DELETE_ATTACHMENT="DeleteUsecaseActionAttachmentByRecordId";
	// Constructors

	/**
	 * 
	 */
	private static final long serialVersionUID = 8731886589760209161L;

	/** default constructor */
	public CoAttachment() {
	}

	/** minimal constructor */
	public CoAttachment(Long coAttachmentId, CoUsecase coUsecase, Long recordId) {
		super(coAttachmentId, coUsecase, recordId);
	}

	/** full constructor */
	public CoAttachment(Long coAttachmentId, CoUsecase coUsecase,
			CoUsecaseAction coUsecaseAction, Long recordId, String fileName,byte[] attachFile) {
		super(coAttachmentId, coUsecase, coUsecaseAction, recordId,fileName, attachFile);
	}
	public CoAttachment(Long coAttachmentId, CoUsecase coUsecase,
			CoUsecaseAction coUsecaseAction, Long recordId,String fileName){
		super(coAttachmentId, coUsecase, coUsecaseAction, recordId,fileName, null);
	}
}
