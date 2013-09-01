package ir.utopia.common.basicinformation.jobtitle.action;

import ir.utopia.common.basicinformation.jobtitle.persistent.CmJobTitle;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.struts.AbstractUtopiaForm;


@PersistedMapForm
@DataInputForm
public class JobTitleForm  extends AbstractUtopiaForm<CmJobTitle> {
	
	
	
	private Long cmJobTitleId;
	private String code;
	private String name;
	private String description;
	
	
	public JobTitleForm(){
		
	}


	
	@FormId
	@FormPersistentAttribute
	public Long getCmJobTitleId() {
		return cmJobTitleId;
	}
	public void setCmJobTitleId(Long cmJobTitleId) {
		this.cmJobTitleId = cmJobTitleId;
	}

	
	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=1,breakLineAfter=false)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}


	
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=2,breakLineAfter=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	@FormPersistentAttribute
	@InputItem(isManadatory=false,index=3,breakLineAfter=true,displayType=DisplayTypes.LargeString)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
