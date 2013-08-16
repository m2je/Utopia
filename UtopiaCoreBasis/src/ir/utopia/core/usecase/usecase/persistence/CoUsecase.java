package ir.utopia.core.usecase.usecase.persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * CoUsecase entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CO_USECASE",  uniqueConstraints = {})
@TableGenerator(name = "CoUsecaseSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_USECASE")
public class CoUsecase extends AbstractCoUsecase implements
		java.io.Serializable {

	// Constructors

//	private long systemId;
//	private long subSystemId;
	/**
	 * 
	 */
	private static final long serialVersionUID = -3842717939392710215L;

	/** default constructor */
	public CoUsecase() {
	}

	/** minimal constructor */
	public CoUsecase(Long coUsecaseId, String uscsRemoteClass) {
		super(coUsecaseId, uscsRemoteClass);
	}

	/** full constructor */
	public CoUsecase(Long coUsecaseId, String name, Date created,
			Long createdby, Date updated, Long updatedby,
			String uscsRemoteClass, Long isactive) {
		super(coUsecaseId, name, created, createdby, updated, updatedby,
				uscsRemoteClass, isactive);
	}

//	public long getSystemId() {
//		return systemId;
//	}
//
//	public void setSystemId(long systemId) {
//		this.systemId = systemId;
//	}
//
//	public long getSubSystemId() {
//		return subSystemId;
//	}
//
//	public void setSubSystemId(long subSystemId) {
//		this.subSystemId = subSystemId;
//	}
	
	
	
}
