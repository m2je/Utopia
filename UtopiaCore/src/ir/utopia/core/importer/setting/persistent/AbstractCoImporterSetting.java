package ir.utopia.core.importer.setting.persistent;


import ir.utopia.core.constants.Constants;
import ir.utopia.core.persistent.AbstractUtopiaPersistent;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * 
 * @author Golnari@gamil
 */
@MappedSuperclass
public abstract class AbstractCoImporterSetting  extends AbstractUtopiaPersistent implements java.io.Serializable {

	private static final long serialVersionUID = -4025815473731321098L;

	private Long coImporterSettingId;
	private String name;
	private String setting;
	private String comment;
	private CoUsecase coUsecase;
	/** default constructor */
	public AbstractCoImporterSetting() {
	}


	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,
			generator="ImporterSettingSequenceGenerator")
	@Column(name = "CO_IMPORTER_SETTING_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCoImporterSettingId() {
		return this.coImporterSettingId;
	}

	public void setCoImporterSettingId(Long coImporterSettingId) {
		this.coImporterSettingId = coImporterSettingId;
	}

	@Column(name = "NAME", unique = true, nullable = false, insertable = true, updatable = true, length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "SETTING", unique = false, nullable = false, insertable = true, updatable = true, length = 3000)
	public String getSetting() {
		return this.setting;
	}

	@Column(name = "DESCRIPTION", unique = false, nullable = false, insertable = true, updatable = true, length = 3000)
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setSetting(String setting) {
		this.setting = setting;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})	
	@JoinColumn(name = "CO_USECASE_ID", referencedColumnName = "CO_USECASE_ID")
	public CoUsecase getCoUsecase() {
		return coUsecase;
	}


	public void setCoUsecase(CoUsecase coUsecase) {
		this.coUsecase = coUsecase;
	}
	
}