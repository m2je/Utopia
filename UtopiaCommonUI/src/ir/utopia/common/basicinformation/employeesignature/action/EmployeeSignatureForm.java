package ir.utopia.common.basicinformation.employeesignature.action;

import java.io.File;

import ir.utopia.common.basicinformation.employeesignature.persistent.CmEmployeeSignature;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.persistent.annotation.LookupConfiguration;
import ir.utopia.core.struts.AbstractUtopiaForm;

@PersistedMapForm
@DataInputForm
public class EmployeeSignatureForm extends AbstractUtopiaForm<CmEmployeeSignature> {

	private Long cmEmployeeSignatureId;
	private Long cmEmployee;
	private File signatureImage;
	private String bpartnerName;
	private String organiztionName;


	@FormId
	@FormPersistentAttribute
	public Long getCmEmployeeSignatureId() {
		return cmEmployeeSignatureId;
	}
	public void setCmEmployeeSignatureId(Long cmEmployeeSignatureId) {
		this.cmEmployeeSignatureId = cmEmployeeSignatureId;
	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM,index=1)
	@FormPersistentAttribute(method = "getCmEmployee")
	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=1,breakLineAfter=false)
	@LookupConfiguration(displayColumns={"cmPersonBpartner.cmBpartner.code","cmPersonBpartner.cmBpartner.name","cmPersonBpartner.cmBpartner.secoundName"},displayItemSeperator="-")
	public Long getCmEmployee() {
		return cmEmployee;
	}
	
	public void setCmEmployee(Long cmEmployee) {
		this.cmEmployee = cmEmployee;
	}
	
	@FormPersistentAttribute
	@InputItem(isManadatory=true,displayType=DisplayTypes.upload,index=2,breakLineAfter=true)
	public File getSignatureImage() {
		return signatureImage;
	}
	public void setSignatureImage(File signatureImage) {
		this.signatureImage = signatureImage;
	}
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=1)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getBpartnerName() {
		return bpartnerName;
	}
	
	public void setBpartnerName(String bpartnerName) {
		this.bpartnerName = bpartnerName;
	}
	
	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID,index=2)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getOrganiztionName() {
		return organiztionName;
	}
	
	public void setOrganiztionName(String organiztionName) {
		this.organiztionName = organiztionName;
	}

}
