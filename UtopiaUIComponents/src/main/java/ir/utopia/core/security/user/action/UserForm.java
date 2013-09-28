package ir.utopia.core.security.user.action;

import ir.utopia.common.basicinformation.businesspartner.persistent.CmBpartner;
import ir.utopia.common.basicinformation.businesspartner.persistent.CmPersonBpartner;
import ir.utopia.core.ServiceFactory;
import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.form.annotation.DataInputForm;
import ir.utopia.core.form.annotation.FormId;
import ir.utopia.core.form.annotation.FormPersistentAttribute;
import ir.utopia.core.form.annotation.IncludedForm;
import ir.utopia.core.form.annotation.InputItem;
import ir.utopia.core.form.annotation.PersistedMapForm;
import ir.utopia.core.form.annotation.SearchItem;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.UtopiaPersistent;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.security.userroles.action.UserRolesForm;
import ir.utopia.core.struts.AbstractUtopiaForm;

import java.util.Collection;
import java.util.Set;

@PersistedMapForm
@DataInputForm(includedForms={
		@IncludedForm(formClass=UserRolesForm.class,name="userRoles")})
public class UserForm  extends AbstractUtopiaForm<CoUser>{

	private Long coUserId;
	private String username;
	private String password;
	//private String userImage;
	private String bpartnerName;
//	private Long cmBpartnerId;
	private String name;
	private String lastName;
	private Constants.Sex sex;
	private Collection<UserRolesForm> userRoles;
//	private UserCreationType creationType;

	public UserForm(){
		
	}

	@FormId
	@FormPersistentAttribute
	public Long getCoUserId() {
		return coUserId;
	}

	public void setCoUserId(Long coUserId) {
		this.coUserId = coUserId;
	}

	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=1,breakLineAfter=true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=7,breakLineAfter=true,displayType=DisplayTypes.password,shouldConfirm=true)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_ITEM)
//	@FormPersistentAttribute(method="getCmBpartner")
//	@InputItem(isManadatory=true,displayType=DisplayTypes.lookup,index=3)
//	public Long getCmBpartnerId() {
//		return cmBpartnerId;
//	}
//
//	public void setCmBpartnerId(Long cmBpartnerId) {
//		this.cmBpartnerId = cmBpartnerId;
//	}

	@SearchItem(searchConfiguration=SearchItem.SearchType.SHOW_IN_SEARCH_GRID)
	@FormPersistentAttribute(formToEntityMapType=FormPersistentAttribute.FormToEntityMapType.virtual)
	public String getBpartnerName() {
		return bpartnerName;
	}

	public void setBpartnerName(String bpartnerName) {
		this.bpartnerName = bpartnerName;
	}
	@FormPersistentAttribute(method="getCoUserRoleses")
	public Collection<UserRolesForm> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Collection<UserRolesForm> userRoles) {
		this.userRoles = userRoles;
	}
/*
	@SearchItem
	@FormPersistentAttribute
	@InputItem(isManadatory=true,index=3,breakLineAfter=true)
	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
*/


//	public UserCreationType getCreationType() {
//		return creationType;
//	}
//	@InputItem(index=3,displayType=DisplayTypes.RadioButton)
//	public void setCreationType(UserCreationType creationType) {
//		this.creationType = creationType;
//	}
	@InputItem(index=4,isManadatory=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@InputItem(index=5,isManadatory=true,breakLineAfter=true)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@FormPersistentAttribute
	@InputItem(index=6,isManadatory=true,displayType=DisplayTypes.list)
	public Constants.Sex getSex() {
		return sex;
	}

	public void setSex(Constants.Sex sex) {
		this.sex = sex;
	}

	@Override
	public void importPersistent(UtopiaBasicPersistent persistent) {
		super.importPersistent(persistent);
		if(persistent instanceof CoUser){
			CmBpartner partner=((CoUser)persistent).getCmBpartner();
			setName(partner.getName());
			setLastName(partner.getSecoundName());
			Set<CmPersonBpartner> persons=partner.getCmPersonBpartner();
			if(persons!=null){
				for(CmPersonBpartner person:persons)
					setSex(person.getSex());
			}	
			setPassword(ServiceFactory.getSecurityProvider().decrypt(password));
		}
		
	}

	@Override
	public UtopiaPersistent convertToPersistent() {
		CoUser user=(CoUser) super.convertToPersistent();
		user.setPassword(ServiceFactory.getSecurityProvider().encrypt(password));
		return user;
	}
	
	

}
