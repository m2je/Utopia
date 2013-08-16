package ir.utopia.core.attachment.persistent;

import ir.utopia.core.persistent.AbstractBasicPersistent;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.usecase.usecaseaction.persistence.CoUsecaseAction;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * AbstractCoAttachment entity provides the base persistence definition of the
 * CoAttachment entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractCoAttachment extends AbstractBasicPersistent implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8066968790183153107L;
	private Long coAttachmentId;
	private CoUsecase coUsecase;
	private CoUsecaseAction coUsecaseAction;
	private Long recordId;
	private byte[] attachFile;
	private String fileName;
	// Constructors
	public AbstractCoAttachment(){
		
	}
	

	public AbstractCoAttachment(Long coAttachmentId2, CoUsecase coUsecase2,
			Long recordId2) {
		this(coAttachmentId2, coUsecase2,
				null, recordId2, null,null);
	}

	public AbstractCoAttachment(Long coAttachmentId, CoUsecase coUsecase,
			CoUsecaseAction coUsecaseAction, Long recordId,String fileName, byte[] attachFile) {
		setCoAttachmentId(coAttachmentId);
		setCoUsecase(coUsecase);
		setCoUsecaseAction(coUsecaseAction);
		setFileName(fileName);
		setAttachFile(attachFile);
		setRecordId(recordId);
	}


	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="AttachmentSequenceGenerator")
	@Column(name = "CO_ATTACHMENT_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoAttachmentId() {
		return this.coAttachmentId;
	}

	public void setCoAttachmentId(Long coAttachmentId) {
		this.coAttachmentId = coAttachmentId;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USECASE_ID", unique = false, nullable = false, insertable = true, updatable = true)
	public CoUsecase getCoUsecase() {
		return this.coUsecase;
	}

	public void setCoUsecase(CoUsecase coUsecase) {
		this.coUsecase = coUsecase;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "CO_USECASE_ACTION_ID", unique = false, nullable = true, insertable = true, updatable = true)
	public CoUsecaseAction getCoUsecaseAction() {
		return this.coUsecaseAction;
	}

	public void setCoUsecaseAction(CoUsecaseAction coUsecaseAction) {
		this.coUsecaseAction = coUsecaseAction;
	}

	@Column(name = "RECORD_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getRecordId() {
		return this.recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name = "ATTACH_FILE", unique = false, nullable = true, insertable = true, updatable = true)
	public byte[] getAttachFile() {
		return this.attachFile;
	}

	public void setAttachFile(byte[] attachFile) {
		this.attachFile = attachFile;
	}

	@Column(name="FILE_NAME")
	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}